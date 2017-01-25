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
To be implemented by 2D drawable classes (UI elements, flat graphics, etc.) that have no need to be sorted by distance.
*/
public interface TwoDDrawable 
{
    void draw(Graphics g);
}
