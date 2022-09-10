package co.edu.uniquindio.compiladores.lexico

/**
 *
 */
class AnalizadorLexico(private var codigoFuente:String) {

    private var listaTokens = ArrayList<Token>()
    var listaErrores = ArrayList<Error>()

    private var posicionActual = 0
    private var caracterActual = codigoFuente[0]
    private var finCodigo = 0.toChar()
    private var filaActual = 0
    private var columnaActual = 0

    fun analizar(){
        while (caracterActual != finCodigo){
            //Ignorar Espacios en blanco, tabulaciones y saltos de línea.
            if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
                obtenerSiguienteCaracter()
                continue
            }
            //Bloque de validacion Tokens.
            if (esEntero()) continue

            almacenarToken(""+caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    private fun obtenerSiguienteCaracter(){
        if( posicionActual == codigoFuente.length-1 ){
            caracterActual = finCodigo
        }else{
            if( caracterActual == '\n' ){
                filaActual++
                columnaActual = 0
            }else{
                columnaActual++
            }
            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }

    private fun esEntero():Boolean{
        if( caracterActual.isDigit() ){
            //Inicialización de variables necesarias para almacenar información
            var lexema = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            //Transición Inicial
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            //Bucle
            while( caracterActual.isDigit() ){
                //Transición
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            //Bactracking BT
            if( caracterActual == '.' ){
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            //Aceptación y Almacenamiento AA
            almacenarToken(lexema, Categoria.ENTERO, filaInicial, columnaInicial)
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun almacenarToken(lexema:String, categoria: Categoria, filaInicial:Int, columnaInicial:Int) =
        listaTokens.add(Token(lexema,categoria,filaInicial,columnaInicial))

    private fun backTracking(posicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }
}