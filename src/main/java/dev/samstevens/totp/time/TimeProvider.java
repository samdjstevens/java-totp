package dev.samstevens.totp.time;

public interface TimeProvider {
    /**
     * Get the number of seconds since Jan 1st 1970, 00:00:00.
     */
    long getTime();
}
