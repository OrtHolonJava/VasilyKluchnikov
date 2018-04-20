package configuration;

import boardgame.Chess;
import bots.ChessBot;
import enums.Player;
import ui.ChessFrame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Created by divided on 19.04.2018.
 */
public class GameConfigurationReader
{
    private static final String CONFIG_PATH = ChessFrame.DIR_PATH + "\\src\\main\\resources\\configurations\\gameConfig.properties";

    private static Chess chessGame;
    private static ChessBot chessBot;
    private static int botSearchDepth;
    private static boolean isPlayerColorRandom;
    private static Player playerSide;

    static
    {
        loadConfig();
    }

    private static void loadConfig()
    {
        try
        {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(CONFIG_PATH);
            properties.load(inputStream);

            readChessVariantSetting(properties);
            readBotSettings(properties);
            readPlayerSideSettings(properties);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error while loading the options configuration");
        }
    }

    public static Chess getChessGame()
    {
        if(chessGame != null)
        {
            return new Chess();
        }
        else
        {
            return null;
        }
    }

    public static ChessBot getChessBot()
    {
        return chessBot;
    }

    public static int getBotSearchDepth()
    {
        return botSearchDepth;
    }

    public static boolean isPlayerColorRandom(){
        return isPlayerColorRandom;
    }

    public static Player getPlayerSide()
    {
        return playerSide;
    }

    private static void readChessVariantSetting(Properties properties) throws InvalidPropertiesFormatException
    {
        String chessVariant = properties.getProperty("chessVariant");
        if(chessVariant.equals("standard"))
        {
            setChessGame(new Chess());
        }
        else
        {
            throw new InvalidPropertiesFormatException("Invalid chess variant property");
        }
    }

    private static void readBotSettings(Properties properties) throws InvalidPropertiesFormatException
    {
        int isBotPlaying = Integer.parseInt(properties.getProperty("isBotPlaying"));
        if(isBotPlaying == 1)
        {
            setChessBot(new ChessBot());
            int botSearchDepth = Integer.parseInt(properties.getProperty("botSearchDepth"));
            if(botSearchDepth > 0)
            {
                setBotSearchDepth(botSearchDepth);
            }
            else
            {
                throw new InvalidPropertiesFormatException("Invalid bot search depth property");
            }
        }
        else if (isBotPlaying == 0)
        {
            setChessBot(null);
            setPlayerSide(null);
            setBotSearchDepth(0);
            setIsPlayerColorRandom(false);
        }
        else
        {
            throw new InvalidPropertiesFormatException("Invalid is bot playing property");
        }
    }

    private static void readPlayerSideSettings(Properties properties) throws InvalidPropertiesFormatException
    {
        int isPlayerColorRandom = Integer.parseInt(properties.getProperty("isPlayerColorRandom"));
        if(isPlayerColorRandom == 1)
        {
            setIsPlayerColorRandom(true);
        }
        else if(isPlayerColorRandom == 0)
        {
            setIsPlayerColorRandom(false);
            String playerColor = properties.getProperty("playerSide");

            if(playerColor.equals("white"))
            {
                setPlayerSide(Player.WHITE);
            }
            else if(playerColor.equals("black"))
            {
                setPlayerSide(Player.BLACK);
            }
            else
            {
                throw new InvalidPropertiesFormatException("Invalid player side property");
            }
        }
        else
        {
            throw new InvalidPropertiesFormatException("Invalid is playerSide color random property");
        }
    }

    private static void setChessGame(Chess chessGame)
    {
        GameConfigurationReader.chessGame = chessGame;
    }

    private static void setChessBot(ChessBot chessBot)
    {
        GameConfigurationReader.chessBot = chessBot;
    }

    private static void setBotSearchDepth(int botSearchDepth)
    {
        GameConfigurationReader.botSearchDepth = botSearchDepth;
    }

    private static void setIsPlayerColorRandom(boolean isPlayerColorRandom)
    {
        GameConfigurationReader.isPlayerColorRandom = isPlayerColorRandom;
    }

    private static void setPlayerSide(Player playerSide)
    {
        GameConfigurationReader.playerSide = playerSide;
    }
}
