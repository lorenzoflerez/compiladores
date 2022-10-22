package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class Seleccion( var expresion: Expresion, var bloqueOpciones: ArrayList<Opcion>): Sentencia(){

    override fun toString(): String {
        return "Seleccion(expresion=$expresion, bloqueOpciones=$bloqueOpciones)"
    }
}