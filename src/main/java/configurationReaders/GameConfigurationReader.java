package configurationReaders;

import boardgame.Chess;
import enums.Player;
import ui.ChessFrame;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Created by divided on 19.04.2018.
 */
public class GameConfigurationReader
{
    private static final String CONFIG_FILE_NAME = "gameConfig.properties";

    private static String variantName;
    private static boolean isBotPlaying;
    private static int botSearchDepth;
    private static boolean isPlayerSideRandom;
    private static Player playerSide;

    static
    {
        loadConfig();
    }

    public static void updateGameConfig(String variantString, String isPlayerSideRandomString, String playerSideString, String isBotPlayingString, String botSearchDepthString)
    {
        OutputStream outputStream = null;
        try
        {
            Properties properties = new Properties();
            outputStream = new FileOutputStream(ChessFrame.CONFIG_DIRECTORY_PATH + CONFIG_FILE_NAME);
            properties.setProperty("chessVariant", variantString);
            properties.setProperty("isPlayerSideRandom", isPlayerSideRandomString);
            properties.setProperty("playerSide", playerSideString);
            properties.setProperty("isBotPlaying", isBotPlayingString);
            properties.setProperty("botSearchDepth", botSearchDepthString);

            properties.store(outputStream, null);
            loadConfig();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error while rewriting the game configuration");
        }
        finally
        {
            try
            {
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Failed to close the output stream");
            }
        }
    }

    public static Chess getChessGame()
    {
        if(getVariantName().equals("standard"))
        {
            return new Chess();
        }
        else
        {
            return null;
        }
    }

    public static boolean isBotPlaying()
    {
        return isBotPlaying;
    }

    public static int getBotSearchDepth()
    {
        return botSearchDepth;
    }

    public static boolean isPlayerSideRandom(){
        return isPlayerSideRandom;
    }

    public static Player getPlayerSide()
    {
        return playerSide;
    }

    public static String getVariantName()
    {
        return variantName;
    }

    private static void loadConfig()
    {
        InputStream inputStream = null;
        try
        {
            Properties properties = new Properties();
            inputStream = new FileInputStream(ChessFrame.CONFIG_DIRECTORY_PATH + CONFIG_FILE_NAME);
            properties.load(inputStream);

            readChessVariantSetting(properties);

            readPlayerSideSettings(properties);
            readBotSettings(properties);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error while loading the options configuration");
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Failed to close the input stream");
            }
        }
    }

    private static void readChessVariantSetting(Properties properties) throws InvalidPropertiesFormatException
    {
        String chessVariant = properties.getProperty("chessVariant");
        if(chessVariant.equals("standard") || chessVariant.equals("chess960"))
        {
            setVariantName(chessVariant);
        }
        else
        {
            throw new InvalidPropertiesFormatException("Invalid chess variant property");
        }
    }

    private static void readBotSettings(Properties properties) throws InvalidPropertiesFormatException
    {
        String isBotPlaying = properties.getProperty("isBotPlaying").toLowerCase();
        if(isBotPlaying.equals("true"))
        {
            setIsBotPlaying(true);
        }
        else if(isBotPlaying.equals("false"))
        {
            setIsBotPlaying(false);
        }
        else
        {
            throw new InvalidPropertiesFormatException("Invalid is bot playing property");
        }

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

    private static void readPlayerSideSettings(Properties properties) throws InvalidPropertiesFormatException
    {
        String isPlayerColorRandom = properties.getProperty("isPlayerSideRandom").toLowerCase();
        if(isPlayerColorRandom.equals("true"))
        {
            setIsPlayerSideRandom(true);
        }
        else if(isPlayerColorRandom.equals("false"))
        {
            setIsPlayerSideRandom(false);
            String playerColor = properties.getProperty("playerSide");

            if(playerColor.equals("white"))
            {
                setPlayerSide(Player.WHITE);
            }
            else if(playerColor.equals("black"))
            {
                setPlayerSide(Player.BLACK);
            }
            else if(playerColor.equals(""))
            {
                setPlayerSide(null);
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

    private static void setVariantName(String variantName)
    {
        GameConfigurationReader.variantName = variantName;
    }

    private static void setIsBotPlaying(boolean isBotPlaying)
    {
        GameConfigurationReader.isBotPlaying = isBotPlaying;
    }

    private static void setBotSearchDepth(int botSearchDepth)
    {
        GameConfigurationReader.botSearchDepth = botSearchDepth;
    }

    private static void setIsPlayerSideRandom(boolean isPlayerSideRandom)
    {
        GameConfigurationReader.isPlayerSideRandom = isPlayerSideRandom;
    }

    private static void setPlayerSide(Player playerSide)
    {
        GameConfigurationReader.playerSide = playerSide;
    }
}
