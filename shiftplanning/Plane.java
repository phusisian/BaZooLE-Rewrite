package shiftplanning;

import updatables.MouseUpdatable;
import updatables.Updatable;
import updatables.UpdatableOnQuadrantChange;
import containers.GamePanel;
import inputs.KeyboardInput;
import java.awt.Color;
import java.awt.Graphics;

/*Is a 3D plane upon which objects can be drawn. Can calculate 3D projection points onto its plane for drawing, can handle rotation of
the plane, and can hold objects pertinent to the plane.
 */
public abstract class Plane implements Updatable, ThreeDDrawable, UpdatableOnQuadrantChange
{
    private BasisVector iHat = new BasisVector(Unit.scaledUnit, 0, 0);
    private BasisVector jHat = new BasisVector(0, Unit.scaledUnit, 0);
    private BasisVector kHat = new BasisVector(0, 0, Unit.scaledUnit);
    public static int screenWidth = 1440, screenHeight = 900;
    private double spin, rotation;
    double centerPosX, centerPosY;
    private GamePanel boundPanel;
    private UpdatableOnQuadrantChange[] quadrantUpdatables = new UpdatableOnQuadrantChange[0];
    private SpinQuadrant spinQuadrant;
    private Updatable[] updatables;
    //private ThreeDDrawable[] threeDDrawables;
    private ShapeLayer planeShape;
    private double width, length;
    private DistanceSorter distanceSorter;
    private MouseUpdatable[] mouseUpdatables;//holds all objects that update on mouse interaction.
    private XYZPoint[][] gridPoints;
    private Player player;
    private TwoDDrawable[] twoDDrawables = new TwoDDrawable[0];
    private double zPos;
    //private ArrayList<MouseUpdatable> mouseUpdatables = new ArrayList<MouseUpdatable>();
    
    
    /*
    Make sure that sides of layeredSolids aren't being drawn twice (might be because I see a fuzzy "black" edge around layered solids so 
    maybe I draw it once normally, then shade it, instead of just shading
    */
    /*
        CURRENTLY DOESN'T TAKE A ZPOS. (Note:) Bound planes should take an XYZPoint for their position input
    */
    public Plane(GamePanel boundPanelIn, double centerPosXIn, double centerPosYIn, double zPosIn, double widthIn, double lengthIn)
    {
        boundPanel = boundPanelIn;
        centerPosX = centerPosXIn;
        centerPosY = centerPosYIn;
        spin = 0;
        zPos = zPosIn;
        rotation = 0;
        width = widthIn;
        length = lengthIn;
        spinQuadrant = new SpinQuadrant(this);
        updatables = new Updatable[1];
        mouseUpdatables = new MouseUpdatable[0];
        updatables[0] = spinQuadrant;
        planeShape = ShapeLayer.createShapeLayerUsingIdealPolygon(this, 0, 0, 0, width/2.0, 4, Color.GREEN);//automatically uses a square, but could use a rectangle. Just haven't coded it to.
        addUpdatable(planeShape);
        //threeDDrawables = new ThreeDDrawable[0];
        distanceSorter = new DistanceSorter(this, new ThreeDDrawable[0]);
        addQuadrantUpdatable(distanceSorter);
        gridPoints = new XYZPoint[2][(int)(width+length)];
        setGriddedPlaneShapePoints();
        player = new Player(this, new XYZPoint(this, 0, -.5, 1));
        
        
    }
    
    public abstract BasePlane getBasePlane();
    
    public void addRandomTilesToThreeDDrawables()
    {
        
        Tile t = Tile.createTileUsingBottomLeftCorner(this, new XYZPoint(this, -1, -1, 1), 2, 2);
        //addUpdatable(t);
        //addThreeDDrawable(t);
        //addMouseUpdatable(t);
        int numSolids = 10;
        for(int i = 0; i < numSolids; i++)
        {
            double centerX = (int)((Math.random()*width)-(width/2.0));
            double centerY = (int)(Math.random()*length)-(length/2.0);
            double randomRadius = 0.5 + (Math.random() * 2);
            double randomHeight = 1;//1 + (int)(Math.random() * 2);
            double randomWidth = (int)(1+(Math.random() * 5));
            double randomLength = (int)(1+(Math.random() * 5));
            int randSides = 3+(int)(Math.random() * 4);
            Tile solid = Tile.createTileUsingBottomLeftCorner(this, new XYZPoint(this, centerX, centerY, randomHeight), randomWidth, randomLength);
            
            //addUpdatable(solid);
            //addThreeDDrawable(solid);
            //addMouseUpdatable(solid);
        }
    }
    
    public ShapeLayer getShape(){
        return planeShape;
    }
    
    //converts x and y in terms of relative units into a point to be projected on screen. Uses centerPosX and centerPosY for relative positioning so same conversions can be used for BasePlane and BoundPlane
    public XYPoint convertCoordsToPoint(XYZPoint xyzPoint)
    {
        /*XYZPoint radiusPoint = new XYZPoint();
        radiusPoint.setZ(zPos);
        double radius = XYZPoint.getXYMagnitudeBetweenPoints(radiusPoint, xyzPoint);
        double pointAngle = Math.atan2(xyzPoint.getY(), xyzPoint.getX());
        double xPos = centerPosX + (radius*Unit.scaledUnit*Math.cos(pointAngle + spin));
        double yPos = centerPosY + (getShrink() * radius*Unit.scaledUnit*Math.sin(pointAngle + spin)) - getScaledDistortedHeight(xyzPoint);// - getScaledDistortedHeight(zPos);*/
        
        double[][] vectorComps = new double[3][2];
        vectorComps[0] = iHat.getScalarProduct(xyzPoint.getX());
        vectorComps[1] = jHat.getScalarProduct(xyzPoint.getY());
        vectorComps[2] = kHat.getScalarProduct(xyzPoint.getZ());
        
        double xAdd = 0;
        double yAdd = 0;
        for(int i = 0; i < vectorComps.length; i++){
            xAdd += vectorComps[i][0];
            yAdd += vectorComps[i][1];//subtracting for y since y axis is flipped
        }
        
        return new XYPoint(centerPosX + xAdd, centerPosY + yAdd);
    }
    //precalculate getShrink into a variable. Find a way to precalculate (at least partially) a distorted height constant
    //converts x and y in terms of points on the screen into coordinates to be used in relative placing (e.g. mouse clicks to units). Uses centerPosX and centerPosY for relative positioning so same conversions can be used for BasePlane and BoundPlane
    //CONVERT TO USE IHAT, JHAT, KHAT. Might not be possible?
    public XYZPoint convertPointToCoords(double xPoint, double yPoint)
    {
        double dx = (xPoint - centerPosX)/Unit.scaledUnit;
        double dy = ((yPoint - centerPosY)/(getShrink()))/Unit.scaledUnit;
        double pointAngle = Math.atan2(dy, dx);
        double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double coordX = magnitude*Math.cos(pointAngle-spin);
        double coordY = magnitude*Math.sin(pointAngle-spin);
        return new XYZPoint(this, coordX, coordY, 0);
    }
    
    @Override
    public void draw(Graphics g) 
    {
        planeShape.draw(g);
        g.setColor(new Color(0, 51, 204, 50));//consider switching to color interpolation. Could never get the color looking quite the same with color interpolation and the alpha slowdown was negligible anyway.
        drawGriddedPlaneShape(g);
        for(ThreeDDrawable threeD : distanceSorter.getThreeDDrawables())
        {
            threeD.draw(g);
        }
        for(TwoDDrawable twoD : twoDDrawables){
            twoD.draw(g);
        }
    }
    private void drawGriddedPlaneShape(Graphics g)
    {
        for (int i = 0; i < gridPoints[0].length; i++) 
        {
            gridPoints[0][i].drawLineToPoint(g, gridPoints[1][i]);
        }
    }
    
    public void addMouseUpdatable(MouseUpdatable mu)
    {
        MouseUpdatable[] temp = new MouseUpdatable[mouseUpdatables.length+1];
        for (int i = 0; i < mouseUpdatables.length; i++) 
        {
            temp[i] = mouseUpdatables[i];
        }
        temp[mouseUpdatables.length] = mu;
        mouseUpdatables = temp;
    }
    
    public void updateMouseUpdatablesOnClick()//consider changing the location of mouseUpdatables to the plane?
    {
        for(MouseUpdatable mu : mouseUpdatables)
        {
            mu.updateOnMouseClick();
        }
    }
    
    public void updateMouseUpdatablesOnHold()
    {
        for(MouseUpdatable mu : mouseUpdatables)
        {
            mu.updateOnMouseHold();
        }
    }
    
    public DistanceSorter getDistanceSorter()
    {
        return distanceSorter;
    }
    /*doesn't work?*/
    public void setThreeDDrawables(ThreeDDrawable[] threeDDrawablesIn)
    {
        threeDDrawables = threeDDrawablesIn;
    }
    
    public GamePanel getBoundPanel()
    {
        return boundPanel;
    }
    
    public void addUpdatable(Updatable u)
    {
        Updatable[] tempArray = new Updatable[updatables.length + 1];
        for (int i = 0; i < updatables.length; i++) {
            tempArray[i] = updatables[i];
        }
        tempArray[tempArray.length - 1] = u;
        updatables = tempArray;
    }
    
    public void addQuadrantUpdatable(UpdatableOnQuadrantChange u){
        UpdatableOnQuadrantChange[] temp = new UpdatableOnQuadrantChange[quadrantUpdatables.length + 1];
        for (int i = 0; i < quadrantUpdatables.length; i++) {
            temp[i] = quadrantUpdatables[i];
        }
        temp[temp.length - 1] = u;
        quadrantUpdatables = temp;
    }
    
    public void addThreeDDrawable(ThreeDDrawable threeDDrawableIn)
    {
        /*ThreeDDrawable[] tempArray = new ThreeDDrawable[threeDDrawables.length + 1];
        for (int i = 0; i < threeDDrawables.length; i++) {
            tempArray[i] = threeDDrawables[i];
        }
        tempArray[tempArray.length - 1] = threeDDrawableIn;
        threeDDrawables = tempArray;*/
        distanceSorter.addThreeDDrawable(threeDDrawableIn);

    }
    
    public void addTwoDDrawable(TwoDDrawable twoDDrawableIn)
    {
        TwoDDrawable[] tempArray = new TwoDDrawable[twoDDrawables.length + 1];
        for (int i = 0; i < twoDDrawables.length; i++) {
            tempArray[i] = twoDDrawables[i];
        }
        tempArray[tempArray.length - 1] = twoDDrawableIn;
        twoDDrawables = tempArray;
    }
    
    
    public ThreeDDrawable[] getThreeDDrawables()
    {
        return distanceSorter.getThreeDDrawables();
    }
    
    /*public void setThreeDDrawablesAtIndex(int index, ThreeDDrawable d)
    {
        threeDDrawables[index] = d;
    }*/
    
    
    
    
    public double getShrink()
    {
        return Math.cos(rotation);
    }
    
    private double getScaledDistortedHeight(XYZPoint xyzPoint)
    {
        return xyzPoint.getZ()*Unit.scaledUnit * Math.sin(rotation);// + getScaledDistortedHeight(zPos);//not adding fixed the fact that all the heights on bound planes were twice as high ash they should have been. 
    }
    
    private double getScaledDistortedHeight(double unitHeight){
        return unitHeight*Unit.scaledUnit * Math.sin(rotation);
    }
    
    public double getSpin()
    {
        return spin;
    }
    
    
    public SpinQuadrant getSpinQuadrant(){
        return spinQuadrant;
    }
    
    public void addSpin(double spinAdd)
    {
        spin += spinAdd;
    }
    
    private void setSpinWithinBounds()
    {
        if(spin > Math.PI*2.0)
        {
            spin -= Math.PI*2.0;
        }else if(spin < 0)
        {
            spin += Math.PI*2.0;
        }
    }
    
    public void setSpin(double d){
        spin = d;
    }
    
    
    private void setRotationWithinBounds()
    {
        if(rotation > Math.PI/2.0)
        {
            rotation = Math.PI/2.0;
        }else if(rotation < 0)
        {
            rotation = 0;
        }
    }
    
    public void addRotation(double rotationAdd)
    {
        rotation += rotationAdd;
    }
    
    public void addPositionChange(double dx, double dy)
    {
        centerPosX += dx;
        centerPosY += dy;
    }
    
    private void updateUpdatables()
    {
        for(Updatable u : updatables)
        {
            u.update();
        }
    }
    
    public double getWidth()
    {
        return width;
    }
    public double getLength()
    {
        return length;
    }
    
    public void updateDistanceSorter()
    {
        distanceSorter.update();
    }
    
    @Override
    public void update() 
    {
        updateUpdatables();
        addSpin(KeyboardInput.dSpin);
        addPositionChange(KeyboardInput.dx, KeyboardInput.dy);
        addRotation(KeyboardInput.dRotation);
        setSpinWithinBounds();
        setRotationWithinBounds();
        iHat.setXComp(Unit.scaledUnit * Math.cos(spin));
        iHat.setYComp(Unit.scaledUnit * Math.sin(spin) * getShrink());
        jHat.setXComp(Unit.scaledUnit * Math.cos(spin + (Math.PI/2.0)));
        jHat.setYComp(Unit.scaledUnit * Math.sin(spin + (Math.PI/2.0)) * getShrink());
        kHat.setYComp(-getScaledDistortedHeight(1));
        //traversableLogic.setColorOfConnectedPaths();
    }

    public void setCenterPosX(double xIn){
        centerPosX = xIn;
    }
    
    public void setZPos(double zIn){
        zPos = zIn;
    }
    
    public void setCenterPosY(double yIn){
        centerPosY = yIn;
    }
    
    private void setGriddedPlaneShapePoints()
    {
        int numCounted = 0;
        for(int i = (int)(-width/2.0); i < (int)(width/2.0); i++)
        {
            XYZPoint p1 = new XYZPoint(this, i, -length/2.0, 0);
            XYZPoint p2 = new XYZPoint(this, i, length/2.0, 0);
            gridPoints[0][numCounted] = p1;
            gridPoints[1][numCounted] = p2;
            addUpdatable(p1);
            addUpdatable(p2);
            numCounted++;
        }
        for (int i = (int)(-length/2.0); i < (int)(length/2.0); i++) 
        {
            XYZPoint p1 = new XYZPoint(this, -width/2.0, i, 0);
            XYZPoint p2 = new XYZPoint(this, width/2.0, i, 0);
            gridPoints[0][numCounted] = p1;
            gridPoints[1][numCounted] = p2;
            addUpdatable(p1);
            addUpdatable(p2);
            numCounted++;
        }
    }

    @Override
    public double getSortDistanceConstant() 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    @Override
    public void updateOnQuadrantChange() {
        for(UpdatableOnQuadrantChange u : quadrantUpdatables){
            u.updateOnQuadrantChange();
        }
        distanceSorter.printDrawablesSortConstants();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
