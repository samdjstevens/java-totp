package dev.samstevens.totp;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;

@SuppressWarnings("WeakerAccess")
public class Totp {

    private static SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private static CodeVerifier codeVerifier = new DefaultCodeVerifier();
    private static ZxingPngQrGenerator qrGenerator = new ZxingPngQrGenerator();

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public byte[] generateQr(QrData data) throws QrGenerationException {
        return qrGenerator.generate(data);
    }

    public boolean verifyCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }
}
