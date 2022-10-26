package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Impresion(var expresion: Expresion) :Sentencia() {

    override fun toString(): String {
        return "Impresion(expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Impresión")
        raiz.children.add( expresion.getArbolVisual() )
        return raiz
    }
}