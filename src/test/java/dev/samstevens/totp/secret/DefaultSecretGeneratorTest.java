package dev.samstevens.totp.secret;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultSecretGeneratorTest {

    @Test
    public void testSecretGenerated() {
        DefaultSecretGenerator generator32 = new DefaultSecretGenerator();
        String secret32 = generator32.generate();
        assertNotNull(secret32);
        assertEquals(32, secret32.length());

        DefaultSecretGenerator generator16 = new DefaultSecretGenerator(16);
        String secret16 = generator16.generate();
        assertNotNull(secret16);
        assertEquals(16, secret16.length());
    }
}
