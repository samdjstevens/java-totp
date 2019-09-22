package dev.samstevens.totp.util;

import dev.samstevens.totp.IOUtils;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;
import static dev.samstevens.totp.util.Utils.*;

public class DataUriEncodingTest {

    @Test
    public void testDataUriEncode() throws IOException {
        byte[] imageData = IOUtils.readImageIntoBytes("example_qr.png", "png");
        String expectedDataUri = IOUtils.readTextFile("/example_qr_data_uri.txt");

        String dataUri = getDataUriForImage(imageData, "image/png");

        assertEquals(expectedDataUri, dataUri);
    }
}
