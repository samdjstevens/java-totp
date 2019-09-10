package dev.samstevens.totp.qr;

import dev.samstevens.totp.exceptions.QrGenerationException;

public interface QrGenerator {
    /**
     * @param data The QrData object to encode in the generated image.
     * @return The raw image data as a byte array.
     * @throws QrGenerationException thrown if image generation fails for any reason.
     */
    byte[] generate(QrData data) throws QrGenerationException;
}