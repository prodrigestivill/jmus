/*
 * Created on Jun 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus;

import java.awt.Image;

import javax.imageio.ImageIO;

/**
 * @author Pau Rodriguez-Estivill
 */
public class ImageMus {

    public static Image getImage(String str) throws Exception{
        return  ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(str));
    }


}
