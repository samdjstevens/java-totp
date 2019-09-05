package dev.samstevens.totp.code;

interface CodeVerifier {
    boolean isValidCode(String secret, String code);
}
