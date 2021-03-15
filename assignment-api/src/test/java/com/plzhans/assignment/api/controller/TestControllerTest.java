package com.plzhans.assignment.api.controller;

import com.plzhans.assignment.api.service.test.TestService;
import com.plzhans.assignment.api.service.test.datatype.TestDto;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.plzhans.assignment.api.DocumentUtils.getDocumentRequest;
import static com.plzhans.assignment.api.DocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
@AutoConfigureRestDocs()
//@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;

    @Test
    public void test() throws Exception {

        // GIVEN
        val list = new ArrayList<TestDto>();
        list.add(TestDto.builder()
                .no(1)
                .name("이름")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        given(testService.getAllTests()).willReturn(list);

        // WHEN
        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/test")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // THEN
        result.andExpect(status().isOk())
                .andDo(document("{class-name}/${method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
//                        pathParameters(
//                                parameterWithName("id").description("아이디")
//                        ),
//                        requestFields(
//                                fieldWithPath("firstName").type(JsonFieldType.STRING).description("이름"),
//                                fieldWithPath("lastName").type(JsonFieldType.STRING).description("성"),
//                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
//                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("취미")
//                        ),
                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
                                fieldWithPath("[].no").type(JsonFieldType.NUMBER).description("번호"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성일"),
                                fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("수정일")
                        )
                ));
    }
}
