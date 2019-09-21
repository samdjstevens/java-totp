package dev.samstevens.totp.time;

import dev.samstevens.totp.exceptions.TimeProviderException;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NtpTimeProvider implements TimeProvider {

    private final NTPUDPClient client;
    private final InetAddress ntpHost;

    public NtpTimeProvider(String ntpHostname) throws UnknownHostException {
        client = new NTPUDPClient();
        client.setDefaultTimeout(3000);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public long getTime() throws TimeProviderException {
        try {
            TimeInfo timeInfo = client.getTime(ntpHost);

            return timeInfo.getReturnTime();
        } catch (IOException e) {
            throw new TimeProviderException();
        }
    }
}
