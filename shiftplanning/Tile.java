package shiftplanning;

import updatables.MouseUpdatable;
import updatables.Updatable;
import updatables.UpdatableOnQuadrantChange;
import inputs.MouseInput;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import shiftcolor.ColorPalette;

/**
 *
 * @author phusisian
 */

/*** Since I shifted from shoving every threeDDrawable into the plane to putting them into the tile (since the sort order algorithm 
 * didn't account for objects placed on top of each other), check for clones of the same things slowing down performance***/
public class Tile extends Prism implements MouseUpdatable, UpdatableOnQuadrantChange
{
    private static final int numTicksToMovePerUnit = 50;
    private boolean clicked = false;
    private double width, length;
    private double height;
    private Color color = Color.GREEN;
    private XYZPoint translateMovementPoint;//holds a point made up of dx, dy, dz of the tile's movement. 
    private int numTicksMoved = 0;
    //private Updatable[] updatables = new Updatable[0];
    //private ThreeDDrawable[] tileDrawables = new ThreeDDrawable[0];
    //private BentPath path;
    
    //consider moving a shape layer array into a class? ( I guess that's technically layered solid, which you need to pass to it to instantiate)
    
    public Tile(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn, double widthIn, double lengthIn, double zHeightIn)
    {
        super(boundPlaneIn, shapeLayersIn);
        width = widthIn;
        length = lengthIn;
        height = zHeightIn;
        
        
        XYZPoint randomPoint = new XYZPoint(getBoundPlane(), getBaseBounds().getPlacementPoint().getX() + 1, getBaseBounds().getPlacementPoint().getY() + .5, getShapeLayer(getLayerLength() - 1).getHighestPoint().getZ());
        
        Traversable traversable = BentPath.createAndAddToLists(getBoundPlane(), this, randomPoint, (int)(Math.random() * 4) * Math.PI/2.0);//currently not working for exact angles of 90 degrees. Band aid fix is to add a tiny tiny fraction to it.
        getBoundPlane().getBasePlane().addTraversable(traversable);
        Tree t = new Tree(getBoundPlane(), this, getRandomPointOnTile());
        LayeredSolid[] treeSolids = t.getScenerySolids().getSolids();
        for(LayeredSolid ls : treeSolids){
            addThreeDDrawable(ls);
        }
        Flower f = new Flower(getBoundPlane(), this, getRandomPointOnTile());
        LayeredSolid[] flowerSolids = f.getScenerySolids().getSolids();
        for(LayeredSolid ls : flowerSolids){
            addThreeDDrawable(ls);
        }
    }
    
    private XYZPoint getRandomPointOnTile(){
        XYZRect rect = getBaseBounds();
        double xRand = rect.getPlacementPoint().getX() + width * Math.random();
        double yRand = rect.getPlacementPoint().getY() + length * Math.random();
        return new XYZPoint(getBoundPlane(), xRand, yRand, height);
    }
    
    /***Originally took a zHeight double, but decided instead to make it so that the height of the bottom left corner XYZPoint would be used.***/
    public static Tile createTileUsingBottomLeftCorner(Plane boundPlaneIn, XYZPoint bottomLeftCorner, double width, double length)
    {
        ShapeLayer layer2 = ShapeLayer.createFlatShapeLayerRectangle(boundPlaneIn, bottomLeftCorner, width, length, ColorPalette.defaultGrassColor);
        XYZPoint newPoint = new XYZPoint(boundPlaneIn, bottomLeftCorner.getX(), bottomLeftCorner.getY(), 0);//bottomLeftCorner.getZ() + bottomLeftCorner.getZ());
        ShapeLayer layer1 = ShapeLayer.createFlatShapeLayerRectangle(boundPlaneIn, newPoint, width, length, ColorPalette.defaultGrassColor);
        ShapeLayer[] layers = {layer1, layer2};
        Tile t = new Tile(boundPlaneIn, layers, width, length, bottomLeftCorner.getZ());
        boundPlaneIn.addMouseUpdatable(t);
        boundPlaneIn.addUpdatable(t);
        boundPlaneIn.addQuadrantUpdatable(t);
        boundPlaneIn.addThreeDDrawable(t);
        return t;
    }
    
    /*
    public void addThreeDDrawable(ThreeDDrawable u){
        ThreeDDrawable[] temp = new ThreeDDrawable[tileDrawables.length + 1];
        for (int i = 0; i < tileDrawables.length; i++) {
            temp[i] = tileDrawables[i];
        }
        temp[tileDrawables.length] = u;
        tileDrawables = temp;
    }*/
    
    /*
    public void addUpdatable(Updatable u){
        Updatable[] temp = new Updatable[updatables.length + 1];
        for (int i = 0; i < updatables.length; i++) {
            temp[i] = updatables[i];
        }
        temp[updatables.length] = u;
        updatables = temp;
    }*/
    
    public void setHeight(double heightIn)
    {
        height = heightIn;
    }
    
    public void setWidth(double widthIn)
    {
        width = widthIn;
    }
    
    public void setLength(double lengthIn)
    {
        length = lengthIn;
    }
    
    @Override
    public void updateOnMouseHold() 
    {
        
    }
    
    private void respondToClick()
    {
        if(visibleSideContainsPoint(MouseInput.clickX, MouseInput.clickY) && !getBoundPlane().getBasePlane().isTraversableClicked()){
            clicked = true;
            color = Color.RED;
            getShapeLayer(getLayerLength() - 1).setColor(color);
        }
    }
    
    private void respondToUnclick()
    {
        if(!visibleSideContainsPoint(MouseInput.mouseX, MouseInput.mouseY) && !getBoundPlane().getBasePlane().isTraversableClicked()){
            if(getTranslateMovementPoint() != null)
            {
                translateMovementPoint = getTranslateMovementPoint();

                //translate(getTranslateMovementPoint());
            }
            //setCenter(MouseInput.mouseCoordsOnPlane);
            clicked = false;
            color = ColorPalette.defaultGrassColor;
            getShapeLayer(getLayerLength() - 1).setColor(color);
        }else{
            clicked = false;
            color = ColorPalette.defaultGrassColor;
            getShapeLayer(getLayerLength() - 1).setColor(color);
        }
    }
    /*
    private boolean isClickMoveX()
    {
        return (MouseInput.mouseCoordsOnPlane.getY() > clickMidpoint.getY() - (length/2.0) && MouseInput.mouseCoordsOnPlane.getY() < clickMidpoint.getY() + (length/2.0));
        
    }
    
    private boolean isClickMoveY()
    {
        return (MouseInput.mouseCoordsOnPlane.getX() > clickMidpoint.getX() - (width/2.0) && MouseInput.mouseCoordsOnPlane.getX() < clickMidpoint.getX() + (width/2.0));
    }
*/
    
    private boolean isClickMoveX(){
        return(MouseInput.mouseCoordsOnPlane.getY() > getBaseBounds().getPlacementPoint().getY() && MouseInput.mouseCoordsOnPlane.getY() < getBaseBounds().getPlacementPoint().getY() + length);
    }
    
    private boolean isClickMoveY(){
        return(MouseInput.mouseCoordsOnPlane.getX() > getBaseBounds().getPlacementPoint().getX() && MouseInput.mouseCoordsOnPlane.getX() < getBaseBounds().getPlacementPoint().getX() + width);
    }
    
    private XYZPoint getTranslateMovementPoint()
    {
        XYZPoint placePoint = getBaseBounds().getPlacementPoint();
        if(isClickMoveX()){
            if(MouseInput.mouseCoordsOnPlane.getX() < placePoint.getX()){
                return new XYZPoint(getBoundPlane(), (int)(MouseInput.mouseCoordsOnPlane.getX() - placePoint.getX()) - 1, 0, 0);
            }else{
                return new XYZPoint(getBoundPlane(), (int)(MouseInput.mouseCoordsOnPlane.getX() - width - placePoint.getX()) + 1, 0, 0);
            }
        }else if(isClickMoveY()){
            if(MouseInput.mouseCoordsOnPlane.getY() < placePoint.getY()){
                return new XYZPoint(getBoundPlane(), 0, (int)(MouseInput.mouseCoordsOnPlane.getY() - placePoint.getY()) - 1, 0);
            }else{
                return new XYZPoint(getBoundPlane(), 0, (int)(MouseInput.mouseCoordsOnPlane.getY() - length - placePoint.getY()) + 1, 0);
            }
        }
        return null;
    }
    
    private void moveAndUpdateSortOrder()
    {
        if(translateMovementPoint != null && numTicksMoved < numUnitsToMove())
        {
            translate(new XYZPoint(getBoundPlane(), translateMovementPoint.getX()/(double)numUnitsToMove(), translateMovementPoint.getY()/(double)numUnitsToMove(), 0));
            getBoundPlane().updateDistanceSorter();
            numTicksMoved++;
        }else{
            translateMovementPoint = null;
            numTicksMoved = 0;
        }
    }
    
    private int numUnitsToMove()
    {
        return (int)Math.abs(translateMovementPoint.getX() * numTicksToMovePerUnit + translateMovementPoint.getY() * numTicksToMovePerUnit);
    }
    
    public void updateOnMouseClick()
    {
        if(!clicked)
        {
            respondToClick();
        }else if(clicked){
            respondToUnclick();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        super.draw(g);
        //path.draw(g);
        /*if(clicked){
            ArrayList<Traversable> traversableList = getBoundPlane().getBasePlane().getTraversableLogic().getTraversableChain(path);
            for(Traversable t : traversableList){
                //note that if a circle isn't drawn, this isn't (probably) because the path connection system is broken, 
                //just that the draw order makes other tiles draw on top of the circle.
                g.setColor(Color.RED);
                t.getMovementPoints().getPoint(1).draw(g);
            }
        }*/
       
            
        
        /*for(ThreeDDrawable threeD : tileDrawables){
            threeD.draw(g);
        }*/
        
    }
    
    
    @Override
    public void update()
    {
        super.update();
        moveAndUpdateSortOrder();
        //Updatables of the tile should probably be moved to the bigger list of the entire plane's updatables?
        
    }

    @Override
    public void updateOnQuadrantChange() {
        getDistanceSorter().updateOnQuadrantChange();
    }
}
