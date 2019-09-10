package dev.samstevens.totp.qr;

import org.junit.Test;
import static org.junit.Assert.*;

public class QrDataTest {

    @Test
    public void testUriGeneration() {
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret("the-secret-here")
                .issuer("AppName AppCorp")
                .digits(6)
                .period(30)
                .build();

        assertEquals(
            "otpauth://totp/example%40example.com?secret=the-secret-here&issuer=AppName%20AppCorp&algorithm=SHA1&digits=6&period=30",
            data.getUri()
        );
    }
}
