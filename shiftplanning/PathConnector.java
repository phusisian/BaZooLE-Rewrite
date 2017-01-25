package shiftplanning;

import updatables.Updatable;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author phusisian
 */
public class PathConnector implements TwoDDrawable, Updatable{
    public static final double connectorOverhang = 0.25;
    public static final double minMagnitudeConnection = .2;
    private XYZPointCollection movementPointCollection;
    private XYZPoint[] connectorPoints = new XYZPoint[2];
    private Traversable boundTraversable;
    public PathConnector(Traversable boundTraversableIn){
        boundTraversable = boundTraversableIn;
        movementPointCollection = boundTraversable.getMovementPoints();
        setConnectorPoints();
    }
    
    /*
    This relies on a very specific ordering of movement points.
    */
    private void setConnectorPoints(){
        XYZLine[] lines = movementPointCollection.getCollectionAsLines();
        XYZLine startLine = lines[0];
        XYZLine endLine = lines[lines.length - 2];
        connectorPoints[0] = movementPointCollection.getPoint(0);
        connectorPoints[1] = movementPointCollection.getPoint(movementPointCollection.getXYZPointsLength() - 1);
        //the below uses the old method of seeing if the points sticking out of the paths made contact with another path. 
        //changed to find distance between the edges of paths.
        //connectorPoints[0] = startLine.getExtensionPoint(- connectorOverhang);
        //connectorPoints[1] = endLine.getExtensionPoint(endLine.getMagnitude() + connectorOverhang);
    }
    
    public XYZPoint[] getConnectorPoints(){
        return connectorPoints;
    }
    
    public boolean traversableIsConnected(Traversable t){
        for(XYZPoint xyz : connectorPoints){
           
            for(XYZPoint compareXYZ : t.getConnectorPoints()){
                if(XYZPoint.getTotalMagnitudeBetweenPoints(compareXYZ, xyz) < minMagnitudeConnection){
                    return true;
                }
            }
            //used with old method to see if overhanging connector points made contact with the polygon of another path
            /*
            Point connectedPoint = xyz.getAsPoint();
            if(t.getShape().getAsPolygon().contains(connectedPoint)){// && xyz.getZ() == t.getBoundTile().getHeight()){
                //System.out.println("Returned true");
                return true;
                
            }*/
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        for(XYZPoint xyz : connectorPoints){
            xyz.draw(g);
        }
    }
    /*
        translate not used when magnitude of connector distance method is used instead of containment inside polygon
    */
    public void translate(XYZPoint translatePoint){
        for(XYZPoint xyz : connectorPoints){
            xyz.translate(translatePoint);
        }
    }

    @Override
    public void update() {
        for(XYZPoint xyz : connectorPoints){
            xyz.update();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
