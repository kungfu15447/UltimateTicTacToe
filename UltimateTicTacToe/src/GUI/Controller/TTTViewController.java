/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controller;

import BLL.bot.IBot;
import BLL.field.IField;
import BLL.game.GameState;
import BLL.game.IGameState;
import BLL.game.GameManager;
import BLL.move.Move;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Frederik Jensen
 */
public class TTTViewController implements Initializable
{

    private int macroRowIndex;
    private int macroColumnIndex;
    private GameState currentState;
    private IBot bot;
    private IBot bot2;
    private IField field;
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
    private SplitPane splitPane;

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        Integer col = GridPane.getColumnIndex((Node) event.getSource());
        int r = (row == null) ? 0 : row;
        int c = (col == null) ? 0 : col;
        int boardXCoordinates = r + macroRowIndex * 3;
        int boardYCoordinates = c + macroColumnIndex * 3;
        String setO = "O";
        String setX = "X";
        Button btn = (Button) event.getSource();

        if (gameManager.getCurrentPlayer() == 0)
        {
            btn.setText(setX);

        } else if (gameManager.getCurrentPlayer() == 1)
        {
            btn.setText(setO);

        }
        gameManager.updateGame(new Move(boardXCoordinates, boardYCoordinates));

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
        if (result.get() == ButtonType.YES)
        {
            field.clearBoard();
        } else
        {
            //close the dialog!
        }

    }

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
            }
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

    public void initializeGameManager(GameManager gameManager)
    {
        this.gameManager = gameManager;
    }

    @FXML
    private void getMacroBoardCoordinates(MouseEvent event)
    {
        Integer row = GridPane.getRowIndex((Node) event.getSource());
        Integer col = GridPane.getColumnIndex((Node) event.getSource());
        macroRowIndex = (row == null) ? 0 : row;
        macroColumnIndex = (col == null) ? 0 : col;
    }

    @FXML
    private void handleMainMenuBtn(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) splitPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/View/OpenScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
