package dev.samstevens.totp.qr;

import dev.samstevens.totp.code.HashingAlgorithm;
import org.junit.Test;
import static org.junit.Assert.*;

public class QrDataTest {

    @Test
    public void testUriGeneration() {
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret("the-secret-here")
                .issuer("AppName AppCorp")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(30)
                .build();

        assertEquals(
            "otpauth://totp/example%40example.com?secret=the-secret-here&issuer=AppName%20AppCorp&algorithm=SHA256&digits=6&period=30",
            data.getUri()
        );
    }
}
