package dev.samstevens.totp.code;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratedCodeTest {

    @Test
    public void testEquality() {
        GeneratedCode code1 = createGeneratedCode("123456", 1567631536, 1567631596);
        GeneratedCode code2 = createGeneratedCode("123456", 1567631536, 1567631596);
        GeneratedCode code3 = createGeneratedCode("123457", 1567631536, 1567631596);
        GeneratedCode code4 = createGeneratedCode("123456", 1567631536, 1567631597);
        GeneratedCode code6 = new GeneratedCode("123457", null);

        assertTrue(code1.equals(code1));
        assertTrue(code1.equals(code2));
        assertTrue(code2.equals(code1));

        assertFalse(code1.equals(code3));
        assertFalse(code3.equals(code1));
        assertFalse(code2.equals(code4));
        assertFalse(code3.equals(code6));

        assertFalse(code1.equals(new Object()));
        assertFalse(code1.equals(null));
    }

    /**
     * Helper method to construct a GeneratedCode object.
     */
    private static GeneratedCode createGeneratedCode(String digits, long startTime, long endTime) {
        return new GeneratedCode(digits, new ValidityPeriod(Instant.ofEpochSecond(startTime), Instant.ofEpochSecond(endTime)));
    }
}
