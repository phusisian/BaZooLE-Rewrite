package shiftplanning;

import java.awt.Color;
import shiftcolor.ColorPalette;

public class Tree extends Scenery{

    private static final Color logColor = new Color(86, 53, 17);
    public Tree(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn, XYZPoint placementPoint) {
        super(boundPlaneIn, boundSolidIn);
        initScenerySolids(boundPlaneIn, placementPoint);
    }

    public static Tree createTreeAndAddToLists(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn, XYZPoint placementPoint){
        Tree t = new Tree(boundPlaneIn, boundSolidIn, placementPoint);
        for(LayeredSolid ls : t.getScenerySolids().getSolids()){
            boundSolidIn.addThreeDDrawable(ls);
            boundSolidIn.addUpdatable(ls);
        }
        return t;
    }
    
    @Override
    public void initScenerySolids(Plane boundPlaneIn, XYZPoint placementPoint) {
        LayeredSolid[] solids = new LayeredSolid[6];
        solids[0] = getLogSolid(placementPoint);
        
        /*
        Below needs to be better tuned later, basically copy-pasted (and adapted) from old Tree code.
        */
        double addNum = .2;
        
        double initialHeight = .1;
        double upperHeight = addNum * 4 + initialHeight;
        double scaleNumber = 1.0;
        int numShapes = 1;
        for(double heightCount = initialHeight; heightCount < upperHeight; heightCount+= addNum)
        {
            //top side is .175 units smaller than bottom side.
            TruncatedPyramid tp = TruncatedPyramid.createPyramidUsingIdealPolygons(boundPlaneIn, placementPoint.getTranslatedPoint(0, 0, heightCount), placementPoint.getTranslatedPoint(0,0, heightCount + addNum), scaleNumber*(1.0/4.0), (scaleNumber*(1.0/4.0))-0.1, 4, ColorPalette.defaultGrassColor);
            scaleNumber -= 0.15;
            solids[numShapes] = tp;
            numShapes++;
        }
        solids[5] = Pyramid.createPyramidUsingIdealPolygon(boundPlaneIn, placementPoint.getTranslatedPoint(0, 0, upperHeight), placementPoint.getTranslatedPoint(0, 0, upperHeight + addNum), .25*(1.0/4.0), 4, ColorPalette.defaultGrassColor);
        SolidCollection sc = new SolidCollection(boundPlaneIn, solids);
        setScenerySolids(sc);
    }
    
    private LayeredSolid getLogSolid(XYZPoint placementPoint){
        LayeredSolid[] solids = new LayeredSolid[2];
        XYZPoint basePoint = new XYZPoint(getBoundPlane(), placementPoint.getX(), placementPoint.getY(), placementPoint.getZ() + 0);
        XYZPoint secondPoint = new XYZPoint(getBoundPlane(), placementPoint.getX(), placementPoint.getY(), placementPoint.getZ() + .2);
        return Prism.createPrism(getBoundPlane(), basePoint, secondPoint, 1.0/14.0, 4, logColor);
    }

}
