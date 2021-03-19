package com.plzhans.assignment.api.service.spread;

import com.plzhans.assignment.api.auth.AuthRoomRequester;
import com.plzhans.assignment.api.infra.lock.ILock;
import com.plzhans.assignment.api.infra.lock.LockInfra;
import com.plzhans.assignment.api.repository.SpreadRepository;
import com.plzhans.assignment.api.repository.cache.CacheRepository;
import com.plzhans.assignment.api.service.spread.datatype.DistributeParam;
import com.plzhans.assignment.api.service.spread.datatype.DistributeReceiveResultCode;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import com.plzhans.assignment.common.error.ClientError;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * The type Spread service impl test.
 */
//@ActiveProfiles("debug")
@DisplayName("SpreadService - Test")
@ExtendWith(SpringExtension.class)
public class SpreadServiceImplTest {

    @MockBean
    private SpreadRepository spreadRepository;
    @MockBean
    CacheRepository cacheRepository;
    @MockBean
    LockInfra lockInfra;

    private SpreadService spreadService;
    private AuthRoomRequester testRequester;

    private SpreadEventEntity createTestSpreadEventEntity(String token) {
        val entity = SpreadEventEntity.builder()
                .state(SpreadState.Active)
                .token(token)
                .userId(testRequester.getUserId())
                .roomId(testRequester.getRoomId())
                .totalAmount(1000)
                .receiverCount(4)
                .expiredSeconds(1)
                .build();
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(entity.getCreatedAt());
        entity.addAmount(new int[]{100, 200, 300, 400});
        return entity;
    }

    class TestLock implements ILock {
        @Override
        public boolean tryLock(long time, long waitTime) throws InterruptedException {
            return true;
        }
    }

    /**
     * Init.
     */
    @BeforeEach
    public void init() {
        //
        given(this.cacheRepository.getValue(anyString())).willReturn(null);
        given(this.lockInfra.getLock(anyString(), anyInt())).willReturn(new TestLock());

        //
        this.spreadService = new SpreadServiceImpl(spreadRepository, cacheRepository, lockInfra);
        this.testRequester = new AuthRoomRequester(3019, "test-room");
    }

    /**
     * 기본 로직
     *
     * @throws Exception the exception
     */
    @DisplayName("기본 로직")
    @Test
    public void test_logic() throws Exception {

        // GIVEN
        int totalAmount = 1000;
        int totalCount = 5;

        given(spreadRepository.save(any(SpreadEventEntity.class))).willAnswer((Answer<SpreadEventEntity>) invocation -> {
            SpreadEventEntity data = (SpreadEventEntity) invocation.getArguments()[0];
            if (data.getNo() == 0) {
                data.setNo(1);
                data.setCreatedAt(LocalDateTime.now());
                data.setUpdatedAt(data.getCreatedAt());

                given(spreadRepository.findByTokenAndRoomId(data.getToken(), data.getRoomId())).willReturn(data);
            }
            return data;
        });

        // WHEN
        val param = DistributeParam.builder()
                .amount(totalAmount)
                .receiverCount(totalCount)
                .build();

        val result = this.spreadService.distribute(testRequester, param);

        // THEN
        assertNotNull(result.getToken());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getExpiredDate());

        // TEST : getDistributeStatusByToken
        // WHEN
        val result2 = this.spreadService.getDistributeStatusByToken(testRequester, result.getToken());

        // THEN
        assertNotNull(result2.getData());
        assertEquals(result2.getData().getCreatedDate(), result.getCreatedDate());
        assertEquals(result2.getData().getTotalAmount(), param.getAmount());

        // TEST : receiveByToken
        int tempAmount = 0;
        for (int i = 0; i < totalCount; i++) {
            // WHEN
            val receiver = new AuthRoomRequester(testRequester.getUserId() + i + 1, testRequester.getRoomId());
            val receiveResult = this.spreadService.receiveByToken(receiver, result.getToken());

            // THEN
            assertEquals(receiveResult.getCode(), DistributeReceiveResultCode.Ok);
            assertNotNull(receiveResult.getAmount());

            // NEXT
            tempAmount += receiveResult.getAmount();

            // WHEN
            val statusResult = this.spreadService.getDistributeStatusByToken(testRequester, result.getToken());
            assertEquals(statusResult.getData().getTotalAmount(), totalAmount);
            assertEquals(statusResult.getData().getReceivedAmount(), tempAmount);
        }

        // WHEN
        val receiver = new AuthRoomRequester(testRequester.getUserId() + totalCount + 1, testRequester.getRoomId());
        val receiveResult = this.spreadService.receiveByToken(receiver, result.getToken());

        // THEN
        assertEquals(receiveResult.getCode(), DistributeReceiveResultCode.Finished);
    }

    /**
     * 자신이 받기
     *
     * @throws Exception the exception
     */
    @DisplayName("자신이 받기 - 오류")
    @Test
    public void receiveByToken_me_receive() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        Throwable throwable = assertThrows(ClientError.InvalidParam.class, () -> {
            val receiver = testRequester;
            this.spreadService.receiveByToken(receiver, token);
        });

        // THEN
        assertEquals(throwable.getMessage(), "user_id");
    }

    /**
     * 받기시간 만료
     *
     * @throws Exception the exception
     */
    @DisplayName("받기시간 만료 - 오류")
    @Test
    public void receiveByToken_expired() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        entity.setCreatedAt(LocalDateTime.now().minusSeconds(SpreadServiceImpl.DEFAULT_DISTRIBUTE_EXPIRED_SECONDS).minusNanos(1));
        entity.setUpdatedAt(entity.getCreatedAt());
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        val receiver = new AuthRoomRequester(testRequester.getUserId() + 1, testRequester.getRoomId());
        val receiveResult = this.spreadService.receiveByToken(receiver, token);

        // THEN
        assertEquals(receiveResult.getCode(), DistributeReceiveResultCode.Expired);
        assertNotNull(receiveResult.getAmount());
    }

    /**
     * 중복 받기
     *
     * @throws Exception the exception
     */
    @DisplayName("중복 받기 - 오류")
    @Test
    public void receiveByToken_overlap() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        val receiver = new AuthRoomRequester(testRequester.getUserId() + 1, testRequester.getRoomId());
        val receiveResult = this.spreadService.receiveByToken(receiver, token);

        // THEN
        assertEquals(receiveResult.getCode(), DistributeReceiveResultCode.Ok);
        assertNotNull(receiveResult.getAmount());

        // WHEN
        val receiver2 = this.spreadService.receiveByToken(receiver, token);

        // THEN
        assertEquals(receiver2.getCode(), DistributeReceiveResultCode.Received);
    }

    /**
     * 다른 룸 접근
     *
     * @throws Exception the exception
     */
    @DisplayName("다른 룸 접근 - 오류")
    @Test
    public void receiveByToken_not_equals_room() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // THEN
        Throwable throwable = assertThrows(ClientError.Notfound.class, () -> {
            val receiver = new AuthRoomRequester(testRequester.getUserId() + 1, testRequester.getRoomId() + 1);
            this.spreadService.receiveByToken(receiver, token);
        });

        // THEN
        assertEquals(throwable.getMessage(), "spread");
    }

    /**
     * 만료된 뿌리기 조회
     *
     * @throws Exception the exception
     */
    @DisplayName("만료된 뿌리기 조회 - 오류")
    @Test
    public void getDistributeStatusByToken_expired() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        entity.setCreatedAt(LocalDateTime.now().minusDays(SpreadServiceImpl.DEFAULT_FIND_INACTIVE_DAYS).minusNanos(1));
        entity.setUpdatedAt(entity.getCreatedAt());
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        Throwable throwable = assertThrows(ClientError.Expired.class, () -> {
            // WHEN
            val invalidRequester = new AuthRoomRequester(testRequester.getUserId(), testRequester.getRoomId());
            this.spreadService.getDistributeStatusByToken(invalidRequester, token);
        });

        // THEN
        assertEquals(throwable.getMessage(), String.format("expired %d days.", SpreadServiceImpl.DEFAULT_FIND_INACTIVE_DAYS));
    }


    /**
     * 다른 유저 뿌리기 조회
     *
     * @throws Exception the exception
     */
    @DisplayName("다른 유저 뿌리기 조회 - 오류")
    @Test
    public void getDistributeStatusByToken_invalid_user() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        Throwable throwable = assertThrows(ClientError.Unauthorized.class, () -> {
            val invalidRequester = new AuthRoomRequester(testRequester.getUserId() + 1, testRequester.getRoomId());
            this.spreadService.getDistributeStatusByToken(invalidRequester, token);
        });

        // THEN
        assertEquals(throwable.getMessage(), "user_id");
    }

    /**
     * 다른 룸 뿌리기 조회
     *
     * @throws Exception the exception
     */
    @DisplayName("다른 룸 뿌리기 조회 - 오류")
    @Test
    public void getDistributeStatusByToken_invalid_room() throws Exception {

        // GIVEN
        String token = "tok";

        val entity = createTestSpreadEventEntity(token);
        given(spreadRepository.findByTokenAndRoomId(token, testRequester.getRoomId())).willReturn(entity);

        // WHEN
        Throwable throwable = assertThrows(ClientError.Notfound.class, () -> {
            val invalidRequester = new AuthRoomRequester(testRequester.getUserId(), testRequester.getRoomId() + 1);
            this.spreadService.getDistributeStatusByToken(invalidRequester, token);
        });

        // THEN
        assertEquals(throwable.getMessage(), "spread");
    }
}