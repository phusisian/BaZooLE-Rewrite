package shiftcolor;

import java.awt.Color;

/**
 *
 * @author phusisian
 */
public class ColorPalette 
{
    public static final Color defaultGrassColor = new Color(80,124,41);
    public static final Color baseWaterColor = new Color(30, 144, 255);
    public static Color getLerpColor(Color topColor, Color bottomColor, double alpha)
    {
        int red, green, blue;
        
        if(topColor.getRed() > bottomColor.getRed())
        {
            red = (int)(topColor.getRed() + ((alpha)*(bottomColor.getRed()-topColor.getRed())));
        }else{
            red = (int)(topColor.getRed() + ((1-alpha)*(bottomColor.getRed()-topColor.getRed())));
        }
        if(topColor.getGreen() > bottomColor.getGreen())
        {
            green = (int)(topColor.getGreen() + ((alpha)*(bottomColor.getGreen()-topColor.getGreen())));
        }else{
            green = (int)(topColor.getGreen() + ((1-alpha)*(bottomColor.getGreen()-topColor.getGreen())));
        }
        if(topColor.getBlue() > bottomColor.getBlue())
        {
            blue = (int)(topColor.getBlue() + ((alpha)*(bottomColor.getBlue()-topColor.getBlue())));
        }else{
            blue = (int)(topColor.getBlue() + ((1-alpha)*(bottomColor.getBlue()-topColor.getBlue())));
        }
        return new Color(red,green,blue);
    }
}
