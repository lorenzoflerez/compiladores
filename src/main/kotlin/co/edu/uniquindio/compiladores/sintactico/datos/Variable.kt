package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Variable( var tipoDato: Token, var identificador: Token, var expresion: Expresion ) {

    override fun toString(): String {
        return "Variable(tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }
}