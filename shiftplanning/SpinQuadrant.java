/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import updatables.Updatable;
import updatables.UpdatableOnQuadrantChange;

/**
 *
 * @author phusisian
 */
public class SpinQuadrant implements Updatable, UpdatableOnQuadrantChange
{
    private Plane boundPlane;
    private int spinQuadrantNum = 0;
    private double[] visibleAngleRange = {0,0};
    public SpinQuadrant(Plane boundPlaneIn)
    {
        boundPlane = boundPlaneIn;
        update();
    }

    public int getSpinQuadrantNum()
    {
        return spinQuadrantNum;
    }
    
    public boolean isAngleWithinVisibleRange(double angle)//returns whether an angle should be visible based on the spin quadrant number
    {
        if(visibleAngleRange[1] <= Math.PI*2.0)
        {
            return (angle >= visibleAngleRange[0] && angle <= visibleAngleRange[1]);
        }else
        {
            return ((angle >= visibleAngleRange[0] && angle <= visibleAngleRange[1]) || (angle + (Math.PI*2.0) >= visibleAngleRange[0] && angle + (Math.PI*2.0) <= visibleAngleRange[1]));
        }
        
    }
    
    public double[] getVisibleAngleRange()
    {
        return visibleAngleRange;
    }
    
    private double[] calculateVisibleAngleRange()//using a switch statement because this is much simpler with it...
    {
        double min, max;
        min = boundPlane.getSpin() - Math.PI;
        max = boundPlane.getSpin();
        if(min < 0)
        {
            min += Math.PI*2.0;
            max += Math.PI*2.0;
        }
        double[] giveReturn = {min, max};
        return giveReturn;
    }
    
    private int calculateSpinQuadrantNum()
    {
        return (int)((boundPlane.getSpin()%(Math.PI*2.0))/(Math.PI/2.0));
    }
    
    @Override
    public void update() 
    {
        int calculatedSpinQuadrantNum = calculateSpinQuadrantNum();
        visibleAngleRange = calculateVisibleAngleRange();
        
        
        if(spinQuadrantNum != calculatedSpinQuadrantNum)
        {
            spinQuadrantNum = calculatedSpinQuadrantNum;
            updateOnQuadrantChange();
        }
    }

    /*
    Nothing in here yet, but shoudl be useful for actions that must occur when the spin quadrant changes
    */
    @Override
    public void updateOnQuadrantChange() 
    {
        boundPlane.updateOnQuadrantChange();
    }
}
