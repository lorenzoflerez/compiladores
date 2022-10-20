package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class Alternativa( var expresion: ExpresionLogica, var bloqueSentencias: ArrayList<Sentencia> ) {

    override fun toString(): String {
        return "Alternativa(expresion=$expresion, bloqueSentencias=$bloqueSentencias)"
    }
}