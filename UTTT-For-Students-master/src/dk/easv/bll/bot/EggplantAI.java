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
import java.util.List;

/**
 *
 * @author Frederik Jensen
 */
public class EggplantAI implements IBot
{
    private final String BOT_NAME = "EggplantAI";
    private String player = "";
    private String enemy = "";
    int[][] preferredMoves = {
            {0, 0}, {2, 2}, {0, 2}, {2, 0}, //Corners
            {1, 1}, //Corners ordered across
            {0, 1}, {2, 1}, {1, 0}, {1, 2}}; //Outer Middles ordered across
   
    
    @Override
    public IMove doMove(IGameState state)
    {
        checkWhoIAm(state);
        //Find macroboard to play in
        for (int[] move : preferredMoves)
        {
            if (checkForWins(state) != null) {
                    return checkForWins(state);
                }
            if(state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD))
            {
                
                //find move to play
                for (int[] selectedMove : preferredMoves)
                {
                    int x = move[0]*3 + selectedMove[0];
                    int y = move[1]*3 + selectedMove[1];
                    if(state.getField().getBoard()[x][y].equals(IField.EMPTY_FIELD))
                    {
                        return new Move(x,y);
                    }
                }
            }
        }

        //NOTE: Something failed, just take the first available move I guess!
        return state.getField().getAvailableMoves().get(0);
    }

    @Override
    public String getBotName()
    {
        return BOT_NAME;
    }
    
    private IMove checkForWins(IGameState state)
    {
        String[][] board = state.getField().getBoard();
        List<IMove> availableMoves = state.getField().getAvailableMoves();
        for (IMove move : availableMoves)
        {
            board[move.getX()][move.getY()] = player;
            int localX = move.getX() % 3;
            int localY = move.getY() % 3;
            int startX = move.getX() - (localX);
            int startY = move.getY() - (localY);
            //check col
            for (int i = startY; i < startY + 3; i++)
            {
                if (!board[move.getX()][i].equals(player))
                {
                    break;
                }
                if (i == startY + 3 - 1)
                {
                    return move;
                }
            }

            //check row
            for (int i = startX; i < startX + 3; i++)
            {
                if (!board[i][move.getY()].equals(player))
                {
                    break;
                }
                if (i == startX + 3 - 1)
                {
                    return move;
                }
            }

            //check diagonal
            if (localX == localY)
            {
                //we're on a diagonal
                int y = startY;
                for (int i = startX; i < startX + 3; i++)
                {
                    if (!board[i][y++].equals(player))
                    {
                        break;
                    }
                    if (i == startX + 3 - 1)
                    {
                        return move;
                    }
                }
            }

            //check anti diagonal
            if (localX + localY == 3 - 1)
            {
                int less = 0;
                for (int i = startX; i < startX + 3; i++)
                {
                    if (!board[i][(startY + 2) - less++].equals(player))
                    {
                        break;
                    }
                    if (i == startX + 3 - 1)
                    {
                        return move;
                    }
                }
            }
        }
        
        return null;
    }
    
    private void checkWhoIAm(IGameState state) {
        if (player.equals("") && enemy.equals("")) {
            if (state.getField().isEmpty()) {
                player = "0";
                enemy = "1";
                
            }else{
                player = "1";
                enemy = "0";
            }
        }
    }
    
}
