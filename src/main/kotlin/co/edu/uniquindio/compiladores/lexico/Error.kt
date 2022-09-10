package co.edu.uniquindio.compiladores.lexico

class Error(private var error:String, private var fila:Int, private var columna:Int) {
    override fun toString(): String {
        return "Error(error='$error', fila=$fila, columna=$columna)"
    }
}