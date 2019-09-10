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

        // allow for a -/+ ~30 second discrepancy
        assertTrue(isValidCode(secret, correctCode, timeToRunAt - 30));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt + 30));

        // but no more
        assertFalse(isValidCode(secret, correctCode, timeToRunAt + 45));
    }

    private boolean isValidCode(String secret, String code, long time) {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(time);

        CodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        return verifier.isValidCode(secret, code);
    }
}
