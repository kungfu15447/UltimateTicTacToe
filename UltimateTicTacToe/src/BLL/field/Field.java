/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL.field;

import BLL.move.IMove;
import BLL.move.Move;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frederik Jensen
 */
public class Field implements IField
{

    private String[][] board;
    private String[][] macroBoard;

    public Field()
    {
        board = new String[9][9];
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = EMPTY_FIELD;
            }
        }

        macroBoard = new String[3][3];
        
        for (int i = 0; i < macroBoard.length; i++) {
            for (int j = 0; j < macroBoard[0].length; j++) {
                macroBoard[i][j] = AVAILABLE_FIELD;
            }
        }
    }

    @Override
    public void clearBoard()
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = EMPTY_FIELD;
            }
        }

        macroBoard = new String[3][3];
        
        for (int i = 0; i < macroBoard.length; i++) {
            for (int j = 0; j < macroBoard[0].length; j++) {
                macroBoard[i][j] = AVAILABLE_FIELD;
            }
        }
    }

    @Override
    public List<IMove> getAvailableMoves()
    {
        List<IMove> availableMoves = new ArrayList<>();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                if (board[i][j].equals(EMPTY_FIELD) && isInActiveMicroboard(i, j))
                {
                    availableMoves.add(new Move(i,j));
                }
            }
        }
        return availableMoves;
    }

    @Override
    public String getPlayerId(int column, int row)
    {
        return board[row][column];
    }

    @Override
    public boolean isEmpty()
    {
        for (String[] e : board)
        {
            for (String elem : e) {
                if (elem.equals(EMPTY_FIELD) || elem.equals(AVAILABLE_FIELD))
                { 
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFull()
    {
        for (String[] u : board)
        {
            for (String elem : u)
            {
                if (elem.equals(AVAILABLE_FIELD) || elem.equals(EMPTY_FIELD))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Boolean isInActiveMicroboard(int x, int y)
    {
        int macroX = x/3;
        int macroY = y/3;
        if (macroBoard[macroX][macroY].equals(AVAILABLE_FIELD))
        {
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public String[][] getBoard()
    {
        return board;
    }

    @Override
    public String[][] getMacroboard()
    {
        return macroBoard;
    }

    @Override
    public void setBoard(String[][] board)
    {
        this.board = board;
    }

    @Override
    public void setMacroboard(String[][] macroboard)
    {
        this.macroBoard = macroboard;
    }

}
