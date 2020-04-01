package dev.samstevens.totp.qr;

import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static dev.samstevens.totp.IOUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZxingPngQrGeneratorTest {

    @Test
    public void testSomething() throws QrGenerationException, IOException {
        ZxingPngQrGenerator generator = new ZxingPngQrGenerator();

        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB")
                .issuer("AppName")
                .digits(6)
                .period(30)
                .build();

        writeFile(generator.generate(data), "./test_qr.png");
    }

    @Test
    public void testMimeType() {
        assertEquals("image/png", new ZxingPngQrGenerator().getImageMimeType());
    }

    @Test
    public void testImageSize() throws QrGenerationException, IOException {
        ZxingPngQrGenerator generator = new ZxingPngQrGenerator();
        generator.setImageSize(500);
        byte[] data = generator.generate(getData());

        // Write the data to a temp file and read it into a BufferedImage to get the dimensions
        String filename = "/tmp/test_qr.png";
        writeFile(data, filename);
        File file = new File(filename);
        BufferedImage image = ImageIO.read(file);

        assertEquals(500, generator.getImageSize());
        assertEquals(500, image.getWidth());
        assertEquals(500, image.getHeight());

        // Delete the temp file
        file.delete();
    }

    @Test
    public void testExceptionIsWrapped() throws WriterException {
        Throwable exception = new RuntimeException();
        Writer writer = mock(Writer.class);
        when(writer.encode(anyString(), any(), anyInt(), anyInt())).thenThrow(exception);

        ZxingPngQrGenerator generator = new ZxingPngQrGenerator(writer);

        QrGenerationException e = assertThrows(QrGenerationException.class, () -> {
            generator.generate(getData());
        });

        assertEquals("Failed to generate QR code. See nested exception.", e.getMessage());
        assertEquals(exception, e.getCause());
    }

    private QrData getData() {
        return new QrData.Builder()
                .label("example@example.com")
                .secret("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB")
                .issuer("AppName")
                .digits(6)
                .period(30)
                .build();
    }
}
