package dev.samstevens.totp.qr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings("WeakerAccess")
public class QrData {

    private String type;
    private String label;
    private String secret;
    private String issuer;
    private String algorithm;
    private int digits;
    private int period;

    /**
     * Force use of builder to create instances.
     */
    private QrData(String type, String label, String secret, String issuer, String algorithm, int digits, int period) {
        this.type = type;
        this.label = label;
        this.secret = secret;
        this.issuer = issuer;
        this.algorithm = algorithm;
        this.digits = digits;
        this.period = period;
    }

    /**
     * Get the URI/message to encode into the QR image.
     * This is the format specified here: https://github.com/google/google-authenticator/wiki/Key-Uri-Format
     */
    public String getUri() throws UnsupportedEncodingException {
        return "otpauth://" +
                uriEncode(type) + "/" +
                uriEncode(label) + "?" +
                "secret=" + uriEncode(secret) +
                "&issuer=" + uriEncode(issuer) +
                "&algorithm=" + uriEncode(algorithm) +
                "&digits=" + digits +
                "&period=" + period;
    }

    private String uriEncode(String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8").replaceAll("\\+", "%20");
    }

    public static class Builder {
        private String label;
        private String secret;
        private String issuer;
        private String algorithm = "SHA1";
        private int digits = 6;
        private int period = 30;

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder algorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder digits(int digits) {
            this.digits = digits;
            return this;
        }

        public Builder period(int period) {
            this.period = period;
            return this;
        }

        public QrData build() {
            // @todo: add checks for required fields before building
            return new QrData("totp", label, secret, issuer, algorithm, digits, period);
        }
    }
}
