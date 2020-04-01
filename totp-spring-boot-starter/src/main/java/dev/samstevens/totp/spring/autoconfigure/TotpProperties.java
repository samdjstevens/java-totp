package dev.samstevens.totp.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "totp")
public class TotpProperties {

    private static final int DEFAULT_SECRET_LENGTH = 32;
    private static final int DEFAULT_CODE_LENGTH = 6;
    private static final int DEFAULT_TIME_PERIOD = 30;
    private static final int DEFAULT_TIME_DISCREPANCY = 1;

    private final Secret secret = new Secret();
    private final Code code = new Code();
    private final Time time = new Time();

    public Secret getSecret() {
        return secret;
    }

    public Code getCode() {
        return code;
    }

    public Time getTime() {
        return time;
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

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }

    public static class Time {
        private int period = DEFAULT_TIME_PERIOD;
        private int discrepancy = DEFAULT_TIME_DISCREPANCY;

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getDiscrepancy() {
            return discrepancy;
        }

        public void setDiscrepancy(int discrepancy) {
            this.discrepancy = discrepancy;
        }
    }
}
