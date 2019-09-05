package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import java.time.Instant;

public class DefaultCodeVerifier implements CodeVerifier {

    private CodeGenerator codeGenerator;

    public DefaultCodeVerifier(CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public boolean isValidCode(String secret, String code) {
        // Get the current number of seconds since the epoch and
        // calculate the number of 30 second buckets passed.
        long currentBucket = Math.floorDiv(getCurrentTime(), 30L);

        // Calculate and compare the codes for the previous, current, and next time
        // buckets, comparing all 3 to avoid timing attacks
        boolean success = false;
        for (int i = -1; i <= 1; i++) {
            success = checkCode(secret, currentBucket + i, code) || success;
        }

        return success;
    }

    /**
     * Get the number of seconds since Jan 1st 1970, 00:00:00.
     * Used to determine the counter values to check.
     */
    private long getCurrentTime() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Check if a code matches for a given secret and counter.
     */
    private boolean checkCode(String secret, long counter, String code) {
        try {
            String actualCode = codeGenerator.generate(secret, counter);
            return timeSafeStringComparison(actualCode, code);
        } catch (CodeGenerationException e) {
            return false;
        }
    }

    /**
     * Compare two strings for equality without leaking timing information.
     */
    private boolean timeSafeStringComparison(String a, String b) {
        byte[] aBytes = a.getBytes();
        byte[] bBytes = b.getBytes();

        if (aBytes.length != bBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }

        return result == 0;
    }
}