package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Seleccion( var expresion: Expresion, var bloqueOpciones: ArrayList<Opcion>) {

    override fun toString(): String {
        return "Seleccion(expresion=$expresion, bloqueOpciones=$bloqueOpciones)"
    }
}