package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.datos.ValorNumerico
import javafx.scene.control.TreeItem

class ExpresionAritmetica(): Expresion() {



    var expresionIzquierda: ExpresionAritmetica? = null
    var expresionDerecha: ExpresionAritmetica? = null
    var operador: Token? = null
    var valorNumerico: ValorNumerico? = null

    constructor( expresionIzquierda:ExpresionAritmetica?, operador:Token?, expresionDerecha: ExpresionAritmetica? ): this(){
        this.expresionIzquierda = expresionIzquierda
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( expresionIzquierda:ExpresionAritmetica?): this(){
        this.expresionIzquierda = expresionIzquierda
    }

    constructor( valorNumerico: ValorNumerico?, operador:Token?, expresionDerecha: ExpresionAritmetica? ): this(){
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( valorNumerico: ValorNumerico? ): this(){
        this.valorNumerico = valorNumerico
    }

    override fun toString(): String {
        return "ExpresionAritmetica(expresionIzquierda=$expresionIzquierda, expresionDerecha=$expresionDerecha, operador=$operador, valorNumerico=$valorNumerico)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Aritmética")
        return raiz
    }
}