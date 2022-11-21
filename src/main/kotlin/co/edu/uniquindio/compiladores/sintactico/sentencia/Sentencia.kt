package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import javafx.scene.control.TreeItem

open class Sentencia() {

    open fun getArbolVisual(): TreeItem<String> {
        return  TreeItem("Sentencia")
    }

    open fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String>, ambito:String) {

    }
}