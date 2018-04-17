/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.file;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga;
import zombie_crush_saga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import zombie_crush_saga.data.ZombieCrushSagaLevelRecord;
import zombie_crush_saga.data.ZombieCrushSagaRecord;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;

import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;

/**
 *
 * @author Brijesh
 */
public class ZombieCrushSagaFileManager 
{
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE
    private ZombieCrushSagaMiniGame miniGame;
    
    public ZombieCrushSagaFileManager(ZombieCrushSagaMiniGame initMiniGame) 
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;       
    }

    public void loadLevel(String levelFile) 
    {
        //LOAD THE RAW DATA SO WE CAN USE IT
        //OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        //FOLLOWED BY THE GRID VALUES
        try
        {
            File fileToOpen = new File(levelFile);
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //here we load all the data at once
            bis.read(bytes);
            bis.close();
            //now we transfer the data from byte to our level record file
            DataInputStream dis = new DataInputStream(bais);
            //note that we must load the data in the same 
            //order and format as we saved it
            
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            int [][] newGrid = new int [initGridColumns][initGridRows];
            // AND NOW ALL THE CELL VALUES
            for (int i = 0; i < initGridColumns; i++)
            {                        
                for (int j = 0; j < initGridRows; j++)
                {
                    newGrid[i][j] = dis.readInt();
                }
            }
            ZombieCrushSagaDataModel dataModel = (ZombieCrushSagaDataModel)miniGame.getDataModel();
            dataModel.initLevelGrid(newGrid, initGridColumns, initGridRows);
            dataModel.setCurrentLevel(levelFile);
            dataModel.setUpScoring();
            miniGame.updateBoundaries();
        }
        catch (Exception e)
        {
            //level loading error
            miniGame.getErrorHandler().processError(ZombieCrushSaga.ZombieCrushSagaPropertyType.LOAD_LEVEL_ERROR);
        }
    }
    public void loadJellyLevel(String levelFile)
    {
        //LOAD THE RAW DATA SO WE CAN USE IT
        //OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        //FOLLOWED BY THE GRID VALUES
        try
        {
            File fileToOpen = new File(levelFile);
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //here we load all the data at once
            bis.read(bytes);
            bis.close();
            //now we transfer the data from byte to our level record file
            DataInputStream dis = new DataInputStream(bais);
            //note that we must load the data in the same 
            //order and format as we saved it
            
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            int [][] newGrid = new int [initGridColumns][initGridRows];
            // AND NOW ALL THE CELL VALUES
            for (int i = 0; i < initGridColumns; i++)
            {                        
                for (int j = 0; j < initGridRows; j++)
                {
                    newGrid[i][j] = dis.readInt();
                }
            }
            ZombieCrushSagaDataModel dataModel = (ZombieCrushSagaDataModel)miniGame.getDataModel();
            dataModel.initLevelJellyGrid(newGrid, initGridColumns, initGridRows);
        }
        catch (Exception e)
        {
            //level loading error
            miniGame.getErrorHandler().processError(ZombieCrushSaga.ZombieCrushSagaPropertyType.LOAD_LEVEL_ERROR);
        }
    }
    public ZombieCrushSagaRecord loadRecord()
    {
        ZombieCrushSagaRecord recordToLoad = new ZombieCrushSagaRecord();
        
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
            String recordPath = dataPath+props.getProperty(ZombieCrushSagaPropertyType.RECORD_FILE_NAME);
            File fileToOpen = new File(recordPath);
            
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            bis.read(bytes);
            bis.close();
            
            DataInputStream dis = new DataInputStream(bais);
            
            int numLevels = dis.readInt();
            for(int i =0; i<numLevels;i++)
            {
                String levelName = dis.readUTF();
                ZombieCrushSagaLevelRecord rec = new ZombieCrushSagaLevelRecord();
                rec.score = dis.readInt();
                recordToLoad.addZombieCrushSagaLevelRecord(levelName, rec);
            }
            
        }
        catch(Exception e)
        {
            //we will not do anything
        }
        
        return recordToLoad;
    }

    public void saveRecord(ZombieCrushSagaRecord record) 
    {
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(ZombieCrushSagaPropertyType.RECORD_FILE_NAME);
            File fileToSave = new File(recordPath);
            
            byte[] bytes = record.toByteArray();
            
            FileOutputStream fos = new FileOutputStream(fileToSave);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.write(bytes);
            
            dos.close();
        }
        catch(Exception e)
        {
            miniGame.getErrorHandler().processError(ZombieCrushSagaPropertyType.RECORD_SAVE_ERROR);
        }
    }
    
}
