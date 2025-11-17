package com.fhc.alcatrazclientspring.exception;

/**
 * Wird geworfen, wenn ein State-Resync fehlschlägt.
 */
public class ResyncException extends RuntimeException {

    public ResyncException(String message) {
        super(message);
    }

    public ResyncException(String message, Throwable cause) {
        super(message, cause);
    }
}