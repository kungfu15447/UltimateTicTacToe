/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Frederik Jensen
 */
public class EggplantAI implements IBot {

    private final String BOT_NAME = "EggplantAI";
    private String player = "";
    private String enemy = "";
    private Random rand = new Random();

    @Override
    public IMove doMove(IGameState state) {
        List<IMove> moves = state.getField().getAvailableMoves();

        checkWhoIAm(state);
        if (checkForWins(state, player) != null) {
            return checkForWins(state, player);
        } else if (checkForWins(state, enemy) != null) {
            return checkForWins(state, enemy);
        }
        if (moves.size() > 0) {
            return moves.get(rand.nextInt(moves.size()));
            /* get random move from available moves */
        }
        return state.getField().getAvailableMoves().get(0);
    }

    @Override
    public String getBotName() {
        return BOT_NAME;
    }

    private IMove checkForWins(IGameState state, String currentPlayer) {
        List<IMove> availableMoves = state.getField().getAvailableMoves();
        for (IMove move : availableMoves) {
            String[][] board = state.getField().getBoard();
            board[move.getX()][move.getY()] = currentPlayer;
            int localX = move.getX() % 3;
            int localY = move.getY() % 3;
            int startX = move.getX() - (localX);
            int startY = move.getY() - (localY);
            //check col
            for (int i = startY; i < startY + 3; i++) {
                if (!board[move.getX()][i].equals(currentPlayer)) {
                    break;
                }
                if (i == startY + 3 - 1) {
                    return move;
                }
            }

            //check row
            for (int i = startX; i < startX + 3; i++) {
                if (!board[i][move.getY()].equals(currentPlayer)) {
                    break;
                }
                if (i == startX + 3 - 1) {
                    return move;
                }
            }

            //check diagonal
            if (localX == localY) {
                //we're on a diagonal
                int y = startY;
                for (int i = startX; i < startX + 3; i++) {
                    if (!board[i][y++].equals(currentPlayer)) {
                        break;
                    }
                    if (i == startX + 3 - 1) {
                        return move;
                    }
                }
            }

            //check anti diagonal
            if (localX + localY == 3 - 1) {
                int less = 0;
                for (int i = startX; i < startX + 3; i++) {
                    if (!board[i][(startY + 2) - less++].equals(currentPlayer)) {
                        break;
                    }
                    if (i == startX + 3 - 1) {
                        return move;
                    }
                }
            }
            board[move.getX()][move.getY()] = IField.EMPTY_FIELD;
        }

        return state.getField().getAvailableMoves().get(0);
    }

    private void checkWhoIAm(IGameState state) {
        if (player.equals("") && enemy.equals("")) {
            if (state.getField().isEmpty()) {
                player = "0";
                enemy = "1";

            } else {
                player = "1";
                enemy = "0";
            }
        }
    }
    
    private boolean checkIfEnemyWinsNextBoard(IGameState state, IMove move) {
        return false;
    }

}
