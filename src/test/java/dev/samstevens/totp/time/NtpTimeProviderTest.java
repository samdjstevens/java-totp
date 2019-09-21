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

        assertTrue(currentTime > 1569076881);
    }

    @Test(expected = UnknownHostException.class)
    public void testUnknownHostThrowsException() throws UnknownHostException {
        new NtpTimeProvider("sdfsf/safsf");
    }

    @Test(expected = TimeProviderException.class)
    public void testNonNtpHostThrowsException() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("www.example.com");
        time.getTime();
    }
}
