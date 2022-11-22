package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.sintactico.datos.ValorTexto
import javafx.scene.control.TreeItem

class ExpresionCadena( var cadenas: ArrayList<ValorTexto>): Expresion() {

    override fun toString(): String {
        return "ExpresionCadena(cadenas=$cadenas)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Cadena")
        val text =  getTexto()
        raiz.children.add(TreeItem(text))
        return raiz
    }

    override fun getTexto(): String {
        var texto: String=""

        for(valor in cadenas){
            texto += valor.valor.lexema
            texto += "+"
        }
        val text = texto.substring(0,texto.length - 1)

        return text;
    }
}