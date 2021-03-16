package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * The type Distribute receive result.
 */
@Builder
@Getter
public class DistributeReceiveResult {
    /**
     * 받음 금액
     */
    @JsonProperty("amount")
    int amount;
}
