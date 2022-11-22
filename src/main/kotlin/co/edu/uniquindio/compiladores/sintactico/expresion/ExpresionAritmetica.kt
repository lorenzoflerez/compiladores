package co.edu.uniquindio.compiladores.sintactico.expresion

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
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
            if (expresionDerecha != null) {
                raiz.children.add(expresionDerecha!!.getArbolVisual())
            }
            else{
                raiz.children.add(expresionIzquierda!!.getArbolVisual())
            }
        }
        return raiz
    }

    override fun getTexto(): String {
        if (valorNumerico != null) {
            if (expresionDerecha != null) {
                return "${valorNumerico!!.numero.lexema} ${operador!!.lexema} ${expresionDerecha!!.valorNumerico!!.numero.lexema}"
            }
            return valorNumerico!!.numero.lexema
        }
        else{
            return ""
        }
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String): String {
        if(expresionIzquierda!=null && expresionDerecha!=null){
            val tipoIzquierda = expresionIzquierda!!.obtenerTipo(tablaSimbolos,ambito)
            val tipoDerecha = expresionDerecha!!.obtenerTipo(tablaSimbolos,ambito)
            return if(tipoIzquierda=="float"||tipoDerecha=="float")
                "float"
            else{
                "int"
            }
        }

        if (valorNumerico != null) {
            if (expresionDerecha != null) {

            }
            else{
                if(valorNumerico!!.numero.categoria==Categoria.ENTERO){
                    return "int"
                }
                else if(valorNumerico!!.numero.categoria==Categoria.DECIMAL){
                    return "float"
                }
                else{
                    val simbolo = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
                    if(simbolo!=null){
                        return simbolo.tipo
                    }
                    else{
                        //erroresSemanticos.add(Error("El campo ${valorNumerico!!.numero.lexema} no existe dentro del ámbito $ambito",valorNumerico!!.numero.fila, valorNumerico!!.numero.columna))
                    }
                }
            }
        }
        else{
            if (expresionDerecha != null) {
                val tipoIzquierda = expresionIzquierda!!.obtenerTipo(tablaSimbolos,ambito)
                val tipoDerecha = expresionDerecha!!.obtenerTipo(tablaSimbolos,ambito)
                return if(tipoIzquierda=="float"||tipoDerecha=="float")
                    "float"
                else{
                    "int"
                }

            }
            else{

            }
        }
        return ""
    }

    private fun obtenerCampo(valorNumerico: ValorNumerico?, ambito: String, tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>):String{
        if(valorNumerico!!.numero.categoria==Categoria.ENTERO){
            return "int"
        }
        else if(valorNumerico!!.numero.categoria==Categoria.DECIMAL){
            return "float"
        }
        else{
            val simbolo = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
            if(simbolo!=null){
                return simbolo.tipo
            }
            else{
                erroresSemanticos.add(Error("El campo ${valorNumerico!!.numero.lexema} no existe dentro del ámbito $ambito",valorNumerico!!.numero.fila, valorNumerico!!.numero.columna))
            }
        }
        return ""
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        if(valorNumerico!=null){
            if(valorNumerico!!.numero.categoria == Categoria.IDENTIFICADOR){
                val simbolo  = tablaSimbolos.buscarSimboloVariable(valorNumerico!!.numero.lexema,ambito)
                //if(simbolo.tipo == )

                if(simbolo == null){
                    erroresSemanticos.add(Error("El campo ${valorNumerico!!.numero.lexema} no existe dentro del ámbito $ambito",valorNumerico!!.numero.fila,valorNumerico!!.numero.columna))
                }
            }
        }
        if(expresionIzquierda!=null){
            expresionIzquierda!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if(expresionDerecha!=null){
            expresionDerecha!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }
}