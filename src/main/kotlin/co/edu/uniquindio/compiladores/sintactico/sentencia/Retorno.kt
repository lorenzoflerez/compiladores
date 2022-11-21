package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Retorno( var expresion: Expresion ): Sentencia() {

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Retorno")
        raiz.children.add( expresion.getArbolVisual() )
        return raiz
    }
}