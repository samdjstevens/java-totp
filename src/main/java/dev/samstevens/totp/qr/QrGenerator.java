package dev.samstevens.totp.qr;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface QrGenerator {
    byte[] generate(QrData data) throws QrGenerationException;
}