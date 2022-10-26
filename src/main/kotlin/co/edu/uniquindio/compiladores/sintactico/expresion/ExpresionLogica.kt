package co.edu.uniquindio.compiladores.sintactico.expresion

import javafx.scene.control.TreeItem

class ExpresionLogica():Expresion() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz =  TreeItem("Expresión Lógica")
        return raiz
    }


}