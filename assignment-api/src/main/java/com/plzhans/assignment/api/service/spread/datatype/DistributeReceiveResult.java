package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Distribute receive result.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DistributeReceiveResult {
    /**
     * 결과 코드
     */
    @JsonProperty("code")
    DistributeReceiveResultCode code;

    /**
     * 받음 금액
     */
    @JsonProperty("amount")
    int amount;

    /**
     * Instantiates a new Distribute receive result.
     *
     * @param code the code
     */
    public DistributeReceiveResult(DistributeReceiveResultCode code) {
        this.code = code;
    }
}
