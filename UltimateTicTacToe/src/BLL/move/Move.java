/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL.move;

/**
 *
 * @author Frederik Jensen
 */
public class Move implements IMove
{
    private final int X;
    private final int Y;
    
    public Move(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    @Override
    public int getX()
    {
        return X;
    }

    @Override
    public int getY()
    {
        return Y;
    }
    
}
