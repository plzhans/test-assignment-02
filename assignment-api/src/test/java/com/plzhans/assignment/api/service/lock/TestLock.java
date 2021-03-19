package com.plzhans.assignment.api.service.lock;

import com.plzhans.assignment.api.infra.lock.ILock;

public class TestLock implements ILock {
    @Override
    public boolean tryLock(long time, long waitTime) throws InterruptedException {
        return true;
    }

    @Override
    public void unLock() {
    }
}