package dev.samstevens.totp.qr;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
