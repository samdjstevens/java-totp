package dev.samstevens.totp.qr;

import dev.samstevens.totp.code.HashingAlgorithm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QrDataFactoryTest {

    @Test
    public void testFactorySetsDefaultsOnBuilder()
    {
        QrDataFactory qrDataFactory = new QrDataFactory(HashingAlgorithm.SHA256, 6, 30);
        QrData data = qrDataFactory.newBuilder().build();

        assertEquals(HashingAlgorithm.SHA256.getFriendlyName(), data.getAlgorithm());
        assertEquals(6, data.getDigits());
        assertEquals(30, data.getPeriod());
    }
}
