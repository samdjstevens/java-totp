package dev.samstevens.totp.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.samstevens.totp.exceptions.QrGenerationException;
import java.io.ByteArrayOutputStream;

public class ZxingPngQrGenerator implements QrGenerator {

    private final Writer writer;
    private int imageSize = 350;

    public ZxingPngQrGenerator() {
        this(new QRCodeWriter());
    }

    public ZxingPngQrGenerator(Writer writer) {
        this.writer = writer;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getImageSize() {
        return imageSize;
    }

    public String getImageMimeType() {
        return "image/png";
    }

    @Override
    public byte[] generate(QrData data) throws QrGenerationException {
        try {
            BitMatrix bitMatrix = writer.encode(data.getUri(), BarcodeFormat.QR_CODE, imageSize, imageSize);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            throw new QrGenerationException("Failed to generate QR code. See nested exception.", e);
        }
    }
}
