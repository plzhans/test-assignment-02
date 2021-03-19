package com.plzhans.assignment.common.entity;

import com.plzhans.assignment.common.domain.spread.SpreadAmountState;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.convert.SpreadAttributeConverters;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Test entity.
 */
@Builder
@EqualsAndHashCode(callSuper=false)
@Data
@ToString(exclude = "amounts")
@Entity
@Table(name = "t_spread_event")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SpreadEventEntity extends BaseDatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    int no;

    @Convert(converter = SpreadAttributeConverters._SpreadState.class)
    @Column(name = "state")
    SpreadState state;

    @Column(name = "user_id")
    int userId;

    @Column(name = "room_id")
    String roomId;

    @Column(name = "token")
    String token;

    @Column(name = "total_amount")
    int totalAmount;

    @Column(name = "receiver_count")
    int receiverCount;

    @Column(name = "expired_seconds")
    int expiredSeconds;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SpreadAmountEntity> amounts;

    public LocalDateTime getExpiredDate() {
        return this.createdAt.plusSeconds(this.expiredSeconds);
    }

    public void addAmount(int[] values) {
        if (this.amounts == null) {
            this.amounts = new ArrayList<>();
        }
        for (int value : values) {
            this.amounts.add(SpreadAmountEntity.builder()
                    .event(this)
                    .state(SpreadAmountState.Ready)
                    .amount(value)
                    .build());
        }
    }
}

