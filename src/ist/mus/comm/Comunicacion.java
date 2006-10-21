/*
 * Created on May 12, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.comm;

import ist.mus.gui.GUIcode;
import ist.mus.gui.JMusBuscar;
import ist.mus.gui.JMusLogin;

import org.jivesoftware.smack.XMPPException;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Comunicacion {

    private static JMusLogin gui = null;

    private static Comm comm = null;

    public static Comm getComm() {
        if (!isConnected()) {
            if (comm != null)
                comm.close();
            comm = null;
            gui = new JMusLogin(GUIcode.getInstance().musFrame, true);
            if (gui.func == 1) {
                try {
                    comm = new Comm(gui.username.getText(), String
                            .copyValueOf(gui.passwd.getPassword()),
                            (String) gui.server.getModel().getSelectedItem()
                                    .toString(), ((Integer) gui.port.getModel()
                                    .getValue()).intValue(), gui.sslchk
                                    .getModel().isArmed());
                } catch (XMPPException e) {
                    if ((e != null) && (e.getXMPPError() != null)
                            && (e.getXMPPError().getCode() == 401))
                        GUIcode.getInstance().setState("Contrasenya erronea.");
                    else
                        GUIcode.getInstance().setState("Error en la conexion.");
                    e.printStackTrace();
                    comm = null;
                }
                if (comm != null)
                    comm.xmpp.addConnectionListener(GUIcode.getInstance());
            } else
                comm = null;
            // Esborra el formulari.
            gui.setVisible(false);
            gui = null;
        }

        return comm;
    }

    public static boolean isConnected() {
        return ((comm != null) && (comm.xmpp.isConnected()));
    }

    public static int buscarSala() {
        if (isConnected()) {
            JMusBuscar j = new JMusBuscar(GUIcode.getInstance().musFrame, true);
            j.setVisible(false);

            if (j.func) {
                Comunicacion.getComm().setRoom(j.room);
                j = null;
                int n = Comunicacion.getComm().getRoomNumber();
                if (n < 0)
                    return 2;
                return 1;

            }
            j = null;
        }
        return 0;
    }

}
