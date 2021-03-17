package com.plzhans.assignment.api.controller;

import com.plzhans.assignment.api.service.spread.datatype.*;
import com.plzhans.assignment.common.domain.CodeEnumable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@Slf4j
public class SpreadDocument {

    private static final String DEFAULT_IDENTIFIER = "{class-name}/${method-name}";

    private static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
//                        .scheme("https")
//                        .host("docs.api.com")
                        .removePort(),
                prettyPrint());
    }

    private static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

    private static RestDocumentationResultHandler defaultDocument(Snippet... snippets) {
        return document(DEFAULT_IDENTIFIER, getDocumentRequest(), getDocumentResponse(), snippets);
    }

    private static String getEnumDescription(String title, Class<?> clazz) {
        String newLine = String.format("%n", "");
        StringBuilder sbText = new StringBuilder();
        sbText.append(title).append(" +").append(newLine);
        for (Object obj : clazz.getEnumConstants()) {
            CodeEnumable code = (CodeEnumable) obj;
            if (obj instanceof CodeEnumable) {
                sbText.append(code.getInt()).append(" : ").append(code.getString()).append(" +").append(newLine);
            } else {
                sbText.append(obj);
            }
        }
        return sbText.toString();
    }

    public static RestDocumentationResultHandler distribute() {
        log.debug("responseObject:%s", DistributeResult.class);
        return defaultDocument(
                requestFields(
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("금액"),
                        fieldWithPath("receiver_count").type(JsonFieldType.NUMBER).description("인원수")
                ),
                responseFields(
                        fieldWithPath("token").type(JsonFieldType.STRING).description("Token"),
                        fieldWithPath("created_date").type(JsonFieldType.STRING).description("뿌린 시간"),
                        fieldWithPath("expired_date").type(JsonFieldType.STRING).description("만료 시간")
                )
        );
    }

    public static RestDocumentationResultHandler distribute_status() {
        log.debug("responseObject:%s", DistributeStatusResult.class);
        log.debug("responseObject:%s", DistributeStatus.class);
        log.debug("responseObject:%s", DistributeReceiver.class);
        return defaultDocument(
                requestParameters(
                        parameterWithName("token").description("Token")
                ),
                responseFields(
                        fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("뿌린 시간"),
                        fieldWithPath("data.total_amount").type(JsonFieldType.NUMBER).description("뿌린 금액"),
                        fieldWithPath("data.received_amount").type(JsonFieldType.NUMBER).description("받기 완료된 금액"),
                        fieldWithPath("data.receivers[].amount").type(JsonFieldType.NUMBER).description("받은 금액"),
                        fieldWithPath("data.receivers[].user_id").type(JsonFieldType.NUMBER).description("받은사용자 아이디")
                )
        );
    }

    public static RestDocumentationResultHandler receive() {
        log.debug("responseObject:%s", DistributeReceiveResult.class);

        return defaultDocument(
                requestParameters(
                        parameterWithName("token").description("Token")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description(getEnumDescription("결과 코드",DistributeReceiveResultCode.class)),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("받은 금액")
                )
        );
    }

}

