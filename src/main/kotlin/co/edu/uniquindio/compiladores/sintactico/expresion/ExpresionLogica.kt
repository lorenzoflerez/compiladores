package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.datos.ValorLogico
import javafx.scene.control.TreeItem

class ExpresionLogica():Expresion() {

    var expresionIzquierda: ExpresionLogica? = null
    var expresionDerecha: ExpresionLogica? = null
    var operador: Token? = null
    var valorLogico: ValorLogico? = null

    constructor( expresionIzquierda:ExpresionLogica?, operador:Token?, expresionDerecha: ExpresionLogica? ): this(){
        this.expresionIzquierda = expresionIzquierda
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( expresionIzquierda:ExpresionLogica?): this(){
        this.expresionIzquierda = expresionIzquierda
    }

    constructor(valorLogico: ValorLogico?, operador:Token?, expresionDerecha: ExpresionLogica? ): this(){
        this.valorLogico = valorLogico
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( valorLogico: ValorLogico? ): this(){
        this.valorLogico = valorLogico
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Lógica")
        return raiz
    }

    override fun toString(): String {
        return "ExpresionLogica(expresionIzquierda=$expresionIzquierda, expresionDerecha=$expresionDerecha, operador=$operador, valorLogico=$valorLogico)"
    }
}