package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Lectura(var identificadorLectura: Token) : Sentencia() {

    override fun toString(): String {
        return "Lectura(identificadorLectura=$identificadorLectura)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Lectura")
        raiz.children.add( TreeItem("variable : ${identificadorLectura.lexema}" ))
        return raiz
    }
}