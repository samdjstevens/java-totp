package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import org.junit.jupiter.api.Test;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;

public class NtpTimeProviderTest {

    @Test
    public void testProvidesTime() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("pool.ntp.org");
        long currentTime = time.getTime();

        // epoch should be 10 digits for the foreseeable future...
        assertEquals(10, String.valueOf(currentTime).length());
    }

    @Test
    public void testUnknownHostThrowsException() {
        assertThrows(UnknownHostException.class, () -> {
            new NtpTimeProvider("sdfsf/safsf");
        });
    }

    @Test
    public void testNonNtpHostThrowsException() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("www.example.com");

        TimeProviderException e = assertThrows(TimeProviderException.class, time::getTime);
        assertNotNull(e.getCause());
    }

    @Test
    public void testRequiresDependency() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> {
            // Use package-private constructor to test depending on a non-existing "fake.class.Here" class
            new NtpTimeProvider("www.example.com", "fake.class.Here");
        });

        assertEquals("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.", e.getMessage());
    }
}
