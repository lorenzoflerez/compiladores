package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class CicloWhile( var expresion:ExpresionLogica, var bloqueSentencias: ArrayList<Sentencia> ): Ciclo() {

    override fun toString(): String {
        return "CicloWhile(expresion=$expresion, bloqueSentencias=$bloqueSentencias)"
    }
}