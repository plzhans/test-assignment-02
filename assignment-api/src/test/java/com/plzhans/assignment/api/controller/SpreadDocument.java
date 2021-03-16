package com.plzhans.assignment.api.controller;

import com.plzhans.assignment.api.service.spread.datatype.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static com.plzhans.assignment.api.DocumentUtils.getDocumentRequest;
import static com.plzhans.assignment.api.DocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

@Slf4j
public class SpreadDocument {

    private static final String DEFAULT_IDENTIFIER = "{class-name}/${method-name}";

    private static RestDocumentationResultHandler defaultDocument(Snippet... snippets) {
        return document(DEFAULT_IDENTIFIER, getDocumentRequest(), getDocumentResponse(), snippets);
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
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("받은 금액")
                )
        );
    }
}