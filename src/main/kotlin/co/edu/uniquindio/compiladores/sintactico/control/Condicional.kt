package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class Condicional( var expresion: ExpresionLogica, var bloqueSentenciasSi: ArrayList<Sentencia>,
                   var bloqueSentenciasNo: ArrayList<Sentencia>? ): Sentencia() {

    override fun toString(): String {
        return "Condicional(expresion=$expresion, bloqueSentenciasSi=$bloqueSentenciasSi, bloqueSentenciasNo=$bloqueSentenciasNo)"
    }
}