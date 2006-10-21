/*
 * Created on May 8, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.baraja;

import java.util.HashMap;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Baraja40 {

    private static HashMap b40 = null;

    private Baraja40() {
        super();
    }

    private static void creaBaraja() {
        b40 = new HashMap();
        Carta c = null;
        byte i, j; /* i = num, j= palo */
        for (j = 0; j < 4; j++) {
            for (i = 1; i < 8; i++) {
                /* Anade de 1 a 7 */
                c = new Carta(i, j);
                b40.put(c.toString(), c);
            }
            for (i = 10; i < 13; i++) {
                /* Anade las figuras */
                c = new Carta(i, j);
                b40.put(c.toString(), c);
            }
        }
    }

    public static HashMap getHM() {
        if (b40 == null)
            creaBaraja();
        return b40;
    }

    public static Cartas getAllCartas() {
        Cartas c = new Cartas();
        c.addAll(getHM().values());
        return c;
    }
}
