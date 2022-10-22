package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

/**
 * <Funcion> ::= function identificador ( [<ListaParametros>] ) [typeOf <Tipodato>] <BloqueSentencias>
 */
class Funcion( var identificador :Token, var parametros: ArrayList<Parametro>?, var tipoRetorno: Token?, var bloqueSentencias: ArrayList<Sentencia> ){

    override fun toString(): String {
        return "Funcion(identificador=$identificador, parametros=$parametros, tipoRetorno=$tipoRetorno, bloqueSentencias=$bloqueSentencias)"
    }
}