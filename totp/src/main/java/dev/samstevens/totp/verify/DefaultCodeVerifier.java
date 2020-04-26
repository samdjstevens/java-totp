package dev.samstevens.totp.verify;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.GeneratedCode;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class DefaultCodeVerifier implements CodeVerifier {

    private final CodeGenerator codeGenerator;
    private final TimeProvider timeProvider;

    public DefaultCodeVerifier(CodeGenerator codeGenerator, TimeProvider timeProvider) {
        this.codeGenerator = codeGenerator;
        this.timeProvider = timeProvider;
    }

    @Override
    public VerifyResult verifyCode(String secret, String code) {
        return verifyCode(secret, code, 0);
    }

    @Override
    public VerifyResult verifyCode(String secret, String code, TemporalAmount acceptableTimeDrift) {
        return verifyConsecutiveCodes(secret, listOf(code), acceptableTimeDrift);
    }

    @Override
    public VerifyResult verifyConsecutiveCodes(String secret, List<String> codes, TemporalAmount acceptableTimeDrift) {

        Instant currentTime = timeProvider.getTime();
        Instant startTime = currentTime.minus(acceptableTimeDrift);
        Instant endTime = currentTime.plus(acceptableTimeDrift);

        List<GeneratedCode> generatedCodes;
        try {
            generatedCodes = codeGenerator.generateBetween(secret, startTime, endTime);
        } catch (CodeGenerationException e) {
            return new VerifyResult(false, null);
        }

        return checkCodes(codes, generatedCodes, currentTime);
    }

    @Override
    public VerifyResult verifyCode(String secret, String code, int acceptableCodeDrift) {
        Instant currentTime = timeProvider.getTime();

        // Generate all the valid codes
        List<GeneratedCode> generatedCodes;
        try {
            generatedCodes = codeGenerator.generate(secret, currentTime, acceptableCodeDrift);
        } catch (CodeGenerationException e) {
            return new VerifyResult(false, null);
        }

        // Check the code against the valid codes
        return checkCodes(listOf(code), generatedCodes, currentTime);
    }









    private <T> List<T> listOf(T item) {
        List<T> items = new ArrayList<>();
        items.add(item);

        return items;
    }



    private VerifyResult checkCodes(List<String> codes, List<GeneratedCode> generatedCodes, Instant currentTime) {
        boolean success = false;
        GeneratedCode successCode = null;

        int currentCodeBeingChecked = 0;

        // Loop through all the generated codes
        for (GeneratedCode generatedCode : generatedCodes) {

            // Is the current code we're checking valid?
            boolean isValid = timeSafeStringComparison(generatedCode.getDigits(), codes.get(currentCodeBeingChecked));

            if (isValid) {
                currentCodeBeingChecked++;
            } else {
                isValid = timeSafeStringComparison(generatedCode.getDigits(), codes.get(0));
                if (isValid) {
                    currentCodeBeingChecked++;
                }
            }

            if (isValid && currentCodeBeingChecked == 1) {
                successCode = generatedCode;
            }

            if (!success && currentCodeBeingChecked == codes.size()) {
                success = true;
                currentCodeBeingChecked = 0;
            }
        }

        return new VerifyResult(success, getTimeDrift(currentTime, successCode));
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

    private Duration getTimeDrift(Instant currentTime, GeneratedCode successCode) {
        if (successCode == null) {
            return null;
        }

        return Duration.between(currentTime, successCode.getValidityPeriod().getStart());
    }
}