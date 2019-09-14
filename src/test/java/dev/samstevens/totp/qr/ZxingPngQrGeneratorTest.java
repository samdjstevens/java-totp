package dev.samstevens.totp.qr;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class ZxingPngQrGeneratorTest {

    @Test
    public void testSomething() throws QrGenerationException {
        ZxingPngQrGenerator generator = new ZxingPngQrGenerator();

        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB")
                .issuer("AppName")
                .digits(6)
                .period(30)
                .build();

        writeTestFile(generator.generate(data), "./test_qr.png");
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
        writeTestFile(data, filename);
        File file = new File(filename);
        BufferedImage image = ImageIO.read(file);

        assertEquals(500, image.getWidth());
        assertEquals(500, image.getHeight());

        // Delete the temp file
        file.delete();
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

    private void writeTestFile(byte[] contents, String filePath) {
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
