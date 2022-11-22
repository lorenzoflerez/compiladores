package co.edu.uniquindio.compiladores.lexico

class Token ( var lexema:String,  var categoria: Categoria,  var fila:Int,  var columna:Int){
    override fun toString(): String {
        return "Token(lexema='$lexema', categoria=$categoria, fila=$fila, columna=$columna)"
    }

    fun getJavaCode():String{
        if(categoria == Categoria.PALABRA_RESERVADA){
            when (lexema) {
                "float" -> {  return "double" }
                "string" -> { return "String" }
                "const" -> {  return "final" }
                "print" -> {  return "println" }
                "read" -> {   return "scanf" }
            }
        }
        else if(categoria == Categoria.OPERADOR_RELACIONAL){
            if (lexema == "¬=") {
                return "!="
            }
        }
        else if(categoria == Categoria.OPERADOR_LOGICO){
            when (lexema) {
                "&" -> { return "&&" }
                "|" -> { return "||" }
                "¬" -> { return "!" }
            }
        }
        else if(categoria == Categoria.OPERADOR_ASIGNACION){
            when (lexema) {
                "=:" -> { return "="}
                "=+" -> { return "+="}
                "=-" -> { return "-="}
                "=/" -> { return "/="}
                "=*" -> { return "*="}
                "=%" -> { return "%="}
            }
        }
        return ""
    }

}