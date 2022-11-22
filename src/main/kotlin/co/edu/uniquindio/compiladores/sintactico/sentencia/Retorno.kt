package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.estructura.Funcion
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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        val tipoExpresion= expresion.obtenerTipo(tablaSimbolos, ambito)
        //comparar tipo de retorno con tipo de funcion
        //val tipoFuncion = tablaSimbolos.buscarSimboloFuncion()
    }
}