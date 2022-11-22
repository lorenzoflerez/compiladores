package co.edu.uniquindio.compiladores.controlador

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantico.AnalizadorSemantico
import co.edu.uniquindio.compiladores.sintactico.AnalizadorSintactico
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
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

    @FXML
    private fun analizar( e: ActionEvent) {
        if(codigoFuente.text.isNotEmpty()){
            tablaTokens.items.clear()
            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            tablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
            tablaErroresLexicos.items = FXCollections.observableArrayList(lexico.listaErrores)

            if( lexico.listaErrores.isEmpty()){
                val sintaxis = AnalizadorSintactico(lexico.listaTokens)
                val unidad = sintaxis.esUnidadDeCompilacion()

                tablaErroresSintacticos.items.clear()
                println(sintaxis.listaErrores)
                tablaErroresSintacticos.items = FXCollections.observableArrayList(sintaxis.listaErrores)

                if(unidad != null){
                    tablaErroresSemanticos.items.clear()
                    arbolVisual.root = unidad.getArbolVisual()
                    val semantica = AnalizadorSemantico(unidad)
                    semantica.llenarTablaSimbolos()
                    println(semantica.tablaSimbolos)
                    semantica.analizarSemantica()
                    println(semantica.erroresSemanticos)
                    tablaErroresSemanticos.items = FXCollections.observableArrayList(semantica.erroresSemanticos)
                }
            }
            else{
                val alerta = Alert(Alert.AlertType.WARNING)
                alerta.headerText = "Mensaje"
                alerta.contentText = "El código tiene errores léxicos"
            }

        }
    }

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
    }
}