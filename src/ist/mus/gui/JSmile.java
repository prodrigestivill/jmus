/*
 * Created on May 14, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.gui;

import ist.mus.ImageMus;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author Pau Rodriguez-Estivill
 */
public class JSmile extends JComponent {

    public static final String[] smiles_array = { "Senyas...", "3 cerdos",
            "2 cerdos", "3 pitos", "2 pitos", "duplex", "solomo", "una",
            "ciego" };

    private static final String[] smileStr = { "normal", "3cerdos", "2cerdos",
            "3pitos", "2pitos", "duplex", "solomo", "una", "ciego" };

    public static final int s_normal = 0;

    public static final int s_3cerdos = 1;

    public static final int s_2cerdos = 2;

    public static final int s_3pitos = 3;

    public static final int s_2pitos = 4;

    public static final int s_duplex = 5;

    public static final int s_solomo = 6;

    public static final int s_una = 7;

    public static final int s_ciego = 8;

    private int smile = 0;

    private BufferedImage img = null;

    private Dimension dim = new Dimension();

    private int orientation;

    /**
     *  
     */
    public JSmile() {
        this(0);
    }

    public JSmile(int s) {
        super();
        ImageIO.setUseCache(true);
        setSmile(s);
    }

    public void paint(Graphics g) {
        if (img instanceof Image)
            g.drawImage(img, 0, 0, this);
    }

    public void setSmile() {
        setSmile(0);
    }

    public void setSmile(int s) {
        if ((s > -1) && (s <= smileStr.length))
            smile = s;
        resetImage();
    }

    private void resetImage() {
        try {
            img = (BufferedImage) ImageMus.getImage("res/images/senyes/" + smileStr[smile] + ".png");
            this.setSize(img.getWidth(), img.getHeight());
        } catch (Exception e) {
            img = null;
        }
        this.repaint();
    }

    public int getSmile() {
        return smile;
    }
}
