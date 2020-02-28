package dev.samstevens.totp.exceptions;

public final class TimeProviderException extends RuntimeException {
    public TimeProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
