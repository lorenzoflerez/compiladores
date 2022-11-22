package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.estructura.Argumento
import javafx.scene.control.TreeItem

class Invocacion(var identificadorFuncion: Token, var argumentos: ArrayList<Argumento>) : Sentencia() {

    override fun toString(): String {
        return "Invocacion(identificadorFuncion=$identificadorFuncion, argumentos=$argumentos)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Invocación Función")
        raiz.children.add( TreeItem("función : ${identificadorFuncion.lexema}" ))
        if ( argumentos!!.isNotEmpty()) {
            val args = TreeItem("Argumentos")
            raiz.children.add(args)
            for (argumento in argumentos!!) {
                args.children.add(argumento.getArbolVisual())
            }
        }
        return raiz
    }

    fun obtenerTiposArgumentos(tablaSimbolos: TablaSimbolos, ambito: String):ArrayList<String>{
        val listaArgs =ArrayList<String>()
        for (arg in argumentos){
            listaArgs.add(arg.expresion.obtenerTipo(tablaSimbolos, ambito))
        }
        return listaArgs
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        val tipoArgs = obtenerTiposArgumentos(tablaSimbolos, ambito)
        val simbolo = tablaSimbolos.buscarSimboloFuncion(identificadorFuncion.lexema,tipoArgs)
        if(simbolo==null){
            erroresSemanticos.add(Error("La función ${identificadorFuncion.lexema} $tipoArgs no existe",identificadorFuncion.fila,identificadorFuncion.columna))
        }
    }
}