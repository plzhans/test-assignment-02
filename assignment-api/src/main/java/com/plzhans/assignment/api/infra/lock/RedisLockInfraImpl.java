package com.plzhans.assignment.api.infra.lock;

import lombok.val;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

/**
 * The type Redis lock infra.
 */
@Repository
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
