package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import java.time.Duration;

public class DefaultCodeVerifier implements CodeVerifier {

    private final CodeGenerator codeGenerator;
    private final TimeProvider timeProvider;
    private int timePeriod = 30;
    private int allowedTimePeriodDiscrepancy = 1;

    public DefaultCodeVerifier(CodeGenerator codeGenerator, TimeProvider timeProvider) {
        this.codeGenerator = codeGenerator;
        this.timeProvider = timeProvider;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setAllowedTimePeriodDiscrepancy(int allowedTimePeriodDiscrepancy) {
        this.allowedTimePeriodDiscrepancy = allowedTimePeriodDiscrepancy;
    }

    public void setAllowedTimePeriodDuration(Duration duration) {
        long periods = duration.getSeconds() / this.getTimePeriod();
        this.setAllowedTimePeriodDiscrepancy((int) periods);
    }

    @Override
    public boolean isValidCode(String secret, String code) {
        // Get the current number of seconds since the epoch and
        // calculate the number of time periods passed.
        long currentBucket = Math.floorDiv(timeProvider.getTime(), timePeriod);

        // Calculate and compare the codes for all the "valid" time periods,
        // even if we get an early match, to avoid timing attacks
        boolean success = false;
        for (int i = -allowedTimePeriodDiscrepancy; i <= allowedTimePeriodDiscrepancy; i++) {
            success = checkCode(secret, currentBucket + i, code) || success;
        }

        return success;
    }


    public boolean areValidCodes(String secret, String... codes) {
        // Get the current number of seconds since the epoch and
        // calculate the number of time periods passed.
        long currentBucket = Math.floorDiv(timeProvider.getTime(), timePeriod);

        // Calculate and compare the codes for all the "valid" time periods,
        // even if we get an early match, to avoid timing attacks
        boolean success = false;
        int successiveMatches = 0;
        int currentCodeBeingChecked = 0;


        boolean isValid;
        for (int i = -allowedTimePeriodDiscrepancy; i <= allowedTimePeriodDiscrepancy; i++) {
            isValid = checkCode(secret, currentBucket + i, codes[currentCodeBeingChecked]);
            if (isValid) {
                currentCodeBeingChecked++;
            } else {
                if (currentCodeBeingChecked > 0) {
                    isValid = checkCode(secret, currentBucket + i, codes[0]);
                    if (isValid) {
                        currentCodeBeingChecked++;
                    }
                }
            }

            System.out.println(currentCodeBeingChecked);
            if (!success && currentCodeBeingChecked == codes.length) {
                success = true;
                currentCodeBeingChecked = 0;
            }
        }

        return success;
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