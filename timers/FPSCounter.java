/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import shiftplanning.TwoDDrawable;
import updatables.Updatable;

/**
 *
 * @author phusisian
 */
public class FPSCounter implements Updatable, ActionListener, TwoDDrawable
{
    private Timer fpsCounterTimer;
    private int numFramesPassed = 0;
    private int numMillisUntilUpdate;
    private int framesPerSecond = 0;
    public FPSCounter(int numMillisUntilUpdateIn)
    {
        numMillisUntilUpdate = numMillisUntilUpdateIn;
        fpsCounterTimer = new Timer(numMillisUntilUpdate, this);
        fpsCounterTimer.setRepeats(true);
        fpsCounterTimer.start();
    }
    @Override
    public void update() 
    {
        numFramesPassed++;
    }
    
    public int getFramesPerSecond()
    {
        return framesPerSecond;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        framesPerSecond = (int)((double)numFramesPassed/(double)(numMillisUntilUpdate/1000.0));
        numFramesPassed = 0;
    }

    @Override
    public void draw(Graphics g) 
    {
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + Integer.toString(framesPerSecond), 100, 100);
    }
    
}
