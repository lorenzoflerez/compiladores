package co.edu.uniquindio.compiladores.sintactico.datos

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import co.edu.uniquindio.compiladores.sintactico.estructura.Argumento
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

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema, tipoDato.lexema,true,ambito,identificador.fila,identificador.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        for(arg in listaArgumentos){
            arg.expresion.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            val tipo = arg.expresion.obtenerTipo(tablaSimbolos, ambito)
            if(tipo.isEmpty()){
                erroresSemanticos.add(Error("El campo de dato del argumento (${tipo}) no coincide con el del arreglo", identificador.fila, identificador.columna))
            }
            else if(tipo != tipoDato.lexema){
                erroresSemanticos.add(Error("El tipo de dato del argumento (${tipo}) no coincide con el del arreglo", identificador.fila, identificador.columna))
            }
        }
    }
}