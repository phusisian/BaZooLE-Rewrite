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
public class GameStateTimer implements ActionListener
{
    private int numTicksPerSecond;
    private Timer gameStateTimer;
    private GamePanel boundPanel; 
    public GameStateTimer(int numTicksPerSecondIn, GamePanel boundPanelIn)
    {
        numTicksPerSecond = numTicksPerSecondIn;
        boundPanel = boundPanelIn;
        gameStateTimer = new Timer((int)(1000.0/(double)numTicksPerSecond), this);
        gameStateTimer.setRepeats(true);
        gameStateTimer.start();
    }
    
    public int getNumTicksPerSecond()
    {
        return numTicksPerSecond;
    }
    
    public void setNumTicksPerSecondAndAdjustTimer(int numTicksIn)
    {
        numTicksPerSecond = numTicksIn;
        gameStateTimer.stop();
        gameStateTimer = new Timer((int)(1000.0/(double)numTicksPerSecond), this);
        gameStateTimer.setRepeats(true);
        gameStateTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        boundPanel.update();
    }
    
}
