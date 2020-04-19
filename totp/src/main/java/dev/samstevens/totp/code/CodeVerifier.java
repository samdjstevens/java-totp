package dev.samstevens.totp.code;

public interface CodeVerifier {
    /**
     * @deprecated Replaced by {@link #verifyCode(String, String)}.
     * @param secret The shared secret/key to check the code against.
     * @param code The n-digit code given by the end user to check.
     * @return If the code is valid or not.
     */
    @Deprecated
    boolean isValidCode(String secret, String code);

    /**
     * @param secret The shared secret/key to check the code against.
     * @param code The n-digit code given by the end user to check.
     */
    VerifyResult verifyCode(String secret, String code);

    /**
     * @param secret The shared secret/key to check the code against.
     * @param codes The n-digit codes given by the end user to check. Codes must be valid and consecutive.
     */
    VerifyResult verifyConsecutiveCodes(String secret, String... codes);
}
