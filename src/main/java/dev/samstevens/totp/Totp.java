package dev.samstevens.totp;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;

@SuppressWarnings("WeakerAccess")
public class Totp {

    private static SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private static CodeVerifier codeVerifier = new DefaultCodeVerifier();

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }
}
