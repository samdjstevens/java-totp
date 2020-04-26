package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;

public class NtpTimeProvider implements TimeProvider {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(3);
    private final NTPUDPClient client;
    private final InetAddress ntpHost;

    public NtpTimeProvider(String ntpHostname) throws UnknownHostException {
        this(ntpHostname, DEFAULT_TIMEOUT);
    }

    public NtpTimeProvider(String ntpHostname, Duration timeout) throws UnknownHostException {
        this(ntpHostname, timeout, "org.apache.commons.net.ntp.NTPUDPClient");
    }

    // Package-private, for tests only
    NtpTimeProvider(String ntpHostname, String dependentClass) throws UnknownHostException {
        this(ntpHostname, DEFAULT_TIMEOUT, dependentClass);
    }

    private NtpTimeProvider(String ntpHostname, Duration timeout, String dependentClass) throws UnknownHostException {
        // Check the optional commons-net dependency is on the classpath
        checkHasDependency(dependentClass);

        client = new NTPUDPClient();
        client.setDefaultTimeout((int) timeout.toMillis());
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public Instant getTime() throws TimeProviderException {
        try {
            TimeInfo timeInfo = client.getTime(ntpHost);

            return Instant.ofEpochSecond((long) Math.floor(timeInfo.getReturnTime() / 1000L));
        } catch (Exception e) {
            throw new TimeProviderException("Failed to provide time from NTP server. See nested exception.", e);
        }
    }

    private void checkHasDependency(String dependentClass) {
        try {
            Class.forName(dependentClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.");
        }
    }
}
