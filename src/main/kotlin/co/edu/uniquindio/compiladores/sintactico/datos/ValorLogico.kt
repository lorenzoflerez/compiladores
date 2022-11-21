package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionRelacional

class ValorLogico(): Valor(){

    var expresion: ExpresionRelacional? = null
    var valorLogico: Token? = null

    constructor( expresion: ExpresionRelacional?): this(){
        this.expresion = expresion
    }

    constructor( valorLogico: Token?): this(){
        this.valorLogico = valorLogico
    }

    override fun toString(): String {
        return "ValorLogico(expresion=$expresion, valorLogico=$valorLogico)"
    }
}