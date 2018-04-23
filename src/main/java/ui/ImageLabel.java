package ui;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A label which contains an image
 */
public class ImageLabel extends JLabel
{
    private BufferedImage image;

    public ImageLabel(BufferedImage image, Dimension size)
    {
        setImage(image);
        setIcon(new ImageIcon(getImage()));
        BufferedImage newImage = ImageUtils.scaleImage(getImage(), (int)size.getWidth(), (int)size.getHeight());
        setIcon(new ImageIcon(newImage));
    }

    private BufferedImage getImage()
    {
        return image;
    }

    private void setImage(BufferedImage image)
    {
        this.image = image;
    }
}
