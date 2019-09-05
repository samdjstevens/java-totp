package dev.samstevens.totp.secret;

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

public class DefaultSecretGenerator implements SecretGenerator {

    private final SecureRandom randomBytes = new SecureRandom();
    private final static Base32 encoder = new Base32();
    private final int numberOfBits;

    @SuppressWarnings("WeakerAccess")
    public DefaultSecretGenerator() {
        this.numberOfBits = 160;
    }

    @SuppressWarnings("WeakerAccess")
    public DefaultSecretGenerator(int numberOfBits) {
        if ((numberOfBits % 40) != 0) {
            throw new IllegalArgumentException("Number of bits must be divisible by 40, try 80 or 160.");
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
