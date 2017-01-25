/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import updatables.Updatable;
import shiftmath.AngleMath;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class XYZPointCollection implements Updatable, TwoDDrawable
{
    private Plane boundPlane;
    private XYZPoint midpoint;
    private XYZPoint[] xyzPoints;
    private int[][] xyPolyPoints;
    private double smallestOffsetAngle = 0;
    public XYZPointCollection(Plane boundPlaneIn, XYZPoint[] xyzPointsIn)
    {
        boundPlane = boundPlaneIn;
        xyzPoints = xyzPointsIn;
        midpoint = getMidpoint();
        smallestOffsetAngle = getSmallestOffsetAngleFromMidpoint();
        xyPolyPoints = new int[2][xyzPoints.length];
        //update();
        //sortPointsByOffsetAngleFromMidpoint();
    }
    
    public int getXYZPointsLength()
    {
        return xyzPoints.length;
    }
    
    public void removePointIntersections(){
        XYZLine[] lines = getCollectionAsLines();
        
        int pointSubtract = 0;
        for(int i = 0; i < lines.length; i++){
            if(lines[i].getIntersectionWithinBounds(lines[(i+1)%lines.length]) != null){
                //xyzPoints[(i-pointSubtract)%xyzPoints.length] = lines[i].getIntersectionWithinBounds(lines[(i+1)%lines.length]);
                //removePoint((i+1 - pointSubtract)%xyzPoints.length);
                //pointSubtract ++;
                //removePoint((i+1)%xyzPoints.length);
                //addPoint(i, lines[i].getIntersectionWithinBounds(lines[(i+1)%xyzPoints.length]));
                //removePoint(i);
                //addPoint(i, lines[i].getIntersectionWithinBounds(lines[(i+1)%lines.length]));
                
            }
        }
    }
    
    //returns the point halfway through the list. Useful for paths.
    public XYZPoint getHalfListPoint(){
        return xyzPoints[(xyzPoints.length/2)];
    }
    
    public Plane getBoundPlane(){
        return boundPlane;
    }
    
    
    public XYZLine[] getCollectionAsLines(){
        XYZLine[] lines = new XYZLine[xyzPoints.length];
        for(int i = 0; i < xyzPoints.length; i++){
            if(i < xyzPoints.length - 1){
                lines[i] = new XYZLine(xyzPoints[i], xyzPoints[i+1]);
            }else{
                lines[i] = new XYZLine(xyzPoints[i], xyzPoints[0]);
            }
        }
        return lines;
    }
    
    public void addPoint(int index, XYZPoint p1){
        XYZPoint[] temp = new XYZPoint[xyzPoints.length + 1];
        for(int i = 0; i < temp.length; i++){
            if(i < index){
                temp[i] = xyzPoints[i];
            }else if(i == index){
                temp[i] = p1;
            }else{
                temp[i] = xyzPoints[i-1];
            }  
        }
        xyzPoints = temp;
    }
    
    public void removePoint(int index){
        XYZPoint[] temp = new XYZPoint[xyzPoints.length - 1];
        for(int i = 0; i < temp.length; i++){
            if(i < index){
                temp[i] = xyzPoints[i];
            }else{
                temp[i] = xyzPoints[i+1];
            }
        }
        xyzPoints = temp;
    }
    
    public void rotate(XYZPoint pivot, double theta){
        for(XYZPoint point : xyzPoints){
            double dx = point.getX() - pivot.getX();
            double dy = point.getY() - pivot.getY();
            double initTheta = Math.atan2(dy, dx);
            double radius = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            point.setX(pivot.getX() + radius * Math.cos(initTheta + theta));
            point.setY(pivot.getY() + radius * Math.sin(initTheta + theta));
        }
    }
    
    public XYZPoint[] getXYZPoints(){
        return xyzPoints;
    }
    
    public XYZPoint getStartPoint(){
        return xyzPoints[0];
    }
    
    public XYZPoint getEndPoint(){
        return xyzPoints[xyzPoints.length - 1];
    }
    
    public XYZPoint getMidpoint()
    {
        double xCount = 0, yCount = 0, zCount = 0;
        for(XYZPoint xyz : xyzPoints)
        {
            xCount += xyz.getX();
            yCount += xyz.getY();
            zCount += xyz.getZ();
        }
        return new XYZPoint(boundPlane, xCount/(double)xyzPoints.length, yCount/(double)xyzPoints.length, zCount/(double)xyzPoints.length);
    }
    
    public double getOffsetAngleFromMidpointAtIndex(int index)
    {
        double giveReturn = Math.atan2(-(xyzPoints[index].getY()-midpoint.getY()), xyzPoints[index].getX() - midpoint.getX());
        if(giveReturn < 0)
        {
            giveReturn += Math.PI*2.0;
        }
        return giveReturn;
    }
    
    public XYZPoint getFarthestPointToFront()
    {
        double fitTheta = Math.PI + ((boundPlane.getSpinQuadrant().getVisibleAngleRange()[0] + boundPlane.getSpinQuadrant().getVisibleAngleRange()[1])/2.0);
        int smallestDifferenceIndex = 0;
        double smallestDifference = AngleMath.getDifferenceOfAngles(getRelativeOffsetAngleFromMidpointAtIndex(0), fitTheta);
        for(int i = 1; i < xyzPoints.length; i++)
        {
            double angleDiff = AngleMath.getDifferenceOfAngles(getRelativeOffsetAngleFromMidpointAtIndex(i), fitTheta);
            if(angleDiff < smallestDifference)
            {
                smallestDifference = angleDiff;
                smallestDifferenceIndex = i;
            }
        }
        return xyzPoints[smallestDifferenceIndex];
    }
    
    public XYZPoint getClosestPointToFront()
    {
        double fitTheta = (boundPlane.getSpinQuadrant().getVisibleAngleRange()[0] + boundPlane.getSpinQuadrant().getVisibleAngleRange()[1])/2.0;
        int smallestDifferenceIndex = 0;
        double smallestDifference = AngleMath.getDifferenceOfAngles(getRelativeOffsetAngleFromMidpointAtIndex(0), fitTheta);
        for(int i = 1; i < xyzPoints.length; i++)
        {
            double angleDiff = AngleMath.getDifferenceOfAngles(getRelativeOffsetAngleFromMidpointAtIndex(i), fitTheta);
            if(angleDiff < smallestDifference)
            {
                smallestDifference = angleDiff;
                smallestDifferenceIndex = i;
            }
        }
        return xyzPoints[smallestDifferenceIndex];
    }
    
    /*
    IMPORTANT FOR CLARITY: Used to have a different function from getOffsetAngleFromMidpointAtIndex and code was built around it. Edited to account for an error I had made and am still using this.
    */
    public double getRelativeOffsetAngleFromMidpointAtIndex(int index)
    {
        return getOffsetAngleFromMidpointAtIndex(index);// + (smallestOffsetAngle/2.0);
    }
    
    /*
    private void sortPointsByOffsetAngleFromMidpoint()
    {
        for (int i = 0; i < xyzPoints.length; i++) 
        {
            int smallestAngleIndex = 0;
            double smallestAngle = getOffsetAngleFromMidpointAtIndex(0);
            for (int j = i + 1; j < xyzPoints.length; j++) 
            {
                if(getOffsetAngleFromMidpointAtIndex(j) < smallestAngle)
                {
                    smallestAngle = getOffsetAngleFromMidpointAtIndex(j);
                    smallestAngleIndex = j;
                }
            }
            XYZPoint tempPoint = xyzPoints[i];
            xyzPoints[i] = xyzPoints[smallestAngleIndex];
            xyzPoints[smallestAngleIndex] = tempPoint;
        }
    }*/
    
    public double getSmallestOffsetAngle()
    {
        return smallestOffsetAngle;
    }
    
    private double getSmallestOffsetAngleFromMidpoint()
    {
        double smallestAngle = getOffsetAngleFromMidpointAtIndex(0);
        for (int i = 1; i < xyzPoints.length; i++) {
            double currentAngle = getOffsetAngleFromMidpointAtIndex(i);
            if( currentAngle < smallestAngle)
            {
                smallestAngle = currentAngle;
            }
        }
        return smallestAngle;
    }
    
    public void translate(XYZPoint translatePoint)
    {
        for(XYZPoint xyzPoint : xyzPoints)
        {
            xyzPoint.translate(translatePoint);
        }
        midpoint = getMidpoint();
    }
    
    /*
    public double getInitialOffsetAngleFromMidpoint()
    {
        XYZPoint midpoint = getMidpoint();
        return Math.atan2(xyzPoints[0].getY() - midpoint.getY(), xyzPoints[0].getX() - midpoint.getX());
    }*/
    
    private int[][] calculateXYPolyPoints()
    {
        int[][] returnPolyPoints = new int[2][xyzPoints.length];
        for (int i = 0; i < returnPolyPoints[0].length; i++) 
        {
            returnPolyPoints[0][i] = (int)xyzPoints[i].getPrecalculatedPointProjection().getX();
            returnPolyPoints[1][i] = (int)xyzPoints[i].getPrecalculatedPointProjection().getY();
        }
        return returnPolyPoints;
    }
    
    public XYZPoint getPoint(int index){
        return xyzPoints[index];
    }
    
    public void setPoint(int index, XYZPoint p){
        xyzPoints[index] = p;
    }
    
    public int[][] getXYPolyPoints(){
        return xyPolyPoints;
    }

    @Override
    public void update() 
    {
        for(XYZPoint xyz : xyzPoints)
        {
            xyz.update();
        }
        xyPolyPoints = calculateXYPolyPoints();
    }

    public XYZPoint getHighestPoint(){
        int highestPointIndex = 0;
        for(int i = 1; i < xyzPoints.length; i++){
            if(xyzPoints[i].getZ() > xyzPoints[highestPointIndex].getZ()){
                highestPointIndex = i;
            }
        }
        return xyzPoints[highestPointIndex];
    }
    
    @Override
    public void draw(Graphics g) {
        for(int i = 0; i < xyzPoints.length - 1; i++){
            g.setColor(Color.BLACK);
            xyzPoints[i].drawLineToPoint(g, xyzPoints[i+1]);
            g.setColor(Color.BLACK);
            xyzPoints[i].draw(g);
        }
    }
    
}
