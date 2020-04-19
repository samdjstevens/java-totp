package dev.samstevens.totp.code;

public class VerifyResult {
    private final boolean isValid;
    private final int timePeriodDifference;

    public VerifyResult(boolean isValid, int timePeriodDifference) {
        this.isValid = isValid;
        this.timePeriodDifference = timePeriodDifference;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getTimePeriodDifference() {
        return timePeriodDifference;
    }
}
