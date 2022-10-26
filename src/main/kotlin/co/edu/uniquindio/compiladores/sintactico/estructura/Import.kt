package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Import ( var identificador: Token ){

    override fun toString(): String {
        return "Import(identificador=$identificador)"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Import")
        raiz.children.add( TreeItem("libreria : ${identificador.lexema}") )
        return raiz
    }
}