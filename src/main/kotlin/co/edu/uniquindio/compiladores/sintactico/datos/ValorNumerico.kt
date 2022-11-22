package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token

class ValorNumerico(var signo: Token?, var numero: Token): Valor() {

    override fun toString(): String {
        return "ValorNumerico(signo=$signo, numero=$numero)"
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if(signo!=null){
            codigo += signo!!.getJavaCode()
        }
        codigo += numero.getJavaCode()
        return codigo
    }
}