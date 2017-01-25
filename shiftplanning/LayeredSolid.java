package shiftplanning;

import shiftcolor.PolygonShader;
import updatables.MouseUpdatable;
import updatables.Updatable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author phusisian
 */
public class LayeredSolid implements ThreeDDrawable, Updatable, MouseUpdatable
{
    /***Consider making a poly point class?***/
    private ShapeLayer[] shapeLayers;
    private Plane boundPlane;
    private ArrayList<Polygon>[] visibleSidePolygons;
    int[][][] solidPolyPoints;
    private ShapeLayer baseBounds;
    /***NOTE: To function properly, points must be passed in the order from theta = 0 to theta = 2pi. Sides are connected using next index and the next index must be at a positive angle(counterclockwise) to the one prior***/
    public LayeredSolid(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn)
    {
        shapeLayers = shapeLayersIn;
        boundPlane = boundPlaneIn;
        visibleSidePolygons = (ArrayList<Polygon>[])new ArrayList[shapeLayers.length - 1];
        for(int i = 0; i < visibleSidePolygons.length; i++)
        {
            visibleSidePolygons[i] = new ArrayList<Polygon>();
        }
        solidPolyPoints = new int[shapeLayers.length][2][shapeLayers[0].getNumPoints()];
    }
    
    public XYZRect getBaseBounds(){
        return shapeLayers[0].getLayerBounds();
    }
    
    public double getHeight(){
        return shapeLayers[shapeLayers.length - 1].getHighestPoint().getZ();
    }
    
    public XYZPoint getPlacementPoint(){
        return getShapeLayer(getLayerLength() - 1).getLayerBounds().getPlacementPoint();
    }
    
    public boolean visibleSidesContainPoint(int x, int y)
    {
        for(ArrayList<Polygon> polygons : visibleSidePolygons)
        {
            for(Polygon p : polygons)
            {
                if(p.contains(x, y))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public ShapeLayer[] getLayers(){
        return shapeLayers;
    }
    
    public ShapeLayer getShapeLayer(int index){
        return shapeLayers[index];
    }
    
    public int getLayerLength(){
        return shapeLayers.length;
    }
    
    public boolean topSideContainsPoint(int x, int y)
    {
        Polygon p = new Polygon(shapeLayers[shapeLayers.length - 1].getXYZPointCollection().getXYPolyPoints()[0], shapeLayers[shapeLayers.length - 1].getXYZPointCollection().getXYPolyPoints()[1], shapeLayers[shapeLayers.length - 1].getXYZPointCollection().getXYPolyPoints()[0].length);
        return p.contains(x, y);
    }
    
    public boolean visibleSideContainsPoint(int x, int y)
    {
        for(int i = 0; i < visibleSidePolygons.length; i++)
        {
            for(Polygon p : visibleSidePolygons[i])
            {
                if(p.contains(x, y))
                {
                    return true;
                }
            }
        }
        return topSideContainsPoint(x,y);
    }
    
    public void setShapeLayers(ShapeLayer[] newLayers)
    {
        shapeLayers = newLayers;
    }
    
    public Plane getBoundPlane()
    {
        return boundPlane;
    }
    
    //Think about how to add functionality so that the lower layerShape doesn't need to have the same number of points as the one above it -- e.g. figure out how to connect the docts (have two points merge into one)
    //Make drawing make more sense with color -- can't really pass a color to draw, implements draw.
    @Override
    public void draw(Graphics g) 
    {
        drawTopPolygon(g);
        drawSidePolygons(g, g.getColor());
    }
    
    public XYZPoint getMidpointAtIndex(int index)
    {
        return shapeLayers[index].getXYZPointCollection().getMidpoint();
    }
    
    public XYZPoint getAverageMidpoint(){
        double xAvg = 0, yAvg = 0, zAvg = 0;
        for(ShapeLayer sl : shapeLayers){
            XYZPoint mid = sl.getXYZPointCollection().getMidpoint();
            xAvg += mid.getX(); yAvg += mid.getY(); zAvg += mid.getZ();
        }
        xAvg /= (double)(shapeLayers.length);
        yAvg /= (double)(shapeLayers.length);
        zAvg /= (double)(shapeLayers.length);
        return new XYZPoint(boundPlane, xAvg, yAvg, zAvg);
    }
    
    private void drawSidePolygons(Graphics g, Color topColor)
    {
        for (int i = 0; i < visibleSidePolygons.length; i++)
        {
            PolygonShader.simpleShadeSidePolygons(g, visibleSidePolygons[i], shapeLayers[i].getColor());
        }
    }
    
    private void drawTopPolygon(Graphics g)
    {
        shapeLayers[shapeLayers.length - 1].draw(g);
    }
    
    private void updateSolidPolyPoints()
    {
        for (int i = 0; i < solidPolyPoints.length; i++) 
        {
            solidPolyPoints[i] = shapeLayers[i].getXYZPointCollection().getXYPolyPoints();//fills the array of polypoints
        }
    }
    
    private void updateVisibleSidePolygons()
    {
        for(int i = 0; i < visibleSidePolygons.length; i++)
        {
            visibleSidePolygons[i].clear();
        }
        for (int i = 1; i < solidPolyPoints.length; i++) 
        {
            int closestIndex = closestIndexToVisibleLowerBound(shapeLayers[i].getXYZPointCollection());
            for (int j = closestIndexToVisibleLowerBound(shapeLayers[i].getXYZPointCollection()) - getNumVisibleSidesInLayer(i); j < closestIndexToVisibleLowerBound(shapeLayers[i].getXYZPointCollection()); j++) 
            {
                int startPoint = j;
                int endPoint = j+1;
                if(startPoint < 0)
                {
                    startPoint += solidPolyPoints[i][0].length;
                }
                if(endPoint < 0)
                {
                    endPoint += solidPolyPoints[i][0].length;
                }
                int[] xPoints = {solidPolyPoints[i][0][startPoint], solidPolyPoints[i][0][endPoint], solidPolyPoints[i-1][0][endPoint], solidPolyPoints[i-1][0][startPoint]};
                int[] yPoints = {solidPolyPoints[i][1][startPoint], solidPolyPoints[i][1][endPoint], solidPolyPoints[i-1][1][endPoint], solidPolyPoints[i-1][1][startPoint]};
                visibleSidePolygons[i-1].add(new Polygon(xPoints, yPoints, xPoints.length));   
            }
        }
    }
    
    private int getNumVisibleSidesInLayer(int layerNum)//adjust for odd-sided shape layers that can have two possible visible sides showing?
    {
        return (int)Math.round((double)solidPolyPoints[layerNum][0].length/2.0);
    }
    
    private int closestIndexToVisibleLowerBound(XYZPointCollection xyzPointCollection)
    {
        double range[] = boundPlane.getSpinQuadrant().getVisibleAngleRange();
        int closestIndex = 0;
        double difference = getDifferenceOfAngles(xyzPointCollection.getRelativeOffsetAngleFromMidpointAtIndex(0), range[0]);
        for (int i = 1; i < xyzPointCollection.getXYZPointsLength(); i++) {
            if(getDifferenceOfAngles(xyzPointCollection.getRelativeOffsetAngleFromMidpointAtIndex(i), range[0]) < difference)
            {
                
                difference = getDifferenceOfAngles(xyzPointCollection.getRelativeOffsetAngleFromMidpointAtIndex(i), range[0]);
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    public void translate(XYZPoint translatePoint)
    {
        for(ShapeLayer sl : shapeLayers)
        {
            sl.translate(translatePoint);
        }
    }
    
    /*
    NOTE: Returns sort distance constant from the BOTTOM shape layer only. Ideally would return the point that is the closest to the front's sort distance constant.
    */
    @Override
    public double getSortDistanceConstant() 
    {
        return shapeLayers[0].getSortDistanceConstant();
    }
    
    @Override
    public double getBackSortDistanceConstant(){
        return shapeLayers[0].getBackSortDistanceConstant();
    }
    
    @Override
    public double getZ(){
        return shapeLayers[shapeLayers.length - 1].getHighestPoint().getZ();
    }
    
    /*
    SWITCH THIS TO USE ANGLEMATH'S IMPLEMENTATION
    */
    private double getDifferenceOfAngles(double base, double subtract)
    {
        double giveReturn = Math.abs(base - subtract);
        if(giveReturn > Math.PI)
        {
            giveReturn = Math.abs(base+Math.PI*2.0 - subtract);
        }
        return giveReturn;
    }

    @Override
    public void update() 
    {
        for(ShapeLayer shapeLayer : shapeLayers)
        {
            shapeLayer.update();
        }
        
        updateSolidPolyPoints();
        updateVisibleSidePolygons();
    }

    /***Currently no use for updating all layered solids on mouse interaction. Implemented so that subclasses can implement UpdatableOnMouseClick***/
    @Override
    public void updateOnMouseClick() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void updateOnMouseHold() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
