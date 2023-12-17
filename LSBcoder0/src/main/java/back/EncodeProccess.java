package back;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public class EncodeProccess {

    /**
     * Основной процесс кодирования
     * @param imagePath путь к исходному изображнию
     * @param message исходное сообщение
     */
    public static void process(String imagePath, String message) {
        BufferedImage image = SourceImage.getImage(imagePath);
        BufferedImage hiddenImage = new BufferedImage(SourceImage.getStats(image)[0], SourceImage.getStats(image)[1], TYPE_INT_RGB);
        EncodeLsb.hideMessage(image, hiddenImage, message);
        SourceImage.saveImage(image, "result\\output_image.bmp");
        SourceImage.saveImage(hiddenImage, "result\\hidden_image.bmp");
    }
}

