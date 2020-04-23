package dev.samstevens.totp.code;

import java.time.Duration;

public class VerifyResult {
    private final boolean isValid;
    private final Duration timeDrift;

    public VerifyResult(boolean isValid, Duration timeDrift) {
        this.isValid = isValid;
        this.timeDrift = timeDrift;
    }

    public boolean isValid() {
        return isValid;
    }

    public Duration getTimeDrift()
    {
        return timeDrift;
    }
}
