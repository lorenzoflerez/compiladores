package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.datos.ValorNumerico
import javafx.scene.control.TreeItem

class ExpresionAritmetica(
    var expresionIzquierda: ExpresionAritmetica? ,
    var expresionDerecha: ExpresionAritmetica? ,
    var operador: Token? ,
    var valorNumerico: ValorNumerico?
): Expresion() {

    constructor( expresionIzquierda:ExpresionAritmetica?, operador:Token?, expresionDerecha: ExpresionAritmetica? ) : this(expresionIzquierda,expresionDerecha,operador,null) {
        this.expresionIzquierda = expresionIzquierda
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( expresionIzquierda:ExpresionAritmetica?): this(expresionIzquierda,null,null,null){
        this.expresionIzquierda = expresionIzquierda
    }

    constructor( valorNumerico: ValorNumerico?, operador:Token?, expresionDerecha: ExpresionAritmetica? ): this(null, expresionDerecha, operador, valorNumerico){
        this.valorNumerico = valorNumerico
        this.operador = operador
        this.expresionDerecha = expresionDerecha
    }

    constructor( valorNumerico: ValorNumerico? ): this(null,null,null,valorNumerico){
        this.valorNumerico = valorNumerico
    }

    override fun toString(): String {
        return "ExpresionAritmetica(expresionIzquierda=$expresionIzquierda, expresionDerecha=$expresionDerecha, operador=$operador, valorNumerico=$valorNumerico)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Aritmética")
        if (valorNumerico != null) {
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
        if (valorNumerico != null) {
            if (expresionDerecha != null) {
                return "${valorNumerico!!.numero.lexema} ${operador!!.lexema} ${expresionDerecha!!.valorNumerico!!.numero.lexema}"
            }
            return "${valorNumerico!!.numero.lexema}"
        }
        else{
            return ""
        }
    }
}