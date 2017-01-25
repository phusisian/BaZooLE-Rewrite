package shiftplanning;

import updatables.Updatable;
import java.awt.Color;
import java.awt.Graphics;


public class BentPath implements TwoDDrawable, Traversable, Updatable {

    private final Color darkBrown = new Color(125, 62, 17);
    private final Color darkerBrown = new Color(115, 59, 18);
    private final Color evenDarkerBrown = new Color(102, 52, 16);
    private double theta;
    private XYZPoint vertex;
    private ContainerLayeredSolid boundSolid;
    private XYZPointCollection movementPoints;
    private XYZPoint relativeTilePoint;
    private ShapeLayer pathShape;
    private PathConnector pathConnector;
    
    public BentPath(ContainerLayeredSolid boundSolidIn, XYZPoint vertexIn, double thetaIn){
        boundSolid = boundSolidIn;
        vertex = vertexIn;
        theta = thetaIn;
        double relativeTileX = vertex.getX() - boundSolid.getPlacementPoint().getX();
        double relativeTileY = vertex.getY() - boundSolid.getPlacementPoint().getY();
        relativeTilePoint = new XYZPoint(boundSolid.getBoundPlane(), relativeTileX, relativeTileY, boundSolid.getHeight());
        
        setMovementPoints();
        pathShape = ShapeLayer.createShapeFromLineWithStroke(movementPoints, .1, Color.BLUE);
        pathConnector = new PathConnector(this);
        pathShape.setColor(darkerBrown);
    }
    
    public static BentPath createAndAddToLists(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn, XYZPoint vertexIn, double thetaIn){
        BentPath bp = new BentPath(boundSolidIn, vertexIn, thetaIn);
        boundSolidIn.addTwoDDrawable(bp);
        boundSolidIn.addUpdatable(bp);
        return bp;
    }
    
    
    private void setMovementPoints(){
        XYZPoint[] movementPointArray = new XYZPoint[3];
        //The order of these points DO matter, as the path the player takes will be reversed if they are in the wrong order. Just reverse them if the path is reversed.
        //Code that "un-reverses" the point order if, say, the player is moving backwards across it, will be fitted around a certain ordering.
        movementPointArray[1] = vertex;
        movementPointArray[2] = boundSolid.getShapeLayer(boundSolid.getLayerLength() - 1).getIntersectingEdgePoint(vertex, theta - (Math.PI/2.0));
        movementPointArray[0] = boundSolid.getShapeLayer(boundSolid.getLayerLength() - 1).getIntersectingEdgePoint(vertex, theta);
        movementPoints = new XYZPointCollection(boundSolid.getBoundPlane(), movementPointArray);
    }
    
    @Override
    public void draw(Graphics g) {
        movementPoints.draw(g);
        pathShape.draw(g);
        pathShape.drawOutline(g, evenDarkerBrown, 1.0);
        pathConnector.draw(g);
    }

    @Override
    public XYZPointCollection getMovementPoints() {
        return movementPoints;
    }

    @Override
    public void update() {
        movementPoints.update();
        double newRelX = vertex.getX() - boundSolid.getPlacementPoint().getX();
        double newRelY = vertex.getY() - boundSolid.getPlacementPoint().getY();
        double dx = relativeTilePoint.getX() - newRelX;
        double dy = relativeTilePoint.getY() - newRelY;
        XYZPoint translatePoint = new XYZPoint(vertex.getBoundPlane(), dx, dy, 0);
        movementPoints.translate(translatePoint);
        pathShape.update();
        pathShape.translate(translatePoint);
        pathConnector.update();
        //pathConnector.translate(translatePoint);
    }
    
    @Override
    public LayeredSolid getBoundSolid(){
        return boundSolid;
    }
    
    @Override
    public boolean isConnected(Traversable t){
        return pathConnector.traversableIsConnected(t);
    }

    @Override
    public ShapeLayer getShape() {
        return pathShape;
    }

    @Override
    public XYZPoint[] getConnectorPoints() {
        return pathConnector.getConnectorPoints();
    }
}                                                                           