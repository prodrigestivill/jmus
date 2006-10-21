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
public class JBolas extends JComponent {

    private int num = 0;

    private int tipo = 1;

    private BufferedImage img = null;

    private int orientation;

    /**
     *  
     */
    public JBolas() {
        this(0, 1);
    }

    public JBolas(int o) {
        this(o, 1);
    }

    public JBolas(int o, int t) {
        super();
        ImageIO.setUseCache(true);
        orientation = o;
        setTipo(t);
    }

    public int getNum() {
        return num;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getTipo() {
        return tipo;
    }

    public void paint(Graphics g) {
        if (img instanceof Image)
            if ((orientation % 2) == 1)
                for (int i = 0; i < num; i++)
                    g.drawImage(img, 0, (this.getHeight() / num) * i, this);
            else
                for (int i = 0; i < num; i++)
                    g.drawImage(img, (this.getWidth() / num) * i, 0, this);
    }

    private void resetImage() {
        try {
            if (tipo == 1)
                img = (BufferedImage) ImageMus.getImage("res/images/punts/piedra.png"); /* Piedra */
            else
                img = (BufferedImage) ImageMus.getImage("res/images/punts/amarraco.png"); /* Amarraco */
        } catch (Exception e) {
            img = null;
        }
    }

    public void setNum() {
        setNum(0);
    }

    public void setNum(int n) {
        num = n;
        if ((orientation % 2) == 1)
            this.setSize((int) img.getWidth(),
                    (int) (img.getHeight() * num * 1.5));
        else
            this.setSize((int) (img.getWidth() * num * 1.5), (int) img
                    .getHeight());
        this.repaint();

        if (tipo == 1) {
            if (n == 1)
                setToolTipText("Una piedra");
            else
                setToolTipText(n + " piedras");
        } else {
            if (n == 1)
                setToolTipText("Un amarraco");
            else
                setToolTipText(n + " amarracos");
        }
    }

    public void setOrientation(int o) {
        int i;
        Dimension dim = new Dimension();

        this.resetImage();

        if (img == null)
            return;

        if ((o % 2) == 1)
            dim.setSize(img.getHeight(), img.getWidth());
        else
            dim.setSize(img.getWidth(), img.getHeight());

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
        setNum(num);
    }

    public void setTipo(int t) {
        tipo = t;
        setOrientation(orientation);
    }

}