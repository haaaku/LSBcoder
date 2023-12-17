package com.example.lsbcoder;

import back.EncodeLsb;
import back.EncodeProccess;
import back.SourceImage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс приложения с реализацией графического интерфейса
 */
public class LSBcoderApplication extends Application {
    /**
     * Файл с исходным изображением
     */
    static File file = null;
    /**
     * Сообщение для кодирования
     */
    static String message = null;
    /**
     * изменяемое изображение, генерируемое из file
     */
    static BufferedImage img;
    static EncodeProccess encoding = new EncodeProccess();
    /**
     * Основной шрифт
     */
    static Font mainFont = new Font("Arial Rounded MT Bold", 16);
    /**
     * Основное окно
     */
    static Stage mainstage;
    /**
     * Сцены (начальное меню и меню результата)
     */
    static Scene primaryScene, secondScene;
    /**
     * Макет содержимого начального меню
     */
    static VBox begin;
    /**
     * Макет содержимого меню результата
     */
    static TilePane resultMenu;
    /**
     * Кнопки (выбора исходника, запуска кодирования, перезапуска, сохранения)
     */
    static Button chooseImage, encodeBtn, restartBtn, saveAsBtn;
    /**
     * FileChooser для файла исходника через проводник
     */
    static FileChooser choose;
    /**
     * TextArea для получения сообщения для кодирования
     */
    static TextArea getMessageArea;
    /**
     * Подписи (указаний действий в начальном меню, выходного изображения, изображения с lsb)
     */
    static Label getMessageLabel, srcImgLabel, hidImgLabel;
    /**
     * изображения (выходное, отображение lsb)
     */
    static ImageView outputImg1 = null, outputImg2 = null;
    /**
     * Переменная для логгирования
     */
    private static final Logger logger = LogManager.getLogger("LSBcoderApplication");
    /**
     * DirectoryChooser для сохранения выходного файла через проводник
     */
    static DirectoryChooser save;

    /**
     * Задаёт параметры окон
     *
     * @throws FileNotFoundException исключение в случае ненайденной иконки приложения
     */
    public static void setWindow() throws FileNotFoundException {
        mainstage = new Stage();
        mainstage.setTitle("LSBcoder");
        mainstage.setResizable(false);
        mainstage.setWidth(800);
        mainstage.setHeight(650);
    }

    /**
     * Задаёт параметры кнопкам
     */
    public static void setButtons() {
        chooseImage = new Button("Choose your image");
        chooseImage.setFont(mainFont);
        chooseImage.setStyle("-fx-background-color: #9d93ff;");
        chooseImage.setPrefSize(200, 50);

        encodeBtn = new Button("Encode");
        encodeBtn.setStyle("-fx-background-color: #ea8b9b;");
        encodeBtn.setFont(mainFont);
        encodeBtn.setPrefSize(90, 30);
        encodeBtn.setDisable(true);

        restartBtn = new Button("To the main menu");
        restartBtn.setStyle("-fx-background-color: #85dfe9;");
        restartBtn.setFont(mainFont);
        restartBtn.setPrefSize(200, 20);


        saveAsBtn = new Button("Save to...");
        saveAsBtn.setFont(mainFont);
        saveAsBtn.setStyle("-fx-background-color: #9d93ff;");
        saveAsBtn.setPrefSize(200, 20);

    }

    /**
     * Задаёт параметры сцен
     */
    public static void setScenes() {
        begin = new VBox();
        begin.setSpacing(20);
        begin.setPadding(new Insets(20, 20, 20, 20));
        begin.setAlignment(Pos.CENTER);

        resultMenu = new TilePane();
        resultMenu.setPadding(new Insets(20, 20, 20, 20));
        resultMenu.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Задаёт параметры надписей
     */
    public static void setLabels() {
        getMessageLabel = new Label("Enter the message: ");
        getMessageLabel.setFont(mainFont);
        getMessageLabel.setAlignment(Pos.CENTER_LEFT);

        srcImgLabel = new Label("Output image:");
        srcImgLabel.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        srcImgLabel.setFont(mainFont);
        srcImgLabel.setMinWidth(300);
        srcImgLabel.setMinHeight(30);

        hidImgLabel = new Label("Less significant bits:");
        hidImgLabel.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        hidImgLabel.setFont(mainFont);
        hidImgLabel.setMinWidth(300);
        hidImgLabel.setMinHeight(30);
    }

    /**
     * Задаёт параметры выборщику файлов
     */
    public static void setFileChooser() {
        choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        choose.setTitle("Open Resource File");
    }

    /**
     * Задаёт параметры поля для ввода текста
     */
    public static void setTextArea() {
        getMessageArea = new TextArea();
        getMessageArea.setStyle("-fx-background-color: #f5062c;");
        getMessageArea.setEditable(false);
    }

    /**
     * Очищает переменные перед перезапуском
     */
    public static void cleanup() {
        file = null;
        message = null;
        img = null;
    }

    /**
     * Основная функция активации приложения со сценариями событий при нажатии на кнопки
     *
     * @param stage дефолтное окно
     * @throws IOException в случае отсуствия окна
     */
    @Override
    public void start(Stage stage) throws IOException {
        setWindow();
        setScenes();
        setButtons();
        setLabels();
        setTextArea();

        begin.getChildren().addAll(chooseImage, getMessageLabel, getMessageArea, encodeBtn);
        primaryScene = new Scene(begin, 800, 650);
        mainstage.setScene(primaryScene);

        chooseImage.setOnAction(event -> { // Нажатие на кнопку выбора файла
            logger.info("chooseImage was clicked.");
            setFileChooser();
            file = choose.showOpenDialog(mainstage);
            logger.info("File chosen");

            if (file == null) {
                Alert alert_nofile = new Alert(Alert.AlertType.ERROR, "File not found. Try again", ButtonType.OK);
                alert_nofile.showAndWait();
                getMessageLabel.setText("File not found. Try again"); // Ошибка файл не выбран
                logger.error("File not found.");
            } else {
                logger.info("File found");
                getMessageLabel.setText("Complete! Now enter the text: ");
                getMessageArea.setEditable(true);
                logger.info("getMessageArea is active");
                getMessageArea.setStyle("-fx-background-color: #67e8c2;");
                encodeBtn.setDisable(false); // Активация поля ввода текста
            }
        });

        encodeBtn.setOnAction(event -> { // Нажатие на кнопку кодирования
            new File("C:\\LSBcoder\\result").mkdirs();
            logger.info("encodeBtn was clicked.");
            message = getMessageArea.getText();

            try {
                img = ImageIO.read(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (message.isEmpty()) {
                System.out.println(message);
                Alert alert_nomsg = new Alert(Alert.AlertType.ERROR, "Message not found. Try again", ButtonType.OK);
                alert_nomsg.showAndWait();
                logger.error("Message not found.");

            } else if (!EncodeLsb.checkMessageLength(img, message)) {
                Alert alert_toobigmsg = new Alert(Alert.AlertType.ERROR, "Message is too big for this image!", ButtonType.OK);
                alert_toobigmsg.showAndWait();
                logger.error("Too large message.");
                getMessageLabel.setText("Too large message.");
            } else {
                EncodeProccess.process(file.getAbsolutePath(), message);
                logger.info("Encoding success");

                try {
                    outputImg1 = new ImageView(new Image(new FileInputStream("C:\\LSBcoder\\result\\output_image.bmp")));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                outputImg1.setFitHeight(200);
                outputImg1.setFitWidth(200);

                try {
                    outputImg2 = new ImageView(new Image(new FileInputStream("C:\\LSBcoder\\result\\hidden_image.bmp")));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                outputImg2.setFitHeight(200);
                outputImg2.setFitWidth(200);

                resultMenu.getChildren().addAll(srcImgLabel, outputImg1, hidImgLabel, outputImg2, restartBtn, saveAsBtn);

                secondScene = new Scene(resultMenu, 800, 650);
                mainstage.setScene(secondScene);
            }
        });

        restartBtn.setOnAction(event -> { // Нажатие на кнопку перезапуска
            logger.info("Program restarted");
            cleanup();
            try {
                mainstage.close();
                start(mainstage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        saveAsBtn.setOnAction(event -> { //Нажатие на кнопку сохранения
            logger.info("saveAsBtn was clicked");
            save = new DirectoryChooser();
            save.setTitle("Open Output Directory");
            String dir = save.showDialog(stage).getAbsolutePath() + "\\output.bmp";

            try {
                File outputFile = new File(dir);
                ImageIO.write(SourceImage.getImage("C:\\LSBcoder\\result\\output_image.bmp"), "bmp", outputFile);
                String complete = String.format("Complete! File saved to %s", dir);
                Alert alert_saved = new Alert(Alert.AlertType.INFORMATION, complete, ButtonType.OK);
                alert_saved.showAndWait();
                logger.info(String.format("Saved to ", dir));
            } catch (Exception e) {
                Alert alert_nodirectory = new Alert(Alert.AlertType.ERROR, "Path not found.", ButtonType.OK);
                alert_nodirectory.showAndWait();
                logger.error("Path not found.");
            }
        });

        mainstage.show();
    }
}