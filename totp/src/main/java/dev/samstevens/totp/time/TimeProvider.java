package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import java.time.Instant;

public interface TimeProvider {
    /**
     * @return The current time represented as an Instant object.
     */
    Instant getTime() throws TimeProviderException;
}
