package com.plzhans.assignment.api.service.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * The type Redis lock.
 */
public class RedisLock implements ILock {

    /**
     * The Rlock.
     */
    RLock rlock;

    /**
     * Instantiates a new Redis lock.
     *
     * @param rlock the rlock
     */
    public RedisLock(RLock rlock) {
        this.rlock = rlock;
    }

    @Override
    public boolean tryLock(long waitTimeMs, long leaseTimeMs) throws InterruptedException {
        return this.rlock.tryLock(waitTimeMs, leaseTimeMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void release() {
        this.rlock.unlock();
    }
}
