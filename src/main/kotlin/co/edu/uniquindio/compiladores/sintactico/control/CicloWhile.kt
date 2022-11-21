package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class CicloWhile( var expresion:ExpresionLogica, var bloqueSentencias: ArrayList<Sentencia> ): Ciclo() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Ciclo While")
        raiz.children.add(expresion.getArbolVisual())
        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in bloqueSentencias){
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "CicloWhile(expresion=$expresion, bloqueSentencias=$bloqueSentencias)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String>, ambito: String) {
        for (sentencia in bloqueSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }

        //super.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
    }
}