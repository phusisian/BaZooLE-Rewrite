package shiftplanning;

import java.awt.Color;
import shiftcolor.ColorPalette;

/**
 *
 * @author phusisian
 */
public class Flower extends Scenery{

    private static final Color[] possibleFlowerColors = {new Color(176,23,31), new Color(238, 18, 137), new Color(218, 112, 214), new Color(128, 0, 128)};
    private static final double defaultStemHeight = .1;
    private static final double defaultPetalHeight = .05;
    private static final double defaultStemRadius = .0125;
    private static final double defaultPetalRadius = .025;
    
    
    public Flower(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn, XYZPoint placementPoint) {
        super(boundPlaneIn, boundSolidIn);
        initScenerySolids(boundPlaneIn, placementPoint);
    }
    
    public static Flower createFlowerAndAddToLists(Plane boundPlaneIn, ContainerLayeredSolid boundSolidIn, XYZPoint placementPoint){
        Flower f = new Flower(boundPlaneIn, boundSolidIn, placementPoint);
        for(LayeredSolid ls : f.getScenerySolids().getSolids()){
            boundSolidIn.addThreeDDrawable(ls);
            boundSolidIn.addUpdatable(ls);
        }
        return f;
    }

    /***Flowers have no petals currently***/
    @Override
    public void initScenerySolids(Plane boundPlaneIn, XYZPoint placementPoint) {
        LayeredSolid[] solids = new LayeredSolid[2];
        solids[0] = Prism.createPrism(boundPlaneIn, placementPoint, placementPoint.getTranslatedPoint(0,0,defaultStemHeight), defaultStemRadius, 4, ColorPalette.defaultGrassColor);
        solids[1] = Prism.createPrism(boundPlaneIn, placementPoint.getTranslatedPoint(0,0,defaultStemHeight), placementPoint.getTranslatedPoint(0,0,defaultStemHeight + defaultPetalHeight), defaultPetalRadius, 4, getRandomFlowerColor());
        SolidCollection sc = new SolidCollection(boundPlaneIn, solids);
        setScenerySolids(sc);
    }
    
    private Color getRandomFlowerColor() {
        return possibleFlowerColors[(int)(Math.random() * possibleFlowerColors.length)];
    }

}
