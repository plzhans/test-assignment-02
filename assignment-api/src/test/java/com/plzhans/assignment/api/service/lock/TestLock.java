package com.plzhans.assignment.api.service.lock;

/**
 * The type Test lock.
 */
public class TestLock implements ILock {
    @Override
    public boolean tryLock(long waitTimeMs, long leaseTimeMs) {
        return true;
    }

    @Override
    public void release() {
    }
}