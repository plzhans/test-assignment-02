package com.plzhans.assignment.api.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    }
}