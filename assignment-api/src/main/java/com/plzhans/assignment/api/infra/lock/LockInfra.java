package com.plzhans.assignment.api.infra.lock;

/**
 * The interface Lock infra.
 */
public interface LockInfra {
    /**
     * Gets lock.
     *
     * @param key the key
     * @return the lock
     */
    ILock getLock(String key);

    /**
     * Gets lock.
     *
     * @param key1 the key 1
     * @param key2 the key 2
     * @return the lock
     */
    default ILock getLock(String key1, String key2) {
        return this.getLock(String.join(":", key1, key2));
    }

    /**
     * Gets lock.
     *
     * @param key1 the key 1
     * @param key2 the key 2
     * @return the lock
     */
    default ILock getLock(String key1, int key2) {
        return this.getLock(String.format("%s:%d", key1, key2));
    }

    /**
     * Gets lock.
     *
     * @param key1 the key 1
     * @param key2 the key 2
     * @return the lock
     */
    default ILock getLock(int key1, String key2) {
        return this.getLock(String.format("%s:%d", key1, key2));
    }

    /**
     * Gets lock.
     *
     * @param key1 the key 1
     * @param key2 the key 2
     * @return the lock
     */
    default ILock getLock(int key1, int key2) {
        return this.getLock(String.format("%d:%s", key1, key2));
    }
}
