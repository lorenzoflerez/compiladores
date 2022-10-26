package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Variable( var tipoDato: Token, var identificador: Token, var expresion: Expresion? ): Declaracion() {

    override fun toString(): String {
        return "Variable(tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Declaración Variable")
        raiz.children.add( TreeItem("variable : ${identificador.lexema} (${tipoDato.lexema})") )
        if(expresion!=null){
            raiz.children.add( expresion!!.getArbolVisual() )
        }
        return raiz
    }
}