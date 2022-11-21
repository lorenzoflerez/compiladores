package co.edu.uniquindio.compiladores.semantico

class Simbolo {

    var nombre: String = ""
    var tipo: String = ""
    var fila = 0
    var columna = 0
    var ambito: String = ""
    var tipoParametros: ArrayList<String>? = null

    /**
     * Constructor símbolo variable
     */
    constructor(nombre: String, tipo: String, ambito: String, fila:Int, columna:Int){
        this.nombre = nombre
        this.tipo = tipo
        this.ambito = ambito
        this.fila = fila
        this.columna = columna
    }

    /**
     * Constructor símbolo función
     */
    constructor(nombre: String, tipo: String, tipoParametros: ArrayList<String>){
        this.nombre = nombre
        this.tipo = tipo
        this.tipoParametros = tipoParametros
    }

    /**
     * Constructor símbolo import
     */
    constructor(nombre: String, ambito: String, fila: Int, columna: Int) {
        this.nombre = nombre
        this.fila = fila
        this.columna = columna
        this.ambito = ambito
    }
}