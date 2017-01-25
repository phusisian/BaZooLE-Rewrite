package inputs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author phusisian
 */
public class MouseWheelTimer implements ActionListener
{
    private int scrollWaitMillis;
    private Timer scrollTimer;
    
    public MouseWheelTimer(int scrollWaitMillisIn)
    {
        scrollWaitMillis = scrollWaitMillisIn;
        scrollTimer = new Timer(scrollWaitMillis, this);
        scrollTimer.setRepeats(false);
        scrollTimer.start();
    }
    
    public void resetWheelTimer()
    {
        scrollTimer.stop();
        scrollTimer = new Timer(scrollWaitMillis, this);
        scrollTimer.setRepeats(false);
        scrollTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        MouseInput.dScale = 0;
    }
}
