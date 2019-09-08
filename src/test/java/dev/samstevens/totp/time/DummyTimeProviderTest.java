package dev.samstevens.totp.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class DummyTimeProviderTest {

    @Test
    public void testProvidesTime()
    {
        TimeProvider time = new DummyTimeProvider(1567975094);
        assertEquals(1567975094, time.getTime());
    }
}
