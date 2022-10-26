package co.edu.uniquindio.compiladores.sintactico.sentencia

import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import javafx.scene.control.TreeItem

class Asingnacion(var identificadorAsingnacion: Token, var expresion: Expresion) : Sentencia(){

    override fun toString(): String {
        return "Asingnacion(identificadorAsingnacion=$identificadorAsingnacion, expresion=$expresion)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Asignación")
        raiz.children.add( TreeItem("variable : ${identificadorAsingnacion.lexema}" ))
        raiz.children.add( expresion.getArbolVisual() )
        return raiz
    }
}