package shiftplanning;

import inputs.MouseInput;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import updatables.MouseUpdatable;

/**
 *
 * @author phusisian
 */
public class Player implements TwoDDrawable, MouseUpdatable{
    private XYZPoint position;
    //might support multiple planes now? Probably will with some finagling.
    private Plane boundPlane;
    private Traversable clickedTraversable;
    private Traversable boundTraversable;
    private static final int numTicksPerUnit = 50;
    private ArrayList<XYZPoint> movementPoints;
    private int movementPointIndex = 0;
    
    public Player(Plane boundPlaneIn, XYZPoint positionIn){
        boundPlane = boundPlaneIn;
        boundPlane.addTwoDDrawable(this);
        position = positionIn;
        boundPlane.addMouseUpdatable(this);
        //setBoundTraversable();
    }
    
    private void setBoundTraversable(){
        boundTraversable = boundPlane.getBasePlane().getTraversableLogic().getNearestTraversableFromPoint(position);
    }
    
    private void setClickedTraversable(){
        clickedTraversable = boundPlane.getBasePlane().getTraversableLogic().getTraversableAtXYPoint(new Point(MouseInput.mouseX, MouseInput.mouseY));
        setMovementPoints();
        movementPointIndex = 0;
    }
    
    private void setMovementPoints(){
        ArrayList<Traversable> tempChain = boundPlane.getBasePlane().getTraversableLogic().getTraversableChain(boundTraversable);
        if(clickedTraversable != boundTraversable && boundPlane.getBasePlane().getTraversableLogic().traversableInChain(tempChain, clickedTraversable)){
            ArrayList<XYZPointCollection> tempCollections = boundPlane.getBasePlane().getTraversableLogic().getMovementPath(tempChain, boundTraversable, clickedTraversable);
            movementPoints = new ArrayList<XYZPoint>();
            for(XYZPointCollection pc : tempCollections){
                /***Half list point added for sake of correct path following order. WILL pose a problem in the puture for the player to follow
                Paths that don't make sense to just travel from midpoint to midpoint, but for basic right angles, this will function correctly.***/
                movementPoints.add(pc.getHalfListPoint());
            }
        }   
    }
    
    private void moveToPoint(XYZPoint endPoint){
        if(endPoint != null){
            int numTicksToMove = getNumTicksToMove(endPoint);
            XYZPoint translatePoint = XYZPoint.getMovementTranslatePoint(position, endPoint, numTicksToMove);
            position.translate(translatePoint);
            position.update();
            
            if(numTicksToMove == 0){
                position = endPoint.clone();
            }
        }
    }
    
    private int getNumTicksToMove(XYZPoint endPoint){
        return (int)(XYZPoint.getTotalMagnitudeBetweenPoints(position, endPoint) * numTicksPerUnit);
    }
    
    private void move(){
        if(movementPoints != null){
            XYZPoint nextPoint = movementPoints.get(movementPointIndex);
            moveToPoint(nextPoint);
            if(getNumTicksToMove(nextPoint) == 0){
                if(movementPointIndex < movementPoints.size() - 1){
                    movementPointIndex++;
                }else{
                    //movementCollections = null;
                    movementPoints = null;
                    //chain = null;
                    movementPointIndex = 0;
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        setBoundTraversable();
        move();
        g.setColor(Color.BLUE);
        position.draw(g);
    }

    @Override
    public void updateOnMouseClick() {
        setClickedTraversable();
    }

    @Override
    public void updateOnMouseHold() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
