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
public class BackToLevelScreenHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;

    public BackToLevelScreenHandler(ZombieCrushSagaMiniGame initMiniGame) 
    {
        miniGame = initMiniGame;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        miniGame.swithToLevelScreen();
    }
}
