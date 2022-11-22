package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.Simbolo
import co.edu.uniquindio.compiladores.semantico.TablaSimbolos
import javafx.scene.control.TreeItem

class Lectura(var identificadorLectura: Token) : Sentencia() {

    private var tipoSimbolo:Simbolo? = null

    override fun toString(): String {
        return "Lectura(identificadorLectura=$identificadorLectura)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Lectura")
        raiz.children.add( TreeItem("variable : ${identificadorLectura.lexema}" ))
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<Error>, ambito: String) {
        val simbolo = tablaSimbolos.buscarSimboloVariable(identificadorLectura.lexema, ambito)
        if(simbolo==null){
            erroresSemanticos.add(Error("El campo ${identificadorLectura!!.lexema} no existe dentro del ámbito $ambito",identificadorLectura!!.fila,identificadorLectura!!.columna))
        }
    }

    override fun getJavaCode(): String {
        return when (tipoSimbolo!!.tipo) {
            "int" ->     identificadorLectura.getJavaCode() + " = Integer.parseInt(JOptionPane.showInputDialog(null, \"Escribir:\") ); "
            "float" ->   identificadorLectura.getJavaCode() + " = Double.parseDouble(JOptionPane.showInputDialog(null, \"Escribir:\") ); "
            "boolean" -> identificadorLectura.getJavaCode() + " = Boolean.parseBoolean(JOptionPane.showInputDialog(null, \"Escribir:\") ); "
            else ->      identificadorLectura.getJavaCode() + " = JOptionPane.showInputDialog(null, \"Escribir:\") ); "
        }
    }

}