package com.plzhans.assignment.api.repository;

import com.plzhans.assignment.api.service.spread.SpreadTokenGenerator;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import com.plzhans.assignment.common.entity.SpreadEventEntity;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@EnableJpaRepositories({"com.plzhans.assignment.common.repository", "com.plzhans.assignment.api.repository"})
@EnableJpaAuditing
@EntityScan({"com.plzhans.assignment.common.entity", "com.plzhans.assignment.api.entity"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("debug")
@ExtendWith(SpringExtension.class)
class SpreadRepositoryTest {
    @Autowired
    SpreadRepository spreadRepository;

    @Test
    public void test_ok() {

        // WHEN
        val entity = SpreadEventEntity.builder()
                .state(SpreadState.Active)
                .token(new SpreadTokenGenerator().nextToken())
                .userId(1234)
                .roomId("room-x")
                .totalAmount(1000)
                .receiverCount(4)
                .expiredSeconds(20)
                .build();
        entity.addAmount(new int[]{100,200,300,400});
        this.spreadRepository.save(entity);

        // THEN
        assertEquals(entity.getNo(),1);
    }
}