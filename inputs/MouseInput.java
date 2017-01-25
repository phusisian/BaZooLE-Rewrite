package inputs;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import updatables.Updatable;
import static javax.swing.SwingUtilities.convertPointFromScreen;
import shiftplanning.Plane;
import shiftplanning.Unit;
import shiftplanning.XYZPoint;

/***
SHIFT MOUSEINPUT TO AN INSTANCE, MULTIPLE MOUSEINPUTS (ONE FOR EACH PLANE). STATICS = BAD
***/
public class MouseInput extends MouseAdapter implements Updatable
{
    private boolean mousePressed = false;
    private JPanel boundPanel;
    private boolean mouseClickDebounce = false;
    private Plane boundPlane;
    public static double dScaleAmount = 0.01;
    private MouseWheelTimer mouseWheelTimer = new MouseWheelTimer(100);
    public static double dScale = 0;
    public static int mouseX, mouseY;
    public static int clickX = 0, clickY = 0;
    public static XYZPoint mouseCoordsOnPlane;
    
    public MouseInput(JPanel boundPanelIn, Plane boundPlaneIn)
    {
        boundPanel = boundPanelIn;
        boundPlane = boundPlaneIn;
    }
    public void mouseClicked(MouseEvent e)
    {
        
    }   
    public void mousePressed(MouseEvent e)
    {
        mouseClickDebounce = true;
        mousePressed = true;
    }
    public void mouseReleased(MouseEvent e)
    {
        mousePressed = false;
    }
    
    
    
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        int notches = e.getWheelRotation();
        if (notches > 0) {
            dScale = (dScaleAmount)*Unit.scale;
        } else if (notches < 0) {
            dScale = -dScaleAmount*Unit.scale;
        }
        mouseWheelTimer.resetWheelTimer();
    }
    
    
    
    @Override
    public void update() 
    {
        PointerInfo info = MouseInfo.getPointerInfo();
        Point mouseLocationPoint = info.getLocation();
        convertPointFromScreen(mouseLocationPoint, boundPanel);
            
        mouseX = (int) mouseLocationPoint.getX();
        mouseY = (int) mouseLocationPoint.getY();
        mouseCoordsOnPlane = boundPlane.convertPointToCoords(mouseX, mouseY);
        
        if(mousePressed)//consider changing so it only fires on the first time the mouse is clicked, rather than when it is held down
        {
            boundPlane.updateMouseUpdatablesOnHold();
        }
        
        if(mouseClickDebounce)
        {
            clickX = mouseX;
            clickY = mouseY;
            boundPlane.updateMouseUpdatablesOnClick();
            mouseClickDebounce = false;
        }
    }
}
