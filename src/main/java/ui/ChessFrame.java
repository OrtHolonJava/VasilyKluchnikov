package ui;

import configurationReaders.SettingsConfigurationReader;
import ui.panels.ChessGamePanel;
import ui.panels.GameOptionsPanel;
import ui.panels.MainMenuPanel;
import ui.panels.SettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by divided on 17.04.2018.
 */
public class ChessFrame extends JFrame
{
    public static final String WORKING_DIRECTORY_PATH = System.getProperty("user.dir");
    public static final String CONFIG_DIRECTORY_PATH = WORKING_DIRECTORY_PATH + "\\src\\main\\resources\\configurations\\";

    private MainMenuPanel mainMenuPanel;
    private GameOptionsPanel gameOptionsPanel;
    private ChessGamePanel chessGamePanel;
    private SettingsPanel settingsPanel;

    public ChessFrame()
    {
        setSize(SettingsConfigurationReader.getAppResolution());
        setTitle("Classic chess");

        openMainMenu();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    /*
        Opens the main menu panel
     */
    public void openMainMenu()
    {
        hideAllPanels();

        if(getMainMenuPanel() == null)
        {
            MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
            setMainMenuPanel(mainMenuPanel);
            add(mainMenuPanel);
        }
        else
        {
            getMainMenuPanel().setVisible(true);
        }
    }

    /*
        Opens the chess game panel
     */
    public void openChessGame()
    {
        hideAllPanels();

        if(getChessGamePanel() == null)
        {
            ChessGamePanel chessGamePanel = new ChessGamePanel(this);
            setChessGamePanel(chessGamePanel);
            add(getChessGamePanel());
            chessGamePanel.startGame();
        }
        else
        {
            getChessGamePanel().setVisible(true);
            getChessGamePanel().startRematch();
        }
    }

    /*
        Opens the game options panel
     */
    public void openGameOptions()
    {
        hideAllPanels();

        if(getGameOptionsPanel() == null)
        {
            GameOptionsPanel gameOptionsPanel = new GameOptionsPanel(this);
            setGameOptionsPanel(gameOptionsPanel);
            add(getGameOptionsPanel());
        }
        else
        {
            getGameOptionsPanel().setVisible(true);
        }
    }

    /*
        Opens the settings panel
     */
    public void openSettings()
    {
        hideAllPanels();
        if(getSettingsPanel() == null)
        {
            SettingsPanel settingsPanel = new SettingsPanel(this);
            setSettingsPanel(settingsPanel);
            add(getSettingsPanel());
        }
        else
        {
            getSettingsPanel().setVisible(true);
        }
    }

    /*
        Updates the size of the frame based on new resolution, and all other panels too
     */
    public void updateResolution()
    {
        Dimension configResolution = SettingsConfigurationReader.getAppResolution();
        if(!configResolution.equals(getSize()))
        {
            setSize(configResolution);
            if(getChessGamePanel() != null)
            {
                getChessGamePanel().updateSize();
            }
            if(getMainMenuPanel() != null)
            {
                getMainMenuPanel().updateSize();
            }
            if(getGameOptionsPanel() != null)
            {
                getGameOptionsPanel().updateSize();
            }
            if(getSettingsPanel() != null)
            {
                getSettingsPanel().updateSize();
            }
        }
    }

    /*
        Quits the application
     */
    public void quitApplication()
    {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /*
        Hides all panels
     */
    private void hideAllPanels()
    {
        if(getMainMenuPanel() != null)
        {
            getMainMenuPanel().setVisible(false);
        }

        if(getGameOptionsPanel() != null)
        {
            getGameOptionsPanel().setVisible(false);
        }

        if(getChessGamePanel() != null)
        {
            getChessGamePanel().setVisible(false);
        }

        if(getSettingsPanel() != null)
        {
            getSettingsPanel().updateSelections();
            getSettingsPanel().setVisible(false);
        }
    }

    private SettingsPanel getSettingsPanel()
    {
        return settingsPanel;
    }

    private void setSettingsPanel(SettingsPanel settingsPanel)
    {
        this.settingsPanel = settingsPanel;
    }

    private GameOptionsPanel getGameOptionsPanel()
    {
        return gameOptionsPanel;
    }

    private void setGameOptionsPanel(GameOptionsPanel gameOptionsPanel)
    {
        this.gameOptionsPanel = gameOptionsPanel;
    }

    private MainMenuPanel getMainMenuPanel()
    {
        return mainMenuPanel;
    }

    private void setMainMenuPanel(MainMenuPanel mainMenuPanel)
    {
        this.mainMenuPanel = mainMenuPanel;
    }

    private ChessGamePanel getChessGamePanel()
    {
        return chessGamePanel;
    }

    private void setChessGamePanel(ChessGamePanel chessGamePanel)
    {
        this.chessGamePanel = chessGamePanel;
    }
}
