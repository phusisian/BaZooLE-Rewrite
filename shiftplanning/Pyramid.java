package shiftplanning;

import java.awt.Color;

/**
 *
 * @author phusisian
 */
public class Pyramid extends LayeredSolid{

    public Pyramid(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn) {
        super(boundPlaneIn, shapeLayersIn);
    }
    
    public static Pyramid createPyramidUsingIdealPolygon(Plane boundPlaneIn, XYZPoint baseCenter, XYZPoint topCenter, double radius, int numSides, Color c){
        ShapeLayer[] shapeLayers = new ShapeLayer[2];
        shapeLayers[0] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, baseCenter.getX(), baseCenter.getY(), baseCenter.getZ(), radius, numSides, c);
        //XYZPoint[] tempPoints = {topCenter};
        shapeLayers[1] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, topCenter.getX(), topCenter.getY(), topCenter.getZ(), .01, numSides, c);//the reason that 0 is not used for radius is because visible side calculations will not work when dy/dx is 0. cannot get angle from it.
        return new Pyramid(boundPlaneIn, shapeLayers);
    }

}
