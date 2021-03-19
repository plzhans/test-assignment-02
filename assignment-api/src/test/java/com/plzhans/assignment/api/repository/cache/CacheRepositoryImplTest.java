package com.plzhans.assignment.api.repository.cache;

import com.plzhans.assignment.api.config.RedisConfig;
import com.plzhans.assignment.api.config.RedisEmbeddedConfig;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("CacheRepository - Test")
//@EnableConfigurationProperties({
//        RedisEmbedServerImpl.class,
//        RedisConfig.class
//})
@TestPropertySource(locations = "classpath:application.yml")
@ContextConfiguration(classes = {
        RedisEmbeddedConfig.class,
        RedisConfig.class
})
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CacheRepositoryImplTest {

    @Autowired
    RedissonClient redissonClient;

    CacheRepository cacheRepository;

    @BeforeEach
    public void init() {
        cacheRepository = new CacheRepositoryImpl(redissonClient);
    }

    @DisplayName("기본 테스트")
    @Test
    public void test() {

        // WHEN
        cacheRepository.setValue("junittest", "junitvalue", 100);
        val value = cacheRepository.<String>getValue("junittest");

        // THEN
        assertEquals(value, "junitvalue");

        // WHEN
        val removeResult = cacheRepository.removeValue("junittest");

        // THEN
        assertEquals(removeResult, true);

        // WHEN
        val value2 = cacheRepository.<String>getValue("junittest");

        // THEN
        assertNull(value2);

    }
}