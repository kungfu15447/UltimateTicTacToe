/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controller;

import BLL.bot.IBot;
import BLL.game.GameManager;
import BLL.game.GameState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kristian Urup laptop
 */
public class OpenScreenController implements Initializable
{
    OpenScreenController oscontroller;
    GameManager gameManager;
    GameState currentState;
    IBot bot;
    IBot bot2;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        comboBox.setItems(FXCollections.observableArrayList("Human VS Human","Human VS Bot","Bot VS Bot"));
        comboBox.setVisibleRowCount(3);
    }    

    @FXML
    public String handleComboBox() 
    {
        String option;
        int selectIndex = comboBox.getSelectionModel().getSelectedIndex();
        
        switch(selectIndex)
        {
            case 0: option = "humanVsHuman";
            break;
            case 1: option = "humanVsBot";
            break;
            case 2: option = "botVsBot";
            break;
            default:
                throw new UnsupportedOperationException("No option chosen");
        }
        return option;
    }

    @FXML
    private void handleContinueBtn(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/TTTView.fxml"));
        Parent root = (Parent) loader.load();
        
        TTTViewController tttvcontroller = loader.getController();
        tttvcontroller.setchoice(handleComboBox());
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
    public String getOpponent()
    {
        String option = handleComboBox();
        return option;
    }
}
