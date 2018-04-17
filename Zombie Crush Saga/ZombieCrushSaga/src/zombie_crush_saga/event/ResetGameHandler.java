/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga;
import zombie_crush_saga.data.ZombieCrushSagaRecord;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;

/**
 *
 * @author Brijesh
 */
public class ResetGameHandler implements ActionListener
{
    private ZombieCrushSagaMiniGame game;
    public ResetGameHandler(ZombieCrushSagaMiniGame initMiniGame)
    {
        game = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(ZombieCrushSaga.ZombieCrushSagaPropertyType.DATA_PATH);
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSaga.ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        
        for(int i = 0; i< levels.size();i++)
        {
            ((ZombieCrushSagaMiniGame)game).getPlayerRecord().resetScore(dataPath+levels.get(i));
            ((ZombieCrushSagaMiniGame)game).savePlayerRecord();
        }
        JOptionPane.showMessageDialog(null,"DATA RESET SUCESSFUL! :)");
    }
}
