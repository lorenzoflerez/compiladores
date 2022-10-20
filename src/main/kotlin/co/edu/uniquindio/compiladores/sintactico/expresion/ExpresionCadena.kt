package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Token

class ExpresionCadena( var cadenas: ArrayList<Token>, var identificador: Token): Expresion() {

    override fun toString(): String {
        return "ExpresionCadena(cadenas=$cadenas, identificador=$identificador)"
    }
}