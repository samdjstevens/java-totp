package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.verify.DefaultCodeVerifier;
import dev.samstevens.totp.verify.VerifyResult;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultCodeVerifierTest {

    @Test
    public void testCodeIsValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;
        String correctCode = "862707";
        Duration timePeriod = Duration.ofSeconds(30);

        // allow for a -/+ ~30 second discrepancy
        assertTrue(isValidCode(secret, correctCode, timeToRunAt - timePeriod.getSeconds(), timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt, timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt + timePeriod.getSeconds(), timePeriod));

        // but no more
        assertFalse(isValidCode(secret, correctCode, timeToRunAt + timePeriod.getSeconds() + 15, timePeriod));

        // test wrong code fails
        assertFalse(isValidCode(secret, "123", timeToRunAt, timePeriod));
    }

    @Test
    public void testCodeGenerationFailureReturnsFalse() throws CodeGenerationException {

        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(1567975936L));

        CodeGenerator codeGenerator = mock(CodeGenerator.class);
        when(codeGenerator.generate(anyString(), any(Instant.class), anyInt()))
            .thenThrow(new CodeGenerationException("Test", new RuntimeException()));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        VerifyResult result = verifier.verifyCode(secret, "1234");

        assertEquals(false, result.isValid());
        assertNull(result.getTimeDrift());
    }

    /////////////////////

    @Test
    public void testVerifySingleCode() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        // Ran at 2020-05-08 22:35:45 UTC
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(1588977345));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        VerifyResult r = verifier.verifyCode(secret, "237357");
        assertTrue(r.isValid());
        assertEquals(-15, r.getTimeDrift().getSeconds());
    }

    @Test
    public void testVerifySingleCodeWithCodeDrifts() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        // Ran at 2020-05-08 22:35:45 UTC
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(1588977345));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        VerifyResult r = verifier.verifyCode(secret, "631654", 1);
        assertTrue(r.isValid());
        assertEquals(-45, r.getTimeDrift().getSeconds());
    }

    @Test
    public void testVerifySingleCodeWithTimeDrifts() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        // Ran at 2020-05-08 22:35:45 UTC
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(1588977345));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        VerifyResult r = verifier.verifyCode(secret, "877212", Duration.ofMinutes(3));
        assertTrue(r.isValid());
        assertEquals(-165, r.getTimeDrift().getSeconds());
    }

    @Test
    public void testConsecutiveCodesAreValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        String firstCode = "092231";
        String secondCode = "517872";
        String thirdCode = "398601";

        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(1567975936L));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        List<String> codes = Arrays.asList(firstCode, secondCode, thirdCode);
        VerifyResult r = verifier.verifyConsecutiveCodes(secret, codes, Duration.ofHours(24));
        assertTrue(r.isValid());
        assertEquals(-76500, r.getTimeDrift().getSeconds());
    }

    private boolean isValidCode(String secret, String code, long time, Duration timePeriod) {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(Instant.ofEpochSecond(time));

//        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider, timePeriod);
//
//        return verifier.isValidCode(secret, code);
        return false;
    }
}
