package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
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
        if (!parametros.isNullOrEmpty()) {
            val params = TreeItem("Parámetros")
            raiz.children.add(params)
            for (parametro in parametros!!) {
                params.children.add(parametro.getArbolVisual())
            }
        }

        if(bloqueSentencias.isNullOrEmpty()){
            val sentencias = TreeItem("Sentencias: void")
            raiz.children.add(sentencias)
        }
        else{
            val sentencias = TreeItem("Sentencias")
            raiz.children.add(sentencias)
            for (sentencia in bloqueSentencias) {
                sentencias.children.add(sentencia.getArbolVisual())
            }
        }
        return raiz
    }

    private fun obtenerTiposParametros(): ArrayList<String> {
        val tipoParametros = ArrayList<String>()
        if (!parametros.isNullOrEmpty()) {
            for (parametro in parametros!!) {
                tipoParametros.add(parametro.tipoDato.lexema)
            }
        }
        return tipoParametros
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito:String) {
        if (tipoRetorno != null) {
            tablaSimbolos.guardarSimboloFuncion(identificador.lexema, tipoRetorno!!.lexema, obtenerTiposParametros(),ambito,identificador.fila,identificador.columna)
        } else {
            tablaSimbolos.guardarSimboloFuncion(identificador.lexema, null, obtenerTiposParametros(),ambito,identificador.fila,identificador.columna)
        }
        if (!parametros.isNullOrEmpty()) {
            for (parametro in parametros!!) {
                tablaSimbolos.guardarSimboloVariable(parametro.parametro.lexema,parametro.tipoDato.lexema, true,identificador.lexema, parametro.parametro.fila, parametro.parametro.columna)
            }
        }
        if(bloqueSentencias.isNullOrEmpty()){
            for (s in bloqueSentencias) {
                s.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, identificador.lexema)
            }
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>) {
        for (s in bloqueSentencias) {
            s.analizarSemantica(tablaSimbolos, erroresSemanticos, identificador.lexema)
        }
    }




}