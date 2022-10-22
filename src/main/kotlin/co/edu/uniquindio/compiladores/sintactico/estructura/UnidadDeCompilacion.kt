package co.edu.uniquindio.compiladores.sintactico.estructura

import co.edu.uniquindio.compiladores.lexico.Token

/**
 * <UnidadDeCompilacion> ::= class identificador begin [<ListaImport>] <BloqueFunciones> end
 */
class UnidadDeCompilacion( var identificador: Token, var listaImports: ArrayList<Import>?, var bloqueFunciones: ArrayList<Funcion> ) {

    override fun toString(): String {
        return "UnidadDeCompilacion(identificador=$identificador, listaImports=$listaImports, bloqueFunciones=$bloqueFunciones)"
    }
}