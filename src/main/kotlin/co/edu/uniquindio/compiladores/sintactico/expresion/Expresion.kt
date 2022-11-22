package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import javafx.scene.control.TreeItem

open class Expresion {

    open fun getArbolVisual(): TreeItem<String> {
        return  TreeItem("Expresión")
    }

    open fun getTexto(): String{
        return ""
    }

    open fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito:String) {

    }

    open fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String):String{
        return ""
    }

    open fun getJavaCode(): String {
        return ""
    }
}