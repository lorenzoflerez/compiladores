package co.edu.uniquindio.compiladores.controlador

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*

class AppController {

    @FXML lateinit var codigoFuente:TextArea

    @FXML lateinit var tableTokens:TableView<Token>

    @FXML lateinit var colLexema:TableColumn<Token, String>
    @FXML lateinit var colCategoria:TableColumn<Token, String>
    @FXML lateinit var colFila:TableColumn<Token, Int>
    @FXML lateinit var colColumna:TableColumn<Token, Int>

    @FXML
    private fun analizar( e: ActionEvent) {
        if(codigoFuente.text.isNotEmpty()){
            tableTokens.items.clear()
            val lexico = AnalizadorLexico(codigoFuente.text)
            lexico.analizar()
            tableTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
            print(lexico.listaTokens)
        }
    }

    fun initialize(location: URL?, resources: ResourceBundle?) {
        colLexema.cellValueFactory = PropertyValueFactory("lexema")
        colCategoria.cellValueFactory = PropertyValueFactory("categoria")
        colFila.cellValueFactory = PropertyValueFactory("fila")
        colColumna.cellValueFactory = PropertyValueFactory("columna")
    }

}