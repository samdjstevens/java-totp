package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NtpTimeProvider implements TimeProvider {

    private final NTPUDPClient client;
    private final InetAddress ntpHost;

    public NtpTimeProvider(String ntpHostname) throws UnknownHostException {
        // default timeout of  3 seconds
        this(ntpHostname, 3000);
    }

    public NtpTimeProvider(String ntpHostname, int timeout) throws UnknownHostException {
        this(ntpHostname, timeout, "org.apache.commons.net.ntp.NTPUDPClient");
    }

    // Package-private, for tests only
    NtpTimeProvider(String ntpHostname, String dependentClass) throws UnknownHostException {
        // default timeout of  3 seconds
        this(ntpHostname, 3000, dependentClass);
    }

    private NtpTimeProvider(String ntpHostname, int timeout, String dependentClass) throws UnknownHostException {
        // Check the optional commons-net dependency is on the classpath
        checkHasDependency(dependentClass);

        client = new NTPUDPClient();
        client.setDefaultTimeout(timeout);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public long getTime() throws TimeProviderException {
        TimeInfo timeInfo;
        try {
            timeInfo = client.getTime(ntpHost);
            timeInfo.computeDetails();
        } catch (Exception e) {
            throw new TimeProviderException("Failed to provide time from NTP server. See nested exception.", e);
        }

        if (timeInfo.getOffset() == null) {
            throw new TimeProviderException("Failed to calculate NTP offset");
        }

        return (System.currentTimeMillis() + timeInfo.getOffset()) / 1000;
    }

    private void checkHasDependency(String dependentClass) {
        try {
            Class<?> ntpClientClass = Class.forName(dependentClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.");
        }
    }
}
