/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
/**
 *
 * @author Brijesh
 */
public class ScrollUpDownHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;

    public ScrollUpDownHandler(ZombieCrushSagaMiniGame initMiniGame) 
    {
        miniGame = initMiniGame;
    }
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        String whatButton = ae.getActionCommand();
        switch (whatButton) 
        {
            case SCROLLUP_BUTTON_TYPE:
                miniGame.ScrollUp();
                break;
            case SCROLLDOWN_BUTTON_TYPE:
                miniGame.ScrollDown();
                break;
        }
    }
    
}
