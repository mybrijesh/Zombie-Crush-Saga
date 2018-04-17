/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombie_crush_saga.data;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.swing.JOptionPane;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ui.ZombieCrushSagaMiniGame;
import zombie_crush_saga.ui.ZombieCrushSagaTile;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import zombie_crush_saga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import zombie_crush_saga.event.LevelHandler;
import zombie_crush_saga.event.ZombieSmasher;
import zombie_crush_saga.ui.FloatingPointRender;

/**
 *
 * @author Brijesh
 */
public class ZombieCrushSagaDataModel extends MiniGameDataModel
{
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES

    private MiniGame miniGame;
    private ZombieCrushSagaMiniGame game;
    private ZombieCrushSagaMove move;
    private int moveValue;
    private int userScore;
    private int userMove;
    private int curve;
    private int counter;
    private int miniTargetScore;
    private int miniTargetScore1;
    private int miniTargetScore2;
    private int movingTileVelocity = 7;
    private int swapTileVelocity = 5;
    // THE LEVEL GRID REFERS TO THE LAYOUT FOR A GIVEN LEVEL, MEANING
    // HOW MANY TILES FIT INTO EACH CELL WHEN FIRST STARTING A LEVEL
    private int[][] levelGrid;
    private int[][] levelJellyGrid;
    // LEVEL GRID DIMENSIONS
    private int gridColumns;
    private int gridRows;
    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private ArrayList<ZombieCrushSagaTile>[][] tileGrid;

    // THESE ARE THE TILES THE PLAYER HAS MATCHED
    private ArrayList<ZombieCrushSagaTile> stackTiles;
    private ArrayList<ZombieCrushSagaTile> RedStackTiles;
    private ArrayList<ZombieCrushSagaTile> BlueStackTiles;
    private ArrayList<ZombieCrushSagaTile> GreenStackTiles;
    private ArrayList<ZombieCrushSagaTile> YellowStackTiles;
    private ArrayList<ZombieCrushSagaTile> PurpleStackTiles;
    private ArrayList<ZombieCrushSagaTile> OrangeStackTiles;
    private ArrayList<ZombieCrushSagaTile> GlobeStackTiles;
    private ArrayList<ZombieCrushSagaTile> RedWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> BlueWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> GreenWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> YellowWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> OrangeWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> PurpleWrappedStackTiles;
    private ArrayList<ZombieCrushSagaTile> TestTileStackTiles;
    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<ZombieCrushSagaTile> movingTiles;
    private ArrayList<ZombieCrushSagaTile> removingTile;
    private ArrayList<FloatingPointRender> scoresToRender;
    // THIS IS A SELECTED TILE, MEANING THE FIRST OF A PAIR THE PLAYER
    // IS TRYING TO MATCH. THERE CAN ONLY BE ONE OF THESE AT ANY TIME
    private ZombieCrushSagaTile selectedTileFirst;
    private ZombieCrushSagaTile selectedTileSecond;
    // THE INITIAL LOCATION OF TILES BEFORE BEING PLACED IN THE GRID
    private int unassignedTilesX;
    private int unassignedTilesY;
    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    // THE REFERENCE TO THE FILE BEING PLAYED
    private String currentLevel;
    private String levelName;
    private boolean badMove;
    private boolean specialZombieMatched = false;
    private boolean clicked;
    private boolean smasherAvailable;

    public ZombieCrushSagaDataModel(ZombieCrushSagaMiniGame initMiniGame)
    {

        miniGame = initMiniGame;
        game = new ZombieCrushSagaMiniGame();
        move = new ZombieCrushSagaMove();
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
        stackTiles = new ArrayList();
        RedStackTiles = new ArrayList();
        BlueStackTiles = new ArrayList();
        GreenStackTiles = new ArrayList();
        YellowStackTiles = new ArrayList();
        PurpleStackTiles = new ArrayList();
        OrangeStackTiles = new ArrayList();
        GlobeStackTiles = new ArrayList();
        RedWrappedStackTiles = new ArrayList();
        BlueWrappedStackTiles = new ArrayList();
        GreenWrappedStackTiles = new ArrayList();
        YellowWrappedStackTiles = new ArrayList();
        OrangeWrappedStackTiles = new ArrayList();
        PurpleWrappedStackTiles = new ArrayList();
        TestTileStackTiles = new ArrayList();
        movingTiles = new ArrayList();
        removingTile = new ArrayList();
        scoresToRender = new ArrayList();
        selectedTileFirst = null;
        selectedTileSecond = null;
        clicked = false;
        userScore = 0;
        userMove = 0;
        moveValue = 0;
        curve = 0;
        counter = 0;
        miniTargetScore = 0;
        smasherAvailable = true;
        badMove = false;
    }

    public void initTiles()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        int spriteTypeID = 0;
        SpriteType sT;

        ArrayList<String> typeATiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_A_TYPE);
        for (int i = 0; i < typeATiles.size(); i++)
        {
            String imgFile = imgPath + typeATiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 700; j++)
            {
                initTile(sT, TILE_A_TYPE);
                //WE CAN USE THIS IF STATMENT TO SET AMOUNT OF TILES PER LEVEL
//                if(stackTiles.size() >=40)
//                    break;
            }
            spriteTypeID++;
        }
        //this will make red zombie stack for us
        ArrayList<String> typeRedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_RED_TYPE);
        for (int i = 0; i < typeRedTiles.size(); i++)
        {
            String imgFile = imgPath + typeRedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileRed(sT, TILE_RED_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeBlueTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_BLUE_TYPE);
        for (int i = 0; i < typeBlueTiles.size(); i++)
        {
            String imgFile = imgPath + typeBlueTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileBlue(sT, TILE_BLUE_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeGreenTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_GREEN_TYPE);
        for (int i = 0; i < typeGreenTiles.size(); i++)
        {
            String imgFile = imgPath + typeGreenTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileGreen(sT, TILE_GREEN_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeYellowTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_YELLOW_TYPE);
        for (int i = 0; i < typeYellowTiles.size(); i++)
        {
            String imgFile = imgPath + typeYellowTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileYellow(sT, TILE_YELLOW_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeOrangeTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_ORANGE_TYPE);
        for (int i = 0; i < typeOrangeTiles.size(); i++)
        {
            String imgFile = imgPath + typeOrangeTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileOrange(sT, TILE_ORANGE_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typePurpleTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_PURPLE_TYPE);
        for (int i = 0; i < typePurpleTiles.size(); i++)
        {
            String imgFile = imgPath + typePurpleTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTilePurple(sT, TILE_PURPLE_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeBombTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_BOMB_TYPE);
        for (int i = 0; i < typeBombTiles.size(); i++)
        {
            String imgFile = imgPath + typeBombTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileBomb(sT, TILE_BOMB_TYPE);
            }
            spriteTypeID++;
        }
        //this will make red zombie stack for us
        ArrayList<String> typeRedWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_RED_WRAPPED_TYPE);
        for (int i = 0; i < typeRedWrappedTiles.size(); i++)
        {
            String imgFile = imgPath + typeRedWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileRedWrapped(sT, TILE_RED_WRAPPED_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeBlueWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_BLUE_WRAPPED_TYPE);
        for (int i = 0; i < typeBlueWrappedTiles.size(); i++)
        {
            String imgFile = imgPath + typeBlueWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileBlueWrapped(sT, TILE_BLUE_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeGreenWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_GREEN_WRAPPED_TYPE);
        for (int i = 0; i < typeGreenTiles.size(); i++)
        {
            String imgFile = imgPath + typeGreenWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileGreenWrapped(sT, TILE_GREEN_WRAPPED_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeYellowWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_YELLOW_WRAPPED_TYPE);
        for (int i = 0; i < typeYellowWrappedTiles.size(); i++)
        {
            String imgFile = imgPath + typeYellowWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileYellowWrapped(sT, TILE_YELLOW_WRAPPED_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typeOrangeWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_ORANGE_WRAPPED_TYPE);
        for (int i = 0; i < typeOrangeWrappedTiles.size(); i++)
        {
            String imgFile = imgPath + typeOrangeWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTileOrangeWrapped(sT, TILE_ORANGE_WRAPPED_TYPE);
            }
            spriteTypeID++;
        }
        //this will make BLUE zombie stack for us
        ArrayList<String> typePurpleWrappedTiles = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_PURPLE_WRAPPED_TYPE);
        for (int i = 0; i < typePurpleWrappedTiles.size(); i++)
        {
            String imgFile = imgPath + typePurpleWrappedTiles.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + spriteTypeID);
            for (int j = 0; j < 40; j++)
            {
                initTilePurpleWrapped(sT, TILE_PURPLE_WRAPPED_TYPE);
            }
            spriteTypeID++;
        }
        //this is stack to create test type tiles manually
        ArrayList<String> typeTestTile = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.TILE_TEST_TYPE);
        for (int i = 0; i < typeTestTile.size(); i++)
        {
            String imgFile = imgPath + typeTestTile.get(i);
            sT = initTileSpriteType(imgFile, TILE_SPRITE_TYPE_PREFIX + 2);
            for (int j = 0; j < 40; j++)
            {
                initTileTestTile(sT, TILE_TEST_TYPE);
            }

        }
    }

    private void initTile(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        stackTiles.add(newTile);
    }

    private void initTileTestTile(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        TestTileStackTiles.add(newTile);
    }

    private void initTileRed(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        RedStackTiles.add(newTile);
    }

    private void initTileBlue(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        BlueStackTiles.add(newTile);
    }

    private void initTileGreen(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        GreenStackTiles.add(newTile);
    }

    private void initTileYellow(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        YellowStackTiles.add(newTile);
    }

    private void initTileOrange(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        OrangeStackTiles.add(newTile);
    }

    private void initTilePurple(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        PurpleStackTiles.add(newTile);
    }

    private void initTileRedWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        RedWrappedStackTiles.add(newTile);
    }

    private void initTileBlueWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        BlueWrappedStackTiles.add(newTile);
    }

    private void initTileGreenWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        GreenWrappedStackTiles.add(newTile);
    }

    private void initTileYellowWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        YellowWrappedStackTiles.add(newTile);
    }

    private void initTileOrangeWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        OrangeWrappedStackTiles.add(newTile);
    }

    private void initTilePurpleWrapped(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        PurpleWrappedStackTiles.add(newTile);
    }

    private void initTileBomb(SpriteType sT, String tileType)
    {
        ZombieCrushSagaTile newTile = new ZombieCrushSagaTile(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);
        GlobeStackTiles.add(newTile);
    }

    public void initLevelGrid(int[][] initGrid, int initGridColumns, int initGridRows)
    {
        levelGrid = initGrid;
        gridColumns = initGridColumns;
        gridRows = initGridRows;

        tileGrid = new ArrayList[gridColumns][gridRows];
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                tileGrid[i][j] = new ArrayList();
            }
        }
        enableTiles(true);
    }

    public int getLevelGridValueAtPosition(int col, int row)
    {
        return levelGrid[col][row];
    }

    public void initLevelJellyGrid(int[][] initJellyGrid, int initGridColumns, int initGridRows)
    {
        levelJellyGrid = initJellyGrid;
    }

    public int getLevelJellyGridValueAtPosition(int col, int row)
    {
        return levelJellyGrid[col][row];
    }

    public void printLevelJellyGrid()
    {
        for (int i = 0; i < gridRows; i++)
        {
            for (int j = 0; j < gridColumns; j++)
            {
                System.out.print(levelJellyGrid[j][i]);
            }
            System.out.println("");
        }
    }

    public boolean checkIfJellyAtPosition(int col, int row)
    {
        if (levelJellyGrid[col][row] == 1)
        {
            return true;
        }
        return false;
    }

    private SpriteType initTileSpriteType(String imgFile, String spriteTypeID)
    {
        SpriteType sT = new SpriteType(spriteTypeID);
        addSpriteType(sT);

        BufferedImage img = miniGame.loadImageWithColorKey(imgFile, COLOR_KEY);
        Image tempImage = img.getScaledInstance(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.SCALE_SMOOTH);
        img = new BufferedImage(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(tempImage, 0, 0, null);

        sT.addState(INVISIBLE_STATE, img);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(SELECTED_STATE, img);
        sT.addState(INCORRECTLY_SELECTED_STATE, img);
        return sT;
    }

    // ACCESSOR METHODS
    /**
     * Accessor method for getting the level currently being played.
     *
     * @return The level name used currently for the game screen.
     */
    public String getCurrentLevel()
    {
        return currentLevel;
    }

    /**
     * Accessor method for getting the number of tile columns in the game grid.
     *
     * @return The number of columns (left to right) in the grid for the level
     * currently loaded.
     */
    public int getGridColumns()
    {
        return gridColumns;
    }

    /**
     * Accessor method for getting the number of tile rows in the game grid.
     *
     * @return The number of rows (top to bottom) in the grid for the level
     * currently loaded.
     */
    public int getGridRows()
    {
        return gridRows;
    }

    /**
     * Accessor method for getting the tile grid, which has all the tiles the
     * user may select from.
     *
     * @return The main 2D grid of tiles the user selects tiles from.
     */
    public ArrayList<ZombieCrushSagaTile>[][] getTileGrid()
    {
        return tileGrid;
    }

    /**
     * Accessor method for getting the stack tiles.
     *
     * @return The stack tiles, which are the tiles the matched tiles are placed
     * in.
     */
    public ArrayList<ZombieCrushSagaTile> getStackTiles()
    {
        return stackTiles;
    }

    /**
     * Accessor method for getting the moving tiles.
     *
     * @return The moving tiles, which are the tiles currently being animated as
     * they move around the game.
     */
    public Iterator<ZombieCrushSagaTile> getMovingTiles()
    {
        return movingTiles.iterator();
    }

    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }
    /*
     * this method sets up all the scoring and stuff
     */

    public void CheatKeySetUserMove()
    {
        counter = userMove - 1;
    }

    public void CheatKeyClearJelly()
    {
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                levelJellyGrid[i][j] = 0;
            }
        }
    }

    public void CheatKeySetMiniTargetScore()
    {
        userScore = miniTargetScore * 2;
    }
    public int getBronzeTarget()
    {
        return miniTargetScore;
    }
    public int getGoldTarget()
    {
        return miniTargetScore1;
    }
    public int getPlatinumTarget()
    {
        return miniTargetScore2;
    }
    public void setUpScoring()
    {
        smasherAvailable = true;
        String currentLevel = this.getCurrentLevel();
        int lastSlash = currentLevel.lastIndexOf("/");
        levelName = currentLevel.substring(lastSlash + 1);
        int dot = levelName.indexOf(".");
        levelName = levelName.substring(0, dot);
        userScore = 0;
        if (levelName.equals("Level1"))
        {
            userMove = LEVEL_1_NUM_MOVES;
            moveValue = LEVEL_1_ONE_MOVE_VALUE;
            curve = LEVEL_1_CURVE;
            miniTargetScore = LEVEL_1_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_1_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_1_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level2"))
        {
            userMove = LEVEL_2_NUM_MOVES;
            moveValue = LEVEL_2_ONE_MOVE_VALUE;
            curve = LEVEL_2_CURVE;
            miniTargetScore = LEVEL_2_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_2_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_2_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level3"))
        {
            userMove = LEVEL_3_NUM_MOVES;
            moveValue = LEVEL_3_ONE_MOVE_VALUE;
            curve = LEVEL_3_CURVE;
            miniTargetScore = LEVEL_3_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_3_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_3_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level4"))
        {
            userMove = LEVEL_4_NUM_MOVES;
            moveValue = LEVEL_4_ONE_MOVE_VALUE;
            curve = LEVEL_4_CURVE;
            miniTargetScore = LEVEL_4_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_4_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_4_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level5"))
        {
            userMove = LEVEL_5_NUM_MOVES;
            moveValue = LEVEL_5_ONE_MOVE_VALUE;
            curve = LEVEL_5_CURVE;
            miniTargetScore = LEVEL_5_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_5_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_5_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level6"))
        {
            userMove = LEVEL_6_NUM_MOVES;
            moveValue = LEVEL_6_ONE_MOVE_VALUE;
            curve = LEVEL_6_CURVE;
            miniTargetScore = LEVEL_6_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_6_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_6_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level7"))
        {
            userMove = LEVEL_7_NUM_MOVES;
            moveValue = LEVEL_7_ONE_MOVE_VALUE;
            curve = LEVEL_7_CURVE;
            miniTargetScore = LEVEL_7_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_7_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_7_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level8"))
        {
            userMove = LEVEL_8_NUM_MOVES;
            moveValue = LEVEL_8_ONE_MOVE_VALUE;
            curve = LEVEL_8_CURVE;
            miniTargetScore = LEVEL_8_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_8_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_8_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level2"))
        {
            userMove = LEVEL_9_NUM_MOVES;
            moveValue = LEVEL_9_ONE_MOVE_VALUE;
            curve = LEVEL_9_CURVE;
            miniTargetScore = LEVEL_9_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_9_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_9_PLATINUM_STAR_TARGET;
        } else if (levelName.equals("Level10"))
        {
            userMove = LEVEL_10_NUM_MOVES;
            moveValue = LEVEL_10_ONE_MOVE_VALUE;
            curve = LEVEL_10_CURVE;
            miniTargetScore = LEVEL_10_BRONZE_STAR_TARGET;
            miniTargetScore1 = LEVEL_10_GOLD_STAR_TARGET;
            miniTargetScore2 = LEVEL_10_PLATINUM_STAR_TARGET;
        }
    }

    public int getMinimumScore()
    {
        if (userScore >= miniTargetScore)
        {
            return miniTargetScore1;
        } else if (userScore >= miniTargetScore1)
        {
            return miniTargetScore2;
        } else if (userScore >= miniTargetScore2)
        {
            return miniTargetScore2;
        } else
        {
            return miniTargetScore;
        }
    }

    public String getLevelName()
    {
        return levelName;
    }

    public int getScore()
    {
        return userScore;
    }

    public int getCounter()
    {
        return (userMove - counter);
    }

    public int calculateTileXInGrid(int column, int z)
    {
        int cellWidth = TILE_IMAGE_WIDTH;
        float leftEdge = miniGame.getBoundaryLeft();
        return (int) (leftEdge + (cellWidth * column) - (Z_TILE_OFFSET * z));
    }

    public int calculateTileYInGrid(int row, int z)
    {
        int cellHeight = TILE_IMAGE_HEIGHT;
        float topEdge = miniGame.getBoundaryTop();
        return (int) (topEdge + (cellHeight * row) - (Z_TILE_OFFSET * z));
    }
    /*
     * THIS METHOD CHANGES X PIXEL INTO
     * X-COORDIANTE
     */

    public int calculateGridCellColumn(int x)
    {
        float leftEdge = miniGame.getBoundaryLeft();
        x = (int) (x - leftEdge);
        return x / TILE_IMAGE_WIDTH;
    }
    /*
     * THIS METHOD CHANGES Y PIXEL INTO
     * Y-COORDINATE
     */

    public int calculateGridCellRow(int y)
    {
        float topEdge = miniGame.getBoundaryTop();
        y = (int) (y - topEdge);
        return y / TILE_IMAGE_HEIGHT;
    }
    /*
     * THIS METHOD SHOWS/HIDE THE TILES GRID
     */

    public void enableTiles(boolean enable)
    {
        // PUT ALL THE TILES IN ONE PLACE WHERE WE CAN PROCESS THEM TOGETHER
        moveAllTilesToStack();

        // GO THROUGH ALL OF THEM 
        for (ZombieCrushSagaTile tile : stackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : RedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : BlueStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : GreenStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : YellowStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : OrangeStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : PurpleStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : RedWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : BlueWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : GreenWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : YellowWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : OrangeWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : PurpleWrappedStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : GlobeStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
        for (ZombieCrushSagaTile tile : TestTileStackTiles)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            } else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
    }
    /*
     * THIS METHOD MOVES ALL THE TILE TO STACK
     */

    public void moveAllTilesToStack()
    {
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                ArrayList<ZombieCrushSagaTile> cellStack = tileGrid[i][j];
                moveTiles(cellStack, stackTiles);
            }
        }
    }

    private void moveTiles(ArrayList<ZombieCrushSagaTile> from, ArrayList<ZombieCrushSagaTile> to)
    {
        // GO THROUGH ALL THE TILES, TOP TO BOTTOM
        for (int i = from.size() - 1; i >= 0; i--)
        {
            ZombieCrushSagaTile tile = from.remove(i);

            // ONLY ADD IT IF IT'S NOT THERE ALREADY
            if (!to.contains(tile))
            {
                to.add(tile);
            }
        }
    }

    /**
     * This method attempts to select the selectTile argument. Note that this
     * may be the first or second selected tile. If a tile is already selected,
     * it will attempt to process a match/move.
     *
     * @param selectTile The tile to select.
     */
    public void selectTile(ZombieCrushSagaTile selectTile)
    {
//      IF IT'S ALREADY THE SELECTED TILE, DESELECT IT
        if (selectTile == selectedTileSecond)
        {
            selectedTileSecond = null;
            selectTile.setState(VISIBLE_STATE);
        } //      IF IT'S ALREADY THE SELECTED TILE, DESELECT IT
        else if (selectTile == selectedTileFirst)
        {
            selectedTileFirst = selectedTileSecond;
            selectedTileSecond = null;
            selectTile.setState(VISIBLE_STATE);
        } else
        {
//          CHECK TO SEE IF NOT MORE THEN TWO TILES ARE SELECTED 
            if ((selectedTileFirst != null && selectedTileSecond != null))
            {
                selectedTileFirst.setState(VISIBLE_STATE);
                selectedTileSecond.setState(VISIBLE_STATE);
                selectedTileFirst = null;
                selectedTileSecond = null;
                return;
            }
            // IF FIRST SELECTION IS MADE
            if (selectedTileFirst == null)
            {
                selectedTileFirst = selectTile;
                selectedTileFirst.setState(SELECTED_STATE);
            } else
            {
                //CHEKCING TO SEE IF THE TILE USER SELECTED IS NEXT TO
                //OUR FIRST SELECTED TILE
                checkIfSelectTile2Possible(selectedTileFirst, selectTile);

                //MAKE SURE NEW TILES CAN BE SELECTED
                selectedTileFirst = null;
                selectedTileSecond = null;
            }

        }
    }
    /*
     * Checking if Second tile is next to first tile
     */

    public void checkIfSelectTile2Possible(ZombieCrushSagaTile selectTile1, ZombieCrushSagaTile selectTile2)
    {
        //CHECKING IF THE SECOND TILE SELECTED IS IN THE LEFT OF FIRST SELECTED TILE
        if (selectTile1.getGridColumn() == selectTile2.getGridColumn() + 1
                && selectTile1.getGridRow() == selectTile2.getGridRow())
        {
            selectedTileSecond = selectTile2;
            selectTile2.setState(SELECTED_STATE);
        } //CHECKING IF THE SECOND TILE SELECTED IS IN THE RIGHT OF FIRST SELECTED TILE
        else if (selectTile1.getGridColumn() == selectTile2.getGridColumn() - 1
                && selectTile1.getGridRow() == selectTile2.getGridRow())
        {
            selectedTileSecond = selectTile2;
            selectTile2.setState(SELECTED_STATE);
        } //CHECKING IF THE SECOND TILE SELECTED IS UPPER OF FIRST SELECTED TILE
        else if (selectTile1.getGridColumn() == selectTile2.getGridColumn()
                && selectTile1.getGridRow() - 1 == selectTile2.getGridRow())
        {
            selectedTileSecond = selectTile2;
            selectTile2.setState(SELECTED_STATE);
        } //CHECKING IF THE SECOND TILE SELECTED IS BELLOW OF FIRST SELECTED TILE
        else if (selectTile1.getGridColumn() == selectTile2.getGridColumn()
                && selectTile1.getGridRow() + 1 == selectTile2.getGridRow())
        {
            selectedTileSecond = selectTile2;
            selectTile2.setState(SELECTED_STATE);
        } else
        {
            selectTile1.setState(VISIBLE_STATE);
            selectTile2.setState(VISIBLE_STATE);
            selectedTileFirst.setState(VISIBLE_STATE);
            selectedTileFirst = null;
            selectedTileSecond = null;
            return;
        }
        //ASSIGN THE MOVE CLASS THE ORIGINAL
        //FIRST SELECTION AND SECOND SELECTION
        //COLUMNS AND ROWS
        move.col1 = selectTile1.getGridColumn();
        move.row1 = selectTile1.getGridRow();
        move.col2 = selectTile2.getGridColumn();
        move.row2 = selectTile2.getGridRow();
        //MAKING THE TILES VISIBLE BACK AGAIN
        selectTile1.setState(VISIBLE_STATE);
        selectTile2.setState(VISIBLE_STATE);

        //SWAPPING THE TILES
        swapTiles();
        //CHECKING IF THE TILE WAS A CORRECT SWAP
        if (checkTheSwap(move.col1, move.row1) == false
                && checkTheSwap(move.col2, move.row2) == false
                && tileIsFiveMatchSpecialZombie(move.col1,move.row1) == false
                && tileIsFiveMatchSpecialZombie(move.col2,move.row2) == false)
        {
            badMove = true;
        } else
        {
            badMove = false;
            //SINCE WE SWAP THE TILES THEN WE ALSO WANT TO 
            //CHANGE OUR COLUMNS AND ROW IN MOVE CLASS
            swapMovePosition();
//            System.out.println("tile1: " + tileGrid[move.col1][move.row1].get(tileGrid[move.col1][move.row1].size() - 1).getSpriteType().getSpriteTypeID());
//            System.out.println("tile2: " + tileGrid[move.col2][move.row2].get(tileGrid[move.col2][move.row2].size() - 1).getSpriteType().getSpriteTypeID());
            if(tileIsFiveMatchSpecialZombie(move.col1, move.row1) == true &&tileIsFiveMatchSpecialZombie(move.col2, move.row2) == true)
            {
                this.clearGrid();
            }
            else if (tileIsFiveMatchSpecialZombie(move.col1, move.row1) == true)
            {
                executeGlobeTileZombieMatch(move.col1, move.row1, move.col2, move.row2);
            } else if (tileIsFiveMatchSpecialZombie(move.col2, move.row2) == true)
            {
                executeGlobeTileZombieMatch(move.col2, move.row2, move.col1, move.row1);
            } else
            {
                matchFound(move.col1, move.row1);
                if (specialZombieMatched == false)
                {
                    matchFound(move.col2, move.row2);
                } else
                {
                    specialZombieMatched = false;
                }
            }
            counter++;
//            checkForTheWin();
        }
    }

    public boolean tileIsLTSpeicialZombiematch(int col, int row)
    {
        ZombieCrushSagaTile tile = tileGrid[col][row].get(tileGrid[col][row].size() - 1);
        switch (tile.getSpriteType().getSpriteTypeID())
        {
            case "TILE_13":
                return true;
            case "TILE_14":
                return true;
            case "TILE_15":
                return true;
            case "TILE_16":
                return true;
            case "TILE_17":
                return true;
            case "TILE_18":
                return true;
        }
        return false;
    }

    public void executeLTSpecialCandyMatch(int col, int row)
    {
        int num = 0;
        if(col == 0 && row == 0)
        {
            if(!tileGrid[col][row].isEmpty())
                removingTile.add(tileGrid[col][row].remove(tileGrid[col][row].size()-1));
                if (checkIfJellyAtPosition(col, row))
                {
                    levelJellyGrid[col][row] = 0;
                    userScore += 1000;
                    addToRenderScore(1000,col,row,true);
                }
            if(!tileGrid[col][row+1].isEmpty())
                removingTile.add(tileGrid[col][row+1].remove(tileGrid[col][row+1].size()-1));
                if (checkIfJellyAtPosition(col, row+1))
                {
                    levelJellyGrid[col][row+1] = 0;
                    userScore += 1000;
                    addToRenderScore(1000,col,row+1,true);
                }
            if(!tileGrid[col+1][row+1].isEmpty())
                removingTile.add(tileGrid[col+1][row+1].remove(tileGrid[col+1][row+1].size()-1));
                if (checkIfJellyAtPosition(col+1, row+1))
                {
                    levelJellyGrid[col+1][row+1] = 0;
                    userScore += 1000;
                    addToRenderScore(1000,col+1,row+1,true);
                }
            if(!tileGrid[col+1][row].isEmpty())
                removingTile.add(tileGrid[col+1][row].remove(tileGrid[col+1][row].size()-1));
                if (checkIfJellyAtPosition(col+1, row))
                {
                    levelJellyGrid[col+1][row] = 0;
                    userScore += 1000;
                    addToRenderScore(1000,col+1,row,true);
                }
                userScore += ((1 * moveValue) + (num * curve));
            addToRenderScore(((1 * moveValue) + (4 * curve)),col,row,true);
        }
        else if(row == 0 && col != 0)
        {
            for (int i = col-1; i <= col + 1 && i < gridColumns; i++)
            {
                for (int j = row; j <= row + 1 && j < gridRows; j++)
                {
                    if (!tileGrid[i][j].isEmpty())
                    {
                        ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j];
                        removingTile.add(stack.remove(stack.size() - 1));
                        if (checkIfJellyAtPosition(i, j))
                        {
                            levelJellyGrid[i][j] = 0;
                            userScore += 1000;
                            addToRenderScore(1000,i,j,true);
                        }
                        num++;
                    }
                }
            }
            userScore += ((1 * moveValue) + (num * curve));
            addToRenderScore(((1 * moveValue) + (num * curve)),col,row,true);
        }
        else if(col == 0 && row != 0)
        {
            for (int i = col; i <= col + 1 && i < gridColumns; i++)
            {
                for (int j = row-1; j <= row + 1 && j < gridRows; j++)
                {
                    if (!tileGrid[i][j].isEmpty())
                    {
                        ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j];
                        removingTile.add(stack.remove(stack.size() - 1));
                        if (checkIfJellyAtPosition(i, j))
                        {
                            levelJellyGrid[i][j] = 0;
                            userScore += 1000;
                            addToRenderScore(1000,i,j,true);
                        }
                        num++;
                    }
                }
            }
            userScore += ((1 * moveValue) + (num * curve));
            addToRenderScore(((1 * moveValue) + (num * curve)),col,row,true);
        }
        else
        {
            for (int i = col - 1; i <= col + 1 && i < gridColumns; i++)
            {
                for (int j = row - 1; j <= row + 1 && j < gridRows; j++)
                {
                    if (!tileGrid[i][j].isEmpty())
                    {
                        ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j];
                        removingTile.add(stack.remove(stack.size() - 1));
                        if (checkIfJellyAtPosition(i, j))
                        {
                            levelJellyGrid[i][j] = 0;
                            userScore += 1000;
                            addToRenderScore(1000,i,j,true);
                        }
                        num++;
                    }
                }
            }
            userScore += ((1 * moveValue) + (num * curve));
            addToRenderScore(((1 * moveValue) + (num * curve)),col,row,true);
        }
        
        specialZombieMatched = true;
    }

    public boolean tileIsFourSpecialZombiematch(int col, int row)
    {
        ZombieCrushSagaTile tile = tileGrid[col][row].get(tileGrid[col][row].size() - 1);
        switch (tile.getSpriteType().getSpriteTypeID())
        {
            case "TILE_6":
                return true;
            case "TILE_7":
                return true;
            case "TILE_8":
                return true;
            case "TILE_9":
                return true;
            case "TILE_10":
                return true;
            case "TILE_11":
                return true;
        }
        return false;
    }

    public void executeFourSpecialCandyMatch(int col, int row, String direction)
    {
//        if(direction.equals("RIGHT"))
        {
            for (int i = 0; i < gridColumns; i++)
            {
                if (!tileGrid[i][row].isEmpty())
                {
                    ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][row];
                    removingTile.add(stack.remove(stack.size() - 1));
                    if (checkIfJellyAtPosition(i, row))
                    {
                        levelJellyGrid[i][row] = 0;
                        userScore += 1000;
                        addToRenderScore(1000,i,row,true);
                        
                    }
                }
            }
            userScore += ((1 * moveValue) + (gridColumns * curve));
            addToRenderScore(((1 * moveValue) + (gridColumns * curve)),col,row,true);
        }
//        else
//        {
//             for (int i = 0; i < gridRows; i++)
//            {
//                if (!tileGrid[col][i].isEmpty())
//                {
//                    
//                    ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][i];
//                    stack.remove(stack.size() - 1);
//                    if (checkIfJellyAtPosition(row, i))
//                    {
//                        levelJellyGrid[row][i] = 0;
//                    }
//                }
//            }
//             userScore += ((1 * moveValue) + (gridRows * curve));
//        }
        specialZombieMatched = true;
    }

    public boolean tileIsFiveMatchSpecialZombie(int col, int row)
    {
        ZombieCrushSagaTile tile = tileGrid[col][row].get(tileGrid[col][row].size() - 1);
        ZombieCrushSagaTile globe = GlobeStackTiles.get(GlobeStackTiles.size() - 1);
        if (tile.match(globe))
        {
            return true;
        }
        return false;
    }

    public void executeGlobeTileZombieMatch(int globeCol, int globeRow, int tileCol, int tileRow)
    {
        ZombieCrushSagaTile tile = tileGrid[tileCol][tileRow].get(tileGrid[tileCol][tileRow].size() - 1);
        ArrayList<ZombieCrushSagaTile> stacks = tileGrid[globeCol][globeRow];
        stacks.remove(stacks.size() - 1);
        String tileID = tile.getSpriteType().getSpriteTypeID();
        int num = 0;
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (!tileGrid[i][j].isEmpty())
                {
                    ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j];
                    ZombieCrushSagaTile testTile = stack.get(stack.size() - 1);
                    if (testTile.getSpriteType().getSpriteTypeID().equals(tileID))
                    {
                        removingTile.add(stack.remove(stack.size() - 1));
                        if (checkIfJellyAtPosition(i, j))
                        {
                            levelJellyGrid[i][j] = 0;
                            userScore += 1000;
                            addToRenderScore(1000,i,j,true);
                        }
                        num++;
                    }
                }
                addTiles();
            }
        }
        userScore += ((1 * moveValue) + (num * curve));
        addToRenderScore(((1 * moveValue) + (num * curve)),globeCol,globeRow,true);
    }

    public boolean checkIfAllJellyDestroyed()
    {
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (levelJellyGrid[i][j] != 0)
                {
                    return false;
                }
            }
        }

        return true;
    }
    /*
     * This Method swaps the zombie
     */

    public void swapTiles()
    {
        ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[move.col1][move.row1];
        ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[move.col2][move.row2];
        ZombieCrushSagaTile tile1 = stack1.remove(stack1.size() - 1);
        ZombieCrushSagaTile tile2 = stack2.remove(stack2.size() - 1);

        // PUT IT IN THE GRID
        tileGrid[move.col1][move.row1].add(tile2);
        tileGrid[move.col2][move.row2].add(tile1);
        tile2.setGridCell(move.col1, move.row1);
        tile1.setGridCell(move.col2, move.row2);

        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(move.col1, 0);
        float y = calculateTileYInGrid(move.row1, 0);
        tile2.setTarget(x, y);
        tile2.startMovingToTarget(swapTileVelocity);
        movingTiles.add(tile2);

        x = calculateTileXInGrid(move.col2, 0);
        y = calculateTileYInGrid(move.row2, 0);
        tile1.setTarget(x, y);
        tile1.startMovingToTarget(swapTileVelocity);
        movingTiles.add(tile1);
    }

    public void swapMovePosition()
    {
        int col = move.col1;
        int row = move.row1;
        move.col1 = move.col2;
        move.row1 = move.row2;
        move.col2 = col;
        move.row2 = row;
    }

    public boolean checkTheSwap(int col, int row)
    {
        int l;
        int r;
        int u;
        int d;

        l = checkLeftOfTile(col, row);
        r = checkRightOfTile(col, row);
        u = checkUpOfTile(col, row);
        d = checkDownOfTile(col, row);

        if (l == 1 && r == 1 && d == 2)
        {
            return true;
        } else if (u == 1 && l == 2 && d == 1)
        {
            return true;
        } else if (u == 1 && d == 1 && r == 2)
        {
            return true;
        } else if (u == 2 && l == 1 && r == 1)
        {
            return true;
        } else if (u == 2 && r == 2)
        {
            return true;
        } else if (u == 2 && l == 2)
        {
            return true;
        } else if (l == 2 && d == 2)
        {
            return true;
        } else if (r == 2 && d == 2)
        {
            return true;
        } else if (l >= 1 && r >= 1)
        {
            return true;
        } else if (u >= 1 && d >= 1)
        {
            return true;
        } else if (l >= 2)
        {
            return true;
        } else if (r >= 2)
        {
            return true;
        } else if (u >= 2)
        {
            return true;
        } else if (d >= 2)
        {
            return true;
        } else
        {
            return false;
        }
    }
    /*
     * This method trys all kind of possible ways that tile can be matched
     * it can be matched as
     * 3 in row
     * 3 in columns
     * T shape but it can be T-shape, rotated left, rotated rigt 
     * or flipped 
     * 
     * for this method we can use some pre-created method of own to figure
     * this out
     * 
     * checkLeftOfTile(int col, int row)
     * checkRightOfTile(int col, int row)
     * checkUpOfTile(int col, int row)
     * checkDownOfTile(int col, int row)
     */

    public void matchFound(int col, int row)
    {
        int col1 = col;
        int row1 = row;
        int Left, Right, Up, Down;
        //WE FIRST CHECK FOR FIRST TILE SELECTED
        {
            //WHEN WE RECIEVE THE COUNT FROM THIS METHODS
            //IT DOES NOT INCLUDE THE ORIGINAL TILE
            //SO IF METHOD RETURNS 2 THEN THERE ARE TOTAL OF 3 TILES MATCHING
            //INLCUDING OUR ORIGINAL TILE SO NOW WE HAVE A MATCH
            Left = checkLeftOfTile(col1, row1);
            Right = checkRightOfTile(col1, row1);
            Up = checkUpOfTile(col1, row1);
            Down = checkDownOfTile(col1, row1);
            ZombieCrushSagaTile tile = tileGrid[col1][row1].get(tileGrid[col1][row1].size() - 1);

            if (Left == 1 && Right == 1 && Down == 2)//CHECKS REGULAR T
            {
                processMove(col1, row1, Left + 1, "LEFT");
                processMove(col1 + 1, row1, Right, "RIGHT");
                processMove(col1, row1 + 1, Down, "DOWN");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Up == 1 && Left == 2 && Down == 1)//CHEKCING LEFT SIDE FLIPPED T
            {
                processMove(col1, row1 - 1, Up, "UP");
                processMove(col1, row1, Left + 1, "LEFT");
                processMove(col1, row1 + 1, Down, "DOWN");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Up == 1 && Down == 1 && Right == 2)//CHECKING RIGHT SIDE FLIPPED T
            {
                processMove(col1, row1 - 1, Up, "UP");
                processMove(col1, row1 + 1, Down, "DOWN");
                processMove(col1, row1, Right + 1, "RIGHT");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Up == 2 && Left == 1 && Right == 1)//CHECKING FOR UPSIDE DOWN T
            {
                processMove(col1, row1, Up + 1, "UP");
                processMove(col1 - 1, row1, Left, "LEFT");
                processMove(col1 + 1, row1, Right, "RIGHT");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Up == 2 && Right == 2)//CHECKING FOR L
            {
                processMove(col1, row1, Up + 1, "UP");
                processMove(col1 + 1, row1, Right, "RIGHT");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Up == 2 && Left == 2)//CHECKING L FLIPPED TO LEFT
            {
                processMove(col1, row1, Up + 1, "UP");
                processMove(col1 - 1, row1, Left, "LEFT");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Left == 2 && Down == 2)//CHECKING L FLIPPED TO LEFT UPSIDE DOWN
            {
                processMove(col1, row1, Left + 1, "LEFT");
                processMove(col1, row1 + 1, Down, "DOWN");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Right == 2 && Down == 2)//CHECKING L FLIPPED
            {
                processMove(col1, row1, Right + 1, "RIGHT");
                processMove(col1, row1 + 1, Down, "DOWN");
                addFiveMatchWrappedCandy(col1, row1, tile);
                userScore += ((1 * moveValue) + (3 * curve));
                addToRenderScore(((1 * moveValue) + (3 * curve)),col1,row1,true);

            } else if (Left >= 1 && Right >= 1)//checks if swaps in middle
            {
                processMove(col1, row1, Left + 1, "LEFT");
                processMove(col1 + 1, row1, Right, "RIGHT");

                if (Left + Right + 1 == 4)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Left + Right + 1 == 5)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }

            } else if (Up >= 1 && Down >= 1)//checks if swaps in middle
            {
                processMove(col1, row1 - 1, Up, "UP");
                processMove(col1, row1, Down + 1, "DOWN");
                if (Up + Down + 1 == 4)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Up + Down + 1 == 5)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }


            } //REMOVE LEFT
            else if (Left >= 2)
            {
                processMove(col1, row1, Left + 1, "LEFT");
                if (Left == 3)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Left == 4)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }


            } //REMOVE RIGHT
            else if (Right >= 2)
            {
                processMove(col1, row1, Right + 1, "RIGHT");
                if (Right == 3)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Right == 4)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }


            } //REMOVE UP
            else if (Up >= 2)
            {
                processMove(col1, row1, Up + 1, "UP");
                if (Up == 3)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Up == 4)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }


            } //REMOVE DOWN
            else if (Down >= 2)
            {
                processMove(col1, row1, Down + 1, "DOWN");
                if (Down == 3)
                {
                    addFourMatchSpecialCandy(col1, row1, tile);
                    userScore += ((1 * moveValue) + (1 * curve));
                    addToRenderScore(((1 * moveValue) + (1 * curve)),col1,row1,true);
                } else if (Down == 4)
                {
                    addFiveMatchSpecialCandy(col1, row1);
                    userScore += ((1 * moveValue) + (2 * curve));
                    addToRenderScore(((1 * moveValue) + (2 * curve)),col1,row1,true);
                } else
                {
                    userScore += ((1 * moveValue) + (0 * curve));
                    addToRenderScore(((1 * moveValue) + (0 * curve)),col1,row1,true);
                }


            }
//            addTiles();
        }
    }
    public void addToRenderScore(int score, int startX, int startY, boolean ShowOrHide)
    {
        float x = this.calculateTileXInGrid(startX, 0);
        float y = this.calculateTileYInGrid(startY,0);
        FloatingPointRender fpr = new FloatingPointRender(score,x,y,ShowOrHide);
        scoresToRender.add(fpr);
    }
    /*
     * ADD THE WRAPPED TILE WITH EVERY T, L, AND + MATCHES
     */

    public void addFiveMatchWrappedCandy(int col, int row, ZombieCrushSagaTile t)
    {
        if (!tileGrid[col][row].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][row];
            stack.remove(stack.size() - 1);
        }
        // TAKE THE TILE OUT OF THE STACK
        ZombieCrushSagaTile tile = null;
        switch (t.getSpriteType().getSpriteTypeID())
        {
            case "TILE_0":
                tile = GreenWrappedStackTiles.remove(GreenWrappedStackTiles.size() - 1);
                break;
            case "TILE_1":
                tile = BlueWrappedStackTiles.remove(BlueWrappedStackTiles.size() - 1);
                break;
            case "TILE_2":
                tile = RedWrappedStackTiles.remove(RedWrappedStackTiles.size() - 1);
                break;
            case "TILE_3":
                tile = YellowWrappedStackTiles.remove(YellowWrappedStackTiles.size() - 1);
                break;
            case "TILE_4":
                tile = PurpleWrappedStackTiles.remove(PurpleWrappedStackTiles.size() - 1);
                break;
            case "TILE_5":
                tile = OrangeWrappedStackTiles.remove(OrangeWrappedStackTiles.size() - 1);
                break;
        }
        // PUT IT IN THE GRID
        tileGrid[col][row].add(tile);
        tile.setGridCell(col, row);

        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(col, 0);
        float y = calculateTileYInGrid(row, 0);
        tile.setTarget(x, y);
        tile.startMovingToTarget(movingTileVelocity);
        movingTiles.add(tile);
    }
    /*
     * ADD THE BOMB TILE WITH EVERY 5 MATCHES
     */

    public void addFiveMatchSpecialCandy(int col, int row)
    {
        if (!tileGrid[col][row].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][row];
            stack.remove(stack.size() - 1);
        }
        ZombieCrushSagaTile tile = GlobeStackTiles.remove(GlobeStackTiles.size() - 1);
        // PUT IT IN THE GRID
        tileGrid[col][row].add(tile);
        tile.setGridCell(col, row);

        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(col, 0);
        float y = calculateTileYInGrid(row, 0);
        tile.setTarget(x, y);
        tile.startMovingToTarget(movingTileVelocity);
        movingTiles.add(tile);
    }
    /*
     * ADDS THE SPECIAL WRAP ZOMBIE FOR 4 MATCHES
     * */

    public void addFourMatchSpecialCandy(int col, int row, ZombieCrushSagaTile t)
    {
        if (!tileGrid[col][row].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][row];
            stack.remove(stack.size() - 1);
        }
        // TAKE THE TILE OUT OF THE STACK
        ZombieCrushSagaTile tile = null;
        switch (t.getSpriteType().getSpriteTypeID())
        {
            case "TILE_0":
                tile = GreenStackTiles.remove(GreenStackTiles.size() - 1);
                break;
            case "TILE_1":
                tile = BlueStackTiles.remove(BlueStackTiles.size() - 1);
                break;
            case "TILE_2":
                tile = RedStackTiles.remove(RedStackTiles.size() - 1);
                break;
            case "TILE_3":
                tile = YellowStackTiles.remove(YellowStackTiles.size() - 1);
                break;
            case "TILE_4":
                tile = PurpleStackTiles.remove(PurpleStackTiles.size() - 1);
                break;
            case "TILE_5":
                tile = OrangeStackTiles.remove(OrangeStackTiles.size() - 1);
                break;
        }
        // PUT IT IN THE GRID
        tileGrid[col][row].add(tile);
        tile.setGridCell(col, row);

        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(col, 0);
        float y = calculateTileYInGrid(row, 0);
        tile.setTarget(x, y);
        tile.startMovingToTarget(movingTileVelocity);
        movingTiles.add(tile);
    }
    public void removeTileOffTheScreen(ZombieCrushSagaTile tile)
    {
        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(1300, 0);
        float y = calculateTileYInGrid(tile.getGridRow(), 0);
        tile.setTarget(x, y);
        tile.startMovingToTarget(movingTileVelocity);
        movingTiles.add(tile);
    }
    /**
     * This method updates all the necessary state information to process the
     * move argument.
     *
     * This method will get col, row of tile x is number to tiles matched
     * direction is LEFT,RIGHT,UP,DOWN,UPDOWN,LEFTRIGHT
     *
     * @param move The move to make. Note that a move specifies the cell
     * locations for a match.
     */
    public void processMove(int col, int row, int x, String direction)
    {
        if ((x >= 1))
        {
//            addTiles();
            for (int i = 0; i < x; i++)
            {
                //THIS IS IF OUR MATCH IS ON LEFT
                if (direction.equals("LEFT"))
                {
                    if (!tileGrid[col - i][row].isEmpty())
                    {
                        if (this.tileIsFourSpecialZombiematch(col - i, row))
                        {
                            this.executeFourSpecialCandyMatch(col - i, row, "RIGHT");
                        } else if (tileIsLTSpeicialZombiematch(col - i, row))
                        {
                            executeLTSpecialCandyMatch(col - i, row);
                        } else
                        {
                            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col - i][row];
                            removingTile.add(stack1.remove(stack1.size() - 1));
                            if (checkIfJellyAtPosition(col - i, row))
                            {
                                levelJellyGrid[col - i][row] = 0;
                                userScore += 1000;
                                addToRenderScore(1000,col-i,row,true);
                            }
                        }
                    }
                } //THIS IS IF OUR MATCH IS ON RIGHT
                else if (direction.equals("RIGHT"))
                {
                    if (!tileGrid[col + i][row].isEmpty())
                    {
                        if (this.tileIsFourSpecialZombiematch(col + i, row))
                        {
                            this.executeFourSpecialCandyMatch(col + i, row, "RIGHT");
                        } else if (tileIsLTSpeicialZombiematch(col + i, row))
                        {
                            executeLTSpecialCandyMatch(col + i, row);
                        } else
                        {
                            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col + i][row];
                            removingTile.add(stack1.remove(stack1.size() - 1));
                            if (checkIfJellyAtPosition(col + i, row))
                            {
                                levelJellyGrid[col + i][row] = 0;
                                userScore += 1000;
                                addToRenderScore(1000,col+i,row,true);
                            }
                        }
                    }
                } //THIS IS IF OUR MATCH IS UP
                else if (direction.equals("UP"))
                {
                    if (!tileGrid[col][row - i].isEmpty())
                    {
                        if (this.tileIsFourSpecialZombiematch(col, row - i))
                        {
                            this.executeFourSpecialCandyMatch(col, row - i, "DOWN");
                        } else if (tileIsLTSpeicialZombiematch(col, row - i))
                        {
                            executeLTSpecialCandyMatch(col, row - i);
                        } else
                        {
                            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col][row - i];
                            removingTile.add(stack1.remove(stack1.size() - 1));
                            if (checkIfJellyAtPosition(col, row - i))
                            {
                                levelJellyGrid[col][row - i] = 0;
                                userScore += 1000;
                                addToRenderScore(1000,col,row-i,true);
                            }
                        }
                    }
                } //THIS IS IF OUR MATCH ID DOWN
                else if (direction.equals("DOWN"))
                {
                    if (!tileGrid[col][row + i].isEmpty())
                    {
                        if (this.tileIsFourSpecialZombiematch(col, row + i))
                        {
                            this.executeFourSpecialCandyMatch(col, row + i, "DOWN");
                        } else if (tileIsLTSpeicialZombiematch(col, row + i))
                        {
                            executeLTSpecialCandyMatch(col, row + i);
                        } else
                        {
                            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col][row + i];
                            removingTile.add(stack1.remove(stack1.size() - 1));
                            if (checkIfJellyAtPosition(col, row + i))
                            {
                                levelJellyGrid[col][row + i] = 0;
                                userScore += 1000;
                                addToRenderScore(1000,col,row+i,true);
                            }
                        }
                    }
                }
            }
//            addTiles();
        }
    }
    public void removeZombieFromGridProcessMove()
    {
        for(int i=0;i<removingTile.size();i++)
        {
            ZombieCrushSagaTile tile = removingTile.get(i);

            // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
            // OUT WHERE IT'S GOING AND GET IT MOVING
            tile.setTarget(1280, tile.getY());
            tile.startMovingToTarget(2);
            movingTiles.add(tile);
        }
    }

    public int checkLeftOfTile(int col, int row)
    {
        int col1 = col;
        int row1 = row;
        int count = 0;
        if (!tileGrid[col1][row1].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col1][row1];
            ZombieCrushSagaTile tile1 = stack1.get(stack1.size() - 1);
            //CHECKING THE RIGHT SIDE OF THE ZOMBIE FOR MATCH
            for (int i = col1 - 1; i >= 0; i--)
            {
                if (tileGrid[i][row1].isEmpty())
                {
                    break;
                } else
                {
                    ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[i][row1];
                    ZombieCrushSagaTile tile2 = stack2.get(stack2.size() - 1);
                    if (tile1.match(tile2))
                    {
                        count++;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int checkRightOfTile(int col, int row)
    {
        int col1 = col;
        int row1 = row;
        int count = 0;
        if (!tileGrid[col1][row1].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col1][row1];
            ZombieCrushSagaTile tile1 = stack1.get(stack1.size() - 1);
            //CHECKING THE RIGHT SIDE OF THE ZOMBIE FOR MATCH
            for (int i = col1 + 1; i < gridColumns; i++)
            {
                if (tileGrid[i][row1].isEmpty())
                {
                    break;
                } else
                {
                    ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[i][row1];
                    ZombieCrushSagaTile tile2 = stack2.get(stack2.size() - 1);
                    if (tile1.match(tile2))
                    {
                        count++;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int checkUpOfTile(int col, int row)
    {
        int col1 = col;
        int row1 = row;
        int count = 0;
        if (!tileGrid[col1][row1].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col1][row1];
            ZombieCrushSagaTile tile1 = stack1.get(stack1.size() - 1);
            //CHECKING THE RIGHT SIDE OF THE ZOMBIE FOR MATCH
            for (int i = row1 - 1; i >= 0; i--)
            {
                if (tileGrid[col1][i].isEmpty())
                {
                    break;
                } else
                {
                    ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[col1][i];
                    ZombieCrushSagaTile tile2 = stack2.get(stack2.size() - 1);
                    if (tile1.match(tile2))
                    {
                        count++;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        return count;
    }

    public int checkDownOfTile(int col, int row)
    {
        int col1 = col;
        int row1 = row;
        int count = 0;

        if (!tileGrid[col1][row1].isEmpty())
        {
            ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[col1][row1];
            ZombieCrushSagaTile tile1 = stack1.get(stack1.size() - 1);
            //CHECKING THE RIGHT SIDE OF THE ZOMBIE FOR MATCH
            for (int i = row1 + 1; i < gridRows; i++)
            {
                if (tileGrid[col1][i].isEmpty())
                {
                    break;
                } else
                {
                    ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[col1][i];
                    ZombieCrushSagaTile tile2 = stack2.get(stack2.size() - 1);
                    if (tile1.match(tile2))
                    {
                        count++;
                    } else
                    {
                        break;
                    }
                }
            }
        }
        return count;
    }
    /*
     * This method checks if there is any button click on grid 
     */

    public void setClickedTrue()
    {
        clicked = true;
    }

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y)
    {
//        System.out.println(currentLevel);
        // FIGURE OUT THE CELL IN THE GRID
        int col = calculateGridCellColumn(x);
        int row = calculateGridCellRow(y);
        if ((col < 0 || col > this.gridColumns) || (row < 0 || row > this.gridRows))
        {
            
        } else
        {
            ZombieSmasher zs = new ZombieSmasher(this.game);
            {
//                CHECK THE TOP OF THE STACK AT col, row
                ArrayList<ZombieCrushSagaTile> tileStack = tileGrid[col][row];
                if (tileStack.size() >= 0)
                {
                    // GET AND TRY TO SELECT THE TOP TILE IN THAT CELL, IF THERE IS ONE
                    ZombieCrushSagaTile testTile = tileStack.get(tileStack.size() - 1);
                    if (testTile.containsPoint(x, y))
                    {
                        if (clicked == true && smasherAvailable == true)
                        {
                            if (checkIfJellyAtPosition(col, row))
                            {
                                levelJellyGrid[col][row] = 0;
                                userScore += 1000;
                                addToRenderScore(1000,col,row,true);
                            }
                            
                            if (this.tileIsFourSpecialZombiematch(col, row))
                            {
                                this.executeFourSpecialCandyMatch(col, row, "RIGHT");
                                game.getCanvas().setCursor(Cursor.getDefaultCursor());
                                clicked = false;
                                smasherAvailable = false;
                                miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setState(INVISIBLE_STATE);
                                miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setEnabled(false);
                            } else if (tileIsLTSpeicialZombiematch(col, row))
                            {
                                executeLTSpecialCandyMatch(col, row);
                                game.getCanvas().setCursor(Cursor.getDefaultCursor());
                                clicked = false;
                                smasherAvailable = false;
                                miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setState(INVISIBLE_STATE);
                                miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setEnabled(false);
                            }
                            
                            tileStack.remove(tileStack.size() - 1);
                            userScore += ((1 * moveValue)/2 + (0 * curve));
                            addToRenderScore(1000,col,row,true);
                            game.getCanvas().setCursor(Cursor.getDefaultCursor());
                            clicked = false;
                            smasherAvailable = false;
                            miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setState(INVISIBLE_STATE);
                            miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setEnabled(false);
                        } else
                        {
                            selectTile(testTile);
                        }
                    }
                }
            }
        }
    }
    public void checkForTheWin()
    {
        if (checkIfAllJellyDestroyed() && userScore > miniTargetScore && counter >= userMove)
        {
            this.endGameAsWin();
        } else if (counter >= userMove)
        {
            this.endGameAsLoss();
        }
    }
    public void cheatKeyMakeGridFourZombie()
    {
        if (levelName.equals("Level1"))
        {
            tileGrid[2][2].remove(tileGrid[2][2].size() - 1);
            addTileForCheat(2, 2);
            tileGrid[3][2].remove(tileGrid[3][2].size() - 1);
            addTileForCheat(3, 2);
            tileGrid[4][1].remove(tileGrid[4][1].size() - 1);
            addTileForCheat(4, 1);
            tileGrid[5][2].remove(tileGrid[5][2].size() - 1);
            addTileForCheat(5, 2);
        } else
        {
            tileGrid[2][2].remove(tileGrid[2][2].size() - 1);
            addTileForCheat(2, 2);
            tileGrid[2][3].remove(tileGrid[2][3].size() - 1);
            addTileForCheat(2, 3);
            tileGrid[3][4].remove(tileGrid[3][4].size() - 1);
            addTileForCheat(3, 4);
            tileGrid[2][5].remove(tileGrid[2][5].size() - 1);
            addTileForCheat(2, 5);
        }
    }
    public void cheatKeyMakeGridFiveZomibe()
    {
        if(levelName.equals("Level1"))
        {
            tileGrid[0][0].remove(tileGrid[0][0].size() - 1);
            addTileForCheat(0,0);
            tileGrid[1][0].remove(tileGrid[1][0].size() - 1);
            addTileForCheat(1,0);
            tileGrid[2][1].remove(tileGrid[2][1].size() - 1);
            addTileForCheat(2,1);
            tileGrid[3][0].remove(tileGrid[3][0].size() - 1);
            addTileForCheat(3,0);
            tileGrid[4][0].remove(tileGrid[4][0].size() - 1);
            addTileForCheat(4,0);
        }
        else
        {
            tileGrid[0][2].remove(tileGrid[0][2].size() - 1);
            addTileForCheat(0,2);
            tileGrid[1][2].remove(tileGrid[1][2].size() - 1);
            addTileForCheat(1,2);
            tileGrid[2][3].remove(tileGrid[2][3].size() - 1);
            addTileForCheat(2,3);
            tileGrid[3][2].remove(tileGrid[3][2].size() - 1);
            addTileForCheat(3,2);
            tileGrid[4][2].remove(tileGrid[4][2].size() - 1);
            addTileForCheat(4,2);
        }
    }
    public void cheatKeyMakeGridTZomibe()
    {
        if(levelName.equals("Level1"))
        {
            tileGrid[0][0].remove(tileGrid[0][0].size() - 1);
            addTileForCheat(0,0);
            tileGrid[0][1].remove(tileGrid[0][1].size() - 1);
            addTileForCheat(0,1);
            tileGrid[0][3].remove(tileGrid[0][3].size() - 1);
            addTileForCheat(0,3);
            tileGrid[1][2].remove(tileGrid[1][2].size() - 1);
            addTileForCheat(1,2);
            tileGrid[2][2].remove(tileGrid[2][2].size() - 1);
            addTileForCheat(2,2);
        }
        else
        {
            tileGrid[2][3].remove(tileGrid[2][3].size() - 1);
            addTileForCheat(2,3);
            tileGrid[2][2].remove(tileGrid[2][2].size() - 1);
            addTileForCheat(2,2);
            tileGrid[2][5].remove(tileGrid[2][5].size() - 1);
            addTileForCheat(2,5);
            tileGrid[3][4].remove(tileGrid[3][4].size() - 1);
            addTileForCheat(3,4);
            tileGrid[4][4].remove(tileGrid[4][4].size() - 1);
            addTileForCheat(4,4);
        }
    }
    public void cheatKeyMakeGridLZomibe()
    {
        if(levelName.equals("Level1"))
        {
            tileGrid[0][1].remove(tileGrid[0][1].size() - 1);
            addTileForCheat(0,1);
            tileGrid[1][0].remove(tileGrid[1][0].size() - 1);
            addTileForCheat(1,0);
            tileGrid[2][1].remove(tileGrid[2][1].size() - 1);
            addTileForCheat(2,1);
            tileGrid[1][2].remove(tileGrid[1][2].size() - 1);
            addTileForCheat(1,2);
            tileGrid[1][3].remove(tileGrid[1][3].size() - 1);
            addTileForCheat(1,3);
        }
        else
        {
            tileGrid[2][3].remove(tileGrid[2][3].size() - 1);
            addTileForCheat(2,3);
            tileGrid[3][2].remove(tileGrid[3][2].size() - 1);
            addTileForCheat(3,2);
            tileGrid[4][3].remove(tileGrid[4][3].size() - 1);
            addTileForCheat(4,3);
            tileGrid[3][4].remove(tileGrid[3][4].size() - 1);
            addTileForCheat(3,4);
            tileGrid[3][5].remove(tileGrid[3][5].size() - 1);
            addTileForCheat(3,5);
        }
    }
    public void addTileForCheat(int col, int row)
    {
        // TAKE THE TILE OUT OF THE STACK
        ZombieCrushSagaTile tile = TestTileStackTiles.remove(TestTileStackTiles.size() - 1);

        // PUT IT IN THE GRID
        tileGrid[col][row].add(tile);
        tile.setGridCell(col, row);
//        tile.setState(VISIBLE_STATE);
//        // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
//        // OUT WHERE IT'S GOING AND GET IT MOVING
        float x = calculateTileXInGrid(col, 0);
        float y = calculateTileYInGrid(row, 0);
        tile.setTarget(x, y);
        tile.startMovingToTarget(MAX_TILE_VELOCITY);
        movingTiles.add(tile);
    }

    @Override
    public void endGameAsLoss()
    {
        super.endGameAsLoss();
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        miniGame.getGUIDialogs().get(LOSS_DIALOG_TYPE).setState(VISIBLE_STATE);

        miniGame.getGUIButtons().get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
        miniGame.getGUIButtons().get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setEnabled(true);

        counter = 0;
        
        this.moveAllTilesToStack();
        this.clearGrid();
    }

    @Override
    public void endGameAsWin()
    {
        //UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();
        this.moveAllTilesToStack();
        this.clearGrid();
        //RECORD IT AS A WIN
        ((ZombieCrushSagaMiniGame) miniGame).getPlayerRecord().addDataTOLevelRecord(currentLevel, userScore);
        ((ZombieCrushSagaMiniGame) miniGame).savePlayerRecord();
        miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setState(INVISIBLE_STATE);
        miniGame.getGUIButtons().get(ZOMBIE_SMASHER_TYPE).setEnabled(false);
        miniGame.getGUIDialogs().get(WIN_DIALOG_TYPE).setState(VISIBLE_STATE);
        miniGame.getGUIDialogs().get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
//        this.reset(miniGame);
        counter = 0;
        
        
//        LevelHandler lh = new LevelHandler((ZombieCrushSagaMiniGame) miniGame);
//        lh.LevelHandlerSwithToLevelScoreScreen(currentLevel);
    }

    @Override
    public void reset(MiniGame game)
    {
        // PUT ALL THE TILES IN ONE PLACE AND MAKE THEM VISIBLE
        moveAllTilesToStack();
        for (ZombieCrushSagaTile tile : stackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : RedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : BlueStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : GreenStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : YellowStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : OrangeStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : PurpleStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : RedWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : BlueWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : GreenWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : YellowWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : OrangeWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : PurpleWrappedStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : GlobeStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }
        for (ZombieCrushSagaTile tile : TestTileStackTiles)
        {
            tile.setX(TILE_STACK_X);
            tile.setY(TILE_STACK_Y);
            tile.setState(VISIBLE_STATE);
        }

        // RANDOMLY ORDER THEM
        Collections.shuffle(stackTiles);

        // START THE CLOCK
        startTime = new GregorianCalendar();
        System.out.println(stackTiles.size());
        // NOW LET'S REMOVE THEM FROM THE STACK
        // AND PUT THE TILES IN THE GRID        
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                for (int k = 0; k < levelGrid[i][j]; k++)
                {
                    // TAKE THE TILE OUT OF THE STACK
                    ZombieCrushSagaTile tile = stackTiles.remove(stackTiles.size() - 1);

                    // PUT IT IN THE GRID
                    tileGrid[i][j].add(tile);
                    tile.setGridCell(i, j);

                    // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
                    // OUT WHERE IT'S GOING AND GET IT MOVING
                    float x = calculateTileXInGrid(i, k);
                    float y = calculateTileYInGrid(j, k);
                    tile.setTarget(x, y);
                    tile.startMovingToTarget(MAX_TILE_VELOCITY);
                    movingTiles.add(tile);
                }
            }
        }
        // AND START ALL UPDATES
        beginGame();
        setUpScoring();
    }

    public void clearGrid()
    {
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (tileGrid[i][j].isEmpty())
                {
                } else
                {
                    ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j];
                    ZombieCrushSagaTile tile = stack.remove(stack.size() - 1);
                }
            }
        }
    }
    public FloatingPointRender getscoresToRender(int i)
    {
        return scoresToRender.get(i);
    }
    public int getscoresToRenderSize()
    {
        return scoresToRender.size();
    }
    @Override
    public void updateAll(MiniGame game)
    {
        // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
        try
        {
            game.beginUsingData();

            removeZombieFromGridProcessMove();
            makeGridHasOneZomibe();
            for(int i=0;i<scoresToRender.size();i++)
            {
                FloatingPointRender fp = scoresToRender.get(i);
                
                fp.update();
                
                if(!fp.isMovingToTarget())
                    scoresToRender.remove(fp);
                
            }
            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingTiles.size(); i++)
            {
                // GET THE NEXT TILE
                ZombieCrushSagaTile tile = movingTiles.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);

                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingTiles.remove(tile);
                    if(removingTile.contains(tile))
                    {
                        removingTile.remove(tile);
                        tile.setX(0);
                        tile.setY(-100);
                        stackTiles.add(tile);
                        Collections.shuffle(stackTiles);
                    }
                    System.out.println("stack size: "+stackTiles.size());
                        
                }
            }
            if (badMove == true)
            {
                ZombieCrushSagaTile tiles1 = tileGrid[move.col1][move.row1].get(tileGrid[move.col1][move.row1].size() - 1);
                ZombieCrushSagaTile tiles2 = tileGrid[move.col1][move.row1].get(tileGrid[move.col1][move.row1].size() - 1);
                if (movingTiles.contains(tiles1) == false && movingTiles.contains(tiles2) == false)
                {
                    ArrayList<ZombieCrushSagaTile> stack1 = tileGrid[move.col1][move.row1];
                    ArrayList<ZombieCrushSagaTile> stack2 = tileGrid[move.col2][move.row2];
                    ZombieCrushSagaTile tile1 = stack1.remove(stack1.size() - 1);
                    ZombieCrushSagaTile tile2 = stack2.remove(stack2.size() - 1);

                    // PUT IT IN THE GRID
                    tileGrid[move.col1][move.row1].add(tile2);
                    tileGrid[move.col2][move.row2].add(tile1);
                    tile2.setGridCell(move.col1, move.row1);
                    tile1.setGridCell(move.col2, move.row2);

                    // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
                    // OUT WHERE IT'S GOING AND GET IT MOVING
                    float x = calculateTileXInGrid(move.col1, 0);
                    float y = calculateTileYInGrid(move.row1, 0);
                    tile2.setTarget(x, y);
                    tile2.startMovingToTarget(swapTileVelocity);
                    movingTiles.add(tile2);

                    x = calculateTileXInGrid(move.col2, 0);
                    y = calculateTileYInGrid(move.row2, 0);
                    tile1.setTarget(x, y);
                    tile1.startMovingToTarget(swapTileVelocity);
                    movingTiles.add(tile1);
                    badMove = false;
                }
            }
            if (this.inProgress())
            {
                addTiles();
            }
            if (movingTiles.isEmpty() && removingTile.isEmpty())
            {
                checkForMatchesOnGrid();
                if(!keepCheckingDown()&&!keepCheckingRight() && movingTiles.isEmpty() && removingTile.isEmpty())
                {
                    checkForTheWin();
                    if (this.won()||this.lost())
                    {
                        moveAllTilesToStack();
                    }
                }
 
            }

            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
            if (inProgress())
            {
                // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                endTime = new GregorianCalendar();
            }
        } finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    @Override
    public void updateDebugText(MiniGame game)
    {
    }

    public boolean keepCheckingDown()
    {
        int down;
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (tileGrid[i][j].isEmpty())
                {
                } else
                {
                    down = checkDownOfTile(i, j);
                    if (down >= 2)
                    {
                        return true;
                    }
                }
//                down = checkDownOfTile(i, j);
//                if (tileGrid[i][j].isEmpty())
//                {
//                    //keep checking if it's empty
//                } else
//                {
//                    if (down >= 2)
//                    {
//                        return true;
//                    }
//                }

            }
        }
        return false;
    }

    public boolean keepCheckingRight()
    {
        int right;
        for (int j = 0; j < gridRows; j++)
        {
            for (int i = 0; i < gridColumns; i++)
            {
                if (tileGrid[i][j].isEmpty())
                {
                } else
                {
                    right = checkRightOfTile(i, j);
                    if (right >= 2)
                    {
                        return true;
                    }
                }
//                right = checkDownOfTile(c, r);
//                if (tileGrid[c][r].isEmpty())
//                {
//                    //keep checking if it's empty
//                } else
//                {
//                    if (right >= 2)
//                    {
//                        return true;
//                    }
//                }

            }
        }
        return false;
    }

    public void checkForMatchesOnGrid()
    {
        int down;
        int right;
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (!tileGrid[i][j].isEmpty())
                {
                    right = checkRightOfTile(i, j);
//                    ZombieCrushSagaTile tile = tileGrid[i][j].get(tileGrid[i][j].size()-1);
                    if (right >= 2)
                    {
                        this.matchFound(i, j);
//                        processMove(i, j, right+1, "RIGHT");
//                        userScore += ((1 * moveValue) + (0 * curve));
                        return;
                    }
//                    else if(right == 3)
//                    {
//                        processMove(i,j,right+1,"RIGHT");
//                        this.addFourMatchSpecialCandy(i,j, tile);
//                    }
                }
            }
        }
        for (int i = 0; i < gridRows; i++)
        {
            for (int j = 0; j < gridColumns; j++)
            {
                if (!tileGrid[j][i].isEmpty())
                {
                    down = checkDownOfTile(j, i);
//                    ZombieCrushSagaTile tile = tileGrid[i][j].get(tileGrid[i][j].size()-1);
                    if (down >= 2)
                    {
                        this.matchFound(j, i);
//                        processMove(j, i, down+1, "DOWN");
//                        userScore += ((1 * moveValue) + (0 * curve));
                        return;
                    }
//                    else if(down == 3)
//                    {
//                        processMove(i,j,down+1,"DOWN");
//                        this.addFourMatchSpecialCandy(i,j, tile);
//                    }
                }
            }
        }
    }
    /*
     * this method drops the grid and adds new tiles
     */

    public void addTiles()
    {
        if (levelName.equals("Level2"))
        {
            this.level2AddTile();
        } else if (levelName.equals("Level3"))
        {
            level3AddTile();
        } else if (levelName.equals("Level4"))
        {
            level4AddTile();
        } else if (levelName.equals("Level5"))
        {
            level5AddTile();
        } else if (levelName.equals("Level7"))
        {
            level7AddTile();
        } else if (levelName.equals("Level8"))
        {
            level8AddTile();
        } else if (levelName.equals("Level9"))
        {
            level9AddTile();
        } else if (levelName.equals("Level10"))
        {
            level10AddTile();
        } else
        {
            addTileForBox(gridColumns, gridRows, 0, 0);
        }
    }

    public void moveStackTilePosition(int col)
    {
        int x = this.calculateTileXInGrid(col, 0);
        for (ZombieCrushSagaTile tile : stackTiles)
        {
            tile.setX(x);
            tile.setY(50);
            tile.setState(VISIBLE_STATE);
        }
    }

    public void level10AddTile()
    {
        addTileForBox(gridColumns, 3, 0, 0);
        for (int i = 1; i < gridColumns - 1; i++)
        {
            addSingleTileInMiddle(i, 3);
        }
        for (int i = 1; i < gridColumns - 1; i++)
        {
            addSingleTileInMiddle(i, 4);
        }
        addSingleTileInMiddleWiBlankAbove(0, 4);
        addSingleTileInMiddleWiBlankAbove(8, 4);
        addSingleTileInMiddleWiBlankAbove(4, 6);
        for (int i = 0; i < 4; i++)
        {
            for (int j = 5; j < gridRows; j++)
            {
                addSingleTileInMiddle(i, j);
            }
        }
        for (int i = 5; i < gridColumns; i++)
        {
            for (int j = 5; j < gridRows; j++)
            {
                addSingleTileInMiddle(i, j);
            }
        }
        addSingleTileInMiddle(4, 7);
        addSingleTileInMiddle(4, 8);
    }

    public void level9AddTile()
    {
        addTileForBox(gridColumns - 1, gridRows, 1, 0);
        addSingleTileAtTop(0, 2);
        addSingleTileAtTop(gridColumns - 1, 2);
        addSingleTileInMiddleWiBlankAbove(0, 4);
        addSingleTileInMiddleWiBlankAbove(gridColumns - 1, 4);
    }

    public void level8AddTile()
    {
        addTileForBox(4, 7, 0, 0);
        addTileForBox(5, 7, 4, 2);
        addTileForBox(9, 7, 5, 0);
        for (int i = 1; i < gridColumns - 1; i++)
        {
            addSingleTileInMiddle(i, 7);
        }
        for (int i = 2; i < gridColumns - 2; i++)
        {
            addSingleTileInMiddle(i, 8);
        }
    }

    public void level7AddTile()
    {
        addTileForBox(gridColumns, 8, 0, 0);
        for (int i = 1; i < gridColumns - 1; i++)
        {
            for (int j = gridRows - 1; j < gridRows; j++)
            {
                addSingleTileInMiddle(i, j);
            }
        }
    }

    public void level5AddTile()
    {
        addTileForBox(6, 1, 0, 0);
        addSingleTileInMiddle(0, 1);//0,1
        addSingleTileInMiddle(0, 2);//0,2
        addSingleTileInMiddle(5, 1);//0,1
        addSingleTileInMiddle(5, 2);//0,2
//        addTileForBox(5,3, 1, 2);
        addSingleTileInMiddleWiBlankAbove(1, 2);
        addSingleTileInMiddleWiBlankAbove(2, 2);
        addSingleTileInMiddleWiBlankAbove(3, 2);
        addSingleTileInMiddleWiBlankAbove(4, 2);
        addSingleTileInMiddleWiBlankAbove(0, 4);
        addSingleTileInMiddleWiBlankAbove(0, 6);
        addSingleTileInMiddleWiBlankAbove(0, 8);
        addSingleTileInMiddleWiBlankAbove(5, 4);
        addSingleTileInMiddleWiBlankAbove(5, 6);
        addSingleTileInMiddleWiBlankAbove(5, 8);
        for (int i = 1; i < gridColumns - 1; i++)
        {
            for (int j = 3; j < gridRows; j++)
            {
                addSingleTileInMiddle(i, j);
            }
        }
    }
    public void level4AddTile()
    {
        addTileForBox(7, gridRows, 2, 0);
        addTileForBox(gridColumns - 8, gridRows - 1, 0, 1);
        addTileForBox(gridColumns - 7, gridRows - 2, 1, 2);
        addTileForBox(gridColumns - 1, gridRows - 2, 7, 2);
        addTileForBox(gridColumns, gridRows - 1, 8, 1);
    }

    public void level3AddTile()
    {
        addTileForBox(gridColumns - 1, gridRows, 1, 0);
        addTileForBox(gridColumns - 6, gridRows - 1, 0, 1);
        addTileForBox(gridColumns, gridRows - 1, 6, 1);
    }

    public void level2AddTile()
    {
        addTileForBox(gridColumns - 1, gridRows, 1, 0);
        addTileForBox(gridColumns - 8, gridRows - 2, 0, 2);
        addTileForBox(gridColumns, gridRows - 2, 8, 2);
    }

    public void addSingleTileAtTop(int col, int row)
    {
        if (tileGrid[col][row].isEmpty())
        {
            // TAKE THE TILE OUT OF THE STACK
            ZombieCrushSagaTile tile = stackTiles.remove(stackTiles.size() - 1);
            // PUT IT IN THE GRID
            tileGrid[col][row].add(tile);
            tile.setGridCell(col, row);

            // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
            // OUT WHERE IT'S GOING AND GET IT MOVING
            float x = calculateTileXInGrid(col, 0);
            float y = calculateTileYInGrid(row, 0);
            tile.setTarget(x, y);
            tile.startMovingToTarget(movingTileVelocity);
            movingTiles.add(tile);
        }
    }
    public void addSingleTileInMiddle(int col, int row)
    {
        if (tileGrid[col][row].isEmpty())
        {
            // TAKE THE TILE OUT OF THE STACK
            ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][row - 1];
            ZombieCrushSagaTile tile = stack.remove(stack.size() - 1);
            // PUT IT IN THE GRID
            tileGrid[col][row].add(tile);
            tile.setGridCell(col, row);

            // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
            // OUT WHERE IT'S GOING AND GET IT MOVING
            float x = calculateTileXInGrid(col, 0);
            float y = calculateTileYInGrid(row, 0);
            tile.setTarget(x, y);
            tile.startMovingToTarget(movingTileVelocity);
            movingTiles.add(tile);
        }
    }

    public void addSingleTileInMiddleWiBlankAbove(int col, int row)
    {
        if (tileGrid[col][row].isEmpty())
        {
            // TAKE THE TILE OUT OF THE STACK
            ArrayList<ZombieCrushSagaTile> stack = tileGrid[col][row - 2];
            ZombieCrushSagaTile tile = stack.remove(stack.size() - 1);
            // PUT IT IN THE GRID
            tileGrid[col][row].add(tile);
            tile.setGridCell(col, row);

            // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
            // OUT WHERE IT'S GOING AND GET IT MOVING
            float x = calculateTileXInGrid(col, 0);
            float y = calculateTileYInGrid(row, 0);
            tile.setTarget(x, y);
            tile.startMovingToTarget(movingTileVelocity);
            movingTiles.add(tile);
        }
    }
    public void addTileForBox(int col, int row, int startI, int startJ)
    {
        for (int i = startI; i < col; i++)
        {
            for (int j = startJ; j < row; j++)
            {
                if (tileGrid[i][j].isEmpty() && j == startJ)
                {
                    // TAKE THE TILE OUT OF THE STACK
                    ZombieCrushSagaTile tile = stackTiles.remove(stackTiles.size() - 1);
                    // PUT IT IN THE GRID
                    tileGrid[i][j].add(tile);
                    tile.setGridCell(i, j);

                    // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
                    // OUT WHERE IT'S GOING AND GET IT MOVING
                    float x = calculateTileXInGrid(i, 0);
                    float y = calculateTileYInGrid(j, 0);
                    tile.setTarget(x, y);
                    tile.startMovingToTarget(movingTileVelocity);
                    movingTiles.add(tile);
                } else if (tileGrid[i][j].isEmpty() && j != startJ)
                {
                    // TAKE THE TILE OUT OF THE STACK
                    ArrayList<ZombieCrushSagaTile> stack = tileGrid[i][j - 1];
                    ZombieCrushSagaTile tile = stack.remove(stack.size() - 1);
                    // PUT IT IN THE GRID
                    tileGrid[i][j].add(tile);
                    tile.setGridCell(i, j);

                    // WE'LL ANIMATE IT GOING TO THE GRID, SO FIGURE
                    // OUT WHERE IT'S GOING AND GET IT MOVING
                    float x = calculateTileXInGrid(i, 0);
                    float y = calculateTileYInGrid(j, 0);
                    tile.setTarget(x, y);
                    tile.startMovingToTarget(movingTileVelocity);
                    movingTiles.add(tile);
                }
            }
        }
    }

    public void makeGridHasOneZomibe()
    {
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if (!tileGrid[i][j].isEmpty())
                {
                    if (tileGrid[i][j].size() > 1)
                    {
                        for (int k = tileGrid[i][j].size(); k > 1; k--)
                        {
                            tileGrid[i][j].remove(tileGrid[i][j].size() - 1);
                        }
                    }
                }
            }
        }
    }
}