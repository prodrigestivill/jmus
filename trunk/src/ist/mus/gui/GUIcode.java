/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.gui;

import ist.mus.baraja.Carta;
import ist.mus.baraja.Cartas;
import ist.mus.comm.Comunicacion;
import ist.mus.jugadors.Jugadors;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import org.jivesoftware.smack.ConnectionListener;

/**
 * @author Pau Rodriguez-Estivill
 */
public class GUIcode implements ConnectionListener {
    private static GUIcode guicode = null;

    private static final String textChat_fi = "</body></html>\r\n";

    private static final String textChat_inici = "<html><body>\r\n";

    private LinkedList textCentral_vect = null;

    private boolean resetCartas = false;

    public static GUIcode getInstance() {
        if (guicode == null)
            guicode = new GUIcode();
        return guicode;
    }

    public JMusGUI musFrame = null;

    boolean sel = false;

    String textChat = new String();

    Timer tim = new Timer(true);

    /**
     * 
     *  
     */
    private GUIcode() {
        super();
        musFrame = new JMusGUI();
        musFrame.addWindowListener(new HandlerActionAdapter(this, "quit"));
        textChat_clear();
        HandlerActionAdapter haa = new HandlerActionAdapter(this);
        musFrame.textChat.setContentType("text/html");
        musFrame.menu_sortir.addActionListener(haa);
        musFrame.menu_sortir.setActionCommand("quit");
        musFrame.menu_como.addActionListener(haa);
        musFrame.menu_como.setActionCommand("como");
        musFrame.menu_acerca.addActionListener(haa);
        musFrame.menu_acerca.setActionCommand("acerca");
        musFrame.menu_conect.addActionListener(haa);
        musFrame.menu_conect.setActionCommand("login");
        musFrame.menu_nou.addActionListener(haa);
        musFrame.menu_nou.setActionCommand("buscar");
        musFrame.menu_disconect.addActionListener(haa);
        musFrame.menu_disconect.setActionCommand("unlogin");
        musFrame.entradaChat.addActionListener(haa);
        musFrame.entradaChat.setActionCommand("chat");
        musFrame.cmdSena.addActionListener(haa);
        musFrame.cmdSena.setActionCommand("sena");
        textCentral_vect = new LinkedList();
        resetCartas = false;

        for (int i = 0; i < 4; i++)
            musFrame.cartas_juego[0][i]
                    .addMouseListener(new HandlerActionAdapter(this, "carta"
                            + i));
        musFrame.setVisible(true);
        setMano(0);
        setActivateButtons(-1, -1, -1, -1, -1, -1, -1);
    }

    public void initButtons(Object o) {
        JButton b = null;
        HandlerActionAdapter haa = new HandlerActionAdapter(o);

        b = musFrame.cmdMus;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("mus");

        b = musFrame.cmdCorto;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("corto");

        b = musFrame.cmdPaso;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("paso");

        b = musFrame.cmdVeo;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("veo");

        b = musFrame.cmdEnvido;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("envido");

        b = musFrame.cmdOrdago;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("ordago");

        b = musFrame.cmdDescartar;
        try {
            b.removeActionListener(b.getActionListeners()[0]);
        } catch (Exception e) {
        }
        b.addActionListener(haa);
        b.setActionCommand("descartar");
    }

    public void carta0_ActionPerformed() {
        if (sel)
            musFrame.cartas_juego[0][0]
                    .setSelection(!musFrame.cartas_juego[0][0].getSelection());
    }

    public void carta1_ActionPerformed() {
        if (sel)
            musFrame.cartas_juego[0][1]
                    .setSelection(!musFrame.cartas_juego[0][1].getSelection());
    }

    public void carta2_ActionPerformed() {
        if (sel)
            musFrame.cartas_juego[0][2]
                    .setSelection(!musFrame.cartas_juego[0][2].getSelection());
    }

    public void carta3_ActionPerformed() {
        if (sel)
            musFrame.cartas_juego[0][3]
                    .setSelection(!musFrame.cartas_juego[0][3].getSelection());
    }

    public void sena_ActionPerformed() {
        if (Comunicacion.isConnected() && Comunicacion.getComm().inaRoom()) {
            Comunicacion.getComm()
                    .sendSena(musFrame.cmdSena.getSelectedIndex());
        } else
            musFrame.cmdSena.setSelectedIndex(0);
    }

    public void chat_ActionPerformed() {
        if (musFrame.entradaChat.getText().equals(""))
            return;
        if (Comunicacion.isConnected() && Comunicacion.getComm().inaRoom())
            Comunicacion.getComm().sendAllGameUsers(
                    musFrame.entradaChat.getText());
        else
            textChat_add("No esta en ninguna mesa.<br>");
        musFrame.entradaChat.setText("");
    }

    public void connectionClosed() {
        musFrame.menu_conect.setEnabled(true);
        musFrame.menu_disconect.setEnabled(false);
        musFrame.menu_nou.setEnabled(false);
        setState("Desconectado.");
    }

    public void connectionClosedOnError(Exception arg0) {
        connectionClosed();
    }

    public int getCara(int j) {
        if (j > 0 && j < 4)
            return musFrame.smiles[j - 1].getSmile();
        else
            return 0;
    }

    public Carta getCarta(int jugador, int carta) {
        if ((jugador > -1 && jugador < 4) && (carta > -1 && carta < 4))
            return musFrame.cartas_juego[jugador][carta].getCarta();
        else
            return null;
    }

    public Cartas getCartas(int jugador) {
        Cartas c = new Cartas();
        if (jugador > -1 && jugador < 4)
            for (int i = 0; i < 4; i++)
                c.add(getCarta(jugador, i));
        return c;
    }

    public Cartas getSelCartas() {
        Cartas c = new Cartas();
        for (int i = 0; i < 4; i++)
            if (musFrame.cartas_juego[0][i].getSelection()
                    && musFrame.cartas_juego[0][i].getCarta() != null) {
                c.add(musFrame.cartas_juego[0][i].getCarta());
                musFrame.cartas_juego[0][i].setSelection(false);
                musFrame.cartas_juego[0][i].setCarta();
            }
        return c;
    }

    public int getApuesta() {
        return ((Integer) musFrame.cmdApuesta.getValue()).intValue();
    }

    public void login_ActionPerformed() {
        setState("Conectando...");
        Thread c = new Thread() {
            public void run() {
                Comunicacion.getComm();
                if (Comunicacion.isConnected()) {
                    GUIcode.getInstance().setState("Conectado.");
                    GUIcode.getInstance().musFrame.menu_conect
                            .setEnabled(false);
                    GUIcode.getInstance().musFrame.menu_disconect
                            .setEnabled(true);
                    GUIcode.getInstance().musFrame.menu_nou.setEnabled(true);
                    buscar_ActionPerformed();
                } else
                    connectionClosed();
            }
        };
        c.start();
    }

    public void buscar_ActionPerformed() {
        if (Comunicacion.isConnected()) {
            textChat_clear();
            Thread b = new Thread() {
                public void run() {
                    int k = 0;
                    while (k == 0)
                        switch (Comunicacion.buscarSala()) {
                        case 0:
                            k = 1;
                            break;
                        case 1:
                            GUIcode.getInstance().textChat_add(
                                    "Bienvenido a la sala "
                                            + Comunicacion.getComm().getRoom()
                                                    .getName()
                                            + ", "
                                            + Comunicacion.getComm().getRoom()
                                                    .getNick() + "!<br>");
                            GUIcode.getInstance().setState(
                                    "En la sala "
                                            + Comunicacion.getComm().getRoom()
                                                    .getFullJID());
                            k = 1;
                            break;
                        }
                }
            };
            b.start();
        } else
            connectionClosed();
    }

    public void acerca_ActionPerformed() {
        JAboutDialog p = new JAboutDialog(musFrame, true);
    }
    
    public void como_ActionPerformed() {
        JOptionPane.showMessageDialog(musFrame, "Encontrara informacion sobre como jugar en los enlaces de\nhttp://es.wikipedia.org/wiki/Mus", "Como jugar", JOptionPane.INFORMATION_MESSAGE);
    }

    public void quit_ActionPerformed() {
        if (JOptionPane.showConfirmDialog(musFrame,
                "Esta seguro que quiere salir?", "Salir",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            unlogin_ActionPerformed();
            System.exit(0);
        }
    }

    public void reciveMsg(String from, String msg) {
        textChat_add("<b>" + from + ":</b> " + msg + "<br>\r\n");
    }

    public void setName(int j) {
        setName(j, "");
    }

    public void setName(int j, String n) {
        if (j < 1 || j > 3)
            return;
        musFrame.nombres[j - 1].setText(new String(n));
        musFrame.nombres[j - 1].setToolTipText(musFrame.nombres[j - 1]
                .getText());
        musFrame.smiles[j - 1]
                .setToolTipText(musFrame.nombres[j - 1].getText());
    }

    public void setCara(int j) {
        if (j > 0 && j < 4)
            musFrame.smiles[j - 1].setSmile();
    }

    public void setCara(final int j, int c) {
        if (j == 0) {
            GUIcode.getInstance().musFrame.cmdSena.setSelectedIndex(0);
            return;
        }

        if (j > 0 && j < 4)
            musFrame.smiles[j - 1].setSmile(c);
        TimerTask t = new TimerTask() {
            public void run() {
                GUIcode.getInstance().setCara(j);
            }
        };
        tim.schedule(t, 1000);
    }

    public void setCarta(int jugador, int carta) {
        if ((jugador < 0 || jugador > 4) && (carta < 0 || carta > 4))
            return;
        musFrame.cartas_juego[jugador][carta].setCarta();
    }

    /**
     * @param jugador
     * @param carta
     * @param jcarta
     */
    public void setCarta(int jugador, int carta, Carta jcarta) {
        if ((jugador < 0 || jugador > 4) && (carta < 0 || carta > 4))
            return;
        musFrame.cartas_juego[jugador][carta].setCarta(jcarta);
    }

    public void hideCartas() {
        for (int i = 0; i < 4; i++)
            setCartas(i);
        resetCartas = false;
    }

    public void setCartas(int jugador) {
        if (jugador < 0 || jugador > 4)
            return;
        for (int i = 0; i < 4; i++)
            setCarta(jugador, i);
    }

    public void setCartas(int jugador, Cartas c) {
        if (jugador < 0 || jugador > 4)
            return;
        if (c instanceof Cartas) {
            int i;
            for (i = 0; (i < 4) && (i < c.size()); i++)
                setCarta(jugador, i, c.get(i));
            for (; (i < 4); i++)
                setCarta(jugador, i);
        } else
            setCartas(jugador);
    }

    public void setMano(int jugador) {
        int i;
        for (i = 0; i < 4; i++)
            musFrame.cartas_barajas[i].setVisible(i == jugador);
    }

    public void resetNicks() {
        int i;
        for (i = 1; i < Jugadors.getInstance().size(); i++)
            setName(i, Jugadors.getInstance().get(i).getNick());
        for (; i < 4; i++)
            setName(i);
    }

    /**
     * Actualiza los puntos
     *  
     */
    public void resetPuntos() {
        int val;

        val = Jugadors.getInstance().getPuntos(0);
        musFrame.puntos[0].setNum(val / 5); /* Amarracos */
        musFrame.puntos[2].setNum(val % 5); /* Piedras */
        musFrame.cpuntos[0].setText(String.valueOf(val));
        val = Jugadors.getInstance().getJuegos(0);
        musFrame.cjuegos[0].setText(String.valueOf(val));
        val = Jugadors.getInstance().getVacas(0);
        musFrame.cvacas[0].setText(String.valueOf(val));

        val = Jugadors.getInstance().getPuntos(1);
        musFrame.puntos[1].setNum(val / 5); /* Amarracos */
        musFrame.puntos[3].setNum(val % 5); /* Puntos */
        musFrame.cpuntos[1].setText(String.valueOf(val));
        val = Jugadors.getInstance().getJuegos(1);
        musFrame.cjuegos[1].setText(String.valueOf(val));
        val = Jugadors.getInstance().getVacas(1);
        musFrame.cvacas[1].setText(String.valueOf(val));

        musFrame.centerPuntos();
    }

    public void resetCartas() {
        resetCartas(resetCartas);
    }

    public void resetCartas(boolean b) {
        int i, f;
        if (b) {
            f = Jugadors.getInstance().size();
            for (i = 0; i < f; i++)
                setCartas(i, Jugadors.getInstance().get(i).getCartas());

        } else {
            setCartas(0, Jugadors.getInstance().get(0).getCartas());
            f = 1;
        }

        for (i = f; i < 4; i++)
            setCartas(i);
        resetCartas = b;
    }

    public void setSelecionable(boolean b) {
        sel = b;
        if (!sel) {
            for (int i = 0; i < 4; i++)
                musFrame.cartas_juego[0][i].setSelection(false);
        }
    }

    public void setState(String s) {
        musFrame.estat.setText(s);
    }

    public void setState(String s, int i) {
        setState(s);
        TimerTask t = new TimerTask() {
            public void run() {
                GUIcode.getInstance().setState("");
            }
        };
        Timer tim = new Timer(true);
        tim.schedule(t, i);
    }

    public synchronized void textCentral_add(String s) {
        int i;
        textCentral_vect.add(s);
        if (textCentral_vect.size() > 6)
            textCentral_vect.removeFirst();

        musFrame.cmdLabel.setText(textCentral_vect.getLast().toString());

        for (i = textCentral_vect.size() - 2; i > -1; i--)
            musFrame.cmdLabel.append("\n" + textCentral_vect.get(i).toString());

    }

    public synchronized void textChat_clear() {
        textChat = "";
        textChat_add(textChat);
    }

    public synchronized void textChat_add(String text) {
        textChat += text;
        musFrame.textChat.setText(textChat_inici + textChat + textChat_fi);
        textChat_scrollToEnd();
    }

    private boolean textChat_isAdjusting() {
        JScrollBar scrollBar = musFrame.textChatScroll.getVerticalScrollBar();

        if (scrollBar != null && scrollBar.getValueIsAdjusting()) {
            return true;
        }

        return false;
    }

    private void textChat_scrollToEnd() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (textChat_isAdjusting()) {
                    return;
                }

                int height = musFrame.textChat.getHeight();
                musFrame.textChat.scrollRectToVisible(new Rectangle(0,
                        height - 1, 1, height));
            }
        });
    }

    public void unlogin_ActionPerformed() {
        if (Comunicacion.isConnected())
            Comunicacion.getComm().close();
        else
            connectionClosed();
    }

    /**
     * Activa/Desactiva/Deja igual los botones segun si es positivo, negativo o
     * 0 respectivamente.
     * 
     * @param envido
     * @param ordago
     * @param paso
     * @param veo
     * @param mus
     * @param corto
     * @param descarto
     */
    public void setActivateButtons(int envido, int ordago, int paso, int veo,
            int mus, int corto, int descarto) {
        if (envido != 0) {
            musFrame.cmdEnvido.setEnabled(envido > 0);
            musFrame.cmdApuesta.setEnabled(envido > 0);
        }

        if (descarto != 0) {
            musFrame.cmdDescartar.setEnabled(descarto > 0);
            setSelecionable(descarto > 0);
        }

        if (ordago != 0)
            musFrame.cmdOrdago.setEnabled(ordago > 0);
        if (paso != 0)
            musFrame.cmdPaso.setEnabled(paso > 0);
        if (veo != 0)
            musFrame.cmdVeo.setEnabled(veo > 0);
        if (mus != 0)
            musFrame.cmdMus.setEnabled(mus > 0);
        if (corto != 0)
            musFrame.cmdCorto.setEnabled(corto > 0);
    }

}
