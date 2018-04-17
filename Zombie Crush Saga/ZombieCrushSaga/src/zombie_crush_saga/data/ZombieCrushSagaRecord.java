/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Brijesh
 */
public class ZombieCrushSagaRecord 
{
    private HashMap<String, ZombieCrushSagaLevelRecord> levelRecords;
    
    public ZombieCrushSagaRecord()
    {
        levelRecords = new HashMap();
    }
    
    public void addZombieCrushSagaLevelRecord(String levelName, ZombieCrushSagaLevelRecord rec) 
    {
        levelRecords.put(levelName, rec);
    }
    public void resetScore(String levelName)
    {
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);
        if(rec == null)
        {
            rec = new ZombieCrushSagaLevelRecord();
            rec.score = 0;
            levelRecords.put(levelName, rec);
        }
        else
            rec.score = 0;
        
    }
    public void addDataTOLevelRecord(String levelName, int score)
    {
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);
        
        if(rec == null)
        {
            rec = new ZombieCrushSagaLevelRecord();
            rec.score = score;
            levelRecords.put(levelName,rec);
        }
        else
        {
            //SAVE ONLY IF THE SCORE IS HIGHER THEN THE OLD SCORE
            if(rec.score < score)
            {
                rec.score = score;
            }
//            rec.score = score;
        }
    }
    public int getLevelScore(String levelName)
    {
        ZombieCrushSagaLevelRecord rec = levelRecords.get(levelName);
        
        if(rec == null)
            return 0;
        else
            return rec.score;
    }
    public byte[] toByteArray() throws IOException
    {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while(keysIt.hasNext())
        {
            String key = keysIt.next();
            dos.writeUTF(key);
            ZombieCrushSagaLevelRecord rec = levelRecords.get(key);
            dos.writeInt(rec.score);
        }
        return baos.toByteArray();
    }
}
