package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.estructura.Funcion
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Retorno( var expresion: Expresion ): Sentencia() {

    override fun toString(): String {
        return "Retorno(expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Retorno")
        raiz.children.add( expresion.getArbolVisual() )
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        expresion.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        val tipoExpresion= expresion.obtenerTipo(tablaSimbolos, ambito)
        val simboloFuncion = tablaSimbolos.buscarSimboloFuncion(ambito, expresion.obtenerTipo(tablaSimbolos, ambito))
        if(simboloFuncion!=null) {
            val tipoFuncion = simboloFuncion.tipo
            if (tipoFuncion == null) {
                erroresSemanticos.add(Error("Retorno incorrecto en $ambito. Las funciones void no retornan", -1, -1))
            } else if (tipoExpresion == null) {
                erroresSemanticos.add(Error("Retorno incorrecto en $ambito. Las expresión no retorna un tipo valido", -1, -1))
            } else if (tipoFuncion != tipoExpresion) {
                erroresSemanticos.add(
                    Error(
                        "Los tipos de dato no coinciden. Se esperaba un $tipoFuncion pero se encontro un $tipoExpresion en $ambito",
                        -1,
                        -1
                    )
                )
            }
        }
    }

    override fun getJavaCode(): String {
        return "return "+expresion.getJavaCode()+";"
    }
}