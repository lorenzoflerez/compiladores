module co.edu.uniquindio.compiladores.app {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens co.edu.uniquindio.compiladores.controlador to javafx.fxml;
    opens co.edu.uniquindio.compiladores.lexico to javafx.base;
    opens co.edu.uniquindio.compiladores.sintactico to javafx.base;
    opens co.edu.uniquindio.compiladores.semantico to javafx.base;
    exports co.edu.uniquindio.compiladores.app;
}