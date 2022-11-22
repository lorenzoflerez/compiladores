package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionRelacional(var expresionIzquierda: Expresion, var operador: Token, var expresionDerecha: Expresion):Expresion() {


    override fun toString(): String {
        return "ExpresionRelacional(expresionIzquierda=$expresionIzquierda, operador=$operador, expresionDerecha=$expresionDerecha)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Relacional")
        raiz.children.add(TreeItem("${expresionIzquierda.getTexto()} ${operador.lexema} ${expresionDerecha.getTexto()}"))
        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String): String {
        return "boolean"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
       //if()
    }
}