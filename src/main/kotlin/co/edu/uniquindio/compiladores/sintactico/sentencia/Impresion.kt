package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Impresion(var expresion: Expresion) :Sentencia() {

    override fun toString(): String {
        return "Impresion(expresion=$expresion)"
    }
}