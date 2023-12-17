package back;

import com.example.lsbcoder.LSBcoderApplication;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EncodeLsb extends LSBcoderApplication {
    /**
     * Сохранение спрятанного бита в "карту" битов
     * @param hidimg изображение, отображающее замененные lsb
     * @param x координата пикселя по оси X
     * @param y координата пикселя по оси Y
     * @param d степень замены lsb
     */
    public static void saveHiddenBit(BufferedImage hidimg, int x, int y, int d) {
        int color = (d << 24) | (d << 16) | (d << 8) | d;
        hidimg.setRGB(x, y, color);
    }

    /**
     * Перевод десятичного числа в 8-битную двоичную запись
     * @param substring число, которое необходимо представить в двоичной
     * @return 8-битную двоичную запись числа
     */
    public static String to8bit(int substring) {
        return String.format("%8s", Integer.toBinaryString(substring)).replace(' ', '0');
    }

    /**
     * Запись части ообщения в последние два бита
     * @param codedText закодированное сообщение
     * @param byt цветовое значение пикселя(байта цвета)
     * @param messageIn индекс символа сообщения
     * @return измененное значение байта цвета
     */
    public static int attachInfoIntoBit(String codedText, int byt, int messageIn) {
        StringBuilder substring = new StringBuilder(to8bit(byt));
        substring.setCharAt(6, codedText.charAt(messageIn++));
        substring.setCharAt(7, codedText.charAt(++messageIn));
        return Integer.parseInt(substring.toString(), 2);
    }

    /**
     * Двоичное кодирование сообщения по 8 бит
     * @param message исходное сообщение
     * @return закодированное в двоичную систему сообщение
     */
    public static String codingMessage(String message) {
        StringBuilder coded = new StringBuilder();
        char[] symbols = message.toCharArray();
        for (char character : symbols) {
            coded.append(to8bit(character));
        }
        return coded.toString();
    }

    /**
     * Проверка вместимости введенного текста в изображение
     * @param image размер исходного изображения
     * @param message исходное сообщение
     * @return true/false вместит ли изображение сообщение
     */
    public static boolean checkMessageLength(BufferedImage image, String message) {
        if (message.length() * 8 > image.getWidth() * image.getHeight() * 3) {
            System.out.println("Message is too large to fit in the image.");
            return false;
        }
        return true;
    }

    /**
     * Проверка на степень изменения байта цвета
     * @param img исходное изображение
     * @param b исходное значение байта синего
     * @param g исходное значение байта зеленого
     * @param r исходное значение байта красного
     * @param new_b значение изменённого байта синего
     * @param new_g значение изменённого байта зеленого
     * @param new_r значение изменённого байта красного
     * @param x координата пикселя по оси X
     * @param y координата пикселя по оси Y
     */

    public static void checkDegreeRGB(BufferedImage img, int b, int g, int r, int new_b, int new_g, int new_r, int x, int y) {
        int degree = 255;
        if (r != new_r) {
            degree -= 85;
        }
        if (g != new_g) {
            degree -= 85;
        }
        if (b != new_b) {
            degree -= 85;
        }
        saveHiddenBit(img, x, y, degree);
    }

    /**
     * Основной прогон по каждому пикселю и вложение в него информации
     * @param image исходное изображение
     * @param hidimage изображение, отображающее изменённые lsb
     * @param message исходное сообщение
     */
    public static void hideMessage(BufferedImage image, BufferedImage hidimage, String message) {
        int imageWidth = SourceImage.getStats(image)[0];
        int imageHeight = SourceImage.getStats(image)[1];

        if (!checkMessageLength(image, message)) {
            return;
        }
        String codedMessage = codingMessage(message);

        int messageIndex = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color c = new Color(image.getRGB(x, y));

                int blue = c.getBlue(), new_blue = blue;
                int green = c.getGreen(), new_green = green;
                int red = c.getRed(), new_red = red;


                if (messageIndex < codedMessage.length() - 2) {
                    new_blue = attachInfoIntoBit(codedMessage, blue, messageIndex);
                    messageIndex += 2;
                }

                if (messageIndex < codedMessage.length() - 2) {
                    new_green = attachInfoIntoBit(codedMessage, green, messageIndex);
                    messageIndex += 2;
                }

                if (messageIndex < codedMessage.length() - 2) {
                    new_red = attachInfoIntoBit(codedMessage, red, messageIndex);
                    messageIndex += 2;
                }

                checkDegreeRGB(hidimage, blue, green, red, new_blue, new_green, new_red, x, y);
                int newRGB = (new_red << 16) | (new_green << 8) | new_blue;
                image.setRGB(x, y, newRGB);
            }
        }
    }
}
