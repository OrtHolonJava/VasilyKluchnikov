package ui;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by divided on 21.04.2018.
 */
public class ImageLabel extends JLabel
{
    private BufferedImage image;

    public ImageLabel(BufferedImage image, Dimension size)
    {
        setImage(image);
        setIcon(new ImageIcon(getImage()));
        scaleSize(size);
    }

    /*
        Scales the size of the panel and the image inside it
     */
    public void scaleSize(Dimension size)
    {
        //setSize(size);
        //setPreferredSize(size);
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
