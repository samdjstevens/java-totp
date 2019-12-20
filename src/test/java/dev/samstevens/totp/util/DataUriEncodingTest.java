package dev.samstevens.totp.util;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

import org.junit.Test;

public class DataUriEncodingTest {

    @Test
    public void testDataUriEncode() throws IOException, URISyntaxException {

        // Source data : 
        // 1×1 white px PNG image base64 encoded :
        final String pngImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";
        final byte[] imageData = Base64.getDecoder().decode(pngImage);

        assertEquals("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=", 
                     getDataUriForImage(imageData, "image/png"));
    }
}
