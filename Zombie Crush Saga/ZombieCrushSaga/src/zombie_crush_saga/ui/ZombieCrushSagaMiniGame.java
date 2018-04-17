package zombie_crush_saga.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import zombie_crush_saga.ZombieCrushSaga.ZombieCrushSagaPropertyType;
import static zombie_crush_saga.ZombieCrushSagaConstants.*;
import zombie_crush_saga.data.ZombieCrushSagaDataModel;
import zombie_crush_saga.data.ZombieCrushSagaRecord;
import zombie_crush_saga.event.BackToLevelScreenHandler;
import zombie_crush_saga.event.ExitHandler;
import zombie_crush_saga.event.GamePlayHandler;
import zombie_crush_saga.event.LevelHandler;
import zombie_crush_saga.event.ResetGameHandler;
import zombie_crush_saga.event.ScrollUpDownHandler;
import zombie_crush_saga.event.TurnToGameScreenHandler;
import zombie_crush_saga.event.ZombieCrushSagaKeyHandler;
import zombie_crush_saga.event.ZombieSmasher;
import zombie_crush_saga.event.levelButtonShowHideHandler;
import zombie_crush_saga.file.ZombieCrushSagaFileManager;

/**
 *
 * @author Brijesh
 */
public class ZombieCrushSagaMiniGame extends MiniGame
{
    //HANDLES ERROR CONDITIONS

    private ZombieCrushSagaErrorHandler errorHandler;
    //MANAGES LOADING OF LEVELS AND THE PLAYE RECORDS FILE
    private ZombieCrushSagaFileManager fileManager;
    //THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYONG ONE SESSION
    private ZombieCrushSagaRecord record;
    //THE SCREEN CURRENTLY BEING PLAYED
    String currentScreenState;
    //THE CURRENT LEVEL IN PROGRESS
    String currentLevel;

    /*
     *ACCESSOR METHODS
     * 
     * getCurrentLevel
     * getErrorHandler
     * getPlayerRecord
     * getFileManager
     * isCurrentScreenState
     * 
     */
    /*
     * Accessor method for getting the current level in string
     * 
     */
    public String getCurrentLevel()
    {
        return currentLevel;
    }

    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public ZombieCrushSagaErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    /**
     * Accessor method for getting the player record object, which summarizes
     * the player's record on all levels.
     *
     * @return The player's complete record.
     */
    public ZombieCrushSagaRecord getPlayerRecord()
    {
        return record;
    }

    public void savePlayerRecord()
    {
        fileManager.saveRecord(record);
    }

    /**
     * Used for testing to see if the current screen state matches the
     * testScreenState argument. If it mates, true is returned, else false.
     *
     * @param testScreenState Screen state to test against the current state.
     *
     * @return true if the current state is testScreenState, false otherwise.
     */
    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }

    /**
     * Accessor method for getting the app's file manager.
     *
     * @return The file manager.
     */
    public ZombieCrushSagaFileManager getFileManager()
    {
        return fileManager;
    }

    /*
     * ALL SET METHODS FOR ZOMBIE CRUSH SAGA MINI GAME
     * 
     * setCurrentLevel
     */
    /*
     * accessor method that sets the current level to 
     * the level that recently clicked
     */
    public void setCurrentLevel(String levelName)
    {
        currentLevel = levelName;
    }

    /**
     * This method updates the game grid boundaries, which will depend on the
     * level loaded.
     */
    public void updateBoundaries()
    {
        float totalWidth = ((ZombieCrushSagaDataModel) data).getGridColumns() * TILE_IMAGE_WIDTH;
        float halfTotalWidth = totalWidth / 2.0f;
        float halfViewportWidth = data.getGameWidth() / 2.0f;
        boundaryLeft = halfViewportWidth - halfTotalWidth;

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        float topOffset = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_TOP_OFFSET.toString()));
        float totalHeight = ((ZombieCrushSagaDataModel) data).getGridRows() * TILE_IMAGE_HEIGHT;
        float halfTotalHeight = totalHeight / 2.0f;
        float halfViewportHeight = (data.getGameHeight() - topOffset) / 2.0f;
        boundaryTop = topOffset + halfViewportHeight - halfTotalHeight;
    }

    /*
     * ALL SCREEN SWTICH METHOD
     * 
     * switchToSplashScreen
     * swithToLevelScreen
     * swithToGameScreen
     * 
     */
    /**
     * This method switches the application to the splash screen, making all the
     * appropriate UI controls visible & invisible.
     */
    public void switchToSplashScreen()
    {
        currentScreenState = SPLASH_SCREEN_STATE;
//        hideStats();
    }

    /*
     * This method switches the application to the level screen, making 
     * all the appropriate UI controls visible & invisible.
     */
    public void swithToLevelScreen()
    {
        //CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCREEN_STATE);
        guiDecor.get(LEVEL_MAP_TYPE).setState(LEVEL_MAP_STATE);
        //MAKE THE CURRENT SCREEN THE LEVEL SCREEN
        currentScreenState = LEVEL_SCREEN_STATE;
        //DEACTIVATE THE SPLASH SCREEN BUTTONS
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setEnabled(false);
        //DEACTIVATE THE LEVEL SCORE SCREEN BUTTONS
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);

        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);
        //ACTIVATE THE ARROW UP AND ARROW DOWN BUTTONS
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setEnabled(true);
        levelButtonShowHideHandler lbshh = new levelButtonShowHideHandler(this);
        lbshh.showLevelButtons();

        //DEACTIVATE THE GAME SCREEN UI
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);

    }

    /*
     * This method switches the application to Level Score Screen, making
     * all the appropriate UI controls visible * invisible.
     */
    public void swithToLevelScoreScreen()
    {
        //CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SCORE_STATE);

        //hiding the level map digram since we have two images in level screen
        guiDecor.get(LEVEL_MAP_TYPE).setState(INVISIBLE_STATE);
        //DEACTIVATE THE SPLASH SCREEN BUTTONS
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setEnabled(false);

        //deactive the level screen buttons
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(ZOMBIE_SMASHER_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(ZOMBIE_SMASHER_TYPE).setEnabled(false);
                //MAKE THE CURRENT SCREEN STATE TO GAME SCREEN STATE
        currentScreenState = LEVEL_SCORE_STATE;
        //deactivate the level select buttons
        levelButtonShowHideHandler lbshh = new levelButtonShowHideHandler(this);
        lbshh.hideLevelButtons();
        //MAKING ALL THE GAME SCREEN UI VISIBLE
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setEnabled(false);
        guiDialogs.get(WIN_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDialogs.get(LOSS_DIALOG_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(LEVEL_PROGRESS_BAR_TYPE).setState(INVISIBLE_STATE);

        // HIDE THE TILES
        ((ZombieCrushSagaDataModel) data).enableTiles(false);


        //MAKE ALL THE LEVEL SCORE BUTTON VISIBLE
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(true);
    }

    /*
     * This method switches the application to Game Playing Screen, making
     * all the appropriate UI controls visible * invisible.
     */
    public void swithToGameScreen()
    {
        //CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        //MAKE THE CURRENT SCREEN STATE TO GAME SCREEN STATE
        currentScreenState = GAME_SCREEN_STATE;
        //DEACTIVATE THE SPLASH SCREEN BUTTONS
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setEnabled(false);

        //deactive the level screen buttons
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setEnabled(false);
        //deactivate the level select buttons
        //DEACTIVATE THE LEVEL SCORE SCREEN BUTTONS
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);
        //MAKING ALL THE GAME SCREEN UI VISIBLE
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setEnabled(true);
        guiDecor.get(LEVEL_PROGRESS_BAR_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(ZOMBIE_SMASHER_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(ZOMBIE_SMASHER_TYPE).setEnabled(true);
        // MOVE THE TILES TO THE STACK AND MAKE THEM VISIBLE
        ((ZombieCrushSagaDataModel) data).enableTiles(true);
        do
        {
            data.reset(this);
            if (((ZombieCrushSagaDataModel) data).keepCheckingDown() == true || ((ZombieCrushSagaDataModel) data).keepCheckingRight() == true)
            {
                ((ZombieCrushSagaDataModel) data).clearGrid();
//                  ((ZombieCrushSagaDataModel)data).moveAllTilesToStack();
            } else
            {
                break;
            }

        } while (true);
    }

    /*
     * ALL OTHER EXTRA NEED METHODS
     * ScrollUpDownWithMouseOver();
     * ScrollUp()
     * ScrollDown()
     */
    public void ScrollUpDownWithMouseOver()
    {
        //THIS WILL SCROLL LEVEL MAP WHEN MOUSE OVER OCCURS ON EITHER BUTTONS
        int speedup = -10;
        int speeddown = 10;
        Sprite buttonY = guiButtons.get(SCROLLUP_BUTTON_TYPE);
        float y = guiDecor.get(LEVEL_MAP_TYPE).getY();
        if (buttonY.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
        {
            if (guiDecor.get(LEVEL_MAP_TYPE).getY() >= -1080)
            {
                System.out.println("" + guiDecor.get(LEVEL_MAP_TYPE).getY());
                guiDecor.get(LEVEL_MAP_TYPE).setY(y + (speedup));
                y = guiButtons.get(LEVEL_1_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(y + (speedup));
                y = guiButtons.get(LEVEL_2_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(y + (speedup));
            }
        }
        Sprite buttonX = guiButtons.get(SCROLLDOWN_BUTTON_TYPE);
        float x = guiDecor.get(LEVEL_MAP_TYPE).getY();
        if (buttonX.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
        {
            if (guiDecor.get(LEVEL_MAP_TYPE).getY() != 0)
            {
                System.out.println("" + guiDecor.get(LEVEL_MAP_TYPE).getY());
                guiDecor.get(LEVEL_MAP_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_1_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_2_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(x + (speeddown));
            }
        }
    }

    //SCROLL LEVEL MAP UP WHEN BUTTON CLICKED
    public void ScrollUp()
    {
        int speedup = -80;
        Sprite buttonY = guiButtons.get(SCROLLUP_BUTTON_TYPE);
        float y = guiDecor.get(LEVEL_MAP_TYPE).getY();
        if (guiDecor.get(LEVEL_MAP_TYPE).getY() >= -1080)
        {
            System.out.println("" + guiDecor.get(LEVEL_MAP_TYPE).getY());
            guiDecor.get(LEVEL_MAP_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_1_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_2_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_3_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_4_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_5_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_6_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_7_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_8_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_9_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_10_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setY(y + (speedup));
            y = guiButtons.get(LEVEL_11_BUTTON_TYPE).getY();
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setY(y + (speedup));
        }
//                this.guiDecor.get(LEVEL_MAP_TYPE).update(this);
    }
    //SCROLL LEVEL MAP DOWN WHEN BUTTON CLICKED

    public void ScrollDown()
    {
        int speeddown = 80;
        Sprite buttonX = guiButtons.get(SCROLLDOWN_BUTTON_TYPE);
        float x = guiDecor.get(LEVEL_MAP_TYPE).getY();
        if (buttonX.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
        {
            if (guiDecor.get(LEVEL_MAP_TYPE).getY() != 0)
            {
                System.out.println("" + guiDecor.get(LEVEL_MAP_TYPE).getY());
                guiDecor.get(LEVEL_MAP_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_1_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_2_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_3_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_3_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_4_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_4_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_5_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_5_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_6_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_6_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_7_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_7_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_8_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_8_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_9_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_9_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_10_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_10_BUTTON_TYPE).setY(x + (speeddown));
                x = guiButtons.get(LEVEL_11_BUTTON_TYPE).getY();
                guiButtons.get(LEVEL_11_BUTTON_TYPE).setY(x + (speeddown));
            }
        }
//                this.guiDecor.get(LEVEL_MAP_TYPE).update(this);
    }
    /*
     * Override Methods for MiniGame
     * initAudioContent
     * initData
     * initGUIControls
     * initGUIHandlers
     * reset
     * updateGUI
     */

    @Override
    public void initAudioContent()
    {
        //WE HAVE NO AUDIO IN THIS GAME
    }

    @Override
    public void initData()
    {
        // INIT OUR ERROR HANDLER
        errorHandler = new ZombieCrushSagaErrorHandler(window);

        // INIT OUR FILE MANAGER
        fileManager = new ZombieCrushSagaFileManager(this);

//        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();

        // INIT OUR DATA MANAGER
        data = new ZombieCrushSagaDataModel(this);

        // LOAD THE GAME DIMENSIONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int gameWidth = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_WIDTH.toString()));
        int gameHeight = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_HEIGHT.toString()));
        data.setGameDimensions(gameWidth, gameHeight);

        // THIS WILL CHANGE WHEN WE LOAD A LEVEL
        boundaryLeft = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_LEFT_OFFSET.toString()));
        boundaryTop = Integer.parseInt(props.getProperty(ZombieCrushSagaPropertyType.GAME_TOP_OFFSET.toString()));
        boundaryRight = gameWidth - boundaryLeft;
        boundaryBottom = gameHeight;
    }

    @Override
    public void initGUIControls()
    {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(ZombieCrushSagaPropertyType.IMG_PATH);
        String windowIconFile = props.getProperty(ZombieCrushSagaPropertyType.WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new ZombieCrushSagaPanel(this, (ZombieCrushSagaDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = SPLASH_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(SPLASH_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCREEN_BLACK_IMAGE_NAME));
        sT.addState(LEVEL_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_IMAGE_NAME));
        sT.addState(LEVEL_SCORE_STATE, img);
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.GAME_BACKGROUND_IMAGE_NAME));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, SPLASH_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        //LOADING LEVEL MAP
        img = loadImage(imgPath + props.getProperty(ZombieCrushSagaPropertyType.LEVEL_MAP_IMAGE_NAME));
        sT = new SpriteType(LEVEL_MAP_TYPE);
        sT.addState(LEVEL_MAP_STATE, img);
        s = new Sprite(sT, 0, 0, 1800, 1280, INVISIBLE_STATE);
        guiDecor.put(LEVEL_MAP_TYPE, s);

        //PUT THE GAME PLAY BUTTON ON SPLASH SCREEN
        String gameplay_button = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMEPLAY_BUTTON_TYPE);
        img = loadImage(imgPath + gameplay_button);
        sT.addState(VISIBLE_STATE, img);
        String gameplay_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.GAMEPLAY_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + gameplay_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMEPLAY_BUTTON_X, GAMEPLAY_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(GAMEPLAY_BUTTON_TYPE, s);

        //PUT THE GAME RESET BUTTON ON SPLASH SCREEN
        String gamereset_button = props.getProperty(ZombieCrushSagaPropertyType.GAMERESET_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMERESET_BUTTON_TYPE);
        img = loadImage(imgPath + gamereset_button);
        sT.addState(VISIBLE_STATE, img);
        String gamereset_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.GAMERESET_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + gamereset_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMERESET_BUTTON_X, GAMERESET_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(GAMERESET_BUTTON_TYPE, s);

        //PUT THE GAME QUIT BUTTON ON SPLASH SCREEN
        String gamequit_button = props.getProperty(ZombieCrushSagaPropertyType.GAMEQUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAMEQUIT_BUTTON_TYPE);
        img = loadImage(imgPath + gamequit_button);
        sT.addState(VISIBLE_STATE, img);
        String gamequit_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.GAMEQUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + gamequit_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAMEQUIT_BUTTON_X, GAMEQUIT_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(GAMEQUIT_BUTTON_TYPE, s);


        //PUT THE SCROLL UP ARROUW ON LEVEL SCREEN
        String scrollUp_button = props.getProperty(ZombieCrushSagaPropertyType.ARROWUP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SCROLLUP_BUTTON_TYPE);
        img = loadImage(imgPath + scrollUp_button);
        sT.addState(VISIBLE_STATE, img);
        String scrollUp_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.ARROWUP_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + scrollUp_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SCROLLUP_BUTTON_X, SCROLLUP_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SCROLLUP_BUTTON_TYPE, s);

        //PUT THE SCROLL DOWN ARROUW ON LEVEL SCREEN
        String scrollDown = props.getProperty(ZombieCrushSagaPropertyType.ARROWDOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(SCROLLDOWN_BUTTON_TYPE);
        img = loadImage(imgPath + scrollDown);
        sT.addState(VISIBLE_STATE, img);
        String scrollDown_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.ARROWDOWN_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + scrollDown_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SCROLLDOWN_BUTTON_X, SCROLLDOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SCROLLDOWN_BUTTON_TYPE, s);

        //PUT THE QUIT BUTTON ON LEVEL SCREEN
        String lsQuit_button = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_MAP_QUIT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(LEVEL_MAP_QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + lsQuit_button);
        sT.addState(VISIBLE_STATE, img);
        String lsQuit_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_MAP_QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + lsQuit_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEVEL_MAP_QUIT_BUTTON_X, LEVEL_MAP_QUIT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_MAP_QUIT_BUTTON_TYPE, s);

        //PUT THE LEVEL SCORE QUIT BUTTONS
        String lscoQuit_button = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_QUIT_BUTTON_IMGAE_NAME);
        sT = new SpriteType(LEVEL_SCORE_QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + lscoQuit_button);
        sT.addState(VISIBLE_STATE, img);
        String lscoQuit_button_mouse0ver = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_QUIT_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + lscoQuit_button_mouse0ver);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_QUIT_BUTTON_X, LEVEL_SCORE_QUIT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_SCORE_QUIT_BUTTON_TYPE, s);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setEnabled(false);

        //LEVEL SCORE PLAY BUTTON
        String lscoplay_button = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_PLAY_BUTTON_IMGAE_NAME);
        sT = new SpriteType(LEVEL_SCORE_PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + lscoplay_button);
        sT.addState(VISIBLE_STATE, img);
        String lsoplay_button_mouseover = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_PLAY_BUTTON_MOUSE_OVER_IMGAE_NAME);
        img = loadImage(imgPath + lsoplay_button_mouseover);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_PLAY_BUTTON_X, LEVEL_SCORE_PLAY_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_SCORE_PLAY_BUTTON_TYPE, s);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setEnabled(false);

        //LEVEL SCORE STAR 1
        String star1 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_BRONZE_STAR);
        sT = new SpriteType(LEVEL_SCORE_BRONZE_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + star1, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_BRONZE_STAR_X, LEVEL_SCORE_BRONZE_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LEVEL_SCORE_BRONZE_STAR_TYPE, s);
        //LEVEL SCORE STAR 2
        String star2 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_GOLD_STAR);
        sT = new SpriteType(LEVEL_SCORE_GOLD_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + star2, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_GOLD_STAR_X, LEVEL_SCORE_GOLD_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LEVEL_SCORE_GOLD_STAR_TYPE, s);
        //LEVEL SCORE STAR 3
        String star3 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_SCORE_PLATINUM_STAR);
        sT = new SpriteType(LEVEL_SCORE_PLATINUM_STAR_TYPE);
        img = loadImageWithColorKey(imgPath + star3, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_SCORE_PLATINUM_STAR_X, LEVEL_SCORE_PLATINUM_STAR_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LEVEL_SCORE_PLATINUM_STAR_TYPE, s);

        //ADDING QUIT BUTTON TO GAME SCREEN AS WE PLAYING GAME
        String ls_gs_button = props.getProperty(ZombieCrushSagaPropertyType.END_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(GAME_SCREEN_END_GAME_BUTTON_TYPE);
        img = loadImage(imgPath + ls_gs_button);
        sT.addState(VISIBLE_STATE, img);
        String ls_gs_button1 = props.getProperty(ZombieCrushSagaPropertyType.END_GAME_BUTTON_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + ls_gs_button1);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, GAME_SCREEN_END_GAME_BUTTON_X, GAME_SCREEN_END_GAME_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(GAME_SCREEN_END_GAME_BUTTON_TYPE, s);

        //ADDING WIN DIALOG
        String win_dialog = props.getProperty(ZombieCrushSagaPropertyType.WIN_DIALOG_IMAGE_NAME);
        sT = new SpriteType(WIN_DIALOG_TYPE);
        img = loadImage(imgPath + win_dialog);
        sT.addState(VISIBLE_STATE, img);
        int x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        int y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(WIN_DIALOG_TYPE, s);

        //ADDING LOSS DIALOG
        String loss_dialog = props.getProperty(ZombieCrushSagaPropertyType.LOSS_DIALOG_IMAGE_NAME);
        sT = new SpriteType(LOSS_DIALOG_TYPE);
        img = loadImage(imgPath + loss_dialog);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LOSS_DIALOG_TYPE, s);

        //ADDING THE PROGRESS BAR
        String progressbar = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_PROGRESS_BAR_IMAGE_NAME);
        sT = new SpriteType(LEVEL_PROGRESS_BAR_TYPE);
        img = loadImage(imgPath + progressbar);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_PROGRESS_BAR_X, LEVEL_PROGRESS_BAR_Y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(LEVEL_PROGRESS_BAR_TYPE, s);
        
        String ZOMBIESMASHER = props.getProperty(ZombieCrushSagaPropertyType.ZOMBIE_SMASHER_IMAGE_NAME);
        sT = new SpriteType(ZOMBIE_SMASHER_TYPE);
        img = loadImageWithColorKey(imgPath + ZOMBIESMASHER,COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        ZOMBIESMASHER = props.getProperty(ZombieCrushSagaPropertyType.ZOMBIE_SMASHER_MOUSE_OVER_IMAGE_NAME);
        img = loadImage(imgPath + ZOMBIESMASHER);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, ZOMBIE_SMASHER_BUTTON_X, ZOMBIE_SMASHER_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(ZOMBIE_SMASHER_TYPE, s);


        //ADDING STATS BACKGROUND
//        String inGameStats = props.getProperty(ZombieCrushSagaPropertyType.IN_GAME_SCORE_BACKGROUND_IMAGE_NAME);
//        sT = new SpriteType(IN_GAME_SCORE_BACKGROUND_TYPE);
//        img = loadImage(imgPath + inGameStats);
//        sT.addState(VISIBLE_STATE, img);
//        s = new Sprite(sT, IN_GAME_SCORE_BACKGROUND_X, IN_GAME_SCORE_BACKGROUND_Y, 0,0,INVISIBLE_STATE);
//        guiDecor.put(IN_GAME_SCORE_BACKGROUND_TYPE, s);
////        
//        String inGameMoves = props.getProperty(ZombieCrushSagaPropertyType.IN_GAME_MOVE_BACKGROUND_IMAGE_NAME);
//        sT = new SpriteType(IN_GAME_MOVE_BACKGROUND_TYPE);
//        img = loadImage(imgPath + inGameMoves);
//        sT.addState(VISIBLE_STATE, img);
//        s = new Sprite(sT, IN_GAME_MOVE_BACKGROUND_X, IN_GAME_MOVE_BACKGROUND_Y, 0,0,INVISIBLE_STATE);
//        guiDecor.put(IN_GAME_MOVE_BACKGROUND_TYPE, s);
////        


//        //ADDING LEVEL 1 BUTTON
//        String l1 = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_1_BUTTON_IMAGE_NAME);
//        sT = new SpriteType(LEVEL_1_BUTTON_TYPE);
//        img = loadImage(imgPath + l1);
//        sT.addState(VISIBLE_STATE, img);
//        String l1mo = props.getProperty(ZombieCrushSagaPropertyType.LEVEL_1_BUTTON_MOUSE_OVER_IMAGE_NAME);
//        img = loadImage(imgPath + l1mo);
//        sT.addState(MOUSE_OVER_STATE, img);
//        s = new Sprite(sT, LEVEL_1_BUTTON_X,LEVEL_1_BUTTON_Y,0,0,INVISIBLE_STATE);
//        guiButtons.put(LEVEL_1_BUTTON_TYPE, s);

        //ADDING LEVEL BUTTONS TRHOW ARRAY
        ArrayList<String> levels = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_IMAGE_OPTIONS);
        ArrayList<String> levelMouseOverImageNames = props.getPropertyOptionsList(ZombieCrushSagaPropertyType.LEVEL_MOUSE_OVER_IMAGE_OPTIONS);
        //adding all the levels to map manually
        {
            //adding level 1
            sT = new SpriteType(LEVEL_1_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(0));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(0));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_1_BUTTON_X, LEVEL_1_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_1_BUTTON_TYPE, s);

            //adding level 2
            sT = new SpriteType(LEVEL_2_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(1));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(1));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_2_BUTTON_X, LEVEL_2_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_2_BUTTON_TYPE, s);

            //adding level 3
            sT = new SpriteType(LEVEL_3_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(2));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(2));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_3_BUTTON_X, LEVEL_3_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_3_BUTTON_TYPE, s);

            //adding level 4
            sT = new SpriteType(LEVEL_4_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(3));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(3));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_4_BUTTON_X, LEVEL_4_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_4_BUTTON_TYPE, s);

            //adding level 5
            sT = new SpriteType(LEVEL_5_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(4));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(4));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_5_BUTTON_X, LEVEL_5_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_5_BUTTON_TYPE, s);
            //adding level 6
            sT = new SpriteType(LEVEL_6_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(5));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(5));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_6_BUTTON_X, LEVEL_6_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_6_BUTTON_TYPE, s);
            //adding level 7
            sT = new SpriteType(LEVEL_7_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(6));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(6));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_7_BUTTON_X, LEVEL_7_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_7_BUTTON_TYPE, s);
            //adding level 8
            sT = new SpriteType(LEVEL_8_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(7));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(7));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_8_BUTTON_X, LEVEL_8_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_8_BUTTON_TYPE, s);
            //adding level 9
            sT = new SpriteType(LEVEL_9_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(8));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(8));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_9_BUTTON_X, LEVEL_9_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_9_BUTTON_TYPE, s);
            //adding level 10
            sT = new SpriteType(LEVEL_10_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(9));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(9));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_10_BUTTON_X, LEVEL_10_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_10_BUTTON_TYPE, s);
            //adding level 11
            sT = new SpriteType(LEVEL_11_BUTTON_TYPE);
            img = loadImage(imgPath + levelImageNames.get(10));
            sT.addState(VISIBLE_STATE, img);
            img = loadImage(imgPath + levelMouseOverImageNames.get(10));
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, LEVEL_11_BUTTON_X, LEVEL_11_BUTTON_Y, 0, 0, INVISIBLE_STATE);
            guiButtons.put(LEVEL_11_BUTTON_TYPE, s);

        }

        ((ZombieCrushSagaDataModel) data).initTiles();

    }

    @Override
    public void initGUIHandlers()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(ZombieCrushSagaPropertyType.DATA_PATH);

        ZombieCrushSagaKeyHandler kh = new ZombieCrushSagaKeyHandler(this);
        this.setKeyListener(kh);

        //WE'LL HAVE A CUSTOM RESPNSE FOR WHEN USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ExitHandler eh = new ExitHandler(this);

        //GAME QUIT EVENT HANDLER
        ExitHandler geh = new ExitHandler(this);
        guiButtons.get(GAMEQUIT_BUTTON_TYPE).setActionListener(geh);
        guiButtons.get(LEVEL_MAP_QUIT_BUTTON_TYPE).setActionListener(geh);

        //GAME PLAY EVENT HANDLER
        GamePlayHandler gp = new GamePlayHandler(this);
        guiButtons.get(GAMEPLAY_BUTTON_TYPE).setActionListener(gp);

        //GAME RESET EVENT HANDLER
        ResetGameHandler rgh = new ResetGameHandler(this);
        guiButtons.get(GAMERESET_BUTTON_TYPE).setActionListener(rgh);

        BackToLevelScreenHandler blsh = new BackToLevelScreenHandler(this);
        guiButtons.get(LEVEL_SCORE_QUIT_BUTTON_TYPE).setActionListener(blsh);

        TurnToGameScreenHandler tgsh = new TurnToGameScreenHandler(this);
        guiButtons.get(LEVEL_SCORE_PLAY_BUTTON_TYPE).setActionListener(tgsh);

        //THIS WILL MAKE LEVEL MAP SCROLL WHEN BUTTON CLICKED

        ScrollUpDownHandler sud = new ScrollUpDownHandler(this);
        guiButtons.get(SCROLLDOWN_BUTTON_TYPE).setActionListener(sud);
        guiButtons.get(SCROLLUP_BUTTON_TYPE).setActionListener(sud);

        ZombieSmasher zs = new ZombieSmasher(this);
        guiButtons.get(ZOMBIE_SMASHER_TYPE).setActionListener(zs);
        
        //LEVEL 1 BUTTON HANDLER
        LevelHandler lh = new LevelHandler(this);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setActionListener(lh);
        guiButtons.get(GAME_SCREEN_END_GAME_BUTTON_TYPE).setActionListener(lh);

    }

    @Override
    public void reset()
    {
        data.reset(this);
    }

    @Override
    public void updateGUI()
    {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(MOUSE_OVER_STATE);
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE))
            {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
//        ScrollUpDownWithMouseOver();
    }
}