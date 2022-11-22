package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.datos.ValorLogico
import javafx.scene.control.TreeItem

class ExpresionLogica(
    var expresionIzquierda: ExpresionLogica? ,
    var expresionDerecha: ExpresionLogica?,
    var operador: Token? ,
    var valorLogico: ValorLogico?) :Expresion(){

    constructor( expresionIzquierda:ExpresionLogica?, operador:Token?, expresionDerecha: ExpresionLogica? ): this(expresionIzquierda,expresionDerecha,operador,null){
        this.expresionIzquierda = expresionIzquierda
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( expresionIzquierda:ExpresionLogica?): this(expresionIzquierda,null,null,null){
        this.expresionIzquierda = expresionIzquierda
    }

    constructor(valorLogico: ValorLogico?, operador:Token?, expresionDerecha: ExpresionLogica? ): this(null, expresionDerecha, operador, valorLogico){
        this.valorLogico = valorLogico
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( valorLogico: ValorLogico? ): this(null,null,null,valorLogico){
        this.valorLogico = valorLogico
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Logica")
        if (valorLogico != null) {
            if (expresionDerecha != null) {
                val texto = getTexto()
                raiz.children.add(TreeItem(texto))
            }
            else{
                val texto = getTexto()
                raiz.children.add(TreeItem(texto))
            }
        }
        else{
            if (expresionIzquierda != null) {
                raiz.children.add(expresionIzquierda!!.getArbolVisual())
            }
            if (expresionDerecha != null) {
                raiz.children.add(expresionDerecha!!.getArbolVisual())
            }
        }
        return raiz
    }

    override fun getTexto(): String {
        if (valorLogico != null) {
            if (expresionDerecha != null) {
                return "${valorLogico!!.valorLogico!!.lexema} ${operador!!.lexema} ${expresionDerecha!!.valorLogico!!.valorLogico!!.lexema}"
            }
            return "${valorLogico!!.valorLogico!!.lexema}"
        }
        else{
            return ""
        }
    }

    override fun toString(): String {
        return "ExpresionLogica(expresionIzquierda=$expresionIzquierda, expresionDerecha=$expresionDerecha, operador=$operador, valorLogico=$valorLogico)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String): String {
        return "boolean"
    }
}