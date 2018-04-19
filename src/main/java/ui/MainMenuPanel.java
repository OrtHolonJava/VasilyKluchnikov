package ui;

import configuration.OptionsConfigurationReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by divided on 19.04.2018.
 */
public class MainMenuPanel extends JPanel
{
    private static final int NUMBER_OF_BUTTONS = 4;
    private static final Color BACKGROUND_COLOR = new Color(122, 145, 182);

    private ChessFrame chessFrameContainer;
    private JPanel buttonsPanel;

    public MainMenuPanel(ChessFrame chessFrameContainer)
    {
        super();
        setBackground(BACKGROUND_COLOR);
        setChessFrameContainer(chessFrameContainer);
        setSize(OptionsConfigurationReader.getAppResolution());
        initializeButtons();
    }

    private void initializeButtons()
    {

        Collection<MenuButton> menuButtons = new ArrayList<>();

        MenuButton playButton = new MenuButton("Play");
        MenuButton gameOptionsButton = new MenuButton("Game Options");
        MenuButton settingsButton = new MenuButton("Settings");
        MenuButton quitButton = new MenuButton("Quit");

        menuButtons.add(playButton);
        menuButtons.add(gameOptionsButton);
        menuButtons.add(settingsButton);
        menuButtons.add(quitButton);

        playButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            getChessFrameContainer().openChessGame();
        }
    });

        gameOptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getChessFrameContainer().openGameOptions();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getChessFrameContainer().openSettings();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getChessFrameContainer().quitApplication();
            }
        });


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        GridLayout buttonsPanelLayout = new GridLayout(NUMBER_OF_BUTTONS, 1);
        buttonsPanelLayout.setVgap(getGapBetweenButtons());
        buttonsPanel.setLayout(buttonsPanelLayout);

        for(MenuButton button : menuButtons)
        {
            button.setPreferredSize(getSizeOfButton());
            buttonsPanel.add(button);
        }

        setButtonsPanel(buttonsPanel);
        add(buttonsPanel, BorderLayout.CENTER);
    }

    private ChessFrame getChessFrameContainer()
    {
        return chessFrameContainer;
    }

    private void setChessFrameContainer(ChessFrame chessFrameContainer)
    {
        this.chessFrameContainer = chessFrameContainer;
    }

    private int getGapBetweenButtons()
    {
        return (int)getSize().getHeight() / (NUMBER_OF_BUTTONS * 5);
    }

    private Dimension getSizeOfButton()
    {
        int width = getWidth() / 8;
        int height = getHeight() / 16;
        return new Dimension(width, height);
    }

    private JPanel getButtonsPanel()
    {
        return buttonsPanel;
    }

    private void setButtonsPanel(JPanel buttonsPanel)
    {
        this.buttonsPanel = buttonsPanel;
    }
}
