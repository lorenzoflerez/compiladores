package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Asingnacion(var identificadorAsingnacion: Token, var operador:Token) : Sentencia(){

    var expresion:Expresion? = null
    var invocacion:Invocacion? = null

    constructor(identificadorAsingnacion: Token,  operador:Token, expresion: Expresion):this(identificadorAsingnacion, operador){
        this.expresion = expresion
    }

    constructor(identificadorAsingnacion: Token,  operador:Token, invocacion: Invocacion):this(identificadorAsingnacion, operador){
        this.invocacion = invocacion
    }

    override fun toString(): String {
        return "Asingnacion(identificadorAsingnacion=$identificadorAsingnacion, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Asignación")
        raiz.children.add( TreeItem("Variable : ${identificadorAsingnacion.lexema}" ))
        raiz.children.add( TreeItem("Operador : ${operador.lexema}" ))
        if(expresion!=null){
            raiz.children.add( expresion!!.getArbolVisual() )
        }
        if(invocacion!=null){
            raiz.children.add( invocacion!!.getArbolVisual() )
        }
        return raiz
    }

    private fun obtenerTiposArgumentos(tablaSimbolos: TablaSimbolos, ambito: String): ArrayList<String> {
        val tipoArgumentos = ArrayList<String>()

        for (argumento in invocacion!!.argumentos) {
            val tipoArg = argumento.obtenerTipo(tablaSimbolos, ambito)
            tipoArgumentos.add(tipoArg)
        }
        return tipoArgumentos
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        val simbolo = tablaSimbolos.buscarSimboloVariable(identificadorAsingnacion.lexema, ambito)

        if(simbolo==null){
            erroresSemanticos.add(Error("El campo ${identificadorAsingnacion.lexema} no existe dentro del ámbito $ambito",identificadorAsingnacion.fila,identificadorAsingnacion.columna))
        }
        else{
            val tipo = simbolo.tipo
            if(expresion!=null){
                expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
                val tipoExpresion = expresion!!.obtenerTipo(tablaSimbolos, ambito)
                if( tipoExpresion != tipo){
                    erroresSemanticos.add(Error("El tipo de dato del argumento (${tipoExpresion}) no coincide con el del campo (${identificadorAsingnacion.lexema}:$tipo)", identificadorAsingnacion.fila, identificadorAsingnacion.columna))
                }
            }
            else if(invocacion!=null){
                val argumentos = obtenerTiposArgumentos(tablaSimbolos, ambito)
                invocacion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
                val simboloInvocacion = tablaSimbolos.buscarSimboloFuncion(invocacion!!.identificadorFuncion.lexema,argumentos)
                val simboloVariable = tablaSimbolos.buscarSimboloVariable(identificadorAsingnacion.lexema,ambito)
                if(simboloInvocacion==null){
                    erroresSemanticos.add(Error("El campo ${invocacion!!.identificadorFuncion.lexema} no existe dentro del ámbito $ambito",identificadorAsingnacion.fila,identificadorAsingnacion.columna))
                }
                else if(simboloVariable==null){
                    erroresSemanticos.add(Error("El campo ${identificadorAsingnacion.lexema} no existe dentro del ámbito $ambito",identificadorAsingnacion.fila,identificadorAsingnacion.columna))
                }
                else if(simboloInvocacion != simboloVariable){
                    erroresSemanticos.add(Error("El tipo de dato de la función (${simboloInvocacion.tipo}) no coincide con el de la variable (${identificadorAsingnacion.lexema}:$tipo)", identificadorAsingnacion.fila, identificadorAsingnacion.columna))
                }
            }
        }
    }
}