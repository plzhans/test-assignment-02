package com.plzhans.assignment.common.entity;

import com.plzhans.assignment.common.domain.spread.SpreadAmountState;
import com.plzhans.assignment.common.entity.convert.SpreadAttributeConverters;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Table(name = "t_spread_amount")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SpreadAmountEntity extends BaseDatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    int no;

    @Convert(converter = SpreadAttributeConverters._SpreadAmountState.class)
    @Column(name = "state")
    SpreadAmountState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    SpreadEventEntity event;

    @Column(name = "amount")
    int amount;

    @Column(name = "receiver_id")
    int receiverId;

    @Column(name = "received_date")
    LocalDateTime receivedDate;

    public void setReceived(int receiverId) {
        this.state = SpreadAmountState.Received;
        this.receivedDate = LocalDateTime.now();
        this.receiverId = receiverId;
    }

    public boolean isReady() {
        return this.state == SpreadAmountState.Ready && this.receiverId == 0;
    }
}
