package dev.samstevens.totp.verify;

import java.time.temporal.TemporalAmount;
import java.util.List;

public interface CodeVerifier {

    VerifyResult verifyCode(String secret, String code);

    VerifyResult verifyCode(String secret, String code, TemporalAmount acceptableTimeDrift);

    VerifyResult verifyCode(String secret, String code, int acceptableCodeDrift);

    VerifyResult verifyConsecutiveCodes(String secret, List<String> codes, TemporalAmount timeWindow);
}
