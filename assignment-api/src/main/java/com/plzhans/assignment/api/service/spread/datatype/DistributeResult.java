package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
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
     * The Token.
     */
    @JsonProperty("token")
    String token;
    /**
     * 뿌린 시간
     */
    @JsonProperty("created_date")
    LocalDateTime createdDate;
    /**
     * 만료 시간
     */
    @JsonProperty("expired_date")
    LocalDateTime expiredDate;
}
