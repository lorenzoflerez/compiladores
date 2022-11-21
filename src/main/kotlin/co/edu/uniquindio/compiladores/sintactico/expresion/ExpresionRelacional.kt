package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Token

class ExpresionRelacional(var expresionIzquierda: Expresion, var operador: Token, var expresionDerecha: Expresion):Expresion() {
    override fun toString(): String {
        return "ExpresionRelacional(expresionIzquierda=$expresionIzquierda, operador=$operador, expresionDerecha=$expresionDerecha)"
    }
}