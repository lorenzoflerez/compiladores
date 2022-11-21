package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.estructura.Argumento
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia
import javafx.scene.control.TreeItem

class Arreglo(var identificador:Token, var tipoDato: Token, var listaArgumentos:ArrayList<Argumento> ):Declaracion(){

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Arreglo")


        raiz.children.add(TreeItem("${identificador.lexema} : ${tipoDato.lexema}"))
        if(listaArgumentos.isNotEmpty()){
            val raizArgumento = TreeItem("Argumentos")
            for (argumento in listaArgumentos){
                raizArgumento.children.add(argumento.getArbolVisual())
            }
            raiz.children.add(raizArgumento)
        }


        return raiz
    }

    override fun toString(): String {
        return "Arreglo(identificador=$identificador, tipoDato=$tipoDato, listaExpresiones=$listaArgumentos)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String>, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema,tipoDato.lexema,ambito,identificador.fila,identificador.columna)
    }
}