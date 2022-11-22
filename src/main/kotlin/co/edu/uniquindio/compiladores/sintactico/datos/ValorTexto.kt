package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token

class ValorTexto(var valor: Token): Valor() {

    override fun toString(): String {
        return "ValorTexto(valor=$valor)"
    }

    override fun getJavaCode(): String {
        return valor.getJavaCode()
    }
}