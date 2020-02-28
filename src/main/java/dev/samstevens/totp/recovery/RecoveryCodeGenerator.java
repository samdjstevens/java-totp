package dev.samstevens.totp.recovery;

import org.apache.commons.codec.binary.Base32;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class RecoveryCodeGenerator {

    private Random random = new SecureRandom();
    private Base32 codec = new Base32();

    public String[] generateCodes(int amount) {
        // Must generate at least one code
        if (amount < 1) {
            throw new InvalidParameterException("Amount must be at least 1.");
        }

        // Create an array and fill with generated codes
        String[] codes = new String[amount];
        Arrays.setAll(codes, i -> generateCode());

        return codes;
    }

    private String generateCode() {
        int codeLength = 10;
        byte[] bytes = new byte[(codeLength * 5) / 8];
        random.nextBytes(bytes);
        String randomCode = new String(codec.encode(bytes));
        randomCode = randomCode.replaceAll("=", "").toLowerCase();

        return splitCode(randomCode);
    }

    /**
     * Split the code into two halves for readability, e.g. "a7yeh-s823k".
     */
    private String splitCode(String code) {
        return String.format(
            "%s-%s",
            code.substring(0, (int) Math.ceil(code.length() / 2)),
            code.substring(code.length() / 2)
        );
    }
}