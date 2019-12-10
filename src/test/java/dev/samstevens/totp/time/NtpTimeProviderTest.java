package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import org.junit.Test;
import java.net.UnknownHostException;
import static org.junit.Assert.*;

public class NtpTimeProviderTest {

    @Test
    public void testProvidesTime() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("pool.ntp.org");
        long currentTime = time.getTime();

        // epoch should be 10 digits for the foreseeable future...
        assertEquals(10, String.valueOf(currentTime).length());
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostThrowsException() throws UnknownHostException {
        new NtpTimeProvider("sdfsf/safsf");
    }

    @Test(expected = TimeProviderException.class)
    public void testNonNtpHostThrowsException() throws UnknownHostException {
        try {
            TimeProvider time = new NtpTimeProvider("www.example.com");
            time.getTime();
        } catch (TimeProviderException e) {
            // Assert there is a cause for the exception
            assertNotNull(e.getCause());
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testRequiresDependency() throws UnknownHostException {
        try {
            // Use package-private constructor to test depending on a non-existing "fake.class.Here" class
            TimeProvider time = new NtpTimeProvider("www.example.com", "fake.class.Here");
        } catch (RuntimeException e) {
            assertEquals("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.", e.getMessage());
            throw e;
        }
    }
}
