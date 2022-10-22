package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Retorno( var expresion: Expresion ): Sentencia() {

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }
}