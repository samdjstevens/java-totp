package dev.samstevens.totp.secret;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultSecretGeneratorTest {

    @Test
    public void testSecretGenerated() {
        DefaultSecretGenerator generator160 = new DefaultSecretGenerator();
        String secret160 = generator160.generate();
        assertNotNull(secret160);
        assertEquals(32, secret160.length());

        DefaultSecretGenerator generator80 = new DefaultSecretGenerator(80);
        String secret80 = generator80.generate();
        assertNotNull(secret80);
        assertEquals(16, secret80.length());
    }
}
