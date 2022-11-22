package co.edu.uniquindio.compiladores.sintactico

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.KeyWords
import co.edu.uniquindio.compiladores.sintactico.control.*
import co.edu.uniquindio.compiladores.sintactico.datos.*
import co.edu.uniquindio.compiladores.sintactico.estructura.*
import co.edu.uniquindio.compiladores.sintactico.expresion.*
import co.edu.uniquindio.compiladores.sintactico.sentencia.*

class AnalizadorSintactico (var listaTokens:ArrayList<Token>){
    private var keys = KeyWords()
    var posicionActual = 0
    var posicionCheck = posicionActual
    var tokenActual = listaTokens[posicionActual]
    var tokenCheck = tokenActual
    var listaErrores = ArrayList<Error>()


    fun obtenerSiguienteToken(){
        posicionActual++
        if( posicionActual < listaTokens.size ) {
            tokenActual = listaTokens[posicionActual]
        }
    }

    fun checkpoint(){
        posicionCheck = posicionActual
        tokenCheck = tokenActual
    }

    fun backTracking(){
        posicionActual = posicionCheck
        tokenActual = tokenCheck
    }

    fun reportarError(mensaje:String){
        listaErrores.add(Error(mensaje,tokenActual.fila,tokenActual.columna))
    }

    /**
     * <UnidadDeCompilacion> ::= class identificador begin [<BloqueImports>] <BloqueFunciones> end
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        if( leerPalabraReservada("class") ){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                val identificadorClase = tokenActual
                obtenerSiguienteToken()
                if ( leerPalabraReservada("begin") ) {
                    obtenerSiguienteToken()
                    val imports: ArrayList<Import>? = esBloqueImports()
                    val bloqueFunciones: ArrayList<Funcion> ? = esBloqueFunciones()
                    if (bloqueFunciones != null) {
                        obtenerSiguienteToken()
                        if( leerPalabraReservada("end") ){
                            return UnidadDeCompilacion(identificadorClase,imports,bloqueFunciones)
                        } else{
                            reportarError("La clase no termina con end")
                        }
                    }
                    else{
                        reportarError("Falta bloque de funciones ")
                    }
                }
                else{
                    reportarError("La clase no empieza con begin")
                }
            }
            else{
                reportarError("Falta identificador de clase")
            }
        }
        return null
    }

    /**
     * <BloqueImports> ::= include llaveIzquierda <ListaImports> llaveDerecha
     */
    fun esBloqueImports(): ArrayList<Import>?{
        if(leerPalabraReservada("include")){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                obtenerSiguienteToken()
                val listaImports = esListaImports()
                if( listaImports.size > 0 ){
                    if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                        obtenerSiguienteToken()
                        return listaImports
                    }
                    else{
                        reportarError("Falta llave derecha")
                    }
                }
                else{
                    reportarError("Lista de librerías vacía")
                }
            }
            else{
                reportarError("Falta llave izquierda")
            }
        }
        return null
    }

    /**
     * <ListaImports> ::= <Import> [, <ListaImports>]
     */
    fun esListaImports(): ArrayList<Import> {
        val listaImports = ArrayList<Import>()
        var import= esImport()
        while (import!=null){
            listaImports.add(import)
            if (tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                import = esImport()
            }
            else{
                break
            }
        }
        return listaImports
    }

    /**
     * <Import> ::= identificador
     */
    fun esImport(): Import?{
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            val identificadorLibreria = tokenActual
            obtenerSiguienteToken()
            return Import(identificadorLibreria)
        }
        return null
    }

    /**
     * <BloqueFunciones> ::= llaveIzquierda <ListaFunciones> llaveDerecha
     */
    fun esBloqueFunciones(): ArrayList<Funcion>?{
        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
            obtenerSiguienteToken()
            val listaFunciones = esListaFunciones()
            if( listaFunciones.size > 0 ) {
                if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                    obtenerSiguienteToken()
                    return listaFunciones
                } else {
                    reportarError("Falta llave derecha")
                }
            }
            else{
                reportarError("Lista de funciones vacía")
            }
        }
        return null
    }

    /**
     * <ListaFunciones> ::= <Funcion> [<ListaFunciones>]
     */
    fun esListaFunciones(): ArrayList<Funcion> {
        val listaFunciones = ArrayList<Funcion>()
        var funcion= esFuncion()
        while (funcion!=null){
            listaFunciones.add(funcion)
            obtenerSiguienteToken()
            funcion = esFuncion()
        }
        return listaFunciones
    }

    /**
     * <Funcion> ::= function identificador ( [<ListaParametros>] ) [typeOf <Tipodato>] <BloqueSentencias>
     */
    fun esFuncion(): Funcion?{
        if(leerPalabraReservada("function")){
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR){
                val identificadorFuncion = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()
                    val parametros: ArrayList<Parametro> = esListaParametros()
                    if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                        obtenerSiguienteToken()
                        var tipoDato: Token? = null
                        if (leerPalabraReservada("typeOf")){
                            obtenerSiguienteToken()
                            tipoDato = tokenActual
                            obtenerSiguienteToken()
                        }
                        val bloqueSentencias: ArrayList<Sentencia> ? = esBloqueSentencias()
                        if (bloqueSentencias != null) {
                            return Funcion(identificadorFuncion,parametros,tipoDato,bloqueSentencias)
                        }
                        else{
                            reportarError("Falta bloque de sentencias")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis derecho")
                    }
                }
                else{
                    reportarError("Falta paréntesis izquierdo")
                }
            }
            else{
                reportarError("Falta identificador de función")
            }
        }
        return null
    }

    /**
     * <ListaParametros> ::= <Parametro> [, <ListaParametros>]
     */
    fun esListaParametros(): ArrayList<Parametro>{
        val listaParametros = ArrayList<Parametro>()
        var parametro= esParametro()
        while (parametro!=null){
            listaParametros.add(parametro)
            if (tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                parametro = esParametro()
            }
            else{
                break
            }
        }
        return listaParametros
    }

    /**
     * <Parametro> ::= <Tipodato> identificador
     */
    fun esParametro(): Parametro?{
        val tipoDato = esTipoDato()
        if( tipoDato != null ){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                val parametro = tokenActual
                obtenerSiguienteToken()
                return Parametro(tipoDato, parametro)
            }
            else{
                reportarError("Falta identificador del parámetro")
            }
        }
        return null
    }

    /**
     * <BloqueSentencias> ::= llaveIzquierda [<ListaSentencias>] llaveDerecha
     */
    fun esBloqueSentencias(): ArrayList<Sentencia>?{
        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
            obtenerSiguienteToken()
            val listaSentencias = esListaSentencias()
            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                return listaSentencias
            } else {
                reportarError("Falta llave derecha")
            }
        }
        return null
    }

    /**
     * <ListaSentencias> ::= <Sentencia> [<ListaSentencias>]
     */
    fun esListaSentencias(): ArrayList<Sentencia>{
        val listaSentencias= ArrayList<Sentencia>()
        var sentencia= esSentencia()
        while (sentencia!=null){
            listaSentencias.add(sentencia)
            obtenerSiguienteToken()
            sentencia = esSentencia()
        }
        return listaSentencias
    }

    /**
     * <Sentencia> ::= <DeclaracionVariable> | <DeclaracionArreglo> | <Asignacion> | <Lectura> | <Impresion> | <InvocacionFuncion> | <Condicional> | <Ciclo> | <Seleccion> | <Retorno> | <Control> | <Incremento> | <Decremento>
     */
    fun esSentencia(): Sentencia?{
        var sentencia: Sentencia? = esDeclaracionVariable()
        if(sentencia!=null)
            return sentencia
        sentencia = esDeclaracionArreglo()
        if(sentencia!=null)
            return sentencia
        sentencia = esAsignacion()
        if(sentencia!=null)
            return sentencia
        sentencia = esLectura()
        if(sentencia!=null)
            return sentencia
        sentencia = esImpresion()
        if(sentencia!=null)
            return sentencia
        sentencia = esInvocacionFuncion()
        if(sentencia!=null)
            return sentencia
        sentencia = esCondicional()
        if(sentencia!=null)
            return sentencia
        sentencia = esCiclo()
        if(sentencia!=null)
            return sentencia
        sentencia = esSeleccion()
        if(sentencia!=null)
            return sentencia
        sentencia = esRetorno()
        if(sentencia!=null)
            return sentencia
        sentencia = esControl()
        if(sentencia!=null)
            return sentencia
        sentencia = esIncremento()
        if(sentencia!=null)
            return sentencia
        sentencia = esDecremento()
        if(sentencia!=null)
            return sentencia
        return null
    }

    /**
     * <DeclaracionVariable> ::= <Variable> | <Constante>
     */
    fun esDeclaracionVariable(): Declaracion? {
        var declaracion: Declaracion? = esVariable()
        if(declaracion!=null)
            return declaracion
        declaracion = esConstante()
        if(declaracion!=null)
            return declaracion
        declaracion = esDeclaracionArreglo()
        if(declaracion!=null)
            return declaracion
        return null
    }

    /**
     * <Variable> ::= var <Tipodato> identificador [operadorAsignacion <Expresion>];
     */
    fun esVariable(): Variable?{
        if( leerPalabraReservada("var")){
            obtenerSiguienteToken()
            val tipoDato = esTipoDato()
            if( tipoDato != null ){
                obtenerSiguienteToken()
                if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                    val identificadorVariable = tokenActual
                    var expresion:Expresion? = null
                    obtenerSiguienteToken()
                    if( tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                        obtenerSiguienteToken()
                        expresion = esExpresion()
                        if(expresion==null){
                            reportarError("Falta expresión de asignación")
                        }
                    }
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                        return Variable(tipoDato, identificadorVariable, expresion)
                    }
                    else{
                        reportarError("Falta fin de sentencia")
                    }
                }
                else{
                    reportarError("Falta identificador de la variable")
                }
            }
            else{
                reportarError("Tipo de dato incorrecto")
            }
        }
        return null
    }

    /**
     * <Constante> ::= const <Tipodato> identificador operadorAsignacion <Expresion>;
     */
    fun esConstante(): Constante?{
        if( leerPalabraReservada("const")){
            obtenerSiguienteToken()
            val tipoDato = esTipoDato()
            if( tipoDato != null ){
                obtenerSiguienteToken()
                if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                    val identificadorConstante = tokenActual
                    obtenerSiguienteToken()
                    if( tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                        obtenerSiguienteToken()
                        val expresion:Expresion? = esExpresion()
                        if(expresion!=null){
                            if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                                return Constante(tipoDato, identificadorConstante, expresion)
                            }
                            else{
                                reportarError("Falta fin de sentencia")
                            }
                        }
                        else{
                            reportarError("Falta expresión de asignación")
                        }
                    }
                    else{
                        reportarError("Falta operador de asignación de constante")
                    }
                }
                else{
                    reportarError("Falta identificador de la variable")
                }
            }
            else{
                reportarError("Tipo de dato incorrecto")
            }
        }
        return null
    }

    /**
     *<DeclaracionArreglo> ::= array <Tipodato> identificador =: corcheteIzquierdo <ListaArgumentos> corcheteDerecho
     */
    fun esDeclaracionArreglo(): Arreglo? {
        if( leerPalabraReservada("array")){
            obtenerSiguienteToken()
            val tipoDato = esTipoDato()
            if( tipoDato != null ){
                obtenerSiguienteToken()
                if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                    val identificadorArreglo = tokenActual
                    var expresion:Expresion? = null
                    obtenerSiguienteToken()
                    if( tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
                            obtenerSiguienteToken()
                            val listaArgumentos = esListaArgumentos()
                            if(listaArgumentos.isNotEmpty()){
                                if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                                    return Arreglo(identificadorArreglo,tipoDato,listaArgumentos)
                                } else {
                                    reportarError("Falta llave derecha")
                                }
                            }
                            else{
                                reportarError("Falta lista de argumentos")
                            }
                        }
                    }
                }
                else{
                    reportarError("Falta identificador del arreglo")
                }
            }
            else{
                reportarError("Tipo de dato incorrecto")
            }
        }
        return null
    }

    /**
     * <InvocacionFuncion> ::= new identificador ([<ListaArgumentos>]);
     */
    fun esInvocacionFuncion(): Invocacion? {
        if(leerPalabraReservada("new")){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ) {
                val identificadorFuncion = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()
                    val argumentos: ArrayList<Argumento> = esListaArgumentos()
                    if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            return Invocacion(identificadorFuncion, argumentos)
                        }
                        else{
                            reportarError("Falta fin de sentencia")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis derecho")
                    }
                }
                else{
                    reportarError("Falta paréntesis izquierdo")
                }
            }
            else{
                reportarError("No se puede llamar función sin identificador")
            }
        }
        return null
    }

    /**
     * <ListaArgumentos> ::= <Argumento> [ , <ListaArgumentos> ]
     */
    fun esListaArgumentos(): ArrayList<Argumento> {
        val listaArgumentos = ArrayList<Argumento>()
        var argumento= esArgumento()
        while (argumento!=null){
            listaArgumentos.add(argumento)
            if (tokenActual.categoria == Categoria.SEPARADOR){
                obtenerSiguienteToken()
                argumento = esArgumento()
            }
            else{
                break
            }
        }
        return listaArgumentos
    }

    /**
     * <Argumento> ::= <Expresion>
     */
    private fun esArgumento(): Argumento? {
        val expresion = esExpresion()
        if (expresion != null) {
            return Argumento(expresion)
        }
        return null
    }

    /**
     * <Asignacion> ::= identificador operadorAsignacion <Expresion> | identificador operadorAsignacion <InvocacionFuncion>
     */
    fun esAsignacion(): Asingnacion? {
        if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
            val identificadorAsingnacion = tokenActual

            obtenerSiguienteToken()

            if(tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                val operador = tokenActual
                obtenerSiguienteToken()
                checkpoint()
                val expresion:Expresion? = esExpresion()
                if(expresion!=null){
                    return Asingnacion(identificadorAsingnacion,operador, expresion)
                }
                else{
                    backTracking()
                    val invocacion:Invocacion? = esInvocacionFuncion()
                    if(invocacion!=null){
                        return Asingnacion(identificadorAsingnacion,operador, invocacion)
                    }
                    reportarError("Falta expresión de asignación")
                }
            }
            else{
                reportarError("Falta operador de asignación")
            }
        }
        return null
    }

    /**
     * <Lectura> ::= read identificador;
     */
    fun esLectura(): Lectura? {
        if(leerPalabraReservada("read")){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                val identificadorLectura = tokenActual
                obtenerSiguienteToken()
                if(tokenActual.categoria == Categoria.FIN_SENTENCIA){
                    return Lectura(identificadorLectura)
                }
                else{
                    reportarError("Falta fin de sentencia")
                }
            }
            else{
                reportarError("Falta identificador de lectura")
            }
        }
        return null
    }

    /**
     * <Impresion> ::= print <Expresion>;
     */
    fun esImpresion(): Impresion? {
        if(leerPalabraReservada("print")){
            obtenerSiguienteToken()
            /*if ( tokenActual.categoria == Categoria.DOS_PUNTOS ){
                obtenerSiguienteToken()
                */val expresion: Expresion? = esExpresion()
            if( expresion != null ){
                if(tokenActual.categoria == Categoria.FIN_SENTENCIA){
                    return Impresion(expresion)
                }
                else{
                    reportarError("Falta fin de sentencia")
                }
            }
            else{
                reportarError("Falta expresión")
            }
            /*}
            else{
                reportarError("Falta operador :")
            }*/
        }
        return null
    }

    /**
     * <Condicional> ::= if (<ExpresionLogica>) do <BloqueSentencias> [else <BloqueSentencias>]
     */
    fun esCondicional(): Condicional? {
        if(leerPalabraReservada("if")){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                obtenerSiguienteToken()
                val expresion = esExpresionLogica()
                if (expresion!=null){
                    if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                        obtenerSiguienteToken()
                        if(leerPalabraReservada("do")){
                            obtenerSiguienteToken()
                            val bloqueCondicional = esBloqueSentencias()
                            obtenerSiguienteToken()
                            if (bloqueCondicional!=null){
                                var bloqueAlternativo : ArrayList<Sentencia>? = null
                                if( leerPalabraReservada("else")) {
                                    obtenerSiguienteToken()
                                    bloqueAlternativo = esBloqueSentencias()
                                    if (bloqueAlternativo != null) {
                                        return Condicional(expresion,bloqueCondicional,bloqueAlternativo)
                                    }
                                    else{
                                        reportarError("Falta bloque de sentencias else")
                                    }
                                }
                                return Condicional(expresion,bloqueCondicional,bloqueAlternativo)
                            }
                            else{
                                reportarError("Falta bloque de sentencias if")
                            }
                        }
                        else{
                            reportarError("Falta operador do")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis derecho")
                    }
                }
                else{
                    reportarError("Expresión lógica if no valida")
                }
            }
            else{
                reportarError("Falta paréntesis izquierdo")
            }
        }
        return null
    }

    /**
     * <Ciclo> ::= <CicloFor> | <CicloWhile> | <CicloDoWhile>
     */
    fun esCiclo(): Ciclo?{
        var ciclo: Ciclo? = esCicloFor()
        if(ciclo!=null)
            return ciclo
        ciclo = esCicloWhile()
        if(ciclo!=null)
            return ciclo
        ciclo = esCicloDoWhile()
        if(ciclo!=null)
            return ciclo
        return null
    }

    /**
     * <Seleccion> ::= switch (<Expresion>) do <BloqueOpciones>
     */
    fun esSeleccion(): Seleccion? {
        if(leerPalabraReservada("switch")){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                obtenerSiguienteToken()
                val expresion = esExpresion()
                if (expresion != null) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                        obtenerSiguienteToken()
                        if( leerPalabraReservada("do")){
                            val bloqueOpciones = esBloqueOpciones()
                            if ( bloqueOpciones != null ){
                                obtenerSiguienteToken()
                                return Seleccion(expresion,bloqueOpciones)
                            }
                        }
                        else{
                            reportarError("Falta operador do")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis derecho")
                    }
                }
                else{
                    reportarError("Falta Expresión")
                }
            }
            else{
                reportarError("Falta paréntesis izquierdo")
            }
        }
        return null
    }

    /**
     * <BloqueOpciones> ::= llaveIzquierda <ListaOpciones> llaveDerecha
     */
    fun esBloqueOpciones(): ArrayList<Opcion>?{
        if (tokenActual.categoria == Categoria.LLAVE_IZQUIERDA) {
            obtenerSiguienteToken()
            val listaOpciones = esListaOpciones()
            if (tokenActual.categoria == Categoria.LLAVE_DERECHA) {
                obtenerSiguienteToken()
                return listaOpciones
            } else {
                reportarError("Falta llave derecha")
            }
        }
        return null
    }

    /**
     * <ListaOpciones> ::= <Opcion> [<ListaOpciones>]
     */
    fun esListaOpciones(): ArrayList<Opcion>{
        val listaOpciones = ArrayList<Opcion>()
        var opcion= esOpcion()
        while (opcion!=null){
            listaOpciones.add(opcion)
            obtenerSiguienteToken()
            opcion = esOpcion()
        }
        return listaOpciones
    }

    /**
     * <Opcion> ::= case <Valor> : <BloqueSentencias> break;
     */
    fun esOpcion(): Opcion? {
        if( leerPalabraReservada("case")){
            obtenerSiguienteToken()
            val valor = esValor()
            if( valor != null ){
                obtenerSiguienteToken()
                if (tokenActual.categoria==Categoria.DOS_PUNTOS){
                    obtenerSiguienteToken()
                    val bloqueSentencias = esBloqueSentencias()
                    if (bloqueSentencias != null){
                        obtenerSiguienteToken()
                        if (leerPalabraReservada("break")){
                            obtenerSiguienteToken()
                            if( tokenActual.categoria == Categoria.FIN_SENTENCIA ){
                                return Opcion(valor, bloqueSentencias)
                            }
                            else{
                                reportarError("Falta fin de sentencia")
                            }
                        }
                        else{
                            reportarError("Falta operador break")
                        }
                    }
                    else{
                        reportarError("Falta bloque de sentencias")
                    }
                }
                else{
                    reportarError("Falta operador dos puntos")
                }
            }
            else{
                reportarError("Falta valor de selección")
            }
        }
        return null
    }

    /**
     * <Retorno> ::= return <Expresion>;
     */
    fun esRetorno(): Retorno? {
        if( leerPalabraReservada("return")){
            obtenerSiguienteToken()
            val expresion: Expresion? = esExpresion()
            if( expresion != null ){
                if(tokenActual.categoria == Categoria.FIN_SENTENCIA){
                    return Retorno(expresion)
                }
                else{
                    reportarError("Falta fin de sentencia")
                }
            }
            else{
                reportarError("Falta expresión")
            }
        }

        return null
    }

    /**
     * <Control> ::= try <BloqueSentencias> catch (<Tipodato> identificador) continue
     */
    fun esControl(): Control? {
        if(leerPalabraReservada("try")){
            obtenerSiguienteToken()
            val bloqueSentencias: ArrayList<Sentencia> ? = esBloqueSentencias()
            if (bloqueSentencias != null) {
                obtenerSiguienteToken()
                if(leerPalabraReservada("catch")){
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                        val tipoDato = esTipoDato()
                        if(tipoDato!=null){
                            obtenerSiguienteToken()
                            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ) {
                                val identificadorExcepcion = tokenActual
                                obtenerSiguienteToken()
                                if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                                    obtenerSiguienteToken()
                                    if(leerPalabraReservada("continue")){
                                        return Control(bloqueSentencias,tipoDato,identificadorExcepcion)
                                    }
                                    else{
                                        reportarError("Falta operador continue")
                                    }
                                }
                                else{
                                    reportarError("Falta paréntesis derecho")
                                }
                            }
                            else{
                                reportarError("Falta identificador de excepción")
                            }
                        }
                        else{
                            reportarError("Tipo de dato invalido")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis izquierdo")
                    }
                }
                else{
                    reportarError("Falta operador catch")
                }
            }
            else{
                reportarError("Falta bloque de sentencias")
            }
        }
        return null
    }

    /**
     * <Incremento> ::= operadorIncremento identificador
     */
    fun esIncremento(): Incremento? {
        if( tokenActual.categoria == Categoria.OPERADOR_INCREMENTO){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                val identificadorVariable = tokenActual
                return Incremento(identificadorVariable)
            }
            else{
                reportarError("Falta identificador de incremento")
            }
        }
        return null
    }

    /**
     * <Decremento> ::= operadorDecremento identificador
     */
    fun esDecremento(): Decremento? {
        if( tokenActual.categoria == Categoria.OPERADOR_DECREMENTO){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
                val identificadorVariable = tokenActual
                return Decremento(identificadorVariable)
            }
            else{
                reportarError("Falta identificador de decremento")
            }
        }
        return null
    }

    /**
     * <CicloFor> ::= for (<Variable> in range <Valor>) do <BloqueSentencias>
     */
    fun esCicloFor(): Ciclo? {
        if(leerPalabraReservada("for")){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                obtenerSiguienteToken()
                val variable = esVariable()
                if (variable!=null){
                    obtenerSiguienteToken()
                    if(leerPalabraReservada("in")){
                        obtenerSiguienteToken()
                        if(leerPalabraReservada("range")){
                            obtenerSiguienteToken()

                            val valor = esValorNumerico()
                            if(valor!=null){
                                obtenerSiguienteToken()
                                if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                                    obtenerSiguienteToken()
                                    if(leerPalabraReservada("do")){
                                        obtenerSiguienteToken()
                                        val bloqueSentencias = esBloqueSentencias()
                                        if (bloqueSentencias!=null){
                                            return CicloFor(variable, valor, bloqueSentencias)
                                        }
                                        else{
                                            reportarError("Falta bloque de sentencias")
                                        }
                                    }
                                    else{
                                        reportarError("Falta operador do")
                                    }
                                }
                                else{
                                    reportarError("Falta paréntesis derecho")
                                }
                            }
                        }
                    }
                }
                else{
                    reportarError("Expresión lógica for no valida")
                }
            }
            else{
                reportarError("Falta paréntesis izquierdo")
            }
        }
        return null
    }

    /**
     * <CicloWhile> ::= while(<ExpresionLogica>) do <BloqueSentencias>
     */
    fun esCicloWhile(): CicloWhile?{
        if(leerPalabraReservada("while")){
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO){
                obtenerSiguienteToken()
                val expresion = esExpresionLogica()
                if (expresion!=null){
                    if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                        obtenerSiguienteToken()
                        if(leerPalabraReservada("do")){
                            obtenerSiguienteToken()
                            val bloqueSentencias = esBloqueSentencias()
                            if (bloqueSentencias!=null){
                                return CicloWhile(expresion,bloqueSentencias)
                            }
                            else{
                                reportarError("Falta bloque de sentencias")
                            }
                        }
                        else{
                            reportarError("Falta operador do")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis derecho")
                    }
                }
                else{
                    reportarError("Expresión lógica while no valida")
                }
            }
            else{
                reportarError("Falta paréntesis izquierdo")
            }
        }
        return null
    }

    /**
     * <CicloDoWhile> ::= do <BloqueSentencias> while(<ExpresionLogica>)
     */
    fun esCicloDoWhile(): CicloDoWhile? {
        if(leerPalabraReservada("do")){
            obtenerSiguienteToken()
            val bloqueSentencias = esBloqueSentencias()
            if (bloqueSentencias!=null){
                obtenerSiguienteToken()
                if(leerPalabraReservada("while")) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                        val expresion = esExpresionLogica()
                        if (expresion != null) {
                            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                return CicloDoWhile(bloqueSentencias, expresion)
                            }
                            else{
                                reportarError("Falta paréntesis derecho")
                            }
                        }
                        else{
                            reportarError("Expresión lógica do while no valida")
                        }
                    }
                    else{
                        reportarError("Falta paréntesis izquierdo")
                    }
                }
                else{
                    reportarError("Falta operador while")
                }
            }
            else{
                reportarError("Falta bloque de sentencias")
            }
        }
        return null
    }

    /**
     * <Expresion> ::= <ExpresionCadena> | <ExpresionLogica> | <ExpresionRelacional> | <ExpresionAritmetica> | <InvocacionFuncion>
     */
    fun esExpresion(): Expresion?{
        if(tokenActual.categoria == Categoria.IDENTIFICADOR) {
            val valor = ValorNumerico(null, tokenActual)
            obtenerSiguienteToken()
            return ExpresionAritmetica(valor)
        }
        var expresion: Expresion? = esExpresionLogica()
        if(expresion!=null)
            return expresion
        expresion = esExpresionRelacional()
        if(expresion!=null)
            return expresion
        expresion = esExpresionAritmetica()
        if(expresion!=null)
            return expresion
        expresion = esExpresionCadena()
        if(expresion!=null)
            return expresion
        return null
    }

    /**
     * <ExpresionCadena> ::= cadenaCaracteres [+ <ExpresionCadena>] | <ValorTexto>
     */
    fun esExpresionCadena(): ExpresionCadena? {
        val expresion = ArrayList<ValorTexto>()
        var valor: ValorTexto?
        if( tokenActual.categoria == Categoria.CADENA_CARACTERES){
            expresion.add(ValorTexto(tokenActual))
            obtenerSiguienteToken()
            while (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual.lexema == "+"){
                obtenerSiguienteToken()
                valor = esValorTexto()
                if (valor != null){
                    expresion.add(valor)
                    obtenerSiguienteToken()
                }
            }
            return ExpresionCadena(expresion)
        }
        else{
            if( tokenActual.categoria == Categoria.CARACTER){
                expresion.add(ValorTexto(tokenActual))
                obtenerSiguienteToken()
                return ExpresionCadena(expresion)
            }
        }
        return null
    }

    /**
     * <ExpresionLogica> ::= [ operadorNegacion ] [(] <ExpresionLogica> [)] | <ExpresionLogica> operadorAND <ExpresionLogica> | <ExpresionLogica> operadorOR <ExpresionLogica> | <ValorLogico>
     */
    fun esExpresionLogica(): ExpresionLogica? {
        if( tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO ){
            obtenerSiguienteToken()
            val expresionIzquierda = esExpresionLogica()
            if( expresionIzquierda != null){
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO ){
                    obtenerSiguienteToken()
                    if( tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                        val operador = tokenActual
                        obtenerSiguienteToken()
                        val expresionDerecha = esExpresionLogica()
                        if( expresionDerecha != null){
                            return ExpresionLogica(expresionIzquierda, operador, expresionDerecha)
                        }
                    }
                    else{
                        return ExpresionLogica(expresionIzquierda)
                    }
                }
            }
        }
        else{
            val valorLogico = esValorLogico()
            if( valorLogico != null){
                obtenerSiguienteToken()
                if( tokenActual.categoria == Categoria.OPERADOR_LOGICO ){
                    val operador = tokenActual
                    obtenerSiguienteToken()
                    val expresion = esExpresionLogica()
                    if( expresion != null){
                        return ExpresionLogica(valorLogico, operador, expresion)
                    }
                }
                else{
                    return ExpresionLogica(valorLogico)
                }
            }
        }
        return null
    }

    /**
     * <ExpresionRelacional> ::= <ExpresionAritmetica> operadorRelacional <ExpresionAritmetica> | <ExpresionCadena> operadorRelacional <ExpresionCadena>
     */
    fun esExpresionRelacional(): ExpresionRelacional? {
        checkpoint()
        var expresionIzquierda: Expresion?
        val expresionDerecha: Expresion?
        expresionIzquierda = esExpresionAritmetica()
        if (expresionIzquierda!= null){
            if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                val operador = tokenActual
                obtenerSiguienteToken()
                expresionDerecha = esExpresionAritmetica()
                if(expresionDerecha != null){
                    return ExpresionRelacional(expresionIzquierda, operador, expresionDerecha)
                }
                else{
                    backTracking()
                }
            }
            else{
                backTracking()
            }
        }
        else{
            expresionIzquierda = esExpresionCadena()
            if (expresionIzquierda!= null){
                if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL){
                    val operador = tokenActual
                    obtenerSiguienteToken()
                    expresionDerecha = esExpresionCadena()
                    if(expresionDerecha != null){
                        return ExpresionRelacional(expresionIzquierda, operador, expresionDerecha)
                    }
                    else{
                        backTracking()
                    }
                }
                else{
                    backTracking()
                }
            }
        }
        return null
    }

    /**
     * <ExpresionAritmetica> ::= <ExpresionAritmetica> operardorAritmetico <ExpresionAritmetica> | (<ExpresionAritmetica>) | <ValorNumerico>
     */
    fun esExpresionAritmetica(): ExpresionAritmetica?{
        if( tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO ){
            obtenerSiguienteToken()
            val expresionIzquierda = esExpresionAritmetica()
            if( expresionIzquierda != null){
                if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO ){
                    obtenerSiguienteToken()
                    if( tokenActual.categoria == Categoria.OPERADOR_ARITMETICO ){
                        val operador = tokenActual
                        obtenerSiguienteToken()
                        val expresionDerecha = esExpresionAritmetica()
                        if( expresionDerecha != null){
                            return ExpresionAritmetica(expresionIzquierda, operador, expresionDerecha)
                        }
                    }
                    else{
                        return ExpresionAritmetica(expresionIzquierda)
                    }
                }
            }
        }
        else{
            val valorNumerico = esValorNumerico()
            if( valorNumerico != null){
                obtenerSiguienteToken()
                if( tokenActual.categoria == Categoria.OPERADOR_ARITMETICO ){
                    val operador = tokenActual
                    obtenerSiguienteToken()
                    val expresionDerecha = esExpresionAritmetica()
                    if( expresionDerecha != null){
                        return ExpresionAritmetica(valorNumerico, operador, expresionDerecha)
                    }
                }
                else{
                    return ExpresionAritmetica(valorNumerico)
                }
            }
        }
        return null
    }

    /**
     * <Valor> ::= <ValorNumerico> | caracter | cadenaCaracteres | identificador
     */
    fun esValor(): Valor? {
        var valor: Valor? = esValorNumerico()
        if(valor!=null)
            return valor
        valor = esValorLogico()
        if(valor!=null)
            return valor
        valor = esValorTexto()
        if(valor!=null)
            return valor
        return null
    }

    /**
     * <ValorNumerico> ::= [<Signo>] decimal | [<Signo>] entero | [<Signo>] identificador
     */
    fun esValorNumerico(): ValorNumerico? {
        var signo: Token? = null
        if(tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual.lexema == "+" || tokenActual.lexema == "-")){
            signo = tokenActual
        }
        if(tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.DECIMAL || tokenActual.categoria == Categoria.IDENTIFICADOR){
            val termino = tokenActual
            return ValorNumerico(signo,termino)
        }
        return null
    }

    /**
     * <ValorLogico> ::=  true | false | <ExpresionRelacional>
     */
    fun esValorLogico(): ValorLogico? {
        val expresion = esExpresionRelacional()
        if(expresion!=null){
            return ValorLogico(expresion)
        }
        if(leerPalabraReservada("true")||leerPalabraReservada("false")){
            val valor: Token?  = tokenActual
            return ValorLogico(valor)
        }
        return null
    }

    /**
     * <ValorTexto> ::= caracter | cadenaCaracteres
     */
    fun esValorTexto(): ValorTexto? {
        if(tokenActual.categoria == Categoria.CARACTER || tokenActual.categoria == Categoria.CADENA_CARACTERES || tokenActual.categoria == Categoria.IDENTIFICADOR){
            val valor = tokenActual
            return ValorTexto(valor)
        }
        return null
    }

    /**
     * <Tipodato> ::= boolean | int |float | string | char | identificador
     */
    fun esTipoDato(): Token?{
        if( tokenActual.categoria == Categoria.PALABRA_RESERVADA ){
            if (tokenActual.lexema=="boolean" || tokenActual.lexema=="int" || tokenActual.lexema=="float" || tokenActual.lexema=="string" || tokenActual.lexema=="char")
                return tokenActual
        }
        return null
    }

    fun leerPalabraReservada(lexema: String):Boolean{
        return (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == lexema)
    }

    private fun verificar(expresion:Expresion?){
        println(expresion)
        println(tokenActual)
    }
}