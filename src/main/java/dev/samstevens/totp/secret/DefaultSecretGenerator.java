package dev.samstevens.totp.secret;

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

public class DefaultSecretGenerator implements SecretGenerator {

    private SecureRandom randomBytes = new SecureRandom();
    private Base32 encoder = new Base32();
    private final int numberOfBits;

    @SuppressWarnings("WeakerAccess")
    public DefaultSecretGenerator() {
        this.numberOfBits = 160;
    }

    @SuppressWarnings("WeakerAccess")
    public DefaultSecretGenerator(int numberOfBits) {
        if ((numberOfBits % 8) != 0) {
            throw new IllegalArgumentException("Number of bits must be divisible by 8, try 80 or 160.");
        }
        this.numberOfBits = numberOfBits;
    }

    public String generate() {
        return new String(encoder.encode(getRandomBytes()));
    }

    private byte[] getRandomBytes() {
        byte[] bytes = new byte[numberOfBits / 8];
        randomBytes.nextBytes(bytes);

        return bytes;
    }
}
