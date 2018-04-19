package configuration;

import java.awt.*;

/**
 * Created by divided on 19.04.2018.
 */
public class OptionsConfigurationReader
{
    public static Dimension getAppResolution()
    {
        return new Dimension(1600, 900);
    }

    public static Color getLightTileColor()
    {
        return Color.WHITE;
    }

    public static Color getDarkTileColor()
    {
        return Color.GRAY;
    }
}
