package dev.samstevens.totp.code;

public class GeneratedCode {
    private final String digits;
    private final ValidityPeriod validityPeriod;

    public GeneratedCode(String digits, ValidityPeriod validity) {
        this.digits = digits;
        this.validityPeriod = validity;
    }

    public String getDigits() {
        return digits;
    }

    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }
}
