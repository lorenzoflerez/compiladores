module co.edu.uniquindio.compiladores.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens co.edu.uniquindio.compiladores.controlador to javafx.fxml;
    exports co.edu.uniquindio.compiladores.app;
}