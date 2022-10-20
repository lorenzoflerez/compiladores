package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token

class TipoDato( var tipo: Token, var identificador: Token? ) {

    override fun toString(): String {
        return "TipoDato(tipo=$tipo, identificador=$identificador)"
    }
}