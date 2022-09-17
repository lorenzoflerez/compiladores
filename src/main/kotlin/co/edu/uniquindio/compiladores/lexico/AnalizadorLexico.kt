package co.edu.uniquindio.compiladores.lexico

/**
 *
 */
class AnalizadorLexico(private var codigoFuente:String) {

    var listaTokens = ArrayList<Token>()
    private var listaErrores = ArrayList<Error>()

    private var lexema = ""
    private var posicionActual = 0
    private var caracterActual = codigoFuente[0]
    private var finCodigo = 0.toChar()
    private var filaActual = 0
    private var columnaActual = 0
    private var posicionInicial = 0
    private var filaInicial = 0
    private var columnaInicial = 0

    fun analizar(){
        while ( caracterActual != finCodigo ){
            //Ignorar Espacios en blanco, tabulaciones y saltos de línea.
            if ( caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n' ) {
                obtenerSiguienteCaracter()
                continue
            }
            //Bloque de validacion Tokens.
            if (esEntero()) continue
            if (esDecimal()) continue
            if (esOperadorLogico()) continue
            if (esOperadorRelacional()) continue
            if (esOperadorAritmetico()) continue
            if (esOperadorAsignacion()) continue
            if (esOperadorIncremental()) continue
            if (esOperadorDecremental()) continue
            if (esComentarioDeLinea()) continue
            if (esComentarioDeBloque()) continue
            if (esCaracter()) continue
            if (esCadenaCaracteres()) continue
            if (esCaracterEspecial()) continue
            if (esFinCodigo()) continue

            almacenarToken("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual)
            obtenerSiguienteCaracter()
        }
    }

    private fun obtenerSiguienteCaracter(){
        if ( posicionActual == codigoFuente.length-1 ){
            caracterActual = finCodigo
        }else{
            if ( caracterActual == '\n' ){
                filaActual++
                columnaActual = 0
            }else{
                columnaActual++
            }
            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }

    private fun almacenarToken( lexema:String, categoria: Categoria, filaInicial:Int, columnaInicial:Int ) =
        listaTokens.add(Token(lexema,categoria,filaInicial,columnaInicial))

    private fun backTracking( posicionInicial: Int, filaInicial: Int, columnaInicial: Int ) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }

    private fun reportarError(error: String, fila: Int, columna: Int) =
        listaErrores.add(Error(error, fila, columna))

    private fun actualizarVariables(){
        lexema = ""
        filaInicial = filaActual
        columnaInicial = columnaActual
        posicionInicial = posicionActual
    }

    private fun esEntero():Boolean{
        if ( caracterActual.isDigit() ){
            //Inicialización de variables necesarias para almacenar información
            actualizarVariables()
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
            if ( caracterActual == '.' ){
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            //Aceptación y Almacenamiento AA
            almacenarToken( lexema, Categoria.ENTERO, filaInicial, columnaInicial )
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esDecimal():Boolean{
        if ( caracterActual.isDigit() ){
            //Inicialización de variables necesarias para almacenar información
            actualizarVariables()
            //Transición Inicial
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            //Bucle
            while( caracterActual.isDigit() ){
                //Transición
                lexema+=caracterActual
                obtenerSiguienteCaracter()
            }
            if ( caracterActual == '.' ){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                if(caracterActual.isDigit()){
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                    while( caracterActual.isDigit() ){
                        //Transición
                        lexema+=caracterActual
                        obtenerSiguienteCaracter()
                    }
                }
                else{
                    //Bactracking BT
                    backTracking(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
            }
            //Aceptación y Almacenamiento AA
            almacenarToken( lexema, Categoria.DECIMAL, filaInicial, columnaInicial )
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorLogico():Boolean{
        if ( caracterActual == '&' || caracterActual == '|' ){
            actualizarVariables()
            //Transición Inicial
            lexema+=caracterActual
            //Aceptación y Almacenamiento AA
            almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        if ( caracterActual == '¬' ){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            //Bactracking BT
            if ( caracterActual == '=' ){
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            //Aceptación y Almacenamiento AA
            almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorRelacional():Boolean{
        if( caracterActual == '<' || caracterActual == '>'){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if ( caracterActual == '=' ){
                lexema += caracterActual
            }
            else {
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
            }
            //Aceptación y Almacenamiento AA
            almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        if ( caracterActual == '¬' || caracterActual == '='){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if ( caracterActual == '=' ){
                //Aceptación y Almacenamiento AA
                lexema += caracterActual
                almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                true
            } else{
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                false
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorAritmetico():Boolean{
        if( caracterActual == '*' || caracterActual == '/' || caracterActual == '%' ){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            //Aceptación y Almacenamiento AA
            almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
            return true
        }
        else{
            if( caracterActual == '+' || caracterActual == '-'){
                return if(subRutinaOperador() == 1){
                    //Bactracking BT
                    backTracking(posicionInicial, filaInicial, columnaInicial)
                    false
                } else{
                    //Aceptación y Almacenamiento AA
                    almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
                    true
                }
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorIncremental():Boolean{
        if( caracterActual == '+'){
            return if(subRutinaOperador() == 0){
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                false
            } else{
                lexema+=caracterActual
                //Aceptación y Almacenamiento AA
                almacenarToken(lexema, Categoria.OPERADOR_INCREMENTO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorDecremental():Boolean{
        if( caracterActual == '-'){
            return if(subRutinaOperador() == 0){
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                false
            } else{
                lexema+=caracterActual
                //Aceptación y Almacenamiento AA
                almacenarToken(lexema, Categoria.OPERADOR_DECREMENTO, filaInicial, columnaInicial)
                true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorAsignacion():Boolean{
        if( caracterActual == '='){
            actualizarVariables()
            //Transición Inicial
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            if(caracterActual == ':' || caracterActual == '+' || caracterActual == '-' || caracterActual == '*' || caracterActual == '/' || caracterActual == '%'){
                lexema+=caracterActual
                //Aceptación y Almacenamiento AA
                almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            //Bactracking BT
            backTracking(posicionInicial, filaInicial, columnaInicial)
            return false
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esComentarioDeLinea():Boolean{
        if( caracterActual == '@'){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if( caracterActual == '@'){
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            else{
                //Bucle
                while( caracterActual != '\n' && caracterActual != finCodigo ){
                    //Transición
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
                //Aceptación y Almacenamiento AA
                almacenarToken( lexema, Categoria.COMENTARIO_LINEA, filaInicial, columnaInicial )
                return true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esComentarioDeBloque():Boolean{
        if( caracterActual == '@'){
            actualizarVariables()
            //Transición Inicial
            lexema+=caracterActual
            obtenerSiguienteCaracter()
            if( caracterActual == '@'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                if( caracterActual == '@'){
                    reportarError("Caracter no válido después del @", filaActual, columnaActual)
                    obtenerSiguienteCaracter()
                    return false
                }
                else if(caracterActual == finCodigo){
                    almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE_SIN_CERRAR, filaInicial, columnaInicial)
                    return true
                }
                else{
                    while ( caracterActual != finCodigo){
                        lexema+=caracterActual
                        obtenerSiguienteCaracter()
                        //Bucle
                        while( caracterActual != '@' && caracterActual != finCodigo){
                            //Transición
                            lexema+=caracterActual
                            obtenerSiguienteCaracter()
                        }
                        lexema+=caracterActual
                        obtenerSiguienteCaracter()
                        if( caracterActual == '@'){
                            lexema+=caracterActual
                            //Aceptación y Almacenamiento AA
                            almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial)
                            obtenerSiguienteCaracter()
                            return true
                        }
                        else if(caracterActual == finCodigo){
                            val auxiliar = lexema.substring(0 ,lexema.length-1)
                            almacenarToken(auxiliar, Categoria.COMENTARIO_BLOQUE_SIN_CERRAR, filaInicial, columnaInicial)
                            return true
                        }
                        else{
                            lexema += caracterActual
                            almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE_SIN_CERRAR, filaInicial, columnaInicial)
                            return true
                        }
                    }
                }
            }
            else{
                //Bactracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esCaracter():Boolean{
        if( caracterActual == '\''){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            if( caracterActual == '\''){
                //Reporte de Error RE
                reportarError("Caracter no válido, no se cerro con \'", filaActual, columnaActual)
                obtenerSiguienteCaracter()
                return false
            }
            val flag = subRutinaEscape()
            if( flag == 0 ){
                return if ( caracterActual == '\''){
                    lexema += caracterActual
                    //Aceptación y Almacenamiento AA
                    almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    true
                } else{
                    almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial)
                    //Reporte de Error RE
                    reportarError("Caracter no válido, no se cerro con \'", filaActual, columnaActual)
                    true
                }
            }
            if(flag == 1){
                reportarError("Caracter no válido después del \'", filaActual, columnaActual)
                return false
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esCadenaCaracteres():Boolean{
        if( caracterActual == '"'){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual != '"' && caracterActual != finCodigo){
                val flag = subRutinaEscape()
                if(flag == 1){
                    reportarError("Caracter no válido después del \'", filaActual, columnaActual)
                    return false
                }
            }
            if (caracterActual == finCodigo){
                return false
            }
            //Aceptación y Almacenamiento AA
            lexema += caracterActual
            almacenarToken(lexema, Categoria.CADENA_CARACTERES, filaInicial, columnaInicial)
            obtenerSiguienteCaracter()
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esCaracterEspecial():Boolean{
        when(caracterActual){
            '(' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.PARENTESIS_IZQUIERDO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            ')' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.PARENTESIS_DERECHO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            '[' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.CORCHETE_IZQUIERDO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            ']' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.CORCHETE_DERECHO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            '{' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.LLAVE_IZQUIERDA, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            '}' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.LLAVE_DERECHA, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            '.' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.PUNTO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            ',' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.SEPARADOR, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
            ';' -> { actualizarVariables()
                lexema += caracterActual
                almacenarToken(lexema, Categoria.FIN_SENTENCIA, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esFinCodigo(): Boolean{
        if (caracterActual == finCodigo)
            return true
        return false
    }

    private fun subRutinaEscape():Int{
        while(true){
            if(caracterActual=='\\'){
                lexema += caracterActual
                obtenerSiguienteCaracter()
                //Validación caracteres de escape.
                if( caracterActual == 'b' || caracterActual == 'n' || caracterActual == 't' || caracterActual == 'r'){
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                }
                else{
                    return if( caracterActual == '\\' || caracterActual == '\'' || caracterActual == '"'){
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        0
                    } else{
                        //Reporte de Error RE
                        1
                    }
                }
            }
            //Cualquier simbolo fuera del bucle de Escape
            else {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                return 0
            }
        }
    }

    private fun subRutinaOperador():Int{
        actualizarVariables()
        //Transición Inicial
        lexema += caracterActual
        val operador = caracterActual
        obtenerSiguienteCaracter()
        return if( operador == caracterActual){
            1
        } else{
            0
        }
    }



}