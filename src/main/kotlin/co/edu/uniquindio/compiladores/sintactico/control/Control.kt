package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class Control(var bloqueSentencias: ArrayList<Sentencia>, var tipoDato: Token, var identificador: Token): Sentencia() {

    override fun toString(): String {
        return "Control(bloqueSentencias=$bloqueSentencias, tipoDato=$tipoDato, identificador=$identificador)"
    }
}