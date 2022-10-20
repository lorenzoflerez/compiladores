package co.edu.uniquindio.compiladores.sintactico

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.KeyWords
import co.edu.uniquindio.compiladores.sintactico.estructura.Import
import co.edu.uniquindio.compiladores.sintactico.estructura.UnidadDeCompilacion

class AnalizadorSintactico (var listaTokens:ArrayList<Token>){
    private var keys = KeyWords()
    var posicionActual = 0
    var tokenActual = listaTokens[posicionActual]
    var listaErrores = ArrayList<Error>()

    fun obtenerSiguienteToken(){
        posicionActual++;
        if( posicionActual < listaTokens.size ) {
            tokenActual = listaTokens[posicionActual]
        }
    }

    /**
     * <UnidadDeCompilacion> ::= class identificador begin [<ListaImport>] <BloqueSentencias> end
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        return null
    }


    /**
     * <ListaImport> ::= include llaveIzquierda <Import> llaveDerecha
     */
    fun esListaImport(): ArrayList<Import>{
        var listaImport: ArrayList<Import> = esListaImport()
        var import= esImport()

        while ( import != null ){
            listaImport.add( import )
            import = esImport()
        }
        return listaImport
    }

    /**
     * <Import> ::= "libreria"; [<Import>]
     */
    fun esImport(): Import?{

        return null
    }

    /**
     *
     */
    fun esTipoDato(): Token?{
        if( tokenActual.categoria == Categoria.PALABRA_RESERVADA ){
            if ( keys.leerLexema(tokenActual.lexema) == "tipoDato")
                return tokenActual
        }
        return null
    }

}