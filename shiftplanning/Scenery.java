package shiftplanning;

import updatables.Updatable;

/**
 *
 * @author phusisian
 */
public abstract class Scenery implements Updatable{
    private SolidCollection scenerySolids;
    private ContainerLayeredSolid boundSolid;
    private XYZPoint relPoint;
    private Plane boundPlane;
    public Scenery(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn){
        boundSolid = boundSolidIn;
        boundSolid.getBoundPlane().addUpdatable(this);
        boundPlane = boundPlaneIn;
    }
    
    public Plane getBoundPlane(){
        return boundPlane;
    }
    
    public abstract void initScenerySolids(Plane boundPlaneIn, XYZPoint placementPoint);
    
    public void setScenerySolids(SolidCollection sc){
        scenerySolids = sc;
        setRelPoint();
    }
    
    public SolidCollection getScenerySolids(){
        return scenerySolids;
    }
    
    public LayeredSolid getBoundSolid(){
        return boundSolid;
    }
    
    private void setRelPoint(){
        XYZPoint mid = scenerySolids.getAverageMidpoint();
        XYZPoint solidCorner = boundSolid.getBaseBounds().getPlacementPoint();
        
        relPoint = new XYZPoint(boundSolid.getBoundPlane(), mid.getX() - solidCorner.getX(), mid.getY() - solidCorner.getY(), mid.getZ() - solidCorner.getZ());
    }
    //returns the point to translate to if the solid it is on has moved so that it stays fixed onto the solid
    private XYZPoint getTranslatePoint(){
        XYZPoint mid = scenerySolids.getAverageMidpoint();
        XYZPoint solidCorner = boundSolid.getBaseBounds().getPlacementPoint();
        double dx =  solidCorner.getX() + relPoint.getX() - mid.getX();
        double dy =  solidCorner.getY() + relPoint.getY() - mid.getY();
        double dz =  solidCorner.getZ() + relPoint.getZ() - mid.getZ();
        //double dy =  mid.getY() - solidCorner.getY();
        //double dz =  mid.getZ() - solidCorner.getZ();
        return new XYZPoint(boundSolid.getBoundPlane(), dx, dy, dz);
    }
    
    @Override
    public void update() 
    {
        if(scenerySolids != null){
            scenerySolids.translate(getTranslatePoint());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
