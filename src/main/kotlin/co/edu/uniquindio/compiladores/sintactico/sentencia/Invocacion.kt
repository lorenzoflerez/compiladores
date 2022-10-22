package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.estructura.Parametro

class Invocacion( var identificadorFuncion: Token, var parametros: ArrayList<Parametro>?) : Sentencia() {

    override fun toString(): String {
        return "Invocacion(identificadorFuncion=$identificadorFuncion, parametros=$parametros)"
    }
}