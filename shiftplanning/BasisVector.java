package shiftplanning;

/**
 *
 * @author phusisian
 */
public class BasisVector {
    private double xComp, yComp, zComp;
    public BasisVector(double xCompIn, double yCompIn, double zCompIn){
        xComp = xCompIn; 
        yComp = yCompIn;
        zComp = zCompIn;
    }
    
    public double getXComp(){
        return xComp;
    }
    
    public double getYComp(){
        return yComp;
    }
    
    public double getZComp(){
        return zComp;
    }
    
    public void setZComp(double d){
        zComp = d;
    }
    
    public void setYComp(double d){
        yComp = d;
    }
    
    public void setXComp(double d){
        xComp = d;
    }
    
    public double[] getScalarProduct(double multiplier){
        double[] giveReturn = {xComp * multiplier, yComp*multiplier};
        return giveReturn;
    }
    
}
