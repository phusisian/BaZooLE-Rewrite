package shiftplanning;

import java.awt.BasicStroke;
import updatables.Updatable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/*Holds a single layer to a shape made up of layers (e.g. rocketship)*/
public class ShapeLayer implements ThreeDDrawable, Updatable
{   private Color color;
    private Plane boundPlane;
    private XYZPointCollection xyzPointCollection; //holds the XYZPoints that make up this layer
    private XYZRect layerBounds;
    
    public ShapeLayer(Plane boundPlaneIn, XYZPointCollection xyzPointCollectionIn, Color colorIn)
    {
        boundPlane = boundPlaneIn;
        xyzPointCollection = xyzPointCollectionIn;
        color = colorIn;
        setLayerBounds();
    }
    
    public void setColor(Color c){
        color = c;
    }
    
    public Color getColor(){
        return color;
    }
    
    public XYZRect getLayerBounds(){
        return layerBounds;
    }
    
    public XYZPoint getHighestPoint(){
        return xyzPointCollection.getHighestPoint();
    }
    
    public static ShapeLayer createShapeFromLineWithStroke(XYZPointCollection pointCollection, double stroke, Color c){
        XYZPoint[] points = pointCollection.getXYZPoints();
        XYZPoint[] rightPoints = new XYZPoint[points.length];
        XYZPoint[] leftPoints = new XYZPoint[points.length];
        double normalOne = points[0].getAngleToPoint(points[1])+(Math.PI/2.0);
        rightPoints[0] = new XYZPoint(pointCollection.getBoundPlane(), points[0].getX() - stroke*Math.cos(normalOne), points[0].getY() - stroke*Math.sin(normalOne), points[0].getZ());
        leftPoints[0] = new XYZPoint(pointCollection.getBoundPlane(), points[0].getX() + stroke*Math.cos(normalOne), points[0].getY() + stroke*Math.sin(normalOne), points[0].getZ());
        
        for(int i = 1; i < points.length - 1; i++){
            double avgAngle = (Math.PI/2.0)+((points[i-1].getAngleToPoint(points[i]) + points[i].getAngleToPoint(points[i+1]))/2.0);
            double halfAngle = avgAngle - (points[i-1].getAngleToPoint(points[i]));
            double angledHyp = stroke/Math.sin(halfAngle);
            rightPoints[i] = new XYZPoint(pointCollection.getBoundPlane(), points[i].getX() - angledHyp*Math.cos(avgAngle), points[i].getY() - angledHyp*Math.sin(avgAngle), points[i].getZ());
            leftPoints[i] = new XYZPoint(pointCollection.getBoundPlane(), points[i].getX() + angledHyp*Math.cos(avgAngle), points[i].getY() + angledHyp*Math.sin(avgAngle), points[i].getZ());
        }
        double normalTwo = points[points.length - 2].getAngleToPoint(points[points.length - 1])+(Math.PI/2.0);
        rightPoints[rightPoints.length - 1] = new XYZPoint(pointCollection.getBoundPlane(), points[points.length - 1].getX() - stroke*Math.cos(normalTwo), points[points.length - 1].getY() - stroke*Math.sin(normalTwo), points[points.length - 1].getZ());
        leftPoints[leftPoints.length - 1] = new XYZPoint(pointCollection.getBoundPlane(), points[points.length - 1].getX() + stroke*Math.cos(normalTwo), points[points.length - 1].getY() + stroke*Math.sin(normalTwo), points[points.length - 1].getZ());
        int count = 0;
        XYZPoint[] strokePoints = new XYZPoint[points.length * 2];
        for(int i = 0; i < rightPoints.length; i++){
            strokePoints[count] = rightPoints[i];
            count++;
        }
        for(int i = leftPoints.length - 1; i >= 0; i--){
            strokePoints[count] = leftPoints[i];
            count++;
        }
        XYZPointCollection collection = new XYZPointCollection(pointCollection.getBoundPlane(), strokePoints);
        return new ShapeLayer(pointCollection.getBoundPlane(), collection, c);
    }
    
    public void setPoint(int index, XYZPoint p){
        xyzPointCollection.setPoint(index, p);
    }
    
    public Polygon getAsPolygon(){
        int[][] polyPoints = xyzPointCollection.getXYPolyPoints();
        return new Polygon(polyPoints[0], polyPoints[1], polyPoints[0].length);
    }
    
    /*
    public static ShapeLayer createShapeFromLineWithStroke(XYZPointCollection pointCollection, double stroke){
        XYZPoint[] points = pointCollection.getXYZPoints();
        ArrayList<XYZPoint> rightPoints = new ArrayList<XYZPoint>();
        ArrayList<XYZPoint> leftPoints = new ArrayList<XYZPoint>();
        //XYZPoint[] rightPoints = new XYZPoint[points.length];
        //XYZPoint[] leftPoints = new XYZPoint[points.length];
        
        //double normalOne = points[0].getAngleToPoint(points[1]) + (Math.PI/2.0);
        //rightPoints.add(new XYZPoint(points[0].getBoundPlane(), points[0].getX() + stroke*Math.cos(normalOne), points[0].getY() + stroke*Math.sin(normalOne), points[0].getZ()));
        //leftPoints.add(new XYZPoint(points[0].getBoundPlane(), points[0].getX() - stroke*Math.cos(normalOne), points[0].getY() - stroke*Math.sin(normalOne), points[0].getZ())); 
        
        
        
        for(int i = 0; i < points.length - 1; i++){
            double normalFirst = points[i].getAngleToPoint(points[i+1]) + (Math.PI/2.0);
            rightPoints.add(new XYZPoint(points[i].getBoundPlane(), points[i].getX() + stroke*Math.cos(normalFirst), points[i].getY() + stroke*Math.sin(normalFirst), points[i].getZ()));
            leftPoints.add(new XYZPoint(points[i].getBoundPlane(), points[i].getX() - stroke*Math.cos(normalFirst), points[i].getY() - stroke*Math.sin(normalFirst), points[i].getZ()));
            rightPoints.add(new XYZPoint(points[i+1].getBoundPlane(), points[i+1].getX() + stroke*Math.cos(normalFirst), points[i+1].getY() + stroke*Math.sin(normalFirst), points[i+1].getZ()));
            leftPoints.add(new XYZPoint(points[i+1].getBoundPlane(), points[i+1].getX() - stroke*Math.cos(normalFirst), points[i+1].getY() - stroke*Math.sin(normalFirst), points[i+1].getZ()));
        }
        
        XYZPoint[] strokedPoints = new XYZPoint[leftPoints.size() + rightPoints.size()];
        int count = 0;
        for(int i = 0; i < rightPoints.size(); i++){
            strokedPoints[count] = rightPoints.get(i);
            count++;
        }
        for(int i = leftPoints.size() - 1; i >= 0; i--){
            strokedPoints[count] = leftPoints.get(i);
            count++;
        }
        //System.out.println("Count: " + count + " Length: " + strokedPoints.length);
        
        XYZPointCollection collection = new XYZPointCollection(strokedPoints[0].getBoundPlane(), strokedPoints);
        collection.removePointIntersections();
        return new ShapeLayer(strokedPoints[0].getBoundPlane(), collection);
    }*/
    
    private void setLayerBounds(){
        XYZPoint[] xyzPoints = xyzPointCollection.getXYZPoints();
        double leftX = xyzPoints[0].getX();
        double rightX = xyzPoints[0].getX();
        double topY = xyzPoints[0].getY();
        double bottomY = xyzPoints[0].getY();
        double smallestZ = xyzPoints[0].getZ();
        for(int i = 1; i < xyzPoints.length; i++){
            if(xyzPoints[i].getX() < leftX){
                leftX = xyzPoints[i].getX();
            }
            if(xyzPoints[i].getX() > rightX){
                rightX = xyzPoints[i].getX();
            }
            if(xyzPoints[i].getY() > topY){
                topY = xyzPoints[i].getY();
            }
            if(xyzPoints[i].getY() < bottomY){
                bottomY = xyzPoints[i].getY();
            }
            if(xyzPoints[i].getZ() < smallestZ){
                smallestZ = xyzPoints[i].getZ();
            }
        }
        layerBounds = XYZRect.initAsRect(boundPlane, new XYZPoint(boundPlane, leftX, bottomY, smallestZ), rightX - leftX, topY - bottomY);
    }
    
    public XYZPointCollection getXYZPointCollection()
    {
        return xyzPointCollection;
    }
    
    public static ShapeLayer createFlatShapeLayerRectangle(Plane boundPlaneIn, XYZPoint bottomLeftCorner, double width, double length, Color c)
    {
        double startX = bottomLeftCorner.getX();
        double startY = bottomLeftCorner.getY();
        double z = bottomLeftCorner.getZ();
        XYZPoint[] xyzPoints = {bottomLeftCorner, new XYZPoint(boundPlaneIn, startX + width, startY, z), new XYZPoint(boundPlaneIn, startX+width, startY+length, z), new XYZPoint(boundPlaneIn, startX, startY+length, z)};
        return new ShapeLayer(boundPlaneIn, new XYZPointCollection(boundPlaneIn, xyzPoints), c);
    }
    
    public void translate(XYZPoint translatePoint)
    {
        xyzPointCollection.translate(translatePoint);
        layerBounds.translate(translatePoint);
    }
    
    public int getNumPoints()
    {
        return xyzPointCollection.getXYZPointsLength();
    }
    
    
    public XYZPoint getIntersectingEdgePoint(XYZPoint pivot, double theta){
        if(theta%(Math.PI/2.0) == 0){
            theta += 0.0000000000001;//is a hack to fix 90 degree angles not solving correctly (either because y is constant or because the function is indeterminant
        }
        XYZPoint[] surroundingPoints = getPointsSurroundingAngle(pivot, theta);
        for(XYZPoint xyzPoint : surroundingPoints){
            //System.out.print("XYZPoint: " + xyzPoint.getX() + ", " + xyzPoint.getY() + ", ");
        }
        double pointSlope;
        if((surroundingPoints[1].getX() - surroundingPoints[0].getX()) != 0){
            pointSlope = (surroundingPoints[1].getY() - surroundingPoints[0].getY())/(surroundingPoints[1].getX() - surroundingPoints[0].getX());
        }else{
            pointSlope = (surroundingPoints[1].getY() - surroundingPoints[0].getY())/(surroundingPoints[1].getX() - surroundingPoints[0].getX() + 0.00000001);
        }
                
        
        double thetaSlope = Math.tan(theta);
        //point slope function: y = pointSlope*x - pointSlope*surroundingPoints[0].getX() + surroundingPoints[0].getY();
        //theta slope function: y = thetaSlope*x - thetaSlope*vertex.getX() + vertex.getY()
        //pointSlope*x - pointSlope*surroundingPoints[0].getX() + surroundingPoints[0].getY() = thetaSlope*x - thetaSlope*vertex.getX() + vertex.getY()
        //x =  (-thetaSlope*vertex.getX() + vertex.getY() + pointSlope*surroundingPoints[0].getX() - surroundingPoints[0].getY())/(pointSlope - thetaSlope)
        double xIntercept =  (-thetaSlope*pivot.getX() + pivot.getY() + pointSlope*surroundingPoints[0].getX() - surroundingPoints[0].getY())/(pointSlope - thetaSlope);
        double yIntercept = thetaSlope*xIntercept - thetaSlope*pivot.getX() + pivot.getY();
        
        return new XYZPoint(pivot.getBoundPlane(), xIntercept, yIntercept, pivot.getZ());
    }
    
    public XYZPoint[] getPointsSurroundingAngle(XYZPoint pivot, double theta){
        XYZPoint[] points = {new XYZPoint(pivot.getBoundPlane(), 0, 0, 0), new XYZPoint(pivot.getBoundPlane(), 0, 0, 0)};
        XYZPoint[] xyzPoints = xyzPointCollection.getXYZPoints();
        for(int i = xyzPoints.length - 1; i >= 0; i--){
            double thetaClone = (theta + Math.PI)%(Math.PI*2.0);
            double lowerAngle, upperAngle;
            int lowerIndex, upperIndex;
            if(i != 0){
                upperIndex = i;
                lowerIndex = i - 1;
                
            }else{
                upperIndex = i;
                lowerIndex = xyzPoints.length - 1;
            }
            lowerAngle = pivot.getAngleToPoint(xyzPoints[lowerIndex]);
            upperAngle = pivot.getAngleToPoint(xyzPoints[upperIndex]);
            
            if(lowerAngle < 0){
                lowerAngle = Math.PI*2.0 + lowerAngle;
            }
            if(upperAngle < 0){
                upperAngle = Math.PI*2.0 + upperAngle;
            }
            if(upperAngle < lowerAngle){
                upperAngle += Math.PI*2.0;
            }
            
            if((thetaClone >= lowerAngle && thetaClone < upperAngle)){
                points[0] = xyzPoints[lowerIndex];
                points[1] = xyzPoints[upperIndex];
            }else if(thetaClone <= lowerAngle && thetaClone <= upperAngle%(Math.PI*2.0)){
                points[0] = xyzPoints[lowerIndex];
                points[1] = xyzPoints[upperIndex];
            }
        }
        return points;
    }
    
    public static ShapeLayer createShapeLayerUsingIdealPolygon(Plane boundPlaneIn, double centerX, double centerY, double z, double radius, int numSides, Color c)//make this take an XYZPoint instead of centerX, centerY, z
    {
        radius = 2*radius/Math.sqrt(2);
        XYZPoint[] points = new XYZPoint[numSides];
        double thetaConstant = (Math.PI*2.0)/(double)numSides;
        for (int i = 0; i < points.length; i++) 
        {
            double xPos = centerX + radius*Math.cos((Math.PI/4.0)+i*thetaConstant);
            double yPos = centerY + radius*Math.sin((Math.PI/4.0)+i*thetaConstant);
            points[i] = new XYZPoint(boundPlaneIn, xPos, yPos, z);
        }
        XYZPointCollection tempCollection = new XYZPointCollection(boundPlaneIn, points);
        return new ShapeLayer(boundPlaneIn, tempCollection, c);
    }
    
    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillPolygon(xyzPointCollection.getXYPolyPoints()[0], xyzPointCollection.getXYPolyPoints()[1], xyzPointCollection.getXYZPointsLength());
        g.setColor(Color.BLACK);
    }
    
    public void drawOutline(Graphics g, Color c, double strokeWidth){
        g.setColor(c);
        BasicStroke stroke = new BasicStroke((float)(strokeWidth*Unit.scale));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(stroke);
        g.drawPolygon(xyzPointCollection.getXYPolyPoints()[0], xyzPointCollection.getXYPolyPoints()[1], xyzPointCollection.getXYZPointsLength());
        g2.setStroke(new BasicStroke());
    }
    
    public double getMagnitudeToMidpoint(XYZPoint checkPoint){
        XYZPoint mid = xyzPointCollection.getMidpoint();
        return XYZPoint.getTotalMagnitudeBetweenPoints(mid, checkPoint);
    }

    /***Not sure how to handle sort distance. Using the front or back point has issues***/
    @Override
    public double getSortDistanceConstant() {
        return getBackSortDistanceConstant();//xyzPointCollection.getClosestPointToFront().getSortDistanceConstant();
    }
    
    //@Override
    public double getBackSortDistanceConstant(){
        return xyzPointCollection.getFarthestPointToFront().getSortDistanceConstant();
    }
    
    //@Override
    public double getZ(){
        return getHighestPoint().getZ();
    }
    
    @Override
    public void update()
    {
        xyzPointCollection.update();
    }
    
}
