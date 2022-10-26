package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.estructura.Parametro
import javafx.scene.control.TreeItem

class Invocacion( var identificadorFuncion: Token, var parametros: ArrayList<Parametro>?) : Sentencia() {

    override fun toString(): String {
        return "Invocacion(identificadorFuncion=$identificadorFuncion, parametros=$parametros)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Invocación Función")
        raiz.children.add( TreeItem("función : ${identificadorFuncion.lexema}" ))
        if ( parametros!!.isNotEmpty()) {
            val params = TreeItem("Parámetros")
            raiz.children.add(params)
            for (parametro in parametros!!) {
                params.children.add(parametro.getArbolVisual())
            }
        }
        return raiz
    }
}