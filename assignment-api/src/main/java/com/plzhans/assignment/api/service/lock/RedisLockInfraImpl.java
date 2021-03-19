package com.plzhans.assignment.api.service.lock;

import lombok.val;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * The type Redis lock infra.
 */
@Component
public class RedisLockInfraImpl implements LockInfra {

    private RedissonClient redissonClient;

    /**
     * Instantiates a new Redis lock infra.
     *
     * @param redissonClient the redisson client
     */
    public RedisLockInfraImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public ILock getLock(String key) {
        val rlock = redissonClient.getLock(key);
        ILock lock = new RedisLock(rlock);
        return lock;
    }
}
