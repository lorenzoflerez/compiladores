package co.edu.uniquindio.compiladores.lexico

/**
 *
 */
class AnalizadorLexico(private var codigoFuente:String) {

    var listaTokens = ArrayList<Token>()
    private var listaErrores = ArrayList<Error>()
    private var keys = KeyWords()
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
            if (esNumero()) continue
            if (esOperadorNumerico()) continue
            if (esAsignacionOBooleano()) continue
            if (esComentario()) continue
            if (esCaracter()) continue
            if (esCadenaCaracteres()) continue
            if (esCaracterEspecial()) continue
            if (esIdentificador()) continue

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

    private fun esNumero():Boolean{
        if ( caracterActual.isDigit() ) {
            //Inicialización de variables necesarias para almacenar información
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            //Bucle
            while (caracterActual.isDigit()) {
                //Transición
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }

            val lexemabt = lexema
            val posicionbt = posicionActual
            val filaBt = filaActual
            val columnaBt = columnaActual

            if ( caracterActual == '.' ){
                lexema += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()) {
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    while (caracterActual.isDigit()) {
                        //Transición
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                    //Aceptación y Almacenamiento AA Decimal (DD.DD)
                    almacenarToken( lexema, Categoria.DECIMAL, filaInicial, columnaInicial )
                    return true
                }
                else{
                    //Backtracking BT 2
                    lexema = lexemabt
                    posicionActual = posicionbt
                    filaActual = filaBt
                    columnaActual = columnaBt
                    //Aceptación y Almacenamiento AA Entero
                    almacenarToken( lexema, Categoria.ENTERO, filaInicial, columnaInicial )
                    almacenarToken( ".", Categoria.PUNTO, filaActual, columnaActual )
                    obtenerSiguienteCaracter()
                    return false
                }
            }
            else{
                //Backtracking BT 1
                //Aceptación y Almacenamiento AA Entero
                almacenarToken( lexema, Categoria.ENTERO, filaInicial, columnaInicial )
                return true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esIdentificador():Boolean{
        if(caracterActual.isLetter()){
            actualizarVariables()
            subrutinaConcatenar()

            //esPalabraReservada()
            return if(keys.getPalabras().contains(lexema)){
                almacenarToken(lexema, Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial)
                true
            } else{
                almacenarToken(lexema, Categoria.IDENTIFICADOR, filaInicial, columnaInicial)
                true
            }
        }
        if(caracterActual=='_' ||  caracterActual=='$'){
            actualizarVariables()
            lexema += caracterActual
            obtenerSiguienteCaracter()
            return if (caracterActual.isLetter()){
                subrutinaConcatenar()
                almacenarToken(lexema,Categoria.IDENTIFICADOR,filaInicial,columnaInicial)
                true
            } else{
                //Backtracking BT
                backTracking(posicionInicial, filaInicial, columnaInicial)
                false
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esAsignacionOBooleano():Boolean{
        val operador = "<>=¬|&"
        if(operador.contains(caracterActual)){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual

            when(caracterActual){
                '&','|' -> {
                    obtenerSiguienteCaracter()
                    //Aceptación y Almacenamiento AA Operador Lógico & | '|'
                    almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
                    return true
                }
                '<','>' ->{
                    obtenerSiguienteCaracter()
                    if (caracterActual == '='){
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                    }
                    //Aceptación y Almacenamiento AA Operador relacional < | <= | > | >=
                    almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                    return true
                }
                '¬' -> {
                    obtenerSiguienteCaracter()
                    if (caracterActual == '='){
                        lexema += caracterActual
                        obtenerSiguienteCaracter()
                        //Aceptación y Almacenamiento AA Operador relacional ¬=
                        almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                        return true
                    }
                    //Aceptación y Almacenamiento AA Operador Lógico ¬
                    almacenarToken(lexema, Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial)
                    return true
                }
                '=' -> {
                    obtenerSiguienteCaracter()

                    when (caracterActual) {
                        ':', '+', '-', '*', '/', '%' -> {
                            lexema += caracterActual
                            //Aceptación y Almacenamiento AA Operador Asignación =: | =+ | =- | =* | =/ | =%
                            almacenarToken(lexema, Categoria.OPERADOR_ASIGNACION, filaInicial, columnaInicial)
                            obtenerSiguienteCaracter()
                            return true
                        }
                        '=' -> {
                            lexema += caracterActual
                            //Aceptación y Almacenamiento AA Operador Relacional ==
                            almacenarToken(lexema, Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial)
                            obtenerSiguienteCaracter()
                            return true
                        }
                        else -> {
                            //BackTracking BT '='
                            backTracking(posicionInicial, filaInicial, columnaInicial)
                            return false
                        }
                    }
                }
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esOperadorNumerico():Boolean{
        val operadorNumerico = "+-*/%"
        // + | - | * | / | %
        if (operadorNumerico.contains(caracterActual)){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            if(caracterActual == '+' || caracterActual == '-') {
                val operador = caracterActual
                obtenerSiguienteCaracter()
                // + | -
                if(operador != caracterActual)
                    //Aceptación y Almacenamiento AA operador aritmético
                    almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
                else{
                    lexema += caracterActual
                    obtenerSiguienteCaracter()
                    // --
                    if(caracterActual=='-')
                        //Aceptación y Almacenamiento AA operador decremental
                        almacenarToken(lexema, Categoria.OPERADOR_DECREMENTO, filaInicial, columnaInicial)
                    // ++
                    else
                        //Aceptación y Almacenamiento AA operador incremental
                        almacenarToken(lexema, Categoria.OPERADOR_INCREMENTO, filaInicial, columnaInicial)
                }
                return true
            }
            else{
                //Aceptación y Almacenamiento AA operador aritmético * | / | %
                almacenarToken(lexema, Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial)
                obtenerSiguienteCaracter()
                return true
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esComentario():Boolean{
        if( caracterActual == '@'){
            actualizarVariables()
            //Transición Inicial
            lexema += caracterActual
            obtenerSiguienteCaracter()
            //Transición comentario de bloque - else Backtracking BT comentario de linea.
            if( caracterActual == '@'){
                lexema+=caracterActual
                obtenerSiguienteCaracter()
                //Bucle Comentario de Bloque.
                while(true){
                    if(caracterActual == '@'){
                        lexema+=caracterActual
                        obtenerSiguienteCaracter()
                        //Validación cierre comentario de Bloque
                        if(caracterActual == '@'){
                            lexema+=caracterActual
                            //Aceptación y Almacenamiento AA Comentario de Bloque @@(cos)@@
                            almacenarToken(lexema, Categoria.COMENTARIO_BLOQUE, filaInicial, columnaInicial)
                            obtenerSiguienteCaracter()
                            return true
                        }
                    }
                    else if(caracterActual == finCodigo){
                        val auxiliar = lexema.substring(0 ,lexema.length-1)
                        //Aceptación y Almacenamiento AA Comentario de linea @@(cos)
                        almacenarToken(auxiliar, Categoria.COMENTARIO_BLOQUE_SIN_CERRAR, filaInicial, columnaInicial)
                        return true
                    }
                    //Transición
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
            }
            else{
                //Bucle Comentario de linea.
                while( caracterActual != '\n' && caracterActual != finCodigo ){
                    //Transición
                    lexema+=caracterActual
                    obtenerSiguienteCaracter()
                }
                //Aceptación y Almacenamiento AA Comentario de linea @(cos)\n
                almacenarToken( lexema, Categoria.COMENTARIO_LINEA, filaInicial, columnaInicial )
                return true
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
                //Reporte de Error RE '', carácter vacío
                reportarError("Carácter vacío", filaActual, columnaActual)
                backTracking(posicionInicial, filaInicial, columnaInicial)
                return false
            }
            if( !subRutinaEscape() ){
                return if ( caracterActual == '\''){
                    lexema += caracterActual
                    //Aceptación y Almacenamiento AA Carácter 'c.s'
                    almacenarToken(lexema, Categoria.CARACTER, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    true
                } else{
                    //Reporte de Error RE carácter sin cerrar 'c.s
                    reportarError("Carácter no válido, no se cerro con \'", filaActual, columnaActual)
                    backTracking(posicionInicial, filaInicial, columnaInicial)
                    false
                }
            }
            else {
                //Reporte de error carácter de escape no valido.
                reportarError("Caracter no válido después del \'", filaActual, columnaActual)
                backTracking(posicionInicial, filaInicial, columnaInicial)
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
            while (true){
                if(caracterActual=='"') {
                    //Aceptación y Almacenamiento AA CAdena de caracteres
                    lexema += caracterActual
                    almacenarToken(lexema, Categoria.CADENA_CARACTERES, filaInicial, columnaInicial)
                    obtenerSiguienteCaracter()
                    return true
                }
                if( subRutinaEscape() ){
                    //Reporte de error carácter de escape no valido.
                    reportarError("Carácter no válido después del \'", filaActual, columnaActual)
                    backTracking(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
                if (caracterActual == finCodigo){
                    reportarError("Cadena de Caracteres Sin cerrar", filaActual, columnaActual)
                    //Backtracking BT
                    backTracking(posicionInicial, filaInicial, columnaInicial)
                    return false
                }
            }
        }
        //Rechazo inmediato RI
        return false
    }

    private fun esCaracterEspecial():Boolean{
        val caracteresEspeciales = "()[]{}.,;"
        if (caracteresEspeciales.contains(caracterActual)){
            actualizarVariables()
            lexema += caracterActual
            when(caracterActual){
                '(' -> almacenarToken(lexema, Categoria.PARENTESIS_IZQUIERDO, filaInicial, columnaInicial)
                ')' -> almacenarToken(lexema, Categoria.PARENTESIS_DERECHO, filaInicial, columnaInicial)
                '[' -> almacenarToken(lexema, Categoria.CORCHETE_IZQUIERDO, filaInicial, columnaInicial)
                ']' -> almacenarToken(lexema, Categoria.CORCHETE_DERECHO, filaInicial, columnaInicial)
                '{' -> almacenarToken(lexema, Categoria.LLAVE_IZQUIERDA, filaInicial, columnaInicial)
                '}' -> almacenarToken(lexema, Categoria.LLAVE_DERECHA, filaInicial, columnaInicial)
                '.' -> almacenarToken(lexema, Categoria.PUNTO, filaInicial, columnaInicial)
                ',' -> almacenarToken(lexema, Categoria.SEPARADOR, filaInicial, columnaInicial)
                ';' -> almacenarToken(lexema, Categoria.FIN_SENTENCIA, filaInicial, columnaInicial)
                ':' -> almacenarToken(lexema, Categoria.DOS_PUNTOS, filaInicial, columnaInicial)
            }
            obtenerSiguienteCaracter()
            return true
        }
        //Rechazo inmediato RI
        return false
    }

    private fun subRutinaEscape():Boolean{
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
                        false
                    } else{
                        //Reporte de Error RE
                        true
                    }
                }
            }
            //Cualquier símbolo fuera del bucle de Escape
            else {
                lexema += caracterActual
                obtenerSiguienteCaracter()
                return false
            }
        }
    }

    private fun subrutinaConcatenar(){
        while( lexema.length < 10){
            if (caracterActual=='_' ||  caracterActual.isLetter() || caracterActual.isDigit()){
                lexema += caracterActual
                obtenerSiguienteCaracter()
            }
            else return
        }
    }
}