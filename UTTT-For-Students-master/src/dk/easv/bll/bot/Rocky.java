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

/**
 *
 * @author Schweizeren
 */
public class Rocky implements IBot
{
    private static final String BOTNAME = "Rocky";
    
    protected int[][] winningMoves =
    {
        {2,1},
        {2,2}, {2,0}, {1,1}, {0,0},
        {1,0}, {0,1}, {0,2}, {1,2}};
    
    
    @Override
    public IMove doMove(IGameState state)
    {
        for (int[] move : winningMoves)
        {
            if(state.getField().getMacroboard() [move[0]] [move[1]].equals(IField.AVAILABLE_FIELD))
            {
                for (int[] selectedMove : winningMoves)
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
        return state.getField().getAvailableMoves().get(0);
    }

    @Override
    public String getBotName()
    {
        return BOTNAME;
    }
    
}
