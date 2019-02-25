/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Frederik Jensen
 */
public class TTTViewController implements Initializable
{
    
    @FXML
    private GridPane macroBoard;
    @FXML
    private GridPane board1;
    @FXML
    private GridPane board2;
    @FXML
    private GridPane board4;
    @FXML
    private GridPane board5;
    @FXML
    private GridPane board7;
    @FXML
    private GridPane board8;
    @FXML
    private GridPane board9;
    @FXML
    private GridPane board6;
    @FXML
    private GridPane board3;
    @FXML
    private Label lblPlayersTurn;
    
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void handleRestartBtn(ActionEvent event)
    {
    }

    @FXML
    private void handleChangeOpponent(ActionEvent event)
    {
        List<String> choices = new ArrayList<>();
        choices.add("Human vs. Human");
        choices.add("Human vs. Bot");
        choices.add("Bot vs. Bot");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Human vs. Human", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Which opponent do you want?");
        dialog.setContentText("Choose:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            System.out.println("Your choice: " + result.get());
        }

        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(letter -> System.out.println("Your choice: " + letter));
    }
    
}
