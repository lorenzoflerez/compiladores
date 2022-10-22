package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion

class Asingnacion(var identificadorAsingnacion: Token, var expresion: Expresion) : Sentencia(){

    override fun toString(): String {
        return "Asingnacion(identificadorAsingnacion=$identificadorAsingnacion, expresion=$expresion)"
    }
}