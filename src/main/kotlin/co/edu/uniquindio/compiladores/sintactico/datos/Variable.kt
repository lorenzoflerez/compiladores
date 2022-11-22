package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
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

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema,tipoDato.lexema,true,ambito,identificador.fila,identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if(expresion!=null){
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        val codigo = tipoDato.getJavaCode() + identificador.getJavaCode()
        if (expresion!=null){
           codigo + "=" +expresion!!.getJavaCode()
        }
        return "$codigo;"
    }
}