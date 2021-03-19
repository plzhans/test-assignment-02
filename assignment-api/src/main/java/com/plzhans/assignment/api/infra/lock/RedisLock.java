package com.plzhans.assignment.api.infra.lock;

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
    public boolean tryLock(long waitTime, long leaseTime) throws InterruptedException {
        boolean locked = this.rlock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        return locked;
    }

    @Override
    public void unLock() {
        this.rlock.unlock();
    }
}
