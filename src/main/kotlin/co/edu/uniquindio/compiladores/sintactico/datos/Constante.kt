package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Constante(var tipoDato: Token, var identificador: Token, var expresion: Expresion): Declaracion() {

    override fun toString(): String {
        return "Constante(tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Declaración Constante")
        raiz.children.add( TreeItem("constante : ${identificador.lexema} (${tipoDato.lexema})") )
        raiz.children.add( expresion.getArbolVisual() )
        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema,tipoDato.lexema,false,ambito,identificador.fila,identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if(expresion!=null){
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

}