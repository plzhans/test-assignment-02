package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DistributeRequest {
    @JsonProperty("amount")
    int amount;
    @JsonProperty("receiver_count")
    int receiverCount;
}
