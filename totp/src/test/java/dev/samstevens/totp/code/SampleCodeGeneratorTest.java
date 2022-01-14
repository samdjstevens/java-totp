package dev.samstevens.totp.code;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

public class SampleCodeGeneratorTest {
    @Test
    public void testGenerateSampleCodes() throws CodeGenerationException {
        final DefaultSecretGenerator generator = new DefaultSecretGenerator();
        final String secret = generator.generate();

        final TimeProvider timeProvider = new SystemTimeProvider();

        final SampleCodeGenerator sampleGenerator = new SampleCodeGenerator(
                new DefaultCodeGenerator(), timeProvider);
        sampleGenerator.setTimePeriod(30);
        sampleGenerator.setAllowedTimePeriodDiscrepancy(2);

        final DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);
        verifier.setTimePeriod(30);
        verifier.setAllowedTimePeriodDiscrepancy(2);

        final String[] sampleCodes = sampleGenerator.generateCodes(secret);
        assertEquals(5, sampleCodes.length);
        for (String sampleCode: sampleCodes) {
            assertTrue(verifier.isValidCode(secret, sampleCode));
        }
    }
}
