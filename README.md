Из-за использования JavaFX при сборке jar-файла проект пришлось пересобрать на другой версии языка и библиотеки JavaFx, переписать лаунчер
и вынести его в отдельный файл Main.java. После пересборки перестала работать библиотека eu.hansolo, поэтому она была удалена из проекта
из-за чего невозможна активация медиаплеера, поэтому он тоже был удалён; программа перестала видеть содержимое внутренних директорий
проекта, таких как results, resources. Фоны пришлось удалить и создать новую директорию под results. Во 2-ой версии добавлено логгирование.
Обе версии успешно запускаются внутри IntelliJ.

1.0.
Версии:
Oracle OpenJDK version 21
Javafx 21.0.1.
IntelliJ IDEA 2023.2.4 (Ultimate Edition)
Build #IU-232.10203.10, built on October 24, 2023


2.0.
Версии:
Oracle OpenJDK version 16
Javafx 17.0.9.
IntelliJ IDEA 2023.2.4 (Ultimate Edition)
Build #IU-232.10203.10, built on October 24, 2023

В качестве курсовой работы сдаю вторую версию, на всякий случай, прикрепляю обе. Спасибо за понимание и извините за путаницу.
