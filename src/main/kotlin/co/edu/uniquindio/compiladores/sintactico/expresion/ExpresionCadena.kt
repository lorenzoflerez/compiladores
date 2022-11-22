package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
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

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String):String {
        return "string"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        for(valor in cadenas){
            if(valor.valor.categoria==Categoria.IDENTIFICADOR){
                /*val simbolo = tablaSimbolos.buscarSimboloVariable(valor.valor.lexema,ambito)
                if(simbolo!=null){
                    if(simbol)
                }
                else{
                    erroresSemanticos.add(Error("El campo ${valor.valor.lexema} no existe dentro del ámbito $ambito",valor.valor.fila, valor.valor.columna))
                }*/
            }
        }
    }
}