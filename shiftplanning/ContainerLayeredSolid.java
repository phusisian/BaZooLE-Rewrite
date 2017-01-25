package shiftplanning;

import java.awt.Graphics;
import updatables.MouseUpdatable;
import updatables.Updatable;

/**
 *
 * @author phusisian
 */
public class ContainerLayeredSolid extends LayeredSolid implements ThreeDDrawable, Updatable, MouseUpdatable{

    private Updatable[] updatables = new Updatable[0];
    private DistanceSorter distanceSorter;
    //private ThreeDDrawable[] threeDDrawables = new ThreeDDrawable[0];
    private TwoDDrawable[] twoDDrawables = new TwoDDrawable[0];
    
    public ContainerLayeredSolid(Plane boundPlaneIn, ShapeLayer[] shapeLayersIn) {
        super(boundPlaneIn, shapeLayersIn);
        distanceSorter = new DistanceSorter(boundPlaneIn, new ThreeDDrawable[0]);
    }
    
    public DistanceSorter getDistanceSorter(){
        return distanceSorter;
    }
    
    @Override
    public void update(){
        super.update();
        for(Updatable u : updatables){
            u.update();
        }
    }
    
    @Override
    public void draw(Graphics g){
        super.draw(g);
        for(TwoDDrawable twoD : twoDDrawables){
            twoD.draw(g);
        }
        for(ThreeDDrawable threeD : distanceSorter.getThreeDDrawables()){
            threeD.draw(g);
        }
    }
    
    public ThreeDDrawable[] getThreeDDrawables(){
        return distanceSorter.getThreeDDrawables();
    }
    
    public void addUpdatable(Updatable u){
        updatables = ArrayHelper.addUpdatable(updatables, u);
    }
    
    public void addThreeDDrawable(ThreeDDrawable d){
        distanceSorter.addThreeDDrawable(d);
        //threeDDrawables = ArrayHelper.addThreeDDrawable(threeDDrawables, d);
    }
    
    public void addTwoDDrawable(TwoDDrawable d){
        twoDDrawables = ArrayHelper.addTwoDDrawable(twoDDrawables, d);
    }

}
