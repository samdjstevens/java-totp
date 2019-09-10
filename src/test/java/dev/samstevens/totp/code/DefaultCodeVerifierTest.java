package dev.samstevens.totp.code;

import dev.samstevens.totp.time.TimeProvider;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultCodeVerifierTest {

    @Test
    public void testCodeIsValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;
        String correctCode = "862707";

        TimeProvider frozenTimeProvider = mock(TimeProvider.class);
        when(frozenTimeProvider.getTime()).thenReturn(timeToRunAt);

        CodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), frozenTimeProvider);
        assertTrue(verifier.isValidCode(secret, correctCode));
    }
}
