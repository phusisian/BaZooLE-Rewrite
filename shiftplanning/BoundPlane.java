package shiftplanning;

import containers.GamePanel;
import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
public class BoundPlane extends Plane implements ThreeDDrawable
{
    private XYZPoint positionPoint;
    private Plane boundPlane;
    public BoundPlane(GamePanel boundPanelIn, Plane boundPlaneIn, XYZPoint posPointIn, double width, double length) {
        super(boundPanelIn, boundPlaneIn.convertCoordsToPoint(posPointIn).getX(), boundPlaneIn.convertCoordsToPoint(posPointIn).getY(), posPointIn.getZ(), width, length);
        boundPlane = boundPlaneIn;
        positionPoint = posPointIn;
    }
    
    @Override
    public BasePlane getBasePlane(){
        return boundPlane.getBasePlane();
    }
    
    /***Not implemented yet.***/
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(){
        positionPoint.update();
        XYPoint convertedPoint = getBasePlane().convertCoordsToPoint(positionPoint);
        setCenterPosX(convertedPoint.getX());
        setCenterPosY(convertedPoint.getY());
        setZPos(positionPoint.getZ());
        super.update();
    }
    
    /***Not implemented yet.***/
    @Override
    public double getSortDistanceConstant() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
