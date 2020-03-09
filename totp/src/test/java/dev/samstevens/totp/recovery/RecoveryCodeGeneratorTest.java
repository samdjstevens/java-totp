package dev.samstevens.totp.recovery;

import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class RecoveryCodeGeneratorTest {

    @Test
    public void testCorrectAmountGenerated() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        // Assert 16 non null codes generated
        assertEquals(16, codes.length);
        for (String code : codes) {
            assertNotNull(code);
        }
    }

    @Test
    public void testCodesMatchFormat() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        // Assert each one is the correct format
        for (String code : codes) {
            assertTrue(code.matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), code);
        }
    }

    @Test
    public void testCodesAreUnique() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(25);

        Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));

        assertEquals(25, uniqueCodes.size());
    }

    @Test
    public void testInvalidNumberThrowsException() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();

        assertThrows(InvalidParameterException.class, () -> {
            generator.generateCodes(-1);
        });
    }
}
