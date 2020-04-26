package dev.samstevens.totp.qr;

import dev.samstevens.totp.code.HashingAlgorithm;
import java.time.Duration;

public class QrDataFactory {

    private HashingAlgorithm defaultAlgorithm;
    private int defaultDigits;
    private Duration defaultTimePeriod;

    public QrDataFactory(HashingAlgorithm defaultAlgorithm, int defaultDigits, Duration defaultTimePeriod) {
        this.defaultAlgorithm = defaultAlgorithm;
        this.defaultDigits = defaultDigits;
        this.defaultTimePeriod = defaultTimePeriod;
    }

    public QrData.Builder newBuilder() {
        return new QrData.Builder()
            .algorithm(defaultAlgorithm)
            .digits(defaultDigits)
            .period(defaultTimePeriod);
    }
}
