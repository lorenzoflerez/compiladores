package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

/**
 * <Funcion> ::= function identificador ( [<ListaParametros>] ) [typeOf <Tipodato>] <BloqueSentencias>
 */
class Funcion( var identificador :Token, var parametros: ArrayList<Parametro>?, var tipoRetorno: Token?, var bloqueSentencias: ArrayList<Sentencia> ){

    override fun toString(): String {
        return "Funcion(identificador=$identificador, parametros=$parametros, tipoRetorno=$tipoRetorno, bloqueSentencias=$bloqueSentencias)"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Función")
        if (tipoRetorno != null) {
            raiz.children
                .add( TreeItem("funcion : ${identificador.lexema} ( ${tipoRetorno!!.lexema} )") )
        } else {
            raiz.children.add( TreeItem("funcion : ${identificador.lexema}" ))
        }
        if ( parametros!!.isNotEmpty()) {
            val params = TreeItem("Parámetros")
            raiz.children.add(params)
            for (parametro in parametros!!) {
                params.children.add(parametro.getArbolVisual())
            }
        }
        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in bloqueSentencias) {
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }

}