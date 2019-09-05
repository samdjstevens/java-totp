package dev.samstevens.totp.code;

public interface CodeVerifier {
    boolean isValidCode(String secret, String code);
}
