package com.plzhans.assignment.api.infra.lock;

/**
 * The interface Lock.
 */
public interface ILock {
    /**
     * Try lock boolean.
     *
     * @param time     the time
     * @param waitTime the wait time
     * @return the boolean
     * @throws InterruptedException the interrupted exception
     */
    boolean tryLock(long time, long waitTime) throws InterruptedException;
}
