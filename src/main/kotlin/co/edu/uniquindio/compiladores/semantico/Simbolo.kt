package co.edu.uniquindio.compiladores.semantico

class Simbolo() {

    var nombre: String = ""
    var tipo: String = ""
    var modificable: Boolean = true
    var ambito: String = ""
    var fila = 0
    var columna = 0
    var tipoParametros: ArrayList<String>? = null

    /**
     * Constructor símbolo variable
     */
    constructor(nombre: String, tipo: String, modificable:Boolean, ambito: String, fila:Int, columna:Int):this(){
        this.nombre = nombre
        this.tipo = tipo
        this.modificable = modificable
        this.ambito = ambito
        this.fila = fila
        this.columna = columna
    }

    /**
     * Constructor símbolo función
     */
    constructor(nombre: String, tipo: String, tipoParametros: ArrayList<String>, ambito: String):this(){
        this.nombre = nombre
        this.tipo = tipo
        this.tipoParametros = tipoParametros
        this.ambito = ambito
    }

    override fun toString(): String {
        return if(tipoParametros==null){
            "Simbolo(nombre='$nombre', tipo='$tipo', modificable=$modificable, ambito='$ambito', fila=$fila, columna=$columna)"
        }
        else{
            "Simbolo(nombre='$nombre', tipo='$tipo', ambito='$ambito',  tipoParametros=$tipoParametros)"
        }
    }
}