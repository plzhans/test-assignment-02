package com.plzhans.assignment.api.service.lock;

/**
 * The interface Lock.
 */
public interface ILock {

    /**
     * Try lock boolean.
     *
     * @param waitTimeMs  the wait time
     * @param leaseTimeMs the lease time
     * @return the boolean
     * @throws InterruptedException the interrupted exception
     */
    boolean tryLock(long waitTimeMs, long leaseTimeMs) throws InterruptedException;

    /**
     * Un lock.
     */
    void release();
}
