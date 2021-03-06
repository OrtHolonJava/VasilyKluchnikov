package configurationReaders;

import ui.ChessFrame;

import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * Reads from the settings configuration
 */
public class SettingsConfigurationReader
{
    private static final String CONFIG_FILE_NAME = "settingsConfig.properties";
    private static Dimension resolution;
    private static Color lightColor, darkColor;

    static
    {
        loadConfig();
    }

    /*
        Updates the game configuration with new configuration parameters
     */
    public static void updateSettingsConfig(String frameWidthString, String frameHeightString, String lightR,
                                           String lightG, String lightB, String darkR, String darkG, String darkB)
    {
        try (OutputStream outputStream = new FileOutputStream(ChessFrame.CONFIG_DIRECTORY_PATH + CONFIG_FILE_NAME))
        {
            Properties properties = new Properties();
            properties.setProperty("frameWidth", frameWidthString);
            properties.setProperty("frameHeight", frameHeightString);

            properties.setProperty("lightR", lightR);
            properties.setProperty("lightG", lightG);
            properties.setProperty("lightB", lightB);

            properties.setProperty("darkR", darkR);
            properties.setProperty("darkG", darkG);
            properties.setProperty("darkB", darkB);

            properties.store(outputStream, null);
            loadConfig();
        }
        catch (IOException e)
        {
            System.out.println("Error while rewriting the options configuration");
        }
    }

    public static void setToDefault()
    {
        updateSettingsConfig("1600", "900", "255", "255", "255", "120","120","120");
    }

    public static Dimension getAppResolution()
    {
        return new Dimension(resolution);
    }

    public static Color getLightTileColor()
    {
        return lightColor;
    }

    public static Color getDarkTileColor()
    {
        return darkColor;
    }

    /*
        Loads the configuration parameters
     */
    private static void loadConfig()
    {
        try (InputStream inputStream = new FileInputStream(ChessFrame.CONFIG_DIRECTORY_PATH + CONFIG_FILE_NAME))
        {
            Properties properties = new Properties();
            properties.load(inputStream);

            int width = Integer.parseInt(properties.getProperty("frameWidth"));
            int height = Integer.parseInt(properties.getProperty("frameHeight"));
            setResolution(new Dimension(width, height));

            int lightR = Integer.parseInt(properties.getProperty("lightR"));
            int lightG = Integer.parseInt(properties.getProperty("lightG"));
            int lightB = Integer.parseInt(properties.getProperty("lightB"));
            setLightColor(new Color(lightR, lightG, lightB));

            int darkR = Integer.parseInt(properties.getProperty("darkR"));
            int darkG = Integer.parseInt(properties.getProperty("darkG"));
            int darkB = Integer.parseInt(properties.getProperty("darkB"));
            setDarkColor(new Color(darkR, darkG, darkB));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Error while loading the options configurationReaders");
        }
    }

    private static void setResolution(Dimension resolution)
    {
        SettingsConfigurationReader.resolution = resolution;
    }

    private static void setLightColor(Color lightColor)
    {
        SettingsConfigurationReader.lightColor = lightColor;
    }

    private static void setDarkColor(Color darkColor)
    {
        SettingsConfigurationReader.darkColor = darkColor;
    }
}
