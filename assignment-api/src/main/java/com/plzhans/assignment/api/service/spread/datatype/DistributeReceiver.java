package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plzhans.assignment.common.entity.SpreadAmountEntity;

/**
 * The type Distribute receiver dto.
 */
public class DistributeReceiver {
    /**
     * 받은 금액
     */
    @JsonProperty("amount")
    int amount;

    /**
     * 받은사용자 아이디
     */
    @JsonProperty("user_id")
    int userId;

    public DistributeReceiver(SpreadAmountEntity entity) {
        this.amount = entity.getAmount();
        this.userId = entity.getReceiverId();
    }
}
