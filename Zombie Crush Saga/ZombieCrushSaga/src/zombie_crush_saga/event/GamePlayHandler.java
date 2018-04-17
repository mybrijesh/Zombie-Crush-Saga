package zombie_crush_saga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
            
/**
 *
 * @author Brijesh
 */
public class GamePlayHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;
    
    public GamePlayHandler(ZombieCrushSagaMiniGame initMiniGame) 
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        miniGame.swithToLevelScreen();
    }
    
}