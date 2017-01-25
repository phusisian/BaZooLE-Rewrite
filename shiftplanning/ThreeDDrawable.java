/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftplanning;

import java.awt.Graphics;

/**
 *
 * @author phusisian
 */
/*
To be implemented by any classes that have the ability to both be drawn in 3D space AND be sorted back to front for draw order.
*/
public interface ThreeDDrawable
{
    void draw(Graphics g);
    double getSortDistanceConstant();
}
