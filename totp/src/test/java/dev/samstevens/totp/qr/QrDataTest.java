package dev.samstevens.totp.qr;

import dev.samstevens.totp.code.HashingAlgorithm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testNullFieldUriGeneration() {
        QrData data = new QrData.Builder()
                .label(null)
                .secret(null)
                .issuer("AppName AppCorp")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(30)
                .build();

        assertEquals(
            "otpauth://totp/?secret=&issuer=AppName%20AppCorp&algorithm=SHA256&digits=6&period=30",
            data.getUri()
        );
    }
}
