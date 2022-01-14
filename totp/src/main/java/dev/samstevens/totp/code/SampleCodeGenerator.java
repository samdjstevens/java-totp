package dev.samstevens.totp.code;

import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.time.TimeProvider;

public class SampleCodeGenerator {
    private final CodeGenerator codeGenerator;
    private final TimeProvider timeProvider;
    private int timePeriod = 30;
    private int allowedTimePeriodDiscrepancy = 1;

    public SampleCodeGenerator(final CodeGenerator codeGenerator,
                               final TimeProvider timeProvider) {
        this.codeGenerator = codeGenerator;
        this.timeProvider = timeProvider;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public void setAllowedTimePeriodDiscrepancy(int allowedTimePeriodDiscrepancy) {
        this.allowedTimePeriodDiscrepancy = allowedTimePeriodDiscrepancy;
    }

    /**
     * Generate sample codes based on the current time.
     */
    public String[] generateCodes(final String secret) throws CodeGenerationException {
        final String[] sampleCodes = new String[allowedTimePeriodDiscrepancy + allowedTimePeriodDiscrepancy + 1];
        long currentBucket = Math.floorDiv(timeProvider.getTime(), timePeriod);

        for (int x = 0, i = -allowedTimePeriodDiscrepancy; i <= allowedTimePeriodDiscrepancy; i++, x++) {
            sampleCodes[x] = codeGenerator.generate(secret, currentBucket + i);
        }
        return sampleCodes;
    }
}
