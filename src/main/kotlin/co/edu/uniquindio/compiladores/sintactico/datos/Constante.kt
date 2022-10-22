package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Constante(var tipoDato: Token, var identificador: Token, var expresion: Expresion): Declaracion() {

    override fun toString(): String {
        return "Constante(tipoDato=$tipoDato, identificador=$identificador, expresion=$expresion)"
    }
}