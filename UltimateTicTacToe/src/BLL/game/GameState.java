/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL.game;

import BLL.field.Field;
import BLL.field.IField;

/**
 *
 * @author Frederik Jensen
 */
public class GameState implements IGameState
{
    private final IField FIELD;
    private int moveNumber;
    private int roundNumber;
    public GameState() {
        FIELD = new Field();
        moveNumber = 1;
        roundNumber = 1;
    }


    
    @Override
    public IField getField()
    {
        return FIELD;
    }

    @Override
    public int getMoveNumber()
    {
        return moveNumber;
    }

    @Override
    public void setMoveNumber(int moveNumber)
    {
        this.moveNumber = moveNumber;
    }

    @Override
    public int getRoundNumber()
    {
        return roundNumber;
    }

    @Override
    public void setRoundNumber(int roundNumber)
    {
        this.roundNumber = roundNumber;
    }
    
}
