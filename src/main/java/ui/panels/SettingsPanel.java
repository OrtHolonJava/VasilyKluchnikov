package ui.panels;

import configurationReaders.SettingsConfigurationReader;
import ui.ChessFrame;
import ui.buttons.ColorButton;
import ui.buttons.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by divided on 20.04.2018.
 */
public class SettingsPanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = MainMenuPanel.BACKGROUND_COLOR;
    private static final Font TEXT_FONT = new Font("Tahoma", Font.BOLD, 24);

    private static final String[] POSSIBLE_RESOLUTIONS = new String [] {"1024x576", "1280x720", "1366x768", "1600x900", "1920x1080"};

    private static final double OPTIONS_PANEL_WIDTH_RATIO = 0.8;
    private static final double OPTIONS_PANEL_HEIGHT_RATIO = 0.8;
    private static final double ELEMENTS_HORIZONTAL_GAP_RATIO = 0.06;
    private static final double OPTIONS_VERTICAL_GAP_RATIO = 0.03;

    private static final int NUMBER_OF_OPTION_PANEL_ROWS = 7;
    private static final int NUMBER_OF_RESOLUTION_SETTING_ELEMENTS = 2;
    private static final int NUMBER_OF_MENU_BUTTONS = 4;

    private ChessFrame chessFrameContainer;
    private JPanel optionsPanel;
    private JComboBox<String> resolutionSettingBox;
    private ColorButton lightColorButton, darkColorButton;

    public SettingsPanel(ChessFrame chessFrameContainer)
    {
        setChessFrameContainer(chessFrameContainer);
        setSize(SettingsConfigurationReader.getAppResolution());
        setBackground(BACKGROUND_COLOR);

        initializeSettingsUI();
    }

    /*
       Updates the size of the panel based on current resolution setting
    */
    public void updateSize()
    {
        setSize(SettingsConfigurationReader.getAppResolution());
        resizeOptionsPanel();
    }

    /*
        Updates current resolution and color selections, based on the settings config
     */
    public void updateSelections()
    {
        String currentResolutionString = dimensionToString(SettingsConfigurationReader.getAppResolution());
        for(String resolution : POSSIBLE_RESOLUTIONS)
        {
            if(resolution.equals(currentResolutionString))
            {
                getResolutionSettingBox().setSelectedItem(resolution);
            }
        }

        getLightColorButton().setBackground(SettingsConfigurationReader.getLightTileColor());
        getDarkColorButton().setBackground(SettingsConfigurationReader.getDarkTileColor());
    }

    /*
        Initializes settings UI
     */
    private void initializeSettingsUI()
    {
        initializeOptionsPanel();
        initializeResolutionSettings();
        initializeTileColorSettings();
        initializeButtons();
        updateSelections();
    }

    /*
        Initializes the buttons UI
     */
    private void initializeButtons()
    {
        JPanel buttonsPanel = new JPanel();
        GridLayout layout = new GridLayout(1, NUMBER_OF_MENU_BUTTONS);
        layout.setHgap(getElementHorizontalGap());
        buttonsPanel.setLayout(layout);
        buttonsPanel.setBackground(BACKGROUND_COLOR);

        MenuButton backButton = new MenuButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(hasUserChangedSettings() && userWantsToApplySettings())
                {
                    System.out.println("HOI");
                    updateSettingsConfig();
                    getChessFrameContainer().updateResolution();
                }
                getChessFrameContainer().openMainMenu();
            }
        });
        
        MenuButton resetButton = new MenuButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SettingsConfigurationReader.setToDefault();
                updateSelections();
            }
        });

        MenuButton applyButton = new MenuButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateSettingsConfig();
                getChessFrameContainer().updateResolution();
            }
        });

        backButton.setPreferredSize(new Dimension());
        backButton.setFont(TEXT_FONT);
        resetButton.setFont(TEXT_FONT);
        applyButton.setFont(TEXT_FONT);
        buttonsPanel.add(backButton);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(new JLabel());
        buttonsPanel.add(applyButton);

        getOptionsPanel().add(new JLabel());
        getOptionsPanel().add(buttonsPanel);
    }

    /*
        Checks by asking the user if he wants to apply settings
     */
    private boolean userWantsToApplySettings()
    {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Changes were made. Do you want to apply them?","Apply changes?", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    /*
        Updates the settings config based on current selections
     */
    private void updateSettingsConfig()
    {
        Dimension resolution = getDimensionFromResolutionString(getResolutionSettingBox().getSelectedItem().toString());
        String frameWidth = String.valueOf((int)(resolution.getWidth()));
        String frameHeight = String.valueOf((int)(resolution.getHeight()));
        String lightR = String.valueOf(getLightColorButton().getBackground().getRed());
        String lightG = String.valueOf(getLightColorButton().getBackground().getBlue());
        String lightB = String.valueOf(getLightColorButton().getBackground().getGreen());
        String darkR = String.valueOf(getDarkColorButton().getBackground().getRed());
        String darkG = String.valueOf(getDarkColorButton().getBackground().getGreen());
        String darkB = String.valueOf(getDarkColorButton().getBackground().getBlue());
        SettingsConfigurationReader.updateSettingsConfig(frameWidth, frameHeight, lightR, lightG, lightB, darkR, darkG, darkB);
    }

    /*
        Initializes the tile color settings UI
     */
    private void initializeTileColorSettings()
    {
        GridLayout layout = new GridLayout(1, NUMBER_OF_RESOLUTION_SETTING_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());

        JPanel lightColorPanel = new JPanel();
        JPanel darkColorPanel = new JPanel();
        lightColorPanel.setLayout(layout);
        darkColorPanel.setLayout(layout);
        lightColorPanel.setBackground(BACKGROUND_COLOR);
        darkColorPanel.setBackground(BACKGROUND_COLOR);

        JLabel lightColorText = new JLabel("Light color");
        JLabel darkColorText = new JLabel("Dark color");
        lightColorText.setFont(TEXT_FONT);
        darkColorText.setFont(TEXT_FONT);
        ColorButton lightColorButton = new ColorButton(SettingsConfigurationReader.getLightTileColor());
        ColorButton darkColorButton = new ColorButton(SettingsConfigurationReader.getDarkTileColor());

        setLightColorButton(lightColorButton);
        setDarkColorButton(darkColorButton);

        lightColorPanel.add(lightColorText);
        lightColorPanel.add(lightColorButton);
        darkColorPanel.add(darkColorText);
        darkColorPanel.add(darkColorButton);

        getOptionsPanel().add(lightColorPanel);
        getOptionsPanel().add(darkColorPanel);
    }

    /*
        Initializes the resolution settings UI
     */
    private void initializeResolutionSettings()
    {
        JPanel resolutionPanel = new JPanel();
        GridLayout layout = new GridLayout(1, NUMBER_OF_RESOLUTION_SETTING_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());
        resolutionPanel.setLayout(layout);
        resolutionPanel.setBackground(BACKGROUND_COLOR);

        JLabel resolutionText = new JLabel("Resolution");
        JComboBox<String> resolutionSettingBox = new JComboBox<String>(POSSIBLE_RESOLUTIONS);
        setResolutionSettingBox(resolutionSettingBox);

        String currentResolutionString = dimensionToString(SettingsConfigurationReader.getAppResolution());
        for(String resolution : POSSIBLE_RESOLUTIONS)
        {
            if(resolution.equals(currentResolutionString))
            {
                resolutionSettingBox.setSelectedItem(resolution);
            }
        }

        resolutionText.setBackground(BACKGROUND_COLOR);
        resolutionText.setFont(TEXT_FONT);
        resolutionSettingBox.setFont(TEXT_FONT);

        resolutionPanel.add(resolutionText);
        resolutionPanel.add(resolutionSettingBox);

        getOptionsPanel().add(resolutionPanel);
    }

    /*
        Initializes the main option panel which holds all of the settings (and the buttons)
     */
    private void initializeOptionsPanel()
    {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(BACKGROUND_COLOR);
        setOptionsPanel(optionsPanel);
        resizeOptionsPanel();

        GridLayout gridLayout = new GridLayout(NUMBER_OF_OPTION_PANEL_ROWS, 1);
        gridLayout.setVgap(getOptionsVerticalGap());
        optionsPanel.setLayout(gridLayout);


        getOptionsPanel().add(new JLabel());
        add(optionsPanel);
    }

    /*
        Resizes the main options panel
     */
    private void resizeOptionsPanel()
    {
        int width = (int)(getSize().getWidth() * OPTIONS_PANEL_WIDTH_RATIO);
        int height = (int)(getSize().getHeight() * OPTIONS_PANEL_HEIGHT_RATIO);
        optionsPanel.setSize(new Dimension(width, height));
        optionsPanel.setPreferredSize(new Dimension(width, height));
    }

    /*
        Checks whether the user has changed any of the settings
     */
    private boolean hasUserChangedSettings()
    {
        Dimension selectedResolution = getDimensionFromResolutionString(getResolutionSettingBox().getSelectedItem().toString());
        return !selectedResolution.equals(SettingsConfigurationReader.getAppResolution()) ||
                !getLightColorButton().getBackground().equals(SettingsConfigurationReader.getLightTileColor()) ||
                !getDarkColorButton().getBackground().equals(SettingsConfigurationReader.getDarkTileColor());
    }

    /*
        Converts resolution string (of the form WIDTHxHEIGHT) to the corresponding dimension/size
     */
    private Dimension getDimensionFromResolutionString(String resolutionString)
    {
        int indexOfSeparator = resolutionString.indexOf('x');
        int width;
        int height;
        try
        {
            width = Integer.parseInt(resolutionString.substring(0, indexOfSeparator));
            height = Integer.parseInt(resolutionString.substring(indexOfSeparator + 1));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            System.out.println("Error converting string resolution to integer");
            return null;
        }
        return new Dimension(width, height);
    }

    /*
        Converts dimension/size to a resolution string (of the form WIDTHxHEIGHT)
     */
    private String dimensionToString(Dimension dimension)
    {
        return (int)dimension.getWidth() + "x" + (int)dimension.getHeight();
    }

    private int getElementHorizontalGap()
    {
        return (int)(getSize().getWidth() * ELEMENTS_HORIZONTAL_GAP_RATIO);
    }

    private int getOptionsVerticalGap()
    {
        return (int)(getSize().getHeight() * OPTIONS_VERTICAL_GAP_RATIO);
    }

    private ColorButton getLightColorButton()
    {
        return lightColorButton;
    }

    private void setLightColorButton(ColorButton lightColorButton)
    {
        this.lightColorButton = lightColorButton;
    }

    private ColorButton getDarkColorButton()
    {
        return darkColorButton;
    }

    private void setDarkColorButton(ColorButton darkColorButton)
    {
        this.darkColorButton = darkColorButton;
    }

    private JComboBox<String> getResolutionSettingBox()
    {
        return resolutionSettingBox;
    }

    private void setResolutionSettingBox(JComboBox<String> resolutionSettingBox)
    {
        this.resolutionSettingBox = resolutionSettingBox;
    }

    private JPanel getOptionsPanel()
    {
        return optionsPanel;
    }

    private void setOptionsPanel(JPanel optionsPanel)
    {
        this.optionsPanel = optionsPanel;
    }

    private ChessFrame getChessFrameContainer()
    {
        return chessFrameContainer;
    }

    private void setChessFrameContainer(ChessFrame chessFrameContainer)
    {
        this.chessFrameContainer = chessFrameContainer;
    }
}
