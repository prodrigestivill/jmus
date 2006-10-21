/*
 * Created on May 21, 2005
 * 
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 * 
 * This software is licensed under the GNU General Public License. See
 * http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.joc;

import ist.mus.MathMus;
import ist.mus.baraja.Cartas;
import ist.mus.comm.Comunicacion;
import ist.mus.comm.GameData;
import ist.mus.comm.JID;
import ist.mus.gui.GUIcode;
import ist.mus.jugadors.Jugadors;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Joc {
    static final int evento_barajar = 1;

    static final int evento_mus = 2;

    static final int evento_descartar = 3;

    static final int evento_grandes = 4;

    static final int evento_pequenyas = 5;

    static final int evento_pares = 6;

    static final int evento_juego = 7;

    static final int evento_punto = 8;

    static final int evento_final = 10;

    static final int evento_haspares = 2000;

    static final int evento_hasjuego = 3000;

    private static Joc joc = null;

    private Postre postre = null;

    private Usuario usuario = null;

    private int toca = 0;

    public int puntse1 = 0;

    public int puntse2 = 0;

    boolean yopostre = false;

    boolean play = true;

    private Joc() {
        super();
        postre = new Postre();
        usuario = new Usuario();
        // Demana rebre els paquets
        Comunicacion.getComm().setEventListener(this);
        // Demana rebtre les peticions dels botons
        GUIcode.getInstance().initButtons(usuario);
    }

    public static Joc getInstance() {
        if (joc == null)
            joc = new Joc();
        return joc;
    }

    public static void start() {
        stop();
        getInstance();
        joc.mano(Jugadors.getInstance().getMano());
    }

    public static void stop() {
        if (joc == null)
            return;
        joc.postre.para();
        joc.usuario.para();
        Jugadors.getInstance().get(0).setCartas(null);
        Jugadors.getInstance().clear();
    }

    /**
     * @param Manoid
     * @return GUIid
     */
    public static int Manoid2GUIid(int id) {
        return MathMus.Mod(Jugadors.getInstance().getMano() + id, 4);
    }

    /**
     * @param GUIid
     * @return Manoid
     */
    public static int GUIid2Manoid(int id) {
        return MathMus.Mod(id - Jugadors.getInstance().getMano(), 4);
    }

    void mano(int m) {
        if (m < 0 || m > 3)
            return;
        Jugadors.getInstance().setMano(m);
        usuario.empieza();
        joc.yopostre = (Jugadors.getInstance().getPostre() == 0);
        if (joc.yopostre)
            joc.postre.empieza();
    }

    void mano(String m) {
        mano(Jugadors.getInstance().getID(m));
    }

    boolean sendDecartarse() {
        Cartas c = GUIcode.getInstance().getSelCartas();
        if (c.size() == 0)
            return false;
        GameData gd = new GameData();
        gd.setAccion(Usuario.accion_descartar);
        gd.setCartas(c, "D");
        sendGameDataToPostre(gd);
        return true;
    }

    void sendAposta(int val, boolean ordago) {
        GameData gd = new GameData();
        gd.setAccion(Usuario.accion_envido);
        if (ordago)
            gd.setAposta(5, val);
        else
            gd.setAposta(1, val);
        sendGameData(gd);
    }

    void sendGameData(GameData gd, int i) {
        if (i != 0) {
            Comunicacion.getComm()
                    .sendPacket(
                            gd.fillToPacket(Jugadors.getInstance().get(i)
                                    .getFullJID()));
        } else
            processData(gd);
    }

    void sendGameData(GameData gd) {
        Comunicacion.getComm().sendPacket(gd.fillToPacket());
    }

    void sendGameDataToPostre(GameData gd) {
        sendGameData(gd, Jugadors.getInstance().getPostre());
    }

    void sendAccion(int action) {
        GameData gd = new GameData();
        gd.setAccion(action);
        sendGameData(gd);
    }

    void sendWakeAll() {
        GameData gd = new GameData();
        gd.setWake(true);
        sendGameData(gd);
    }

    void sendWake(int i) {
        if (i != 0) {
            GameData gd = new GameData();
            gd.setWake(true);
            sendGameData(gd, i);
        } else
            usuario.despertar();
    }

    void sendEvento(int e, int i) {
        GameData gd = new GameData();
        gd.setEvento(e);
        sendGameData(gd, i);
    }

    void sendEventoAll(int e) {
        GameData gd = new GameData();
        gd.setEvento(e);
        sendGameData(gd);
    }

    /**
     * Procesa los paquetes de datos recividos
     */
    public void processData(GameData gd) {
        JID from;
        int f;
        if (gd.getFrom() == null) {
            f = 0;
            from = Jugadors.getInstance().get(f).getJID();
        } else {
            from = gd.getFrom();
            f = Jugadors.getInstance().getID(from);
        }
        boolean fromPostre = (f == Jugadors.getInstance().getPostre());

        if (fromPostre && gd.getMano() != null)
            mano(gd.getMano());

        if (gd.getPuntos() != null) {
            if (gd.getPuntosPor() != -1) {
                usuario.puntospor(gd.getPuntosPor(), gd.getPuntos()[Jugadors
                        .getInstance().getMano() % 2], gd.getPuntos()[(Jugadors
                        .getInstance().getMano() + 1) % 2]);
            } else if (!yopostre) {
                Jugadors.getInstance().setPuntos(
                        Jugadors.getInstance().getMano(), gd.getPuntos()[0]);
                Jugadors.getInstance()
                        .setPuntos(Jugadors.getInstance().getMano() + 1,
                                gd.getPuntos()[1]);
            }
        }

        //Pon las cartas del que envia o descarta.
        if (gd.getIdCartas() != null && gd.getCartas() != null) {
            if (yopostre && ((String) gd.getIdCartas()).equals("D"))
                postre.descarte(f, gd.getCartas());
            else if (fromPostre
                    && !yopostre
                    && Jugadors.getInstance().hasJugador(
                            (String) gd.getIdCartas())) {

                Jugadors.getInstance()
                        .setCartas(
                                gd.getCartas(),
                                Jugadors.getInstance().getID(
                                        (String) gd.getIdCartas()));
                Jugadors.getInstance().hideOtherCartas();
            }
        }

        if (gd.getAposta() != 0 && yopostre)
            postre.setApuesta(f, gd.getApostaVal(), gd.getAposta() == 5);

        if (gd.getAccion() != 0) {
            if (yopostre)
                postre.rcv_accion(f, gd.getAccion());
            usuario.rcv_accion(f, gd.getAccion(), gd.getApostaVal(), gd
                    .getAposta() == 5);
        }

        if (fromPostre && gd.getEvento() != 0)
            usuario.evento(gd.getEvento());

        if (fromPostre && gd.getWake())
            usuario.despertar();
    }
}
