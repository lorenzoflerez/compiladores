package co.edu.uniquindio.compiladores.lexico

class KeyWords {
    private var palabras = listOf("for", "while", "do", "switch", "case", "break", "continue", "default", "boolean", "int",
        "float", "string", "char", "if", "else", "in", "new", "true", "false", "try", "catch", "return", "include", "class",
        "function", "as")

    private val reservadas = LinkedHashMap<String,String>()

    @JvmName("getPalabrasReservadas")
    fun getPalabras(): List<String> {
        return palabras
    }

    fun leerLexema(lexema:String): String? {
        return reservadas[lexema]
    }

    fun categorizar(){
        reservadas["boolean"] = "tipoDato"
        reservadas["int"] = "tipoDato"
        reservadas["float"] = "tipoDato"
        reservadas["string"] = "tipoDato"
        reservadas["char"] = "tipoDato"
    }
}