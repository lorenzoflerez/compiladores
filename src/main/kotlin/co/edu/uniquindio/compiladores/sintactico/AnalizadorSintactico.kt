package co.edu.uniquindio.compiladores.sintactico

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.KeyWords
import co.edu.uniquindio.compiladores.sintactico.control.*
import co.edu.uniquindio.compiladores.sintactico.datos.Constante
import co.edu.uniquindio.compiladores.sintactico.datos.Declaracion
import co.edu.uniquindio.compiladores.sintactico.datos.Valor
import co.edu.uniquindio.compiladores.sintactico.datos.Variable
import co.edu.uniquindio.compiladores.sintactico.estructura.Funcion
import co.edu.uniquindio.compiladores.sintactico.estructura.Import
import co.edu.uniquindio.compiladores.sintactico.estructura.Parametro
import co.edu.uniquindio.compiladores.sintactico.estructura.UnidadDeCompilacion
import co.edu.uniquindio.compiladores.sintactico.expresion.Expresion
import co.edu.uniquindio.compiladores.sintactico.expresion.ExpresionLogica
import co.edu.uniquindio.compiladores.sintactico.sentencia.*

class AnalizadorSintactico (var listaTokens:ArrayList<Token>){
    private var keys = KeyWords()
    var posicionActual = 0
    var tokenActual = listaTokens[posicionActual]
    var listaErrores = ArrayList<Error>()

    fun obtenerSiguienteToken(){
        posicionActual++;
        if( posicionActual < listaTokens.size ) {
            tokenActual = listaTokens[posicionActual]
        }
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
                reportarError("Falta separador de librerías")
                break
            }
        }
        return listaImports
    }

    /**
     * <Import> ::= identificador;
     */
    fun esImport(): Import?{
        if (tokenActual.categoria == Categoria.IDENTIFICADOR){
            val identificadorLibreria = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                obtenerSiguienteToken()
                return Import(identificadorLibreria)
            }
            else{
                reportarError("Falta fin de sentencia")
            }
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
        val listaFunciones= ArrayList<Funcion>()
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
                        if (leerPalabraReservada("typeOf")){
                            val tipoDato = tokenActual
                            obtenerSiguienteToken()
                            val bloqueSentencias: ArrayList<Sentencia> ? = esBloqueSentencias()
                            if (bloqueSentencias != null) {
                                return Funcion(identificadorFuncion,parametros,tipoDato,bloqueSentencias)
                            }
                            else{
                                reportarError("Falta bloque de sentencias")
                            }
                        }
                        else{
                            reportarError("Falta operador typeOf")
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
                reportarError("Falta separador de parámetros")
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
                obtenerSiguienteToken()
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
                        if(expresion!=null){
                            obtenerSiguienteToken()
                        }
                        else{
                            reportarError("Falta expresión de asignación")
                        }
                    }
                    if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                        obtenerSiguienteToken()
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
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                                obtenerSiguienteToken()
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
                        reportarError("Falta operador de asignación")
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
     *<DeclaracionArreglo> ::= array <Tipodato> identificador =: corcheteIzquierdo <ListaExpresiones> corcheteDerecho;
     */
    fun esDeclaracionArreglo(): Sentencia? {
        TODO("Not yet implemented")
    }

    /**
     * <Asignacion> ::= identificador operadorAsignacion <Expresion>
     */
    fun esAsignacion(): Asingnacion? {
        if ( tokenActual.categoria == Categoria.IDENTIFICADOR ){
            val identificadorAsingnacion = tokenActual
            obtenerSiguienteToken()
            if(tokenActual.categoria == Categoria.OPERADOR_ASIGNACION){
                obtenerSiguienteToken()
                val expresion:Expresion? = esExpresion()
                if(expresion!=null){
                    obtenerSiguienteToken()
                    return Asingnacion(identificadorAsingnacion, expresion)
                }
                else{
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
                    obtenerSiguienteToken()
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
     * <Impresion> ::= print : <Expresion>;
     */
    fun esImpresion(): Impresion? {
        if(leerPalabraReservada("print")){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.DOS_PUNTOS ){
                obtenerSiguienteToken()
                val expresion: Expresion? = esExpresion()
                if( expresion != null ){
                    obtenerSiguienteToken()
                    if(tokenActual.categoria == Categoria.FIN_SENTENCIA){
                        obtenerSiguienteToken()
                        return Impresion(expresion)
                    }
                    else{
                        reportarError("Falta fin de sentencia")
                    }
                }
                else{
                    reportarError("Falta expresión")
                }
            }
            else{
                reportarError("Falta operador :")
            }
        }
        return null
    }

    /**
     * <InvocacionFuncion> ::= new identificador ([<ListaParametros>]);
     */
     fun esInvocacionFuncion(): Invocacion? {
        if(leerPalabraReservada("new")){
            obtenerSiguienteToken()
            if ( tokenActual.categoria == Categoria.IDENTIFICADOR ) {
                val identificadorFuncion = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                    obtenerSiguienteToken()
                    val parametros: ArrayList<Parametro> = esListaParametros()
                    if(tokenActual.categoria == Categoria.PARENTESIS_DERECHO){
                        obtenerSiguienteToken()
                        if (tokenActual.categoria == Categoria.FIN_SENTENCIA){
                            obtenerSiguienteToken()
                            return Invocacion(identificadorFuncion, parametros)
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
                            if (bloqueCondicional!=null){
                                var bloqueAlternativo : ArrayList<Sentencia>? = null
                                obtenerSiguienteToken()
                                if( leerPalabraReservada("else")) {
                                    obtenerSiguienteToken()
                                    bloqueAlternativo = esBloqueSentencias()
                                    if (bloqueAlternativo != null) {
                                        obtenerSiguienteToken()
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
                    reportarError("Expresión lógica no valida")
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

    fun esValor(): Valor? {
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
                obtenerSiguienteToken()
                if(tokenActual.categoria == Categoria.FIN_SENTENCIA){
                    obtenerSiguienteToken()
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
     * <Control> ::= try <BloqueSentencias> catch (<Tipodato> identificador) continue;
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
                obtenerSiguienteToken()
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
                obtenerSiguienteToken()
                return Decremento(identificadorVariable)
            }
            else{
                reportarError("Falta identificador de decremento")
            }
        }
        return null
    }

    fun esCicloFor(): Ciclo? {
        return  null
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
                    reportarError("Expresión lógica no valida")
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
                if(leerPalabraReservada("while")) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.PARENTESIS_IZQUIERDO) {
                        obtenerSiguienteToken()
                        val expresion = esExpresionLogica()
                        if (expresion != null) {
                            if (tokenActual.categoria == Categoria.PARENTESIS_DERECHO) {
                                obtenerSiguienteToken()
                                return CicloDoWhile(bloqueSentencias, expresion)
                            }
                            else{
                                reportarError("Falta paréntesis derecho")
                            }
                        }
                        else{
                            reportarError("Expresión lógica no valida")
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
        var expresion: Expresion? = esExpresionLogica()
        if(expresion!=null)
            return expresion
        return null
    }

    /**
     * <ExpresionLogica> ::= [ operadorNegacion ] <ExpresionLogica> | (<ExpresionLogica>) | <ExpresionLogica> operadorAND <ExpresionLogica> | <ExpresionLogica> operadorOR <ExpresionLogica> | <ExpresionRelacional> | true | false
     */
    fun esExpresionLogica(): ExpresionLogica? {
        return null
    }

    /**
     * <Tipodato> ::= boolean | int |float | string | char | class identificador
     */
    fun esTipoDato(): Token?{
        if( tokenActual.categoria == Categoria.PALABRA_RESERVADA ){
            if ( keys.leerLexema(tokenActual.lexema) == "tipoDato")
                return tokenActual
        }
        return null
    }

    fun leerPalabraReservada(lexema: String):Boolean{
        return (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.lexema == lexema)
    }

}