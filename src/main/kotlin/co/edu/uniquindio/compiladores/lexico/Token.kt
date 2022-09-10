package co.edu.uniquindio.compiladores.lexico

class Token (private var lexema:String, private var categoria: Categoria, private var fila:Int, private var columna:Int){
    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna)"
    }
}