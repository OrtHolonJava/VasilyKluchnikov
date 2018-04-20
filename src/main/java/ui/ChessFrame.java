package ui;

import configurationReaders.OptionsConfigurationReader;
import ui.panels.ChessGamePanel;
import ui.panels.GameOptionsPanel;
import ui.panels.MainMenuPanel;

import javax.swing.*;
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

    public ChessFrame()
    {
        setSize(OptionsConfigurationReader.getAppResolution());

        openMainMenu();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

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

    public void openSettings()
    {
        hideAllPanels();
    }

    public void quitApplication()
    {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

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
