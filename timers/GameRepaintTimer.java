/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import containers.GamePanel;

/**
 *
 * @author phusisian
 */
public class GameRepaintTimer implements ActionListener
{
    private GamePanel boundGamePanel;
    private int refreshRate;
    private Timer refreshTimer;
    public GameRepaintTimer(int refreshRateIn, GamePanel boundGamePanelIn)
    {
        refreshRate = refreshRateIn;
        boundGamePanel = boundGamePanelIn;
        refreshTimer = new Timer((int)(1000.0/(double)refreshRate), this);
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        boundGamePanel.repaint();
    }
    
}
