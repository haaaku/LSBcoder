package com.example.lsbcoder;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Переписанный лаунчер для сборки
 */
public class Main extends LSBcoderApplication {
    /**
     * Переменная для логгирования
     */
    private static final Logger logger = LogManager.getLogger("Main");

    /**
     * Запуск программы
     * @param args пустые аргументы, поскольку ввод двнных через интерфейс
     */
    public static void main(String[] args) {
        logger.info("Program successfully launched.");
        Application.launch(LSBcoderApplication.class, args);
        logger.info("Program was finished.");
    }
}
