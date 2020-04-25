package dev.samstevens.totp.code;

import java.time.Instant;

public class ValidityPeriod {
    private final Instant start;
    private final Instant end;

    public ValidityPeriod(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }
}
