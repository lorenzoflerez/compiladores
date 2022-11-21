package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import javafx.scene.control.TreeItem

/**
 * <UnidadDeCompilacion> ::= class identificador begin [<ListaImport>] <BloqueFunciones> end
 */
class UnidadDeCompilacion( var identificador: Token, var listaImports: ArrayList<Import>?, var bloqueFunciones: ArrayList<Funcion> ) {

    override fun toString(): String {
        return "UnidadDeCompilacion(identificador=$identificador, listaImports=$listaImports, bloqueFunciones=$bloqueFunciones)"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Unidad de compilación")
        raiz.children.add( TreeItem("clase : ${identificador.lexema}") )

        if (!listaImports.isNullOrEmpty()) {
            val imports = TreeItem("Imports")
            raiz.children.add(imports)
            for (import in listaImports!!) {
                imports.children.add(import.getArbolVisual())
            }
        }

        if (bloqueFunciones.isNotEmpty()) {
            val funciones = TreeItem("Funciones")
            raiz.children.add(funciones)
            for (import in bloqueFunciones) {
                funciones.children.add(import.getArbolVisual())
            }
        }

        return raiz
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String>) {
        for (f in bloqueFunciones) {
            f.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, "Unidad de compilación")
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos:
    ArrayList<String>) {
        for (f in bloqueFunciones) {
            //f.analizarSemantica(tablaSimbolos, erroresSemanticos)
        }
    }
}