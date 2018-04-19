package ui;

import configuration.OptionsConfigurationReader;
import ui.panels.ChessGamePanel;
import ui.panels.MainMenuPanel;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by divided on 17.04.2018.
 */
public class ChessFrame extends JFrame
{
    public static final String DIR_PATH = System.getProperty("user.dir");

    private MainMenuPanel mainMenuPanel;
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
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        setMainMenuPanel(mainMenuPanel);
        add(mainMenuPanel);
    }

    public void openChessGame()
    {
        hideAllPanels();
        ChessGamePanel chessGamePanel = new ChessGamePanel(this);
        setChessGamePanel(chessGamePanel);
        add(getChessGamePanel());
        chessGamePanel.startGame();
    }

    public void openGameOptions()
    {
        hideAllPanels();
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
            setMainMenuPanel(null);
        }

        if(getChessGamePanel() != null)
        {
            getChessGamePanel().setVisible(false);
            setMainMenuPanel(null);
        }
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
