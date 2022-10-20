package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class CicloDoWhile ( var bloqueSentencias: ArrayList<Sentencia>, var expresion: ExpresionLogica ): Ciclo()  {

    override fun toString(): String {
        return "CicloDoWhile(bloqueSentencias=$bloqueSentencias, expresion=$expresion)"
    }
}