package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultCodeGeneratorTest {

    @Test
    public void testCodeIsGenerated() throws CodeGenerationException {

        String secret = "W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY";
        int time = 1567631536;
        String code = generateCode(secret, time);

        assertEquals("870366", code);
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

    private String generateCode(String secret, int time) throws CodeGenerationException {
        DefaultCodeGenerator g = new DefaultCodeGenerator();
        return g.generate(secret, time);
    }
}
