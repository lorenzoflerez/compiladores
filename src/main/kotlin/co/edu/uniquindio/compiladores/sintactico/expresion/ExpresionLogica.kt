package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.datos.ValorLogico
import javafx.scene.control.TreeItem

class ExpresionLogica() :Expresion(){

    var valorIzquierda: ValorLogico? = null
    var valorDerecha: ValorLogico? = null
    var operador: Token? = null
    constructor( valorIzquierda:ValorLogico?, operador:Token?, valorDerecha: ValorLogico? ): this(){
        this.valorIzquierda = valorIzquierda
        this.operador = operador
        this.valorDerecha = valorDerecha
    }

    constructor( valorIzquierda:ValorLogico? ): this(){
        this.valorIzquierda = valorIzquierda
    }

    constructor( operador:Token?, valorDerecha: ValorLogico? ): this(){
        this.operador = operador
        this.valorDerecha = valorDerecha
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Expresión Logica")
        if (valorIzquierda != null) {
            if (valorIzquierda!!.expresion!=null){
                raiz.children.add(valorIzquierda!!.expresion!!.getArbolVisual())
            }
        }
        if (operador != null) {
            raiz.children.add(TreeItem(operador!!.lexema))
        }
        if (valorDerecha != null) {
            if (valorDerecha!!.expresion!=null){
                raiz.children.add(valorDerecha!!.expresion!!.getArbolVisual())
            }
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionLogica(valorIzquierda=$valorIzquierda, operador=$operador, valorDerecha=$valorDerecha)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String): String {
        return "boolean"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if(valorIzquierda!=null && valorDerecha!=null){
            valorIzquierda!!.expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            valorDerecha!!.expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        else{
            valorIzquierda!!.expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getJavaCode(): String {
        if(valorIzquierda!=null && valorDerecha!=null){
            return valorIzquierda!!.expresion!!.getJavaCode() + operador!!.getJavaCode() + valorDerecha!!.expresion!!.getJavaCode()
        }
        else if(operador != null && valorIzquierda!=null){
            return operador!!.getJavaCode() + valorIzquierda!!.expresion!!.getJavaCode()
        }
        else{
            return valorIzquierda!!.expresion!!.getJavaCode()
        }
    }
}