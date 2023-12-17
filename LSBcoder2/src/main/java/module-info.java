module com.example.lsbcoder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires org.apache.logging.log4j;

    opens back to javafx.fxml;
    exports back;
    exports com.example.lsbcoder;
    opens com.example.lsbcoder to javafx.fxml;
}