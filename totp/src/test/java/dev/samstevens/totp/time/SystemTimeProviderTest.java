package dev.samstevens.totp.time;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTimeProviderTest {

    @Test
    public void testProvidesTime()
    {
        Instant currentTime = Instant.now();

        TimeProvider time = new SystemTimeProvider();
        Instant providedTime = time.getTime();

        // allow +/-3 second discrepancy for test environments
        Duration difference = Duration.between(currentTime, providedTime);
        assertTrue(Math.abs(difference.getSeconds()) < 3);

        // epoch should be 10 digits for the foreseeable future...
        assertEquals(10, String.valueOf(providedTime.getEpochSecond()).length());
    }
}
