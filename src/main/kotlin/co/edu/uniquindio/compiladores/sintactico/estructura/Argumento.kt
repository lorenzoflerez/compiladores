package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Argumento(var expresion: Expresion){

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Argumento")
        raiz.children.add(expresion.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Argumento(expresion=$expresion)"
    }
}