package com.plzhans.assignment.api.infra.lock;

import com.plzhans.assignment.api.config.RedisConfig;
import com.plzhans.assignment.api.config.RedisEmbeddedConfig;
import com.plzhans.assignment.api.service.lock.LockInfra;
import com.plzhans.assignment.api.service.lock.RedisLockInfraImpl;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("RedisLockInfra - Test")
@TestPropertySource(locations = "classpath:application.yml")
@ContextConfiguration(classes = {
        RedisEmbeddedConfig.class,
        RedisConfig.class
})
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisLockInfraImplTest {

    @Autowired
    RedissonClient redissonClient;

    LockInfra lockInfra;

    @BeforeEach
    public void init() {
        lockInfra = new RedisLockInfraImpl(redissonClient);
    }

    @DisplayName("기본 테스트")
    @Test
    public void test() throws InterruptedException {
        // THEN
        val lock = lockInfra.getLock("test");
        boolean lockOkey = lock.tryLock(1000, 1000);

        // WHEN
        assertEquals(lockOkey, true);
    }

}