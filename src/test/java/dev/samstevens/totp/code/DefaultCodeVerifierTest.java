package dev.samstevens.totp.code;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultCodeVerifierTest {

    @Test
    public void testCodeIsValid() {

        String secret = "W3C5B3WKR4AUKFVWYU2WNMYB756OAKWY";


        CodeVerifier v = new DefaultCodeVerifier(new DefaultCodeGenerator());

        String code = "82371";
        boolean result = v.isValidCode(secret, code);

        assertTrue(result);
    }

}
