package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by divided on 19.04.2018.
 */
public class MenuButton extends JButton
{
    private static Font FONT = new Font("Arial", Font.BOLD,22);
    private static Color BACKGROUND_COLOR = new Color(193, 193, 191);

    public MenuButton(String message)
    {
        super(message);
        setFont(FONT);
        setBackground(BACKGROUND_COLOR);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
    }
}