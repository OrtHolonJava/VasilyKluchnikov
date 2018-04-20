package configuration;

import ui.ChessFrame;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by divided on 19.04.2018.
 */
public class OptionsConfigurationReader
{
    private static final String CONFIG_PATH = ChessFrame.DIR_PATH + "\\src\\main\\resources\\configurations\\optionsConfig.properties";
    private static Dimension resolution;
    private static Color lightColor, darkColor;

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
            System.out.println("Error while loading the options configuration");
        }
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

    private static void setResolution(Dimension resolution)
    {
        OptionsConfigurationReader.resolution = resolution;
    }

    private static void setLightColor(Color lightColor)
    {
        OptionsConfigurationReader.lightColor = lightColor;
    }

    private static void setDarkColor(Color darkColor)
    {
        OptionsConfigurationReader.darkColor = darkColor;
    }
}
