package com.plzhans.assignment.common.error;

/**
 * The type Common exceptions.
 */
public class ClientError {

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
    public static class Unauthorized extends Exception {
        /**
         * Instantiates a new Invalid param exception.
         *
         * @param message the message
         */
        public Unauthorized(String message) {
            super(message);
        }
    }

    /**
     * The type Bad request.
     */
    public static class BadRequest extends Exception {
        /**
         * Instantiates a new Invalid param exception.
         *
         * @param message the message
         */
        public BadRequest(String message) {
            super(message);
        }
    }

    /**
     * The type Invalid param exception.
     */
    public static class InvalidParam extends Exception {
        /**
         * Instantiates a new Invalid param exception.
         *
         * @param message the message
         */
        public InvalidParam(String message) {
            super(message);
        }
    }

    /**
     * The type Nofound data exception.
     */
    public static class Notfound extends Exception {
        /**
         * Instantiates a new Notfound.
         *
         * @param message the message
         */
        public Notfound(String message) {
            super(message);
        }
    }

    /**
     * The type Expired.
     */
    public static class Expired extends Exception {
        /**
         * Instantiates a new Invalid param exception.
         *
         * @param message the message
         */
        public Expired(String message) {
            super(message);
        }
    }
}



