package com.plzhans.assignment.api.repository.cache;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * The type Cache repository.
 */
@Repository
public class CacheRepositoryImpl implements CacheRepository {

    private RedissonClient redissonClient;

    /**
     * Instantiates a new Cache repository.
     *
     * @param redissonClient the redisson client
     */
    public CacheRepositoryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> void setValue(String key, T value, int expiredSeconds) {
        redissonClient.getBucket(key).set(value,expiredSeconds, TimeUnit.SECONDS );
    }

    @Override
    public <T> T getValue(String key) {
        T value = redissonClient.<T>getBucket(key).get();
        return value;
    }
}
