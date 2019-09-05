package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultCodeGeneratorTest {

    @Test
    public void testCodeIsGenerated() throws CodeGenerationException {

        String secret = "W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY";
        int time = 1567631536;
        String code = generateCode(secret, time);

        assertEquals("870366", code);
    }

    private String generateCode(String secret, int time) throws CodeGenerationException {
        DefaultCodeGenerator g = new DefaultCodeGenerator();
        return g.generate(secret, time);
    }
}
