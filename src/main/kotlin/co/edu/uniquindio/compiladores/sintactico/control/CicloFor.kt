package co.edu.uniquindio.compiladores.sintactico.control

import co.edu.uniquindio.compiladores.sintactico.datos.ValorNumerico
import co.edu.uniquindio.compiladores.sintactico.datos.Variable
import co.edu.uniquindio.compiladores.sintactico.sentencia.Sentencia

class CicloFor( var indice: Variable, var limite: ValorNumerico, var bloqueSentencias: ArrayList<Sentencia> ) : Ciclo() {

    override fun toString(): String {
        return "CicloFor(indice=$indice, limite=$limite, bloqueSentencias=$bloqueSentencias)"
    }
}