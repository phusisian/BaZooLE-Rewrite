package shiftmath;

/**
 *
 * @author phusisian
 */

/***currently doesn't work***/

public abstract class Function {
    private double leftBound, rightBound;
    private double increment;
    public Function(double leftBoundIn, double rightBoundIn){
        leftBound = leftBoundIn;
        rightBound = rightBoundIn;
    }
    
    public void setIncrement(double d){
        increment = d;
    }
    
    public Function(){
        
    }
    
    public void setLeftBound(double d){
        leftBound = d;
    }
    
    public void setRightBound(double d){
        rightBound = d;
    }
    
    public abstract double getReturn(double xIn);
}
