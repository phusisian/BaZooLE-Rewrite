package shiftplanning;

import containers.GamePanel;
import inputs.MouseInput;
import java.awt.Color;
import java.awt.Graphics;
import shiftcolor.ColorPalette;


public class BasePlane extends Plane implements ThreeDDrawable
{
    private Plane[] planes = new Plane[0];
    private TraversableLogic traversableLogic;
    
    public BasePlane(GamePanel boundPanelIn, double xPos, double yPos, double width, double length) {
        super(boundPanelIn, xPos, yPos, 0, width, length);
        traversableLogic = new TraversableLogic(this);
        getShape().setColor(ColorPalette.baseWaterColor);
        addRandomTilesToThreeDDrawables();
        SpinTile st = SpinTile.createTileFromCenterPoint(this, new XYZPoint(this, 0, 0, 2), 1);//Tile.createTileUsingBottomLeftCorner(this, new XYZPoint(this, -1, -1, 1), 2, 2);
    }
    
    public void addPlane(Plane p){
        Plane[] temp = new Plane[planes.length + 1];
        for (int i = 0; i < planes.length; i++) {
            temp[i] = planes[i];
        }
        temp[planes.length] = p;
        planes = temp;
    }
    
    public boolean isTraversableClicked(){
        for(Traversable t : traversableLogic.getTraversables()){
            if(t.getShape().getAsPolygon().contains(MouseInput.mouseX, MouseInput.mouseY)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public BasePlane getBasePlane(){
        return this;
    }
    
    public void addTraversable(Traversable t){
        traversableLogic.addTraversable(t);
    }
    
    public TraversableLogic getTraversableLogic(){
        return traversableLogic;
    }
    
    public Plane[] getPlanes(){
        return planes;
    }
    
    @Override
    public void update(){
        super.update();
        for(Plane p : planes){
            p.update();
        }
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
    }
    /***Not implemented yet. Necessary?***/
    //@Override
    public double getBackSortDistanceConstant(){
        return -1;
    }
    
    /***Not implemented yet. Necessary?***/
    @Override
    public double getSortDistanceConstant(){
        return -1;
    }
}
