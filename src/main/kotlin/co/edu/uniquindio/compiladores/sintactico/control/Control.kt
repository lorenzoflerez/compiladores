package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class Control(var bloqueSentencias: ArrayList<Sentencia>, var tipoDato: Token, var identificador: Token): Sentencia() {

    override fun toString(): String {
        return "Control(bloqueSentencias=$bloqueSentencias, tipoDato=$tipoDato, identificador=$identificador)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Control")

        val raizTry = TreeItem("Sentencias try")
        for (sentencia in bloqueSentencias){
            raizTry.children.add(sentencia.getArbolVisual())
        }
        raiz.children.add( raizTry )

        val raizCatch = TreeItem("catch")
        raizCatch.children.add(TreeItem("${identificador.lexema} : ${tipoDato.lexema}"))

        raiz.children.add(raizCatch)
        return raiz
    }
}