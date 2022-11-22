package co.edu.uniquindio.compiladores.semantico

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.sintactico.estructura.UnidadDeCompilacion

class AnalizadorSemantico (var unidadCompilacion: UnidadDeCompilacion){

    var erroresSemanticos: ArrayList<Error> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(erroresSemanticos)

    fun llenarTablaSimbolos() {
        unidadCompilacion.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos) }

    fun analizarSemantica() {
        unidadCompilacion.analizarSemantica(tablaSimbolos, erroresSemanticos) }
}