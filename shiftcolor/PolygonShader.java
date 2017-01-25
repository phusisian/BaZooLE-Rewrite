/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shiftcolor;

import shiftcolor.ColorPalette;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import shiftplanning.Plane;

/**
 *
 * @author phusisian
 */
public class PolygonShader 
{
    
    public static Color shadeColor = Color.BLACK;
    
    /*
    Problem: CURRENTLY NOT WORKING. I have plans to use a more complicated lighting engine so having shadows always stuck to the left side of the solids wouldn't work anyway. Saving for later.
    */
    public static void shadePolygons(Plane boundPlane, Graphics g, ArrayList<Polygon> polygons, Color baseColor, double initialTheta)//find a way to change shade amount based on the angle width from the origin that the side has?? E.g. points very close together then some long sides will shade each by a linear additive amount rather than using how much the side faces the sun.
    {
        /*double shadeShiftAmount = 0.2;
        
        double initialShadeAmount = 0.2 - 0.1*(((Math.PI)-(boundPlane.getSpin()%(Math.PI)))/(Math.PI));
        double shadeAmount = initialShadeAmount;
        double maxShadeAmount = initialShadeAmount + 0.2;
        double shadeAdd = 0.1;//(maxShadeAmount - initialShadeAmount)/(double)polygons.size();*/
        
        double totalShadeAdd = 0.4;
        double finalShade = 0.6;
        double shadeAdd = totalShadeAdd/(double)polygons.size();
        double initialShade = 0.6;//shadeAdd*((boundPlane.getSpin()) % (2*Math.PI/(double)polygons.size() ));
        //double shade = initialShade;
        for(int i = 0; i < polygons.size(); i++)
        {
            //double shade = (finalShade - shadeAdd*((polygons.size() - i)-(((boundPlane.getSpin())%(Math.PI/(double)polygons.size()))/(Math.PI/(double)polygons.size()))));
            double shade = (finalShade - shadeAdd*((polygons.size() - i)-(((boundPlane.getSpin())%(Math.PI/2.0))/(Math.PI/2.0))));

            g.setColor(ColorPalette.getLerpColor(shadeColor, baseColor, shade));
            g.fillPolygon(polygons.get(i));
            shade += shadeAdd;
        }
    }
    
    public static void simpleShadeSidePolygons(Graphics g, ArrayList<Polygon> polygons, Color baseColor)
    {
        double initialShade = 0.1;
        double maxShade = initialShade + 0.2;
        double shadeAdd = (maxShade - initialShade)/(double)polygons.size();
        double shadeNum = initialShade;
        for (int i = 0; i < polygons.size(); i++) {
            shadeNum += shadeAdd;
            g.setColor(ColorPalette.getLerpColor(shadeColor, baseColor, shadeNum));
            g.fillPolygon(polygons.get(i));
        }
    }
}
