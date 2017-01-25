package shiftplanning;

import updatables.Updatable;

/**
 *
 * @author phusisian
 */
public class XYZRect extends XYZPointCollection implements Updatable{

    public XYZRect(Plane boundPlaneIn, XYZPoint[] xyzPointsIn) {
        super(boundPlaneIn, xyzPointsIn);
    }
    
    public static XYZRect initAsRect(Plane boundPlaneIn, XYZPoint bottomLeftCorner, double width, double length){
        XYZPoint[] xyzPoints = new XYZPoint[4];//order of points will be bottom left, bottom right, top right, top left.
        xyzPoints[0] = bottomLeftCorner;
        xyzPoints[1] = bottomLeftCorner.getTranslatedPoint(width, 0, 0);
        xyzPoints[2] = bottomLeftCorner.getTranslatedPoint(width, length, 0);
        xyzPoints[3] = bottomLeftCorner.getTranslatedPoint(0, length, 0);
        return new XYZRect(boundPlaneIn, xyzPoints);
    }

    public double getWidth(){
        return getPoint(1).getX() - getPoint(0).getX();
    }
    
    public double getLength(){
        return getPoint(3).getY() - getPoint(0).getY();
    }
    
    public XYZPoint getPlacementPoint(){
        return getXYZPoints()[0];
    }
    
    public XYZPoint getPoint(int index){
        return getXYZPoints()[index];
    }
    
    /*
    public void addRect(XYZRect addRect){
        if(addRect.getPoint(0).getX() < getPoint(0).getX() || addRect.getPoint(0).getY() < getPoint(0).getY()){
            setPoint(0, addRect.getPoint(0));
            double dx = addRect.getPoint(0).getX() - getPoint(0).getX();
        }
        if(addRect.getPoint(1).getX() > getPoint(0).getX() || add)
    }*/
}
