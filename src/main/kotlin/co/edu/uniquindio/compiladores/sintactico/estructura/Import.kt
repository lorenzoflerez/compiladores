package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token

class Import ( var identificador: Token ){

    override fun toString(): String {
        return "Import(identificador=$identificador)"
    }
}