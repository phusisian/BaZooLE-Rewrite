package shiftmath;

/**
 *
 * @author phusisian
 */

/***not working***/
public class Arc extends Function{
    private double radius;
    private double leftTheta, rightTheta;
    public Arc(double leftThetaIn, double rightThetaIn, double radiusIn, double incrementIn){
        super();
        radius = radiusIn;
        leftTheta = leftThetaIn;
        rightTheta = rightThetaIn;
        setIncrement(incrementIn);
        setLeftBound(radius * Math.cos(leftTheta));
        setRightBound(radius * Math.cos(rightTheta));
    }

    @Override
    public double getReturn(double incNum) {
        return 
    }

}
