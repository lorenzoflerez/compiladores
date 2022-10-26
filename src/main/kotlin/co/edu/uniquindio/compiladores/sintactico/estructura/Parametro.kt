package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Parametro( var tipoDato: Token, var parametro: Token ) {

    override fun toString(): String {
        return "Parametro(parametro=$tipoDato, tipoDato=$parametro)"
    }

    fun getArbolVisual(): TreeItem<String> {
        return TreeItem("${parametro.lexema} : ${tipoDato.lexema}")
    }
}