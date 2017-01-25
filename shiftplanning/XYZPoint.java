/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import updatables.Updatable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*holds x, y, and z coords for ease of access, along with their bound 
 */
public class XYZPoint implements ThreeDDrawable, Updatable
{
    /*** might be better to use x y z instead of an array ***/
    private double[] point = new double[3];
    private XYPoint precalculatedPointProjection = new XYPoint(0,0);
    private Plane boundPlane;
    public XYZPoint(Plane boundPlaneIn, double x, double y, double z)
    {
        boundPlane = boundPlaneIn;
        point[0] = x;
        point[1] = y;
        point[2] = z;
        update();
    }
    
    public XYZPoint()//creates an empty bound point with no bound plane
    {
        point[0] = 0;
        point[1] = 0;
        point[2] = 0;
    }
    
    /*
    Getters and setters for XYZ and boundPlane, including one which returns the point as an array in the form {x,y,z};
    */
    
    public void setX(double xIn){
        point[0] = xIn;
    }
    
    public void setY(double yIn){
        point[1] = yIn;
    }
    
    public void setZ(double zIn){
        point[2] = zIn;
    }
    
    public Plane getBoundPlane(){
        return boundPlane;
    }
    
    public double[] getPointArray()
    {
        return point;
    }
    
    public double getX()
    {
        return point[0];
    }
    
    public double getY()
    {
        return point[1];
    }
    
    public double getZ()
    {
        return point[2];
    }
    
    public static double getTotalMagnitudeBetweenPoints(XYZPoint p1, XYZPoint p2)
    {
        return Math.sqrt( Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) + Math.pow(p2.getZ() - p1.getZ(), 2) );
    }
    
    public static double getXYMagnitudeBetweenPoints(XYZPoint p1, XYZPoint p2)
    {
        return Math.sqrt( Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2) );
    }

    @Override
    public void draw(Graphics g) 
    {
        XYPoint screenPoint = boundPlane.convertCoordsToPoint(this);
        g.fillOval((int)screenPoint.getX() - 5, (int)screenPoint.getY() - 5, 10, 10);
    }
    
    public double getAngleToPoint(XYZPoint anglePoint){
        return Math.atan2(getY() - anglePoint.getY(), getX() - anglePoint.getX());
    }
    
    public void drawLineToPoint(Graphics g, XYZPoint xyzPointIn)
    {
        int[] p1 = precalculatedPointProjection.getPointAsIntArray();
        int[] p2 = xyzPointIn.getPrecalculatedPointProjection().getPointAsIntArray();
        g.drawLine(p1[0], p1[1], p2[0], p2[1]);
    }
    
    public XYPoint getPrecalculatedPointProjection()
    {
        return precalculatedPointProjection;
    }

    @Override
    public double getSortDistanceConstant() 
    {
        double constant = 0;
        double slope = 0;
        switch(boundPlane.getSpinQuadrant().getSpinQuadrantNum() + 1)
        {
            case 1://works
                slope = -1;
                constant = getY()-(slope*getX());
                break;
            case 2://works
                slope = 1;
                constant = -(getY()-(slope*getX()));
                break;
            case 3:
                slope = -1;
                constant = -(getY()-(slope*getX()));
                break;
            case 4:
                slope = 1;
                constant = getY()-(slope*getX());
                break;
        }
        constant += getBoundPlane().getWidth();
        /***adding the z position was to weight higher points as closer constants so things like tree leaves would be drawn in the correct order.
         * The fix kind of makes sense, but also shouldn't work. Keep this in mind if draw order problems exist in the future.
         */
        constant += getZ();
        
        //constant = Math.sqrt( Math.pow(constant, 2) + Math.pow(getZ(), 2));
        return constant;
    }

    public void translate(XYZPoint translatePoint)
    {
        point[0] += translatePoint.getX();
        point[1] += translatePoint.getY();
        point[2] += translatePoint.getZ();
    }
    
    public XYZPoint getTranslatedPoint(double dx, double dy, double dz){
        XYZPoint newPoint = new XYZPoint(boundPlane, getX() + dx, getY() + dy, getZ() + dz);
        return newPoint;
    }
    
    @Override
    public double getBackSortDistanceConstant(){
        return getSortDistanceConstant();
    }
    
    @Override
    public void update()
    {
        precalculatedPointProjection = boundPlane.convertCoordsToPoint(this);
    }
    
    public Point getAsPoint(){
        return precalculatedPointProjection.getAsPoint();
    }
    
    /***Returns the translate point that will allow this point to move to a final point within a number of ticks***/
    public static XYZPoint getMovementTranslatePoint(XYZPoint start, XYZPoint end, int numTicks){
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double dz = end.getZ()- start.getZ();
        dx /= (double)numTicks;
        dy /= (double)numTicks;
        dz /= (double)numTicks;
        return new XYZPoint(start.getBoundPlane(), dx, dy, dz);
    }
    
    public XYZPoint clone(){
        return new XYZPoint(boundPlane, getX(), getY(), getZ());
    }
    
    public String toString()
    {
        return Double.toString(point[0]) + ", " + Double.toString(point[1]) + ", " + Double.toString(point[2]);
    }
}
