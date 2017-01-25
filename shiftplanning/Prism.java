/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import java.awt.Color;

/**
 *
 * @author phusisian
 */
public class Prism extends ContainerLayeredSolid
{   
    /*** Class doesn't have much use. Having it "do" the work of passing to super seems dumb. Just add the same functionality to super and cut the class in general.***/
    public Prism(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn)
    {
        super(boundPlaneIn, shapeLayersIn);
    }
    
    public static Prism createPrism(Plane boundPlaneIn, XYZPoint baseCenter, XYZPoint topCenter, double radius, int numSides, Color c)
    {
        ShapeLayer[] shapeLayers = calculateShapeLayersToPass(boundPlaneIn, baseCenter, topCenter, radius, numSides, c);
        return new Prism(boundPlaneIn, shapeLayers);
    }
    
    private static ShapeLayer[] calculateShapeLayersToPass(Plane boundPlaneIn, XYZPoint baseCenter, XYZPoint topCenter, double radius, int numSides, Color c)
    {
        ShapeLayer[] shapeLayers = new ShapeLayer[2];
        shapeLayers[0] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, baseCenter.getX(), baseCenter.getY(), baseCenter.getZ(), radius/Math.sqrt(2), numSides, c);
        shapeLayers[1] = ShapeLayer.createShapeLayerUsingIdealPolygon(boundPlaneIn, topCenter.getX(), topCenter.getY(), topCenter.getZ(), radius/Math.sqrt(2), numSides, c);
        return shapeLayers;
    }
}
