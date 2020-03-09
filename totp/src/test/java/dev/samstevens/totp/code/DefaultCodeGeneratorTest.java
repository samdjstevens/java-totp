package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.security.InvalidParameterException;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DefaultCodeGeneratorTest {

    @ParameterizedTest
    @MethodSource("expectedCodesProvider")
    public void testCodeIsGenerated(String secret, int time, HashingAlgorithm algorithm, String expectedCode) throws CodeGenerationException {
        String code = generateCode(algorithm, secret, time);

        assertEquals(expectedCode, code);
    }

    static Stream<Arguments> expectedCodesProvider() {
        return Stream.of(
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA1, "082371"),
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA256, "272978"),
            arguments("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536, HashingAlgorithm.SHA512, "325200"),

            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA1, "848586"),
            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA256, "965726"),
            arguments("makrzl2hict4ojeji2iah4kndmq6sgka", 1582750403, HashingAlgorithm.SHA512, "741306")
        );
    }

    @Test
    public void testDigitLength() throws CodeGenerationException {
        DefaultCodeGenerator g = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        String code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536);
        assertEquals(6, code.length());

        g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 8);
        code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536);
        assertEquals(8, code.length());

        g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 4);
        code = g.generate("W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY", 1567631536);
        assertEquals(4, code.length());
    }

    @Test
    public void testInvalidHashingAlgorithmThrowsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new DefaultCodeGenerator(null, 6);
        });
    }
    
    @Test
    public void testInvalidDigitLengthThrowsException() {
        assertThrows(InvalidParameterException.class, () -> {
            new DefaultCodeGenerator(HashingAlgorithm.SHA1, 0);
        });
    }

    @Test
    public void testInvalidKeyThrowsCodeGenerationException() throws CodeGenerationException {
        CodeGenerationException e = assertThrows(CodeGenerationException.class, () -> {
            DefaultCodeGenerator g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 4);
            g.generate("1234", 1567631536);
        });
        assertNotNull(e.getCause());
    }

    private String generateCode(HashingAlgorithm algorithm, String secret, int time) throws CodeGenerationException {
        long currentBucket = Math.floorDiv(time, 30);
        DefaultCodeGenerator g = new DefaultCodeGenerator(algorithm);
        return g.generate(secret, currentBucket);
    }
}
