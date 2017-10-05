package com.mhframework.platform.pc;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.mhframework.MHVideoSettings;
import com.mhframework.math.MHVector;
import com.mhframework.platform.MHGameApplication;
import com.mhframework.platform.graphics.MHGraphicsCanvas;
import com.mhframework.platform.pc.graphics.MHPCGraphics;


public class MHPCApplication implements MHGameApplication
{
    private MHVector displayOrigin, displaySize;
    private JFrame window;
	@SuppressWarnings("unused")
	private MHPCInputEventHandler eventHandler;

    private MHPCApplication(JFrame window, MHVideoSettings displaySettings)
    {
        this.window = window;
        
        displaySize = new MHVector(displaySettings.displayWidth, displaySettings.displayHeight);
        
        configureWindow(window, displaySettings);
        
        // Register event handlers.
        eventHandler = new MHPCInputEventHandler(window);
    }
    
    
    
    public static MHGameApplication create(JFrame window, MHVideoSettings displaySettings)
    {
        return new MHPCApplication(window, displaySettings);
    }



    public void shutdown()
    {
        window.dispose();
        //System.exit(0);
    }



    public void present(MHGraphicsCanvas backBuffer)
    {
        //Graphics g = getGraphics();
        Graphics g = window.getGraphics();
        Image img = ((MHPCGraphics)backBuffer).getImage();
        g.drawImage(img, (int)displayOrigin.getX(), (int)displayOrigin.getY(), null);
    }

    private void configureWindow(JFrame window, final MHVideoSettings settings)
    {
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((d.width - settings.displayWidth) / 2, (d.height - settings.displayHeight) / 2);
        window.setTitle(settings.windowCaption);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Insets insets = window.getInsets();
        displayOrigin = new MHVector(insets.left, insets.top);

        // Hide mouse cursor.
        BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");

        // Set the blank cursor to the JFrame.
        window.getContentPane().setCursor(blankCursor);
        
        resizeToInternalSize(window, settings.displayWidth, settings.displayHeight);
    }

    private void resizeToInternalSize(final JFrame window, final int internalWidth, final int internalHeight)
    {
        final Insets insets = window.getInsets();
        final int newWidth = internalWidth + insets.left + insets.right;
        final int newHeight = internalHeight + insets.top + insets.bottom;

        final Runnable resize = new Runnable()
        {
            public void run()
            {
                window.setSize(newWidth, newHeight);
            }
        };

        if(!SwingUtilities.isEventDispatchThread())
        {
            try
            {
                SwingUtilities.invokeAndWait(resize);
            }
            catch(final Exception e) {}
        }
        else
            resize.run();

        window.setResizable(false);
        window.validate();
    }

    
    public MHVector getDisplayOrigin()
    {
        return displayOrigin;
    }

    
     public MHVector getDisplaySize()
    {
        return displaySize;
    }

}
