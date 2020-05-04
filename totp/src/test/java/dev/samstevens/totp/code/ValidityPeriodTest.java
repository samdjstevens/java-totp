package dev.samstevens.totp.code;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class ValidityPeriodTest {

    @Test
    public void testEquality() {
        ValidityPeriod period1 = new ValidityPeriod(Instant.ofEpochSecond(1567631536), Instant.ofEpochSecond(1567631596));
        ValidityPeriod period2 = new ValidityPeriod(Instant.ofEpochSecond(1567631536), Instant.ofEpochSecond(1567631596));
        ValidityPeriod period3 = new ValidityPeriod(Instant.ofEpochSecond(1567642536), Instant.ofEpochSecond(1568642536));

        assertTrue(period1.equals(period1));
        assertTrue(period1.equals(period2));
        assertTrue(period2.equals(period1));

        assertFalse(period1.equals(period3));
        assertFalse(period3.equals(period1));

        assertFalse(period1.equals(new Object()));
        assertFalse(period1.equals(null));
    }
}
