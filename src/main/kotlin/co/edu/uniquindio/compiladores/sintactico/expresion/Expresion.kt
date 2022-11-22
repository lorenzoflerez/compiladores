package co.edu.uniquindio.compiladores.sintactico.expresion

import javafx.scene.control.TreeItem

open class Expresion {

    open fun getArbolVisual(): TreeItem<String> {
        return  TreeItem("Expresión")
    }

    open fun getTexto(): String{
        return ""
    }
}