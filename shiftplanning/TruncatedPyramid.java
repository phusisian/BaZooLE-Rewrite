package shiftplanning;

import java.awt.Color;

/**
 *
 * @author phusisian
 */
public class TruncatedPyramid extends LayeredSolid{
    
    private TruncatedPyramid(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn) {
        super(boundPlaneIn, shapeLayersIn);
    }
    
    public static TruncatedPyramid createPyramidUsingIdealPolygons(Plane boundPlaneIn, XYZPoint baseCenter, XYZPoint topCenter, double radiusBase, double radiusTop, int numSides, Color c){
        ShapeLayer[] layers = new ShapeLayer[2];
        layers[0] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, baseCenter.getX(), baseCenter.getY(), baseCenter.getZ(), radiusBase, numSides, c);
        layers[1] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, topCenter.getX(), topCenter.getY(), topCenter.getZ(), radiusTop, numSides, c);
        return new TruncatedPyramid(boundPlaneIn, layers);
    }

}
