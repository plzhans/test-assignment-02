package com.plzhans.assignment.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plzhans.assignment.api.auth.AuthRoomResolver;
import com.plzhans.assignment.api.controller.spread.SpreadController;
import com.plzhans.assignment.api.infra.lock.LockInfra;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.repository.cache.CacheRepository;
import com.plzhans.assignment.api.service.lock.TestLock;
import com.plzhans.assignment.api.service.spread.SpreadService;
import com.plzhans.assignment.api.service.spread.SpreadServiceImpl;
import com.plzhans.assignment.api.service.spread.datatype.DistributeParam;
import com.plzhans.assignment.common.domain.spread.SpreadAmountState;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadAmountEntity;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpreadController.class)
@AutoConfigureRestDocs(uriHost = "host")
@ExtendWith(SpringExtension.class)
@DisplayName("SpreadController - Test")
public class SpreadControllerTest {

    @TestConfiguration
    static class Config {

        @Bean
        SpreadService spreadService(SpreadRepository spreadRepository, CacheRepository cacheRepository, LockInfra lockInfra) {
            return new SpreadServiceImpl(spreadRepository, cacheRepository, lockInfra);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpreadRepository spreadRepository;

    @MockBean
    private CacheRepository cacheRepository;

    @MockBean
    private LockInfra lockInfra;

    @Autowired
    private ObjectMapper objectMapper;

    static final String BASE_PATH = SpreadController.BASE_PATH;

    int testUserId = 1001;
    String testRoomId = "room-test-id";

    HttpHeaders getUserAndRoomHeaders() {
        return getUserAndRoomHeaders(testUserId, testRoomId);
    }

    HttpHeaders getUserAndRoomHeaders(int userId, String roomId) {
        HttpHeaders header = new HttpHeaders();
        header.add(AuthRoomResolver.USER_ID_HEADER, String.valueOf(userId));
        header.add(AuthRoomResolver.ROOM_ID_HEADER, roomId);
        return header;
    }

    HttpHeaders getNextUserAndRoomHeaders(int next) {
        return getUserAndRoomHeaders(testUserId + next, testRoomId);
    }

    /**
     * Init.
     */
    @BeforeEach
    public void init() {
        //
        given(this.cacheRepository.getValue(anyString())).willReturn(null);
        given(this.lockInfra.getLock(anyString(),anyInt())).willReturn(new TestLock());
    }

    @DisplayName("뿌리기 성공")
    @Test
    public void distribute() throws Exception {
        // GIVEN
        int givenEventNo = 111;
        LocalDateTime givenCreatedDate = LocalDateTime.now();

        given(spreadRepository.save(any(SpreadEventEntity.class))).willAnswer((Answer<SpreadEventEntity>) invocation -> {
            SpreadEventEntity data = (SpreadEventEntity) invocation.getArguments()[0];
            data.setNo(givenEventNo);
            data.setCreatedAt(givenCreatedDate);
            return data;
        });

        // WHEN
        val body = DistributeParam.builder()
                .amount(1000)
                .receiverCount(5)
                .build();

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post(BASE_PATH + SpreadController.MAPPING_DISTRIBUTE)
                        .headers(getUserAndRoomHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        result.andExpect(status().isOk())
                //.andDo(print())
                .andDo(SpreadDocument.distribute());
    }

    @DisplayName("뿌리기 조회")
    @Test
    public void distribute_status() throws Exception {
        // GIVEN
        val token = "111";
        val entity = SpreadEventEntity.builder()
                .no(111)
                .state(SpreadState.Active)
                .userId(testUserId)
                .roomId(testRoomId)
                .token(token)
                .totalAmount(1000)
                .receiverCount(5)
                .expiredSeconds(60)
                .amounts(new ArrayList<>())
                .build();
        entity.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        entity.setUpdatedAt(entity.getCreatedAt());

        val amount1 = SpreadAmountEntity.builder()
                .no(1)
                .state(SpreadAmountState.Ready)
                .event(entity)
                .amount(100)
                .build();
        amount1.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        amount1.setUpdatedAt(entity.getCreatedAt());
        entity.getAmounts().add(amount1);

        val amount2 = SpreadAmountEntity.builder()
                .no(2)
                .state(SpreadAmountState.Received)
                .event(entity)
                .amount(200)
                .receiverId(testUserId + 1)
                .receivedDate(LocalDateTime.now())
                .build();
        amount2.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        amount2.setUpdatedAt(entity.getCreatedAt());
        entity.getAmounts().add(amount2);

        given(spreadRepository.findByTokenAndRoomId(anyString(), anyString())).willReturn(entity);

        // WHEN
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get(BASE_PATH + SpreadController.MAPPING_DISTRIBUTE_STATUS)
                        .headers(getUserAndRoomHeaders())
                        .param("token", token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        result.andExpect(status().isOk())
                //.andDo(print())
                .andDo(SpreadDocument.distribute_status());
    }

    @DisplayName("받기 성공")
    @Test
    public void receive() throws Exception {
        // GIVEN
        val token = "111";
        val entity = SpreadEventEntity.builder()
                .no(111)
                .state(SpreadState.Active)
                .userId(testUserId)
                .roomId(testRoomId)
                .token(token)
                .totalAmount(1000)
                .receiverCount(5)
                .expiredSeconds(SpreadServiceImpl.DEFAULT_DISTRIBUTE_EXPIRED_SECONDS)
                .amounts(new ArrayList<>())
                .build();
        entity.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        entity.setUpdatedAt(entity.getCreatedAt());

        val amount1 = SpreadAmountEntity.builder()
                .no(1)
                .state(SpreadAmountState.Ready)
                .event(entity)
                .amount(100)
                .build();
        amount1.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        amount1.setUpdatedAt(entity.getCreatedAt());
        entity.getAmounts().add(amount1);

        val amount2 = SpreadAmountEntity.builder()
                .no(2)
                .state(SpreadAmountState.Received)
                .event(entity)
                .amount(200)
                .receiverId(testUserId + 1)
                .receivedDate(LocalDateTime.now())
                .build();
        amount2.setCreatedAt(LocalDateTime.now().minusMinutes(1));
        amount2.setUpdatedAt(entity.getCreatedAt());
        entity.getAmounts().add(amount2);

        given(spreadRepository.findByTokenAndRoomId(anyString(), anyString())).willReturn(entity);

        // WHEN
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post(BASE_PATH + SpreadController.MAPPING_RECEIVE)
                        .headers(getNextUserAndRoomHeaders(1))
                        .param("token", token)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        result.andExpect(status().isOk())
                //.andDo(print())
                .andDo(SpreadDocument.receive());
    }
}
