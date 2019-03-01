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
    private void handleComboBox(ActionEvent event) 
    {
        int selectIndex = comboBox.getSelectionModel().getSelectedIndex();
        
        switch(selectIndex)
        {
            case 1: humanVsHuman();
            break;
            case 2: humanVsBot();
            break;
            case 3: botVsBot();
            break;
        }
    }
        
        public void humanVsHuman()
    {
        gameManager = new GameManager(new GameState());
    }

    public void humanVsBot()
    {
        gameManager = new GameManager(currentState, bot);
    }

    public void botVsBot()
    {
        gameManager = new GameManager(currentState, bot, bot2);
    }

    @FXML
    private void handleContinueBtn(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/View/TTTView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
}
