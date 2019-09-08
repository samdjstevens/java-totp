package dev.samstevens.totp.time;

import java.time.Instant;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public long getTime() {
        return Instant.now().getEpochSecond();
    }
}
