package shiftplanning;

/**
 *
 * @author phusisian
 */
public class XYZLine {
    private XYZPoint start, end;
    private double xComp, yComp, zComp;
    public XYZLine(XYZPoint startIn, XYZPoint endIn){
        start = startIn;
        end = endIn;
        xComp = end.getX() - start.getX();
        yComp = end.getY() - start.getY();
        zComp = end.getZ() - start.getZ();
    }
    
    
    
    public Plane getBoundPlane(){
        return start.getBoundPlane();
    }
    
    public XYZPoint getExtensionPoint(double magIn){
        double mag = getMagnitude();
        double ratio = magIn/mag;
        return new XYZPoint(start.getBoundPlane(), start.getX() + xComp * ratio, start.getY() + yComp*ratio, start.getZ() + zComp*ratio);
    }
    
    public XYZPoint getIntersectionWithinBounds(XYZLine compareLine){
        //almost working: double t2 = ( ((compareLine.getStart().getY() - start.getY())/yComp)- compareLine.getStart().getX() + start.getX() )/(compareLine.getXComp() - (compareLine.getYComp()/yComp));
        double t2 = ( ( (compareLine.getStart().getY() - start.getY() )/yComp) - compareLine.getStart().getX() + start.getX() )/(compareLine.getXComp() - (compareLine.getYComp()/yComp));
        //almost working: double t1 = (compareLine.getYComp()*t2 + compareLine.getStart().getY() - start.getY())/(yComp);
        double t1 = (compareLine.getYComp()*t2 + compareLine.getStart().getY() - start.getY())/(yComp);
        if(t1 < 1 && t2 < 1 && t1 > 0 && t2 > 0){
            double xIntersect = start.getX() + xComp*t1;
            double yIntersect = start.getY() + yComp*t1;
            double zIntersect = start.getZ() + zComp*t1;
            return new XYZPoint(compareLine.getBoundPlane(), xIntersect, yIntersect, zIntersect);
        }
        return null;
    }
    
    public double getMagnitude(){
        return Math.sqrt( Math.pow(xComp, 2) + Math.pow(yComp, 2) + Math.pow(zComp, 2) );
    }
    
    public double getXYAngle(){
        return Math.atan2(yComp, xComp);
    }
    
    public double getXComp(){
        return xComp;
    }
    
    public double getYComp(){
        return yComp;
    }
    
    public double getZComp(){
        return zComp;
    }
    
    public XYZPoint getStart(){
        return start;
    }
    
    public XYZPoint getEnd(){
        return end;
    }
    
    
}
