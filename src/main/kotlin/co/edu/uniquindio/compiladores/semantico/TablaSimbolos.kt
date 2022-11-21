package co.edu.uniquindio.compiladores.semantico

class TablaSimbolos (var listaErrores: ArrayList<String>) {

    var listaSimbolos: ArrayList<Simbolo> = ArrayList()

    /**
     * Permite guardar un símbolo de tipo import en la tabla de símbolos
     */
    fun guardarSimboloImport(nombre: String, fila: Int, columna: Int): Simbolo?{
        val s = buscarSimboloImport(nombre)
        if (s == null) {
            val nuevo = Simbolo(nombre, "Unidad de compilación", fila, columna)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add("La importacion '$nombre' ya existe en $fila:$columna")
        }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun guardarSimboloFuncion(nombre: String, tipo: String?, tipoParametros: ArrayList<String>): Simbolo? {
        val s = buscarSimboloFuncion(nombre, tipoParametros)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo.orEmpty(), tipoParametros)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add("La función $nombre $tipoParametros ya existe") }
        return null
    }

    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     */
    fun guardarSimboloVariable(nombre: String, tipo: String, ambito: String, fila: Int, columna: Int): Simbolo? {
        val s = buscarSimboloVariable(nombre, ambito)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, ambito, fila, columna )
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add("La variable $nombre ya existe en el ámbito $ambito") }
        return null
    }

    /**
     * Busca un símbolo de tipo variable en la tabla de símbolos
     */
    fun buscarSimboloVariable(nombre: String, ambito: String): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (nombre == simbolo.nombre && ambito == simbolo.ambito) {
                return simbolo
            }
        }
        return null
    }

    /**
     * Busca un símbolo de tipo función en la tabla de símbolos
     */
    fun buscarSimboloFuncion(nombre: String, tiposParametros: ArrayList<String>): Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.tipoParametros != null) {
                if (nombre == simbolo.nombre && tiposParametros == simbolo.tipoParametros) {
                    return simbolo
                }
            }
        }
        return null
    }

    /**
     * Busca un símbolo de tipo import en la tabla de símbolos
     */
    fun buscarSimboloImport(nombre: String): Simbolo? {
        for (simbolo in listaSimbolos) if (nombre == simbolo.nombre) {
            return simbolo
        }
        return null
    }
}