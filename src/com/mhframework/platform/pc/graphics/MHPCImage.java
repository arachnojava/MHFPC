package com.mhframework.platform.pc.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.mhframework.platform.graphics.MHBitmapImage;
import com.mhframework.platform.graphics.MHGraphicsCanvas;

public class MHPCImage extends MHBitmapImage
{
    private Image image;
    private MHPCGraphics graphics;

    
    private MHPCImage(String filename)
    {
        try
        {
            // The following line only works for images not in a JAR file.
            image = ImageIO.read(new File(filename));
        }
        catch (final IOException ioe)
        {
            System.err.println("ERROR:  File " + filename + " not found.");
            // The following line only works for images in MHFramework.
//            URL url = this.getClass().getResource(filename);
//            image = Toolkit.getDefaultToolkit().createImage(url);
        }
        catch (final Exception e)
        {
        }
        
        graphics = new MHPCGraphics(image);
        setSize(image.getWidth(null), image.getHeight(null));
    }
    
    
    public MHPCImage(int width, int height)
    {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = new MHPCGraphics(image);
    }


    public static MHBitmapImage create(int width, int height)
    {
        return new MHPCImage(width, height);
    }

    
    public static MHBitmapImage create(String filename)
    {
        return new MHPCImage(filename);
    }

    
    public Image getImage()
    {
        return image;
    }

    
    @Override
    public MHGraphicsCanvas getGraphicsCanvas()
    {
        return graphics;
    }
}
