/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.event;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import zombie_crush_saga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
/**
 *
 * @author Brijesh
 */
public class ZombieSmasher implements ActionListener
{
    private boolean clicked;
    private final ZombieCrushSagaMiniGame game;
    private ZombieCrushSagaDataModel data;
    public ZombieSmasher(ZombieCrushSagaMiniGame initGame)
    {
        game = initGame;
        data= (ZombieCrushSagaDataModel)game.getDataModel();
        clicked = false;
    }
    public boolean getZombieSmasher()
    {
        return clicked;
    }
    public void resetClicked()
    {
        clicked = false;
        game.getCanvas().setCursor(Cursor.getDefaultCursor());
    }
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushSaga.ZombieCrushSagaPropertyType.IMG_PATH);
         //get the game's data model, which is alredy locked for us to use
        
        if(clicked == false)
        {
            clicked = true;
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image img = toolkit.getImage(imgPath+props.getProperty(ZombieCrushSaga.ZombieCrushSagaPropertyType.ZOMBIE_SMASHER_IMAGE_NAME));
            Cursor c = toolkit.createCustomCursor(img,new Point(0,0),"img");
            game.getCanvas().setCursor(c);
            data.setClickedTrue();
        }
        else
            resetClicked();
        System.out.println("ZombieSmasher: "+clicked);
    }
}
