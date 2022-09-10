package co.edu.uniquindio.compiladores.controlador

import javafx.fxml.FXML
import javafx.scene.control.Label

class AppController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
}