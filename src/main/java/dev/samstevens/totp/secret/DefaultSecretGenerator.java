package dev.samstevens.totp.secret;

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

public class DefaultSecretGenerator implements SecretGenerator {

    private SecureRandom randomBytes = new SecureRandom();
    private Base32 encoder = new Base32();
    private final int numberOfBits;

    public DefaultSecretGenerator() {
        this.numberOfBits = 160;
    }

    public DefaultSecretGenerator(int numberOfBits) {
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
