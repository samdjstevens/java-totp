package dev.samstevens.totp.code;

public interface CodeVerifier {
    /**
     * @param secret The shared secret/key to check the code against.
     * @param code The n-digit code given by the end user to check.
     * @return If the code is valid or not.
     */
    boolean isValidCode(String secret, String code);
}
