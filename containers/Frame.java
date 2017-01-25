package containers;

import inputs.KeyboardInput;
import javax.swing.JFrame;

/**
 *
 * @author phusisian
 */
public class Frame extends JFrame
{
    public static int screenWidth = 1440, screenHeight = 900;
    public Frame()
    {
        super("BaZoo!E");
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(true);
        setSize(screenWidth, screenHeight);
        addKeyListener(new KeyboardInput());
        /*Keep below in mind if I ever want to adapt this to any screen*/
        //setSize(java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width, java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);        
    }
}
