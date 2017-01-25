package shiftplanning;

import java.awt.Color;

/**
 *
 * @author phusisian
 */
public interface Traversable {
    public XYZPointCollection getMovementPoints();
    public ShapeLayer getShape();
    public LayeredSolid getBoundSolid();
    public boolean isConnected(Traversable t);
    public XYZPoint[] getConnectorPoints();
}
