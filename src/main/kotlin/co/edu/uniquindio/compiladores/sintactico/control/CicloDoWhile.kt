package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class CicloDoWhile ( var bloqueSentencias: ArrayList<Sentencia>, var expresion: ExpresionLogica ): Ciclo()  {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Ciclo Do-While")
        raiz.children.add(expresion.getArbolVisual())
        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in bloqueSentencias){
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "CicloDoWhile(bloqueSentencias=$bloqueSentencias, expresion=$expresion)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        for (sentencia in bloqueSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        for (sentencia in bloqueSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = "do {"
        for (sentencia in bloqueSentencias) {
            codigo += sentencia.getJavaCode()
        }
        codigo += "} while (" +expresion.getJavaCode()+"){"
        return codigo
    }
}