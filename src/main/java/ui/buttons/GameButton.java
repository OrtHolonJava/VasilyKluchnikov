package ui.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * Created by divided on 19.04.2018.
 */
public class GameButton extends JButton
{
    private static Font FONT = new Font("Serif",Font.BOLD,22);
    private static Color BACKGROUND_COLOR = new Color(49, 60, 82);

    public GameButton(String buttonText)
    {
        super(buttonText);
        setFont(FONT);
        setBackground(BACKGROUND_COLOR);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
    }
}
