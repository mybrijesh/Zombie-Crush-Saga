package zombie_crush_saga.event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
/**
 *
 * @author Brijesh
 */
public class ExitHandler extends WindowAdapter implements ActionListener
{
    private ZombieCrushSagaMiniGame miniGame;
    
    public ExitHandler(ZombieCrushSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }
    
    @Override
    public void windowClosing(WindowEvent we)
    {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        System.exit(0);
    }
}
