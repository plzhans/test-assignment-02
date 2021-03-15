package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DistributeReceiveResult {
    @JsonProperty("amount")
    int amount;
}
