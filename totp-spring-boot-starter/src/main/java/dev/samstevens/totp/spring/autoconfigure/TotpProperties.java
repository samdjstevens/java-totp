package dev.samstevens.totp.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "totp")
public class TotpProperties {

    private static final int DEFAULT_SECRET_LENGTH = 32;
    private static final int DEFAULT_CODE_LENGTH = 6;
    private static final int DEFAULT_CODE_VALIDITY_SECONDS = 30;

    private final Secret secret = new Secret();
    private final Code code = new Code();

    public Secret getSecret() {
        return secret;
    }

    public Code getCode() {
        return code;
    }

    public static class Secret {
        private int length = DEFAULT_SECRET_LENGTH;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }

    public static class Code {
        private int length = DEFAULT_CODE_LENGTH;
        private int validitySeconds = DEFAULT_CODE_VALIDITY_SECONDS;

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getValiditySeconds() {
            return validitySeconds;
        }

        public void setValiditySeconds(int validitySeconds) {
            this.validitySeconds = validitySeconds;
        }
    }
}
