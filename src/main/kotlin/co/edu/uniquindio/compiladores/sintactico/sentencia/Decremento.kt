package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token

class Decremento( var identificadorVariable: Token ): Sentencia() {

    override fun toString(): String {
        return "Decremento(identificadorVariable=$identificadorVariable)"
    }
}