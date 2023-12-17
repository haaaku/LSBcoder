package back;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SourceImage extends EncodeLsb {
    /**
     * Считывание изображения из файла
     * @param imagePath путь к исходному изображению
     * @return изображение-копия исходного, доступное для обработки
     */
    public static BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Can't open file: " + e.getMessage());
        }
        return image;
    }

    /**
     * Получение данных изображения
     * @param image исходное изображение
     * @return ширина и высота исходного изображения
     */
    public static int[] getStats(BufferedImage image) {
        return new int[]{image.getWidth(), image.getHeight()};
    }

    /**
     * Сохранение изображения
     * @param image исходное изображение
     * @param outputPath путь в директорию для сохранения изображения
     */
    public static void saveImage(BufferedImage image, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "bmp", outputFile);
        } catch (IOException e) {
            System.out.println("Can't save file: " + e.getMessage());
        }
    }
}
