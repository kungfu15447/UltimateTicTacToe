/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL.field;

import BLL.move.IMove;
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
        for(String[] u : board) {
            for(String elem : u) {
                elem = AVAILABLE_FIELD;
            }
        }
        
        for(String[] u : macroBoard) {
            for (String elem : u) {
                elem = AVAILABLE_FIELD;
            }
        }
    }

    @Override
    public List<IMove> getAvailableMoves()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPlayerId(int column, int row)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFull()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean isInActiveMicroboard(int x, int y)
    {
        if (board[x][y].equals(AVAILABLE_FIELD)) {
            return true;
        }else {
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
