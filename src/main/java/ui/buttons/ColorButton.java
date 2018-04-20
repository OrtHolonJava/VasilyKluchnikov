package ui.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by divided on 20.04.2018.
 */
public class ColorButton extends JButton
{
    public ColorButton(Color color)
    {
        super();
        setBackground(color);
        addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setBackground(getNewColorInput());

        }
    });
    }

    private Color getNewColorInput()
    {
        return JColorChooser.showDialog(null, "Choose a Color", getBackground());
    }
}
