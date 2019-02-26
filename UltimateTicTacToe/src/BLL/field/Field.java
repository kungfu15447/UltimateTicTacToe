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

        macroBoard = new String[3][3];
    }

    @Override
    public void clearBoard()
    {
        for (String[] u : board)
        {
            for (String elem : u)
            {
                elem = AVAILABLE_FIELD;
            }
        }

        for (String[] u : macroBoard)
        {
            for (String elem : u)
            {
                elem = AVAILABLE_FIELD;
            }
        }
    }

    @Override
    public List<IMove> getAvailableMoves()
    {
        List<IMove> availableMoves = new ArrayList<>();
        for (int i = 0; i < macroBoard.length; i++)
        {
            for (int j = 0; j < macroBoard[0].length; i++)
            {
                if (isInActiveMicroboard(i, j))
                {
                    checkMicroboardForAvailableMoves(i, j, availableMoves);
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
        if (macroBoard[x][y].equals(AVAILABLE_FIELD))
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

    /**
     * Checks a microboard for available fields. If a field is available it adds
     * it to a lst of available moves
     * @param row the row value of the macroboard where the microboard is at
     * @param column the column value of the macroboard where the microboard is
     * at
     * @param availableMoves the list of avaiable moves
     */
    private void checkMicroboardForAvailableMoves(int row, int column, List<IMove> availableMoves)
    {
        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                int microboardX = x + row * 3;
                int microboardY = y + column * 3;
                if (board[microboardX][microboardY].equals(AVAILABLE_FIELD))
                {
                    IMove move = new Move(microboardX, microboardY);
                    availableMoves.add(move);
                }
            }
        }
    }

}
