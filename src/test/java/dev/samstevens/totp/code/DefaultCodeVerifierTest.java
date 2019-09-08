package dev.samstevens.totp.code;

import dev.samstevens.totp.time.DummyTimeProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultCodeVerifierTest {

    @Test
    public void testCodeIsValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;
        String correctCode = "862707";

        CodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), new DummyTimeProvider(timeToRunAt));
        assertTrue(verifier.isValidCode(secret, correctCode));
    }
}
