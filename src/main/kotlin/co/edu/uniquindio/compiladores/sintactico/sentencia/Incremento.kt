package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token

class Incremento( var identificadorVariable: Token ): Sentencia() {

    override fun toString(): String {
        return "Incremento(identificadorVariable=$identificadorVariable)"
    }
}