/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.ui;

import mini_game.MiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.MAX_TILE_VELOCITY;

/**
 *
 * @author Brijesh
 */
public class FloatingPointRender
{
    private boolean movingToTarget;
    private float targetY;
    private float vY;
    private float y;
    private float x;
    private int gridColumn;
    private int gridRow;
    private int score;
    public FloatingPointRender(int initScore,float initX, float initY, boolean isItMovingToTarget)
    {
        score = initScore;
        x = initX;
        y = initY;
        vY = 1;
        targetY = y-20;
        movingToTarget = isItMovingToTarget;
    }
    public int getScore()
    {
        return score;
    }
    public void setScore(int initScore)
    {
        score = initScore;
    }
    public int getGridColumn() 
    { 
        return gridColumn; 
    }
    public int getGridRow() 
    { 
        return gridRow; 
    }
    public float getTargetY() 
    { 
        return targetY; 
    }
    public float getY()
    {
        return y;
    }
    public float getX()
    {
        return x;
    }
    public float getVy()
    {
        return vY;
    }
    public void setY(float initY)
    {
        y = initY;
    }
    public void setX(float initX)
    {
        y = initX;
    }
    public void setVy(float initVy)
    {
        vY = initVy;
    }
    /**
     *
     * @param game
     */
    public void update()
    {
        
        if(y == targetY)
        {
            movingToTarget = false;
        }
        else
            y-=vY;
            
    }
    public boolean isMovingToTarget()
    {
        return movingToTarget;
    }
    public void setTarget(float initTargetY) 
    {
        targetY = initTargetY;        
    }
    public void startMovingToTarget() 
    {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
    }
}
