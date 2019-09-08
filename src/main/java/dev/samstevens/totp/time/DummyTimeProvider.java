package dev.samstevens.totp.time;

public class DummyTimeProvider implements TimeProvider {
    private final long dummyTime;

    public DummyTimeProvider(long dummyTime) {
        this.dummyTime = dummyTime;
    }

    @Override
    public long getTime() {
        return this.dummyTime;
    }
}
