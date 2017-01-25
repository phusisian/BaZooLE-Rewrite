package shiftplanning;

/**
 *
 * @author phusisian
 */
public class SolidCollection{
    private Plane boundPlane;
    private LayeredSolid[] solids;
    public SolidCollection(Plane boundPlaneIn, LayeredSolid[] solidsIn){
        boundPlane = boundPlaneIn;
        solids = solidsIn;
        for(LayeredSolid ls : solids){
            boundPlane.addUpdatable(ls);
        }
    }
    
    public XYZPoint getAverageMidpoint(){
        double xAvg = 0, yAvg = 0, zAvg = 0;
        for(LayeredSolid ls : solids){
            XYZPoint mid = ls.getAverageMidpoint();
            xAvg += mid.getX(); yAvg += mid.getY(); zAvg += mid.getZ();
        }
        xAvg /= (double)(solids.length);
        yAvg /= (double)(solids.length);
        zAvg /= (double)(solids.length);
        return new XYZPoint(boundPlane, xAvg, yAvg, zAvg);
    }
    
    public LayeredSolid[] getSolids(){
        return solids;
    }
    
    public void translate(XYZPoint translatePoint){
        for(LayeredSolid ls : solids){
            ls.translate(translatePoint);
        }
    }
}
