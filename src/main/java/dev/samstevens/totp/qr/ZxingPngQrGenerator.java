package dev.samstevens.totp.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.samstevens.totp.exceptions.QrGenerationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ZxingPngQrGenerator implements QrGenerator {

    private static QRCodeWriter writer = new QRCodeWriter();

    @Override
    public byte[] generate(QrData data) throws QrGenerationException {

        final int imageWidth = 350;
        final int imageHeight = 350;

        try {
            BitMatrix bitMatrix = writer.encode(data.getUri(), BarcodeFormat.QR_CODE, imageWidth, imageHeight);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QrGenerationException();
        }
    }
}
