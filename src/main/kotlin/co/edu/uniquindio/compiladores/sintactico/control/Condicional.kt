package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class Condicional( var expresion: ExpresionLogica, var bloqueSentenciasSi: ArrayList<Sentencia>,
                   var bloqueSentenciasNo: ArrayList<Sentencia>? ): Sentencia() {

    override fun toString(): String {
        return "Condicional(expresion=$expresion, bloqueSentenciasSi=$bloqueSentenciasSi, bloqueSentenciasNo=$bloqueSentenciasNo)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Decisión")
        val condicion =  TreeItem("Condición")

        condicion.children.add( expresion.getArbolVisual() )
        raiz.children.add(condicion)

        val raizTrue = TreeItem("Sentencias si")
        for (sentencia in bloqueSentenciasSi){
            raizTrue.children.add(sentencia.getArbolVisual())
        }

        raiz.children.add( raizTrue )

        if(bloqueSentenciasNo!=null){
            println("si lo hace")
            val raizFalse = TreeItem("Sentencias si no")
            for (sentencia in bloqueSentenciasNo!!){
                raizFalse.children.add(sentencia.getArbolVisual())
            }
            raiz.children.add( raizFalse )
        }

        return raiz

    }
}