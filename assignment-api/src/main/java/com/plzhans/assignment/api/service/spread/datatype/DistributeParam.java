package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;


/**
 * The type Distribute request.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DistributeParam {
    /**
     * The Amount.
     */
    @Min(value = 1, message = "금액을 0이상 입력하세요.")
    @JsonProperty("amount")
    int amount;

    /**
     * The Receiver count.
     */
    @Min(value = 1, message = "인원수를 0이상 입력하세요.")
    @JsonProperty("receiver_count")
    int receiverCount;
}
