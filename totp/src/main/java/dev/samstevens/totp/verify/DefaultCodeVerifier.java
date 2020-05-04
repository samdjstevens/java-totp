package dev.samstevens.totp.verify;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.GeneratedCode;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public VerifyResult verifyCode(String secret, String code, int allowedNumberOfCodesBeforeAndAfter) {
        Instant currentTime = timeProvider.getTime();

        // Generate all the valid codes
        List<GeneratedCode> generatedCodes;
        try {
            generatedCodes = codeGenerator.generate(secret, currentTime, allowedNumberOfCodesBeforeAndAfter);
        } catch (CodeGenerationException e) {
            return new VerifyResult(false, null);
        }

        // Check the code against the valid codes
        return verifyConsecutiveCodesAreValid(Collections.singletonList(code), generatedCodes, currentTime);
    }

    @Override
    public VerifyResult verifyCode(String secret, String code, TemporalAmount acceptableTimeDrift) {
        return verifyConsecutiveCodes(secret, Collections.singletonList(code), acceptableTimeDrift);
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

        return verifyConsecutiveCodesAreValid(codes, generatedCodes, currentTime);
    }

    private VerifyResult verifyConsecutiveCodesAreValid(List<String> codes, List<GeneratedCode> generatedCodes, Instant currentTime) {
        // Transform the list of GeneratedCodes into a list of just the digits
        List<String> generatedCodesStrings = generatedCodes.stream().map(GeneratedCode::getDigits).collect(Collectors.toList());

        // Check if the given codes exist consecutively in the generated codes
        int firstValidCodeIndex = Collections.indexOfSubList(generatedCodesStrings, codes);

        // Given codes do not consecutively appear in valid codes
        if (firstValidCodeIndex < 0) {
            return new VerifyResult(false, null);
        }

        // Codes do appear consecutively, get the duration between now and the first code being valid
        GeneratedCode firstCode = generatedCodes.get(firstValidCodeIndex);
        Duration timeDrift = Duration.between(currentTime, firstCode.getValidityPeriod().getStart());

        return new VerifyResult(true, timeDrift);
    }
}