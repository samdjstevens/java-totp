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
        client = new NTPUDPClient();
        client.setDefaultTimeout(timeout);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public long getTime() throws TimeProviderException {
        try {
            TimeInfo timeInfo = client.getTime(ntpHost);

            return (long) Math.floor(timeInfo.getReturnTime() / 1000L);
        } catch (Exception e) {
            throw new TimeProviderException("Failed to provide time from NTP server. See nested exception.", e);
        }
    }
}
