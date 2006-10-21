/*
 * Created on May 26, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus;

import ist.mus.gui.GUIcode;

/**
 * @author Pau Rodriguez-Estivill
 */
public class MusDebug {

    private MusDebug() {
        super();
    }

    public static void ErrMsg(String m) {
        GUIcode.getInstance().setState(m);
        System.err.println("Err:" + m);
    }

    public static void InfoMsg(String m) {
        GUIcode.getInstance().setState(m);
        System.out.println("Info:" + m);
    }

    public static void DebugMsg(String m) {
        System.out.println("Debug:" + m);
    }

    public static void MusMsg(String m) {
        GUIcode.getInstance().textCentral_add(m);
    }

}
