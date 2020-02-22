package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultCodeVerifierTest {

    @Test
    public void testCodeIsValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;
        String correctCode = "862707";
        int timePeriod = 30;

        // allow for a -/+ ~30 second discrepancy
        assertTrue(isValidCode(secret, correctCode, timeToRunAt - timePeriod, timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt, timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt + timePeriod, timePeriod));

        // but no more
        assertFalse(isValidCode(secret, correctCode, timeToRunAt + timePeriod + 15, timePeriod));

        // test wrong code fails
        assertFalse(isValidCode(secret, "123", timeToRunAt, timePeriod));
    }

    @Test
    public void testCodeGenerationFailureReturnsFalse() throws CodeGenerationException {

        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(1567975936L);

        CodeGenerator codeGenerator = mock(CodeGenerator.class);
        when(codeGenerator.generate(anyString(), anyLong())).thenThrow(new CodeGenerationException("Test", new RuntimeException()));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setAllowedTimePeriodDiscrepancy(1);


        assertEquals(false, verifier.isValidCode(secret, "1234"));
    }

    private boolean isValidCode(String secret, String code, long time, int timePeriod) {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(time);

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);
        verifier.setTimePeriod(timePeriod);

        return verifier.isValidCode(secret, code);
    }
}
