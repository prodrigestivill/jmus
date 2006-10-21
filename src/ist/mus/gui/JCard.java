/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.gui;

import ist.mus.ImageMus;
import ist.mus.baraja.Carta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @author Pau Rodriguez-Estivill
 */
public class JCard extends JComponent {

    private Carta carta = null;

    private Dimension dim = new Dimension();

    private BufferedImage img = null;

    private boolean sel = false;

    private boolean multiple = false;

    private int orientation;

    /**
     *  
     */
    public JCard() {
        this(null, 0, false);
    }

    public JCard(Carta carta) {
        this(carta, 0, false);
    }

    public JCard(Carta carta, int o) {
        this(carta, o, false);
    }

    public JCard(Carta carta, int o, boolean m) {
        super();
        ImageIO.setUseCache(true);
        orientation = o;
        multiple = m;
        setCarta(carta);
    }

    public JCard(int o) {
        this(null, o, false);
    }

    public Carta getCarta() {
        return carta;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean getMultiple() {
        return multiple;
    }

    public void putMultiple(boolean b) {
        this.multiple = b;
        this.setOrientation(orientation);
    }

    public boolean getSelection() {
        return sel;
    }

    public void paint(Graphics g) {
        if (img instanceof Image)
            g.drawImage(img, 0, 0, this);
        if (sel) {
            g.setColor(Color.RED);
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        }
    }

    private void resetImage() {
        try {
            if (carta != null)
                img = (BufferedImage) ImageMus.getImage("res/images/cartes/"
                                + carta.toString() + ".png");
            else if (multiple)
                img = (BufferedImage) ImageMus.getImage("res/images/cartes/REVM.png");
            else
                img = (BufferedImage) ImageMus.getImage("res/images/cartes/REV.png");
        } catch (Exception e) {
            img = null;
        }
    }

    public void setCarta() {
        setCarta(null);
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
        this.setOrientation(orientation);
    }

    public void setOrientation(int o) {
        int i;

        this.resetImage();

        if (img == null)
            return;

        if ((o % 2) == 1)
            dim.setSize(img.getHeight(), img.getWidth());
        else
            dim.setSize(img.getWidth(), img.getHeight());
        super.setSize(dim);

        if (o > 0 && o < 4) {
            BufferedImage bimg = new BufferedImage((int) dim.getWidth(),
                    (int) img.getHeight(), (int) img.getColorModel()
                            .getTransparency());
            Graphics2D g = bimg.createGraphics();

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            switch (o) {
            case 1:
                g.translate(dim.getWidth(), 0);
                break;
            case 2:
                g.translate(dim.getWidth(), dim.getHeight());
                break;
            case 3:
                g.translate(0, dim.getHeight());
                break;
            }
            g.rotate(Math.PI * o / 2);
            g.drawRenderedImage(img, null);

            img = bimg;
        }

        orientation = o;
        this.repaint();
    }

    public void setSelection(boolean b) {
        sel = b;
        this.repaint();
    }

}