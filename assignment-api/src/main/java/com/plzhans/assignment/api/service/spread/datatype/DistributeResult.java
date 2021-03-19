package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plzhans.assignment.common.domain.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * The type Distribute result.
 */
@Builder
@Getter
public class DistributeResult {
    /**
     * Token.
     */
    @JsonProperty("token")
    String token;

    /**
     * 뿌린 시간
     */
    @JsonFormat(pattern = DateTimeFormatter.DEFAULT_ISO_PATTERN)
    @JsonProperty("created_date")
    LocalDateTime createdDate;

    /**
     * 만료 시간
     */
    @JsonFormat(pattern = DateTimeFormatter.DEFAULT_ISO_PATTERN)
    @JsonProperty("expired_date")
    LocalDateTime expiredDate;
}
