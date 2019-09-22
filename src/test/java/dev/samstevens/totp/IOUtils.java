package dev.samstevens.totp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class IOUtils {

    /**
     * Helper method to read the contents of a text file.
     */
    public static String readTextFile(String filename) throws IOException {
        InputStream stream = IOUtils.class.getResourceAsStream(filename);
        StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        return resultStringBuilder.toString().trim();
    }

    /**
     * Helper method to read the data of an image.
     */
    public static byte[] readImageIntoBytes(String filename, String imageFormat) throws IOException {
        BufferedImage bImage = ImageIO.read(ClassLoader.getSystemResource(filename));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, imageFormat, bos);

        return bos.toByteArray();
    }

    /**
     * Helper method to write data to a file.
     */
    public static void writeFile(byte[] contents, String filePath) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(filePath)) {
            stream.write(contents);
        }
    }
}
