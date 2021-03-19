package com.plzhans.assignment.api.service.spread.datatype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plzhans.assignment.common.domain.DateTimeFormatter;
import com.plzhans.assignment.common.domain.spread.SpreadAmountState;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Distribute status dto.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DistributeStatus {
    /**
     * 뿌린 시각
     */
    @JsonFormat(pattern = DateTimeFormatter.DEFAULT_ISO_PATTERN)
    @JsonProperty("created_date")
    LocalDateTime createdDate;

    /**
     * 뿌린 금액
     */
    @JsonProperty("total_amount")
    int totalAmount;

    /**
     * 받기 완료된 금액
     */
    @JsonProperty("received_amount")
    int receivedAmount;

    /**
     * 받기 완료된 정보
     */
    @JsonProperty("receivers")
    List<DistributeReceiver> receivers;

    public DistributeStatus(SpreadEventEntity entity) {
        this.createdDate = entity.getCreatedAt();
        this.totalAmount = entity.getTotalAmount();
        val amounts = entity.getAmounts();
        if (amounts != null) {
            this.receivers = new ArrayList<>();
            amounts.forEach(x -> {
                // 받은 금액만
                if (x.getState() == SpreadAmountState.Received) {
                    this.receivers.add(new DistributeReceiver(x));
                    this.receivedAmount += x.getAmount();
                }
            });
        }

    }
}
