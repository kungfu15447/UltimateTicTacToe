/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controller;

import BLL.bot.IBot;
import BLL.field.IField;
import BLL.game.GameManager;
import BLL.game.GameState;
import BLL.game.IGameState;
import BLL.game.GameManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Frederik Jensen
 */
public class TTTViewController implements Initializable
{

    GameManager gm;
    GameState currentState;
    IBot bot;
    IBot bot2;
    IField field;
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

    private GameManager gameManager;
    
    
    

    @FXML
    private void handleButtonAction(ActionEvent event)
    {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lblPlayersTurn.setText("1");
    }    


    @FXML
    private void handleRestartBtn(ActionEvent event)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Restart the game?");
        alert.setHeaderText("You are about to clear the board");
        alert.setContentText("Are you sure?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()== ButtonType.YES)
        {
            field.clearBoard();
        }
        else
        {
            //close the dialog!
        }

    }

    @FXML
    private void handleChangeOpponent()
    {

        List<String> choices = new ArrayList<>();
        choices.add("Human vs. Human");
        choices.add("Human vs. Bot");
        choices.add("Bot vs. Bot");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Human vs. Human", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Which opponent do you want?");
        dialog.setContentText("Choose:");
        
        String choice;

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
        {
            switch (result.get())
            {
                case "Human vs. Human":
                    humanVsHuman();
                    System.out.println("Human vs. Human");
                    break;
                case "Human vs. Bot":
                    humanVsBot();
                    System.out.println("Human vs. Bot");
                    break;
                case "Bot vs. Bot":
                    botVsBot();
                    System.out.println("Bot vs. Bot");
                    break;
                default: choice = "you have not chosen";
                break;
            }
        }
    }

    public void humanVsHuman()
    {
        gm = new GameManager(currentState);
    }

    public void humanVsBot()
    {
        gm = new GameManager(currentState, bot);
    }

    public void botVsBot()
    {
        gm = new GameManager(currentState, bot, bot2);
    }
}
