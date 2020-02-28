package dev.samstevens.totp.secret;

import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

@SuppressWarnings("WeakerAccess")
public class DefaultSecretGenerator implements SecretGenerator {

    private final SecureRandom randomBytes = new SecureRandom();
    private final static Base32 encoder = new Base32();
    private final int numCharacters;

    public DefaultSecretGenerator() {
        this.numCharacters = 32;
    }

    /**
     * @param numCharacters The number of characters the secret should consist of.
     */
    public DefaultSecretGenerator(int numCharacters) {
        this.numCharacters = numCharacters;
    }

    @Override
    public String generate() {
        return new String(encoder.encode(getRandomBytes()));
    }

    private byte[] getRandomBytes() {
        // 5 bits per char in base32
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        randomBytes.nextBytes(bytes);

        return bytes;
    }
}
