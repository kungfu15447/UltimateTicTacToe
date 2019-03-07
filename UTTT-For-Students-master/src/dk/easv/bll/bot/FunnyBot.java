/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Kristian Urup laptop
 */
public class FunnyBot implements IBot
{

    private final String BOT_NAME = "FunnyBot";
    private String player = "";
    private String enemy = "";
    private final Random RAND = new Random();

    @Override
    public IMove doMove(IGameState state) {
        List<IMove> moves = state.getField().getAvailableMoves();
        IMove moveToBeMade = null;
        checkWhoIAm(state);
        if (moveToBeMade == null) {
            moveToBeMade = checkForWins(state, player);
        } 
        if (moveToBeMade == null) {
            moveToBeMade = checkForWins(state, enemy);
        }
        if(moveToBeMade == null)
        {
            moveToBeMade = checkMacroBoard(state);
        }
        if (moveToBeMade == null && moves.size() > 0) {
            moveToBeMade = moves.get(RAND.nextInt(moves.size()));
            //moveToBeMade = moves.get(RAND.nextInt(moves.size()));
            /* get random move from available moves */
        }
        return moveToBeMade;
    }
    
    public IMove checkMacroBoard(IGameState state)
    {
        for (int i = 0; i < state.getField().getMacroboard().length; i++) {
            for (int j = 0; j < state.getField().getMacroboard()[0].length; j++) {
                if (state.getField().getMacroboard()[i][j].equals(IField.AVAILABLE_FIELD)) {
                    int boardX = i+i*3;
                    int boardY = j+j*3;
                    if (state.getField().getBoard()[boardX][boardY].equals(IField.EMPTY_FIELD)) {
                        return new Move(boardX,boardY);
                    }
                }
            }
        }
        return null;
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

        return null;
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
        List<IMove> availableMoves = new ArrayList<>();
        int macroX = move.getX()/3;
        int macroY = move.getY()/3;
        int startX = 0+macroX*3;
        int startY = 0+macroY*3;
        
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (state.getField().getBoard()[i][j].equals(IField.EMPTY_FIELD)) {
                    availableMoves.add(new Move(i,j));
                }
            }
        }
        if (availableMoves.isEmpty()) {
            return false;
        }
        for (IMove move2 : availableMoves) {
            String[][] board = state.getField().getBoard();
            board[move2.getX()][move2.getY()] = enemy;
            int localX = move.getX() % 3;
            int localY = move.getY() % 3;
            int startingX = move2.getX() - (localX);
            int startingY = move2.getY() - (localY);
            //check col
            for (int i = startingY; i < startingY + 3; i++) {
                if (!board[move2.getX()][i].equals(enemy)) {
                    break;
                }
                if (i == startingY + 3 - 1) {
                    return true;
                }
            }

            //check row
            for (int i = startingX; i < startingX + 3; i++) {
                if (!board[i][move2.getY()].equals(enemy)) {
                    break;
                }
                if (i == startingX + 3 - 1) {
                    return true;
                }
            }

            //check diagonal
            if (localX == localY) {
                //we're on a diagonal
                int y = startingY;
                for (int i = startingX; i < startingX + 3; i++) {
                    if (!board[i][y++].equals(enemy)) {
                        break;
                    }
                    if (i == startingX + 3 - 1) {
                        return true;
                    }
                }
            }

            //check anti diagonal
            if (localX + localY == 3 - 1) {
                int less = 0;
                for (int i = startingX; i < startingX + 3; i++) {
                    if (!board[i][(startY + 2) - less++].equals(enemy)) {
                        break;
                    }
                    if (i == startX + 3 - 1) {
                        return true;
                    }
                }
            }
            board[move2.getX()][move2.getY()] = IField.EMPTY_FIELD;
        }
        
        return false;
    }

}
