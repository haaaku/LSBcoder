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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LSBcoderApplication extends Application {

    static File file = null;
    static String message = null;
    static BufferedImage img;
    static EncodeProccess encoding = new EncodeProccess();
    static Font mainFont = new Font("Arial Rounded MT Bold", 16);
    static Stage mainstage;
    static Scene primaryScene, secondScene;
    static VBox begin;
    static TilePane resultMenu;
    static Button chooseImage, encodeBtn, restartBtn, saveAsBtn;
    static BackgroundImage back;
    static FileChooser choose;
    static TextArea getMessageArea;
    static Label getMessageLabel, srcImgLabel, hidImgLabel;
    static ImageView outputImg1 = null, outputImg2 = null;
    static MediaPlayer mediaPlayer;
    static DirectoryChooser save;


    public static void playerON() {
        String pathMusic = "src\\main\\resources\\music.mp3";
        Media sound = new Media(new File(pathMusic).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.02);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
    }


    public static void playerOFF() {
        mediaPlayer.stop();
    }

    public static void setWindow() throws FileNotFoundException {
        mainstage = new Stage();
        mainstage.setTitle("LSBcoder");
        mainstage.setResizable(false);
        mainstage.setWidth(600);
        mainstage.setHeight(500);
        mainstage.getIcons().add(new Image(new FileInputStream("src/main/resources/icon.png")));
    }

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

    public static void setScenes() throws FileNotFoundException {
        begin = new VBox();
        begin.setSpacing(20);
        begin.setPadding(new Insets(20, 20, 20, 20));
        begin.setAlignment(Pos.CENTER);
        back = new BackgroundImage(new Image(new FileInputStream("src\\main\\resources\\background.png"), 600, 500, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        begin.setBackground(new Background(back));

        resultMenu = new TilePane();
        resultMenu.setPadding(new Insets(50, 50, 50, 50));
        resultMenu.setAlignment(Pos.CENTER);
        resultMenu.setBackground(new Background(back));
    }

    public static void setLabels() {
        getMessageLabel = new Label("Enter the message: ");
        getMessageLabel.setFont(mainFont);
        getMessageLabel.setAlignment(Pos.CENTER_LEFT);

        srcImgLabel = new Label("Output image:                       ");
        srcImgLabel.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        srcImgLabel.setFont(mainFont);

        hidImgLabel = new Label("Less significant bits:              ");
        hidImgLabel.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        hidImgLabel.setFont(mainFont);
    }

    public static void setFileChooser() {
        choose = new FileChooser();
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BMP", "*.bmp"));
        choose.setTitle("Open Resource File");
    }

    public static void setTextArea() {
        getMessageArea = new TextArea();
        getMessageArea.setStyle("-fx-background-color: #f5062c;");
        getMessageArea.setEditable(false);
    }

    public static void cleanup() {
        file = null;
        message = null;
        img = null;
    }

    @Override
    public void start(Stage stage) throws IOException {

        setWindow();

        setScenes();

        playerON();

        setButtons();

        setLabels();

        setTextArea();

        begin.getChildren().addAll(chooseImage, getMessageLabel, getMessageArea, encodeBtn);
        primaryScene = new Scene(begin, 600, 500);
        mainstage.setScene(primaryScene);

        chooseImage.setOnAction(event -> { // Нажатие на кнопку выбора файла
            setFileChooser();
            file = choose.showOpenDialog(mainstage);

            if (file == null) {
                Alert alert_nofile = new Alert(Alert.AlertType.ERROR, "File not found. Try again", ButtonType.OK);
                alert_nofile.showAndWait();
                getMessageLabel.setText("File not found. Try again"); // Ошибка файл не выбран
            } else {
                getMessageLabel.setText("Complete! Now enter the text: ");
                getMessageArea.setEditable(true);
                getMessageArea.setStyle("-fx-background-color: #67e8c2;");
                encodeBtn.setDisable(false); // Активация поля ввода текста
            }
        });

        encodeBtn.setOnAction(event -> { // Нажатие на кнопку кодирования
            message = getMessageArea.getText();

            try {
                img = ImageIO.read(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (message.isEmpty()) {
                System.out.println(message);
                Alert alert_nomsg = new Alert(Alert.AlertType.ERROR, "Text not found. Try again", ButtonType.OK);
                alert_nomsg.showAndWait();

            } else if (!EncodeLsb.checkMessageLength(img, message)) {
                Alert alert_toobigmsg = new Alert(Alert.AlertType.ERROR, "Message is too big for this image!", ButtonType.OK);
                alert_toobigmsg.showAndWait();
                getMessageLabel.setText("Too large message.");
            } else {
                EncodeProccess.process(file.getAbsolutePath(), message);

                try {
                    outputImg1 = new ImageView(new Image(new FileInputStream("result\\output_image.bmp")));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                outputImg1.setFitHeight(150);
                outputImg1.setFitWidth(150);

                try {
                    outputImg2 = new ImageView(new Image(new FileInputStream("result\\hidden_image.bmp")));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                outputImg2.setFitHeight(150);
                outputImg2.setFitWidth(150);

                resultMenu.getChildren().addAll(srcImgLabel, outputImg1, hidImgLabel, outputImg2, restartBtn, saveAsBtn); // Забрать детей

                secondScene = new Scene(resultMenu, 600, 500);
                mainstage.setScene(secondScene);
            }
        });

        restartBtn.setOnAction(event -> { // Нажатие на кнопку перезапуска
            cleanup();
            try {
                playerOFF();
                mainstage.close();
                start(mainstage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        saveAsBtn.setOnAction(event -> { //Нажатие на кнопку сохранения
            save = new DirectoryChooser();
            save.setTitle("Open Output Directory");
            String dir = save.showDialog(stage).getAbsolutePath() + "\\output.bmp";

            try {
                File outputFile = new File(dir);
                ImageIO.write(SourceImage.getImage("result\\output_image.bmp"), "bmp", outputFile);
                String complete = String.format("Complete! File saved to %s", dir);
                Alert alert_saved = new Alert(Alert.AlertType.INFORMATION, complete, ButtonType.OK);
                alert_saved.showAndWait();
            } catch (Exception e) {
                Alert alert_nodirectory = new Alert(Alert.AlertType.ERROR, "Path not found.", ButtonType.OK);
                alert_nodirectory.showAndWait();
            }
        });

        mainstage.show();

    }


    /**
     * Запуск программы
     *
     * @param args пустые, поскольку ввод данных реализован в графической оболочке
     */
    public static void main(String[] args) {
        launch();
    }
}
