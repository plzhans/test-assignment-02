package com.plzhans.assignment.common.error;

/**
 * The type Common exceptions.
 */
public class ServerError {

    /**
     * The type Exception.
     */
    public static class Exception extends RuntimeException {
        /**
         * Instantiates a new Invalid param exception.
         *
         * @param message the message
         */
        public Exception(String message) {
            super(message);
        }
    }

    /**
     * The type Unauthorized.
     */
    public static class ConcurrentModification extends Exception {

        /**
         * Instantiates a new Concurrent modification.
         *
         * @param message the message
         */
        public ConcurrentModification(String message) {
            super(message);
        }
    }

}



