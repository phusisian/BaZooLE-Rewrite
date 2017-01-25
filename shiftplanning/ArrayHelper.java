package shiftplanning;

import updatables.MouseUpdatable;
import updatables.Updatable;

/**
 *
 * @author phusisian
 */
public class ArrayHelper {
    public static Updatable[] addUpdatable(Updatable[] updatables, Updatable u){
        Updatable[] temp = new Updatable[updatables.length+1];
        for(int i = 0; i < updatables.length; i++){
            temp[i] = updatables[i];
        }
        temp[updatables.length] = u;
        return temp;
    }
    
    public static ThreeDDrawable[] addThreeDDrawable(ThreeDDrawable[] drawables, ThreeDDrawable d){
        ThreeDDrawable[] temp = new ThreeDDrawable[drawables.length + 1];
        for(int i = 0; i < drawables.length; i++){
            temp[i] = drawables[i];
        }
        temp[drawables.length] = d;
        return temp;
    }
    
    public static TwoDDrawable[] addTwoDDrawable(TwoDDrawable[] drawables, TwoDDrawable d){
        TwoDDrawable[] temp = new TwoDDrawable[drawables.length + 1];
        for(int i = 0; i < drawables.length; i++){
            temp[i] = drawables[i];
        }
        temp[drawables.length] = d;
        return temp;
    }
    
    public static MouseUpdatable[] addMouseUpdatable(MouseUpdatable[] updatables, MouseUpdatable u){
        MouseUpdatable[] temp = new MouseUpdatable[updatables.length + 1];
        for(int i = 0; i < updatables.length; i++){
            temp[i] = updatables[i];
        }
        temp[updatables.length] = u;
        return temp;
    }
}
