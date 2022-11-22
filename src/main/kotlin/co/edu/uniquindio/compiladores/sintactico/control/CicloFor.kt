package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.datos.Valor
import co.edu.uniquindio.compiladores.sintactico.datos.ValorNumerico
import co.edu.uniquindio.compiladores.sintactico.datos.Variable
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class CicloFor(var indice: Variable, var limite: ValorNumerico, var bloqueSentencias: ArrayList<Sentencia> ) : Ciclo() {
    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Ciclo For")
        raiz.children.add(indice.getArbolVisual())
        raiz.children.add(TreeItem("Rango : ${limite.numero.lexema}"))
        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in bloqueSentencias){
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "CicloFor(indice=$indice, limite=$limite, bloqueSentencias=$bloqueSentencias)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        indice.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        for (sentencia in bloqueSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        indice.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        for (sentencia in bloqueSentencias){
            sentencia.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }
}