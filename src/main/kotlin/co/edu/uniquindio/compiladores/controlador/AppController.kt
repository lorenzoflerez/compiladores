package co.edu.uniquindio.compiladores.controlador

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.AnalizadorSemantico
import co.edu.uniquindio.compiladores.sintactico.AnalizadorSintactico
import co.edu.uniquindio.compiladores.sintactico.estructura.UnidadDeCompilacion
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.io.File
import java.net.URL
import java.util.*

class AppController : Initializable{

    @FXML lateinit var codigoFuente:TextArea

    @FXML lateinit var tablaTokens:TableView<Token>
    @FXML lateinit var lexemaToken: TableColumn<Token, String>
    @FXML lateinit var categoriaToken:TableColumn<Token, String>
    @FXML lateinit var filaToken:TableColumn<Token, Int>
    @FXML lateinit var columnaToken:TableColumn<Token, Int>

    @FXML lateinit var tablaErroresLexicos:TableView<Error>
    @FXML lateinit var mensajeErrorLexico: TableColumn<Error, String>
    @FXML lateinit var filaErrorLexico: TableColumn<Error, Int>
    @FXML lateinit var columnaErrorLexico: TableColumn<Error, Int>

    @FXML lateinit var tablaErroresSintacticos:TableView<Error>
    @FXML lateinit var mensajeErrorSintactico: TableColumn<Error, String>
    @FXML lateinit var filaErrorSintactico: TableColumn<Error, Int>
    @FXML lateinit var columnaErrorSintactico: TableColumn<Error, Int>

    @FXML lateinit var tablaErroresSemanticos:TableView<Error>
    @FXML lateinit var mensajeErrorSemantico: TableColumn<Error, String>
    @FXML lateinit var filaErrorSemantico: TableColumn<Error, Int>
    @FXML lateinit var columnaErrorSemantico: TableColumn<Error, Int>

    @FXML lateinit var arbolVisual:TreeView<String>

    private lateinit var lexico:AnalizadorLexico
    private lateinit var sintaxis: AnalizadorSintactico
    private lateinit var semantica: AnalizadorSemantico
    private var unidadCompilacion:UnidadDeCompilacion? = null

    /**
     * Construye las tablas de la Interfaz
     */
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        lexemaToken.cellValueFactory = PropertyValueFactory("lexema")
        categoriaToken.cellValueFactory = PropertyValueFactory("categoria")
        filaToken.cellValueFactory = PropertyValueFactory("fila")
        columnaToken.cellValueFactory = PropertyValueFactory("columna")

        mensajeErrorLexico.cellValueFactory = PropertyValueFactory("error")
        filaErrorLexico.cellValueFactory = PropertyValueFactory("fila")
        columnaErrorLexico.cellValueFactory = PropertyValueFactory("columna")

        mensajeErrorSintactico.cellValueFactory = PropertyValueFactory("error")
        filaErrorSintactico.cellValueFactory = PropertyValueFactory("fila")
        columnaErrorSintactico.cellValueFactory = PropertyValueFactory("columna")

        mensajeErrorSemantico.cellValueFactory = PropertyValueFactory("error")
        filaErrorSemantico.cellValueFactory = PropertyValueFactory("fila")
        columnaErrorSemantico.cellValueFactory = PropertyValueFactory("columna")

        arbolVisual.root = TreeItem("Unidad de Compilación")

        lexico = AnalizadorLexico()
        sintaxis = AnalizadorSintactico()
        semantica = AnalizadorSemantico()
    }


    @FXML
    fun analizar( e: ActionEvent) {
        if(codigoFuente.text.isNotEmpty()){
            tablaTokens.items.clear()
            lexico.inicializar(codigoFuente.text)
            lexico.analizar()

            tablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
            tablaErroresLexicos.items = FXCollections.observableArrayList(lexico.listaErrores)

            if( lexico.listaErrores.isEmpty()){
                sintaxis.inicializar(lexico.listaTokens)
                unidadCompilacion = sintaxis.esUnidadDeCompilacion()

                tablaErroresSintacticos.items.clear()
                println(sintaxis.listaErrores)
                tablaErroresSintacticos.items = FXCollections.observableArrayList(sintaxis.listaErrores)

                if(unidadCompilacion != null){
                    tablaErroresSemanticos.items.clear()
                    arbolVisual.root = unidadCompilacion!!.getArbolVisual()

                    semantica.inicializar(unidadCompilacion!!)
                    semantica.llenarTablaSimbolos()
                    semantica.analizarSemantica()
                    println(semantica.tablaSimbolos)
                    println(semantica.erroresSemanticos)
                    tablaErroresSemanticos.items = FXCollections.observableArrayList(semantica.erroresSemanticos)
                }
            }
            else{
               arbolVisual.root = TreeItem("Unidad de Compilación")
            }
        }
    }

    @FXML
    fun traducirCodigo(e: ActionEvent?) {
        if (lexico.listaErrores.isEmpty() && sintaxis.listaErrores.isEmpty() && semantica.erroresSemanticos.isEmpty() ) {
            val codigoFuente = unidadCompilacion!!.getJavaCode()
            File("src/Principal.java").writeText( codigoFuente )
            try {
                val p = Runtime.getRuntime().exec("javac src/Principal.java")
                p.waitFor()
                Runtime.getRuntime().exec("java Principal", null, File("src"))
            } catch (ea: Exception) {
                ea.printStackTrace()
            }
        }
        else{
            val alerta = Alert(Alert.AlertType.ERROR)
            alerta.headerText = null
            alerta.contentText = "El código no se puede traducir porque contiene errores"
            alerta.show()
        }
    }


}