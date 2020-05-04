package dev.samstevens.totp.code;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedCode that = (GeneratedCode) o;

        return Objects.equals(digits, that.digits) && Objects.equals(validityPeriod, that.validityPeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digits, validityPeriod);
    }
}
