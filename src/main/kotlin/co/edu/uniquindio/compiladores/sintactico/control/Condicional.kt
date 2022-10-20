package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class Condicional( var expresion: ExpresionLogica, var bloqueSentenciasSi: ArrayList<Sentencia>,
                   var bloqueAlternativas: ArrayList<Alternativa>, var bloqueSentenciasNo: ArrayList<Sentencia> ) {

    override fun toString(): String {
        return "Condicional(expresion=$expresion, bloqueSentenciasSi=$bloqueSentenciasSi, bloqueAlternativas=$bloqueAlternativas, bloqueSentenciasNo=$bloqueSentenciasNo)"
    }
}