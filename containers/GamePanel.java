package containers;

import inputs.MouseInput;
import java.awt.Color;
import timers.FPSCounter;
import timers.GameStateTimer;
import timers.GameRepaintTimer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import shiftplanning.BasePlane;
import shiftplanning.Unit;
import updatables.Updatable;

/**
 *
 * @author phusisian
 */

/*
holds the game's painting -- will attempt to keep its purpose strictly within holding the main paint method (and pinning keyboard and mouse input since 
both are attached to a Frame/JPanel)
*/
public class GamePanel extends JPanel implements Updatable
{
    private BasePlane basePlane = new BasePlane(this, Frame.screenWidth/2, 2*Frame.screenHeight/4,10,10);
    private GameRepaintTimer gameRepaintTimer;
    private GameStateTimer gameStateTimer;
    private FPSCounter fpsCounter;
    private Unit unit = new Unit();
    private ArrayList<Updatable> updatables = new ArrayList<Updatable>();//Holds all updatable objects that are to be updated when this class's "update()" method is called (so doesn't include FPS counter because that is updated on every FPS tick). May want to switch to just a normal array -- not sure if I will need to add updatables to this list

    public GamePanel()
    {
        setBounds(0,0,Frame.screenWidth, Frame.screenHeight);
        setOpaque(true);
        setDoubleBuffered(true);
        gameStateTimer = new GameStateTimer(200, this);
        fpsCounter = new FPSCounter(1000);
        gameRepaintTimer = new GameRepaintTimer(100, this);
        MouseInput mouseInput = new MouseInput(this, basePlane);
        addMouseListener(mouseInput);
        addMouseWheelListener(mouseInput);
        updatables.add(mouseInput);
        updatables.add(unit);
        updatables.add(basePlane);
    }
    
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        basePlane.draw(g);
        fpsCounter.update();
        paintDebugInfo(g);
    }
    
    private void paintDebugInfo(Graphics g)
    {
        fpsCounter.draw(g);
        g.setColor(Color.BLACK);
        g.fillOval(MouseInput.mouseX - 5, MouseInput.mouseY - 5, 10, 10);
        g.drawString("Spin Quadrant: " + Integer.toString(basePlane.getSpinQuadrant().getSpinQuadrantNum()), 100, 150);
        g.drawString("Spin: " + Integer.toString((int)(360*basePlane.getSpin()/(Math.PI*2.0))), 100, 175);
    }
    
    //Created for FPS testing
    public void drawRandomPoints(Graphics g, int numTimes)
    {
        Random r = new Random();
        for (int i = 0; i < numTimes; i++) 
        {
            Math.sin(i);
            Math.cos(i);
            Math.atan2(i-100, Math.pow(i, 2));
            int xPointPos = r.nextInt(Frame.screenWidth);
            int yPointPos = r.nextInt(Frame.screenHeight);
            g.fillOval(xPointPos - 5, yPointPos - 5, 10, 10);
        }
    }
    
    @Override
    public void update()
    {
        updateUpdatables();
    }
    
    private void updateUpdatables()
    {
        for(Updatable u : updatables)
        {
            u.update();
        }
    }
}
