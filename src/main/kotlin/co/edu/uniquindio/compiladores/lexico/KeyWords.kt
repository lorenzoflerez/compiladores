package co.edu.uniquindio.compiladores.lexico

class KeyWords {
    private var palabras = listOf("for", "while", "do", "switch", "case", "break", "continue", "default", "boolean", "int",
        "float", "string", "if", "else", "in", "new", "true", "false", "try", "catch", "return", "include", "class",
        "function", "as")

    private val reservadas = LinkedHashMap<String,String>()

    @JvmName("getPalabrasReservadas")
    fun getPalabras(): List<String> {
        return palabras
    }

    fun categorizar(){
        for (palabra in palabras){
            reservadas[palabra] = "tipo"
        }
    }
}