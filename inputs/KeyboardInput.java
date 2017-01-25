package inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardInput extends KeyAdapter
{
    public static double moveSpeed = 3;
    public static double dSpin = 0;
    public static double dRotation = 0;
    public static double dx = 0, dy = 0;
    
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if(keyCode == e.VK_A)
        {
            dSpin = -Math.PI/400.0;
        }
        if(keyCode == e.VK_D)
        {
            dSpin = Math.PI/400.0;
        }
        if(keyCode == e.VK_W)
        {
            dRotation = -Math.PI/400.0;
        }
        if(keyCode == e.VK_S)
        {
            dRotation = Math.PI/400.0;
        }
        if(keyCode == e.VK_UP)
        {
            dy = -moveSpeed;
        }
        if(keyCode == e.VK_DOWN)
        {
            dy = moveSpeed;
        }
        if(keyCode == e.VK_RIGHT)
        {
            dx = moveSpeed;
        }
        if(keyCode == e.VK_LEFT)
        {
            dx = -moveSpeed;
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if(keyCode == e.VK_A)
        {
            dSpin = 0;
        }
        if(keyCode == e.VK_D)
        {
            dSpin = 0;
        }
        if(keyCode == e.VK_W)
        {
            dRotation = 0;
        }
        if(keyCode == e.VK_S)
        {
            dRotation = 0;
        }
        if(keyCode == e.VK_UP)
        {
            dy = 0;
        }
        if(keyCode == e.VK_DOWN)
        {
            dy = 0;
        }
        if(keyCode == e.VK_RIGHT)
        {
            dx = 0;
        }
        if(keyCode == e.VK_LEFT)
        {
            dx = 0;
        }
    }
}
