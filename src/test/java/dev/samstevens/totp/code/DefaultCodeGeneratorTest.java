package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.junit.Test;
import java.security.InvalidParameterException;
import static org.junit.Assert.*;

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

    @Test(expected = InvalidParameterException.class)
    public void testInvalidDigitLengthThrowsException() {
        new DefaultCodeGenerator(HashingAlgorithm.SHA1, 0);
    }

    @Test(expected = CodeGenerationException.class)
    public void testInvalidKeyThrowsCodeGenerationException() throws CodeGenerationException {
        try {
            DefaultCodeGenerator g = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 4);
            g.generate("1234", 1567631536);
        } catch (CodeGenerationException e) {
            // Assert there is a cause for the exception
            assertNotNull(e.getCause());
            throw e;
        }
    }

    private String generateCode(String secret, int time) throws CodeGenerationException {
        DefaultCodeGenerator g = new DefaultCodeGenerator();
        return g.generate(secret, time);
    }
}
