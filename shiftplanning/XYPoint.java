/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import java.awt.Point;

/**
 *
 * @author phusisian
 */
public class XYPoint
{
    double[] xyPoint;
    public XYPoint(double x, double y)
    {
        double[] tempPoint = {x,y};
        xyPoint = tempPoint;
    }
    
    public double[] getPointArray()
    {
        return xyPoint;
    }
    
    public double getX()
    {
        return xyPoint[0];
    }
    
    public double getY()
    {
        return xyPoint[1];
    }
    
    public void setPoint(double x, double y)
    {
        double[] tempPoint = {x, y};
        xyPoint = tempPoint;
    }
    
    public int[] getPointAsIntArray()
    {
        int[] giveReturn = {(int)xyPoint[0], (int)xyPoint[1]};
        return giveReturn;
    }
    
    public void setPoint(double[] newXYPoint)
    {
        xyPoint = newXYPoint;
    }
    
    public Point getAsPoint(){
        return new Point((int)xyPoint[0], (int)xyPoint[1]);
    }
}
