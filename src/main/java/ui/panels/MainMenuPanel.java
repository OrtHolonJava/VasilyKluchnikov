package ui.panels;

import configurationReaders.SettingsConfigurationReader;
import ui.ChessFrame;
import ui.ImageLabel;
import ui.buttons.MenuButton;
import utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The main menu panel
 */
public class MainMenuPanel extends JPanel
{
    public static final Color BACKGROUND_COLOR = new Color(122, 145, 182);

    private static final String LOGO_IMAGE_PATH = ChessFrame.WORKING_DIRECTORY_PATH + "\\src\\main\\resources\\logos\\logo.png";
    private static final int NUMBER_OF_BUTTONS = 4;

    private static final double MAIN_PANEL_WIDTH_RATIO = 0.2;
    private static final double MAIN_PANEL_HEIGHT_RATIO = 0.6;

    private static final double LOGO_IMAGE_WIDTH_RATIO = 0.3;
    private static final double LOGO_IMAGE_HEIGHT_RATIO = 0.5;

    private static final double VERTICAL_FREE_SPACE_RATIO = 0.1;

    private ChessFrame chessFrameContainer;
    private JPanel mainPanel;
    private ImageLabel logoImage;

    public MainMenuPanel(ChessFrame chessFrameContainer)
    {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOR);
        setChessFrameContainer(chessFrameContainer);
        setSize(SettingsConfigurationReader.getAppResolution());
        initializeMenuUI();
    }

    /*
       Updates the size of the panel based on current resolution setting
    */
    public void updateSize()
    {
        setSize(SettingsConfigurationReader.getAppResolution());
        removeAll();
        initializeMenuUI();
    }

    /*
        Initializes all of the menus UI
     */
    private void initializeMenuUI()
    {
        initializeLogo();
        initializeMainPanel();
        initializeButtons();
        add(Box.createVerticalStrut(getVerticalFreeSpace()));
    }

    /*
        Initializes the games logo
     */
    private void initializeLogo()
    {
        BufferedImage logoImage = null;
        try
        {
            logoImage = ImageIO.read(new File(LOGO_IMAGE_PATH));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Failed to load the logo image");
            return;
        }
        setLogoImage(new ImageLabel(logoImage, getLogoSize()));
        getLogoImage().setPreferredSize(getLogoSize());
        getLogoImage().setMaximumSize(getLogoImage().getPreferredSize());
        getLogoImage().setAlignmentX(Component.CENTER_ALIGNMENT);
        add(getLogoImage());
    }

    /*
        Gets the logo size
     */
    private Dimension getLogoSize()
    {
        int width = (int)(getSize().getWidth() * LOGO_IMAGE_WIDTH_RATIO);
        int height = (int)(getSize().getHeight() * LOGO_IMAGE_HEIGHT_RATIO);
        return new Dimension(width, height);
    }

    /*
        Initializes the main panel
     */
    private void initializeMainPanel()
    {
        setMainPanel(new JPanel());
        scaleMainPanel();
        getMainPanel().setBackground(BACKGROUND_COLOR);
        getMainPanel().setAlignmentX(Component.CENTER_ALIGNMENT);
        add(getMainPanel());
    }

    /*
        Scales the main panel accordingly the the whole panel size
     */
    private void scaleMainPanel()
    {
        int width = (int)(getSize().getWidth() * MAIN_PANEL_WIDTH_RATIO);
        int height = (int)(getSize().getHeight() * MAIN_PANEL_HEIGHT_RATIO);
        getMainPanel().setSize(new Dimension(width, height));
        getMainPanel().setPreferredSize(new Dimension(width, height));

        GridLayout buttonsPanelLayout = new GridLayout(NUMBER_OF_BUTTONS, 1);
        buttonsPanelLayout.setVgap(getGapBetweenButtons());
        getMainPanel().setLayout(buttonsPanelLayout);
        getMainPanel().setMaximumSize(getMainPanel().getPreferredSize());
    }

    /*
        Initializes the buttons UI
     */
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

        for(MenuButton button : menuButtons)
        {
            getMainPanel().add(button);
        }
    }

    private int getVerticalFreeSpace()
    {
        return (int)(getSize().getHeight() * VERTICAL_FREE_SPACE_RATIO);
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

    private JPanel getMainPanel()
    {
        return mainPanel;
    }

    private void setMainPanel(JPanel mainPanel)
    {
        this.mainPanel = mainPanel;
    }

    private ImageLabel getLogoImage()
    {
        return logoImage;
    }

    private void setLogoImage(ImageLabel logoImage)
    {
        this.logoImage = logoImage;
    }
}
