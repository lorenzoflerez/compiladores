package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token

class Lectura(var identificadorLectura: Token) : Sentencia() {

    override fun toString(): String {
        return "Lectura(identificadorLectura=$identificadorLectura)"
    }
}