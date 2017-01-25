package shiftmath;

/**
 *
 * @author phusisian
 */
public class AngleMath 
{
    public static double getDifferenceOfAngles(double base, double subtract)
    {
        double giveReturn = Math.abs(base - subtract);
        if(giveReturn > Math.PI)
        {
            giveReturn = Math.abs(base+Math.PI*2.0 - subtract);
        }
        return giveReturn;
    }
}
