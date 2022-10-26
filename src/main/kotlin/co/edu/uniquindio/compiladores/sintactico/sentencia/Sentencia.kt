package co.edu.uniquindio.compiladores.sintactico.sentencia

import javafx.scene.control.TreeItem

open class Sentencia() {

    open fun getArbolVisual(): TreeItem<String> {
        return  TreeItem("Sentencia")
    }

}