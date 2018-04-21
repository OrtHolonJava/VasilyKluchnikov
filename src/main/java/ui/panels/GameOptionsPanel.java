package ui.panels;

import configurationReaders.GameConfigurationReader;
import configurationReaders.SettingsConfigurationReader;
import enums.Player;
import ui.ChessFrame;
import ui.buttons.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by divided on 20.04.2018.
 */
public class GameOptionsPanel extends JPanel
{
    private static final Color BACKGROUND_COLOR = MainMenuPanel.BACKGROUND_COLOR;
    private static final Font TEXT_FONT = new Font("Tahoma", Font.BOLD, 24);

    private static final int BOT_DEPTH_MEDIUM_DIFFICULTY = 3;

    private static final double OPTIONS_PANEL_WIDTH_RATIO = 0.5;
    private static final double OPTIONS_PANEL_HEIGHT_RATIO = 0.8;
    private static final double ELEMENTS_HORIZONTAL_GAP_RATIO = 0.06;
    private static final double OPTIONS_VERTICAL_GAP_RATIO = 0.03;

    private static final int NUMBER_OF_OPTION_PANEL_ROWS = 7;
    private static final int NUMBER_OF_OPPONENT_OPTION_ELEMENTS = 3;
    private static final int NUMBER_OF_COLOR_OPTION_ELEMENTS = 2;
    private static final int NUMBER_OF_BOT_DIFFICULTY_OPTION_ELEMENTS = 2;
    private static final int NUMBER_OF_VARIANT_OPTION_ELEMENTS = 2;
    private static final int NUMBER_OF_MENU_BUTTONS = 1;

    private ChessFrame chessFrameContainer;
    private JPanel optionsPanel;

    private ButtonGroup opponentButtons;
    private JComboBox<String> playerColorBox;
    private JComboBox<String> botDifficultyBox;
    private JComboBox<String> variantBox;

    public GameOptionsPanel(ChessFrame chessFrameContainer)
    {
        super();
        setSize(SettingsConfigurationReader.getAppResolution());
        setBackground(BACKGROUND_COLOR);
        setChessFrameContainer(chessFrameContainer);
        initializeGameOptionsUI();
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
        Initializes the whole game options UI
     */
    private void initializeGameOptionsUI()
    {
        initializeOptionsPanel();
        initializeOpponentOptions();
        initializePlayerColorOptions();
        initializeBotDifficultyOptions();
        initializeVariantOptions();
        initializeButtons();
    }

    /*
        Updates the game configuration based on the selected settings by the user
     */
    private void updateGameConfig()
    {
        int botSearchDepth;
        String isBotPlayingString;
        String isPlayerSideRandomString;
        String playerSideString;
        String selectedOpponentString = getOpponentButtons().getSelection().getActionCommand();
        String selectedColorString = getPlayerColorBox().getSelectedItem().toString().toLowerCase();
        String selectedBotDifficultyString = getBotDifficultyBox().getSelectedItem().toString().toLowerCase();
        String selectedVariantString = getVariantBox().getSelectedItem().toString().toLowerCase();

        if(selectedOpponentString.equals("player"))
        {
            isBotPlayingString = "false";
        }
        else
        {
            isBotPlayingString = "true";
        }

        if(selectedColorString.equals("random"))
        {
            isPlayerSideRandomString = "true";
            playerSideString = "";
        }
        else
        {
            isPlayerSideRandomString = "false";
            playerSideString = selectedColorString;
        }

        if(selectedBotDifficultyString.equals("medium"))
        {
            botSearchDepth = BOT_DEPTH_MEDIUM_DIFFICULTY;
        }
        else if (selectedBotDifficultyString.equals("hard"))
        {
            botSearchDepth = BOT_DEPTH_MEDIUM_DIFFICULTY + 1;
        }
        else
        {
            botSearchDepth = BOT_DEPTH_MEDIUM_DIFFICULTY - 1;
        }

        GameConfigurationReader.updateGameConfig(selectedVariantString, isPlayerSideRandomString, playerSideString,
                isBotPlayingString, Integer.toString(botSearchDepth));

    }

    /*
        Initializes the UI of the bottom buttons
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
            updateGameConfig();
            getChessFrameContainer().openMainMenu();
        }
    });

        backButton.setFont(TEXT_FONT);
        buttonsPanel.add(backButton);

        getOptionsPanel().add(new JLabel());
        getOptionsPanel().add(buttonsPanel);
    }

    /*
        Initializes the main panel which holds all the options (and the buttons)
     */
    private void initializeOptionsPanel()
    {
        JPanel optionsPanel = new JPanel();
        setOptionsPanel(optionsPanel);
        optionsPanel.setBackground(BACKGROUND_COLOR);

        resizeOptionsPanel();

        GridLayout gridLayout = new GridLayout(NUMBER_OF_OPTION_PANEL_ROWS, 1);
        gridLayout.setVgap(getOptionsVerticalGap());
        optionsPanel.setLayout(gridLayout);

        getOptionsPanel().add(new JLabel());
        add(optionsPanel);
    }

    /*
        Resizes the main options panel based on the current panel size
     */
    private void resizeOptionsPanel()
    {
        int width = (int)(getSize().getWidth() * OPTIONS_PANEL_WIDTH_RATIO);
        int height = (int)(getSize().getHeight() * OPTIONS_PANEL_HEIGHT_RATIO);
        getOptionsPanel().setSize(new Dimension(width, height));
        getOptionsPanel().setPreferredSize(new Dimension(width, height));
    }

    /*
        Initializes the variant options UI
     */
    private void initializeVariantOptions()
    {
        JPanel variantPanel = new JPanel();

        GridLayout layout = new GridLayout(1, NUMBER_OF_VARIANT_OPTION_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());
        variantPanel.setLayout(layout);
        variantPanel.setBackground(BACKGROUND_COLOR);

        JLabel variantText = new JLabel("Variant");
        String standardVariant = "Standard", chess960Variant = "Chess960";
        String[] variantChoices = {standardVariant, chess960Variant};
        JComboBox<String> variantOptionBox = new JComboBox<String>(variantChoices);
        setVariantBox(variantOptionBox);

        if(GameConfigurationReader.getVariantName().equals(standardVariant.toLowerCase()))
        {
            variantOptionBox.setSelectedItem(standardVariant);
        }
        else
        {
            variantOptionBox.setSelectedItem(chess960Variant);
        }

        variantText.setBackground(BACKGROUND_COLOR);
        variantText.setFont(TEXT_FONT);
        variantOptionBox.setFont(TEXT_FONT);

        variantPanel.add(variantText);
        variantPanel.add(variantOptionBox);

        getOptionsPanel().add(variantPanel);
    }

    /*
        Initializes the bot difficulty options UI
     */
    private void initializeBotDifficultyOptions()
    {
        JPanel botDifficultyPanel = new JPanel();
        GridLayout layout = new GridLayout(1, NUMBER_OF_BOT_DIFFICULTY_OPTION_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());
        botDifficultyPanel.setLayout(layout);
        botDifficultyPanel.setBackground(BACKGROUND_COLOR);

        JLabel botDifficultyText = new JLabel("Bot Difficulty");
        String easyString = "Easy", mediumString = "Medium", hardString = "Hard";
        String[] difficultyChoices = {easyString, mediumString, hardString};
        JComboBox<String> botDifficultyOptionBox = new JComboBox<String>(difficultyChoices);
        setBotDifficultyBox(botDifficultyOptionBox);

        if(GameConfigurationReader.getBotSearchDepth() == BOT_DEPTH_MEDIUM_DIFFICULTY)
        {
            botDifficultyOptionBox.setSelectedItem(mediumString);
        }
        else if (GameConfigurationReader.getBotSearchDepth()  > BOT_DEPTH_MEDIUM_DIFFICULTY)
        {
            botDifficultyOptionBox.setSelectedItem(hardString);
        }
        else
        {
            botDifficultyOptionBox.setSelectedItem(easyString);
        }

        botDifficultyText.setFont(TEXT_FONT);
        botDifficultyOptionBox.setFont(TEXT_FONT);
        botDifficultyPanel.add(botDifficultyText);
        botDifficultyPanel.add(botDifficultyOptionBox);

        getOptionsPanel().add(botDifficultyPanel);
    }

    /*
        Initializes the player color options UI
     */
    private void initializePlayerColorOptions()
    {
        JPanel playerColorPanel = new JPanel();
        GridLayout layout = new GridLayout(1, NUMBER_OF_COLOR_OPTION_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());
        playerColorPanel.setLayout(layout);

        JLabel playerColorText = new JLabel("Player's Color");
        String randomString = "Random", whiteString = "White", blackString = "Black";
        String[] colorChoices = {randomString, whiteString, blackString};
        JComboBox<String> colorOptionBox = new JComboBox<String>(colorChoices);
        setPlayerColorBox(colorOptionBox);

        if(GameConfigurationReader.isPlayerSideRandom())
        {
            colorOptionBox.setSelectedItem(randomString);
        }
        else
        {
            if(GameConfigurationReader.getPlayerSide() == Player.WHITE)
            {
                colorOptionBox.setSelectedItem(whiteString);
            }
            else
            {
                colorOptionBox.setSelectedItem(blackString);
            }
        }

        playerColorText.setFont(TEXT_FONT);
        colorOptionBox.setFont(TEXT_FONT);
        playerColorPanel.setBackground(BACKGROUND_COLOR);

        playerColorPanel.add(playerColorText);
        playerColorPanel.add(colorOptionBox);

        getOptionsPanel().add(playerColorPanel);
    }

    /*
        Initializes the opponent options UI
     */
    private void initializeOpponentOptions()
    {
        JPanel opponentSettingsPanel = new JPanel();
        GridLayout layout = new GridLayout(1, NUMBER_OF_OPPONENT_OPTION_ELEMENTS);
        layout.setHgap(getElementHorizontalGap());
        opponentSettingsPanel.setLayout(layout);

        JLabel opponentText = new JLabel("Opponent");

        JRadioButton playerOption = new JRadioButton("Player");
        JRadioButton botOption = new JRadioButton("Bot");
        ButtonGroup buttonGroup = new ButtonGroup();

        playerOption.setActionCommand(playerOption.getText().toLowerCase());
        botOption.setActionCommand(botOption.getText().toLowerCase());
        setOpponentButtonsGroup(buttonGroup);

        if(GameConfigurationReader.isBotPlaying())
        {
            botOption.setSelected(true);
        }
        else
        {
            playerOption.setSelected(true);
        }

        opponentText.setFont(TEXT_FONT);
        playerOption.setFont(TEXT_FONT);
        botOption.setFont(TEXT_FONT);

        buttonGroup.add(playerOption);
        buttonGroup.add(botOption);

        opponentSettingsPanel.add(opponentText);
        opponentSettingsPanel.add(playerOption);
        opponentSettingsPanel.add(botOption);

        playerOption.setBackground(BACKGROUND_COLOR);
        botOption.setBackground(BACKGROUND_COLOR);
        opponentSettingsPanel.setBackground(BACKGROUND_COLOR);

        getOptionsPanel().add(opponentSettingsPanel);
    }

    private int getElementHorizontalGap()
    {
        return (int)(getSize().getWidth() * ELEMENTS_HORIZONTAL_GAP_RATIO);
    }

    private int getOptionsVerticalGap()
    {
        return (int)(getSize().getHeight() * OPTIONS_VERTICAL_GAP_RATIO);
    }

    private ButtonGroup getOpponentButtons()
    {
        return opponentButtons;
    }

    private void setOpponentButtonsGroup(ButtonGroup opponentButtons)
    {
        this.opponentButtons = opponentButtons;
    }

    private JComboBox<String> getPlayerColorBox()
    {
        return playerColorBox;
    }

    private void setPlayerColorBox(JComboBox<String> playerColorBox)
    {
        this.playerColorBox = playerColorBox;
    }

    private JComboBox<String> getBotDifficultyBox()
    {
        return botDifficultyBox;
    }

    private void setBotDifficultyBox(JComboBox<String> botDifficultyBox)
    {
        this.botDifficultyBox = botDifficultyBox;
    }

    private JComboBox<String> getVariantBox()
    {
        return variantBox;
    }

    private void setVariantBox(JComboBox<String> variantBox)
    {
        this.variantBox = variantBox;
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
