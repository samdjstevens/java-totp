package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.apache.commons.codec.binary.Base32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DefaultCodeGenerator implements CodeGenerator {

    private final static int DEFAULT_DIGITS_LENGTH = 6;
    private final static Duration DEFAULT_VALIDITY_DURATION = Duration.ofSeconds(30);

    private final HashingAlgorithm algorithm;
    private final int digits;
    private final long validitySeconds;

    public DefaultCodeGenerator() {
        this(HashingAlgorithm.SHA1, DEFAULT_DIGITS_LENGTH, DEFAULT_VALIDITY_DURATION);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm) {
        this(algorithm, DEFAULT_DIGITS_LENGTH, DEFAULT_VALIDITY_DURATION);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm, int digits) {
        this(algorithm, digits, DEFAULT_VALIDITY_DURATION);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm, int digits, Duration codeValidityDuration) {
        if (algorithm == null) {
            throw new InvalidParameterException("HashingAlgorithm must not be null.");
        }

        if (digits < 1) {
            throw new InvalidParameterException("Number of digits must be higher than 0.");
        }

        if (codeValidityDuration.getSeconds() < 1) {
            throw new InvalidParameterException("Number of seconds codes are valid for must be at least 1.");
        }

        this.algorithm = algorithm;
        this.digits = digits;
        this.validitySeconds = codeValidityDuration.getSeconds();
    }

    @Override
    public List<GeneratedCode> generate(String key, Instant atTime, int howManyBeforeAndAfter) throws CodeGenerationException {
        if (howManyBeforeAndAfter < 0) {
            throw new InvalidParameterException("Number of codes before and after to generate must be greater or equal to zero.");
        }

        long counter = getCounterForTime(atTime);
        long startCounter = counter - howManyBeforeAndAfter;
        long endCounter = counter + howManyBeforeAndAfter;

        return generateCodesForCounterRange(key, startCounter, endCounter);
    }

    @Override
    public List<GeneratedCode> generateBetween(String key, Instant startTime, Instant endTime) throws CodeGenerationException {
        if (endTime.isBefore(startTime)) {
            throw new InvalidParameterException("End time must be after start time.");
        }

        long startCounter = getCounterForTime(startTime);
        long endCounter = getCounterForTime(endTime);

        return generateCodesForCounterRange(key, startCounter, endCounter);
    }

    /**
     * Get the counter value used to generate the hash for the given time.
     */
    private long getCounterForTime(Instant time) {
        return Math.floorDiv(time.getEpochSecond(), validitySeconds);
    }

    /**
     * Create the list of GeneratedCode objects for a given key & start/end counters.
     */
    private List<GeneratedCode> generateCodesForCounterRange(String key, long startCounter, long endCounter) throws CodeGenerationException {
        List<GeneratedCode> codes = new ArrayList<>();
        for (long i = startCounter; i <= endCounter; i++) {
            codes.add(generateForCounter(key, i));
        }

        return codes;
    }

    /**
     * Create the GeneratedCode object for a given key & counter.
     */
    private GeneratedCode generateForCounter(String key, long counter) throws CodeGenerationException {
        try {
            byte[] hash = generateHash(key, counter);
            String digits = getDigitsFromHash(hash);
            ValidityPeriod validity = getValidityPeriodFromTime(counter);

            return new GeneratedCode(digits, validity);
        } catch (Exception e) {
            throw new CodeGenerationException("Failed to generate code. See nested exception.", e);
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
        SecretKeySpec signKey = new SecretKeySpec(decodedKey, algorithm.getHmacAlgorithm());
        Mac mac = Mac.getInstance(algorithm.getHmacAlgorithm());
        mac.init(signKey);

        // Create a hash of the counter value
        return mac.doFinal(data);
    }

    /**
     * Get the n-digit code for a given hash.
     */
    private String getDigitsFromHash(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xF;

        long truncatedHash = 0;

        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= Math.pow(10, digits);

        // Left pad with 0s for a n-digit code
        return String.format("%0" + digits + "d", truncatedHash);
    }

    /**
     * Get the period of time (start and end Instants) that the code for a given counter is valid for.
     */
    private ValidityPeriod getValidityPeriodFromTime(long counter) {
        long startTimeSeconds = counter * validitySeconds;
        long endTimeSeconds = startTimeSeconds + validitySeconds - 1;

        return new ValidityPeriod(Instant.ofEpochSecond(startTimeSeconds), Instant.ofEpochSecond(endTimeSeconds));
    }
}
