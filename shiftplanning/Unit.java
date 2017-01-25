/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import updatables.Updatable;
import inputs.MouseInput;

/**
 *
 * @author phusisian
 */
public class Unit implements Updatable
{
    public static double baseUnit = 50;
    public static double scale = 1;
    public static double scaledUnit = 50;

    @Override
    public void update()
    {
        scale += MouseInput.dScale;
        scaledUnit = baseUnit * scale;
    }
}
