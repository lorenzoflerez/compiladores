package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Decremento( var identificadorVariable: Token ): Sentencia() {

    override fun toString(): String {
        return "Decremento(identificadorVariable=$identificadorVariable)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Decremento")
        raiz.children.add( TreeItem("variable : ${identificadorVariable.lexema} ") )
        return raiz
    }

    override fun getJavaCode(): String {
        return identificadorVariable.getJavaCode() + "--"
    }
}