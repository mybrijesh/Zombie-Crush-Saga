/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;

/**
 *
 * @author Brijesh
 */
public class TurnToGameScreenHandler implements ActionListener
{
    
    private ZombieCrushSagaMiniGame miniGame;

    public TurnToGameScreenHandler(ZombieCrushSagaMiniGame initMiniGame) 
    {
        miniGame = initMiniGame;
    }
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        miniGame.swithToGameScreen();
        LevelHandler lh = new LevelHandler(miniGame);
        lh.alsoupdateJellyGrid();
    }
    
}
