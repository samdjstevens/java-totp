package dev.samstevens.totp.time;

import org.junit.Test;
import java.time.Instant;
import static org.junit.Assert.*;

public class SystemTimeProviderTest {

    @Test
    public void testProvidesTime()
    {
        long currentTime = Instant.now().getEpochSecond();
        TimeProvider time = new SystemTimeProvider();
        assertEquals(currentTime, time.getTime());
    }
}
