package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class DefaultCodeGenerator implements CodeGenerator {

    public String generate(String key, long counter) throws CodeGenerationException {
        try {
            byte[] hash = generateHash(key, counter);
            return getDigitsFromHash(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CodeGenerationException();
        }
    }

    /**
     * Generate a HMAC-SHA1 hash of the counter number.
     */
    private byte[] generateHash(String key, long counter) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] data = new byte[8];
        long value = counter;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        // Create a HMAC-SHA1 signing key from the shared key
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(key);
        String algo = "HmacSHA1";
        SecretKeySpec signKey = new SecretKeySpec(decodedKey, algo);
        Mac mac = Mac.getInstance(algo);
        mac.init(signKey);

        // Create a hash of the counter value
        return mac.doFinal(data);
    }

    /**
     * Get the 6 digit code for a given hash.
     */
    private String getDigitsFromHash(byte[] hash) {
        int offset = hash[20 - 1] & 0xF;

        long truncatedHash = 0;

        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        // Left pad with 0s for a 6 digit code
        return String.format("%06d", truncatedHash);
    }
}
