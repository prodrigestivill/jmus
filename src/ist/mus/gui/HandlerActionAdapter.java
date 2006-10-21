/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Method;

/**
 * @author Pau Rodriguez-Estivill
 */
public class HandlerActionAdapter implements ActionListener, MouseListener,
        WindowListener {
    Object adaptat = null;

    String command = null;

    /**
     *  
     */
    public HandlerActionAdapter(Object arg0) {
        this(arg0, null);
    }

    public HandlerActionAdapter(Object arg0, String arg1) {
        adaptat = arg0;
        command = arg1;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0 != null) {
            //Genera un String con el nombre del method a llamar
            String methodToInvoke = arg0.getActionCommand().replace(' ', '_')
                    + "_ActionPerformed";

            //System.out.println("#"+methodToInvoke+"#");

            //Obtiene la classe del formulario del objeto llamante
            Class origFrame = adaptat.getClass();
            // de moment no fa falta
            //Class argType[] = new Class[1];
            //argType[0] = arg0.getClass();
            //Busca el method en la class
            Method me = null;
            try {
                //me = origFrame.getDeclaredMethod(methodToInvoke, argType);
                me = origFrame.getDeclaredMethod(methodToInvoke, null);
            } catch (Exception err) {
                //Escribe por pantalla que el evento no ha sido efectuado
                System.out.println("Method '" + methodToInvoke
                        + "' not found [from " + adaptat.getClass().toString()
                        + "]");
            }
            //Ejecuta el method
            try {
                if (me != null) {
                    //Object args[] = new Object[1];
                    //args[0] = arg0;
                    //me.invoke(adaptat, args);
                    me.invoke(adaptat, null);
                }
            } catch (Exception err) {
                //Escribe por pantalla que no se ha podido invocar el evento
                err.printStackTrace();
            }
        }

    }

    public void mouseClicked(MouseEvent arg0) {
        ActionEvent a0 = new ActionEvent(arg0.getSource(), arg0.getModifiers(),
                command);
        actionPerformed(a0);
    }

    public void windowClosing(WindowEvent arg0) {
        ActionEvent a0 = new ActionEvent(arg0.getSource(), 0, command);
        actionPerformed(a0);
    }

    public void mousePressed(MouseEvent arg0) {

    }

    public void mouseReleased(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void windowOpened(WindowEvent arg0) {

    }

    public void windowClosed(WindowEvent arg0) {

    }

    public void windowIconified(WindowEvent arg0) {

    }

    public void windowDeiconified(WindowEvent arg0) {

    }

    public void windowActivated(WindowEvent arg0) {

    }

    public void windowDeactivated(WindowEvent arg0) {

    }

}
