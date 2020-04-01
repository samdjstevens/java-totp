package dev.samstevens.totp.time;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTimeProviderTest {

    @Test
    public void testProvidesTime()
    {
        long currentTime = Instant.now().getEpochSecond();
        TimeProvider time = new SystemTimeProvider();
        long providedTime = time.getTime();

        // allow +=5 second discrepancy for test environments
        assertTrue(currentTime - 5 <= providedTime);
        assertTrue(providedTime <= currentTime + 5);

        // epoch should be 10 digits for the foreseeable future...
        assertEquals(10, String.valueOf(currentTime).length());
    }
}
