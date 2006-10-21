/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.comm;

import ist.mus.baraja.Baraja40;
import ist.mus.baraja.Carta;
import ist.mus.baraja.Cartas;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * @author Pau Rodriguez-Estivill
 */
public class GameData {
    public final static int ver = 1;

    public static boolean isGameData(Packet pkt) {
        if (pkt.getProperty("GameName") == null
                || pkt.getProperty("Version") == null)
            return false;
        if (pkt.getProperty("GameName").toString().equalsIgnoreCase("JMus")
                && (((Integer) pkt.getProperty("Version")).intValue() == ver))
            return true;
        else
            return false;
    }

    int accion = 0; /* Manda boton pulsado junto con lo complementario */

    boolean addme = false;

    int aposta = 0;

    int apostaval = 0;

    Cartas cartas = null;

    int evento = 0; /* Evento como vamos a pequenyas i demas */

    JID from = null;

    String idcartas = null; /* Destino de cartas: usar D para descartar */

    String mano = null;

    int puntospor = -1;

    int puntos[] = { -1, -1 };

    int senyas = 0; /* Manda senyas */

    boolean wakeup = false;

    /**
     *  
     */
    public GameData() {
        super();
        clear();
    }

    public GameData(Packet p) {
        this();
        fillFromPacket(p);
    }

    public boolean isEmpty() {
        return senyas == 0 && accion == 0 && aposta == 0 && apostaval == 0
                && !wakeup && cartas == null && idcartas == null
                && mano == null && evento == 0 && !addme && puntos[0] == -1
                && puntos[1] == -1;
    }

    public void clear() {
        from = null;
        senyas = 0;
        accion = 0;
        aposta = 0;
        apostaval = 0;
        wakeup = false;
        cartas = null;
        idcartas = null;
        mano = null;
        evento = 0;
        addme = false;
        puntospor = -1;
        puntos[0] = -1;
        puntos[1] = -1;
    }

    public void fillFromPacket(Packet pkt) {
        Carta c = null;
        int k;
        clear();
        from = new JID(pkt.getFrom());
        //Comprueva Version
        if (isGameData(pkt)) {
            //Coje los datos
            if (pkt.getProperty("Accion") != null)
                accion = ((Integer) pkt.getProperty("Accion")).intValue();
            if (pkt.getProperty("AddMe") != null)
                addme = true;
            if (pkt.getProperty("Aposta") != null)
                aposta = ((Integer) pkt.getProperty("Aposta")).intValue();
            if (pkt.getProperty("ApostaVal") != null)
                apostaval = ((Integer) pkt.getProperty("ApostaVal")).intValue();
            if (pkt.getProperty("Evento") != null)
                evento = ((Integer) pkt.getProperty("Evento")).intValue();
            if (pkt.getProperty("Mano") != null)
                mano = ((String) pkt.getProperty("Mano"));
            if (pkt.getProperty("Sena") != null)
                senyas = ((Integer) pkt.getProperty("Sena")).intValue();
            if (pkt.getProperty("WakeUp") != null)
                wakeup = true;
            if (pkt.getProperty("PuntosPor") != null)
                puntospor = ((Integer) pkt.getProperty("PuntosPor")).intValue();
            if (pkt.getProperty("Puntos0") != null
                    && pkt.getProperty("Puntos1") != null) {
                puntos[0] = ((Integer) pkt.getProperty("Puntos0")).intValue();
                puntos[1] = ((Integer) pkt.getProperty("Puntos1")).intValue();
            }

            //Cartas
            try {
                if (pkt.getProperty("Cartas") != null
                        && pkt.getProperty("IdCartas") != null) {
                    cartas = new Cartas();
                    idcartas = (String) pkt.getProperty("IdCartas");
                    for (k = 0; k < ((Integer) pkt.getProperty("Cartas"))
                            .intValue(); k++)
                        if (pkt.getProperty("Carta_" + k) != null) {
                            c = (Carta) Baraja40.getHM().get(
                                    (String) pkt.getProperty("Carta_" + k));
                            if (c instanceof Carta)
                                cartas.add(c);
                        }
                }
            } catch (Exception e) {
                cartas = null;
                idcartas = null;
            }

        }

    }

    public Packet fillToPacket() {
        return fillToPacket(Comunicacion.getComm().createPacket());
    }

    Packet fillToPacket(Packet pkt) {
        int k;
        /* Marca el paquete conforme es del juego */
        pkt.setProperty("GameName", "JMus");
        pkt.setProperty("Version", ver);
        /* Copia solo las variables establecidas */
        if (accion != 0)
            pkt.setProperty("Accion", accion);
        if (addme)
            pkt.setProperty("AddMe", 1);
        if (aposta != 0)
            pkt.setProperty("Aposta", aposta);
        if (apostaval != 0)
            pkt.setProperty("ApostaVal", apostaval);
        if (evento != 0)
            pkt.setProperty("Evento", evento);
        if (mano != null)
            pkt.setProperty("Mano", mano);
        if (senyas != 0)
            pkt.setProperty("Sena", senyas);
        if (wakeup)
            pkt.setProperty("WakeUp", 1);
        if (puntospor != -1)
            pkt.setProperty("PuntosPor", puntospor);
        if (puntos[0] != -1 && puntos[1] != -1) {
            pkt.setProperty("Puntos0", puntos[0]);
            pkt.setProperty("Puntos1", puntos[1]);
        }

        //Cartas
        if (cartas != null && idcartas != null) {
            pkt.setProperty("IdCartas", idcartas);
            pkt.setProperty("Cartas", cartas.size());
            for (k = 0; k < cartas.size(); k++)
                pkt.setProperty("Carta_" + k, ((Carta) cartas.get(k))
                        .toString());
        }
        return pkt;
    }

    public Packet fillToPacket(String to) {
        Packet pkt = new Message();
        pkt.setTo(to);
        return fillToPacket(pkt);
    }

    public int getAccion() {
        return accion;
    }

    public boolean getAddMe() {
        return addme;
    }

    public int getAposta() {
        return aposta;
    }

    public int getApostaVal() {
        return apostaval;
    }

    public Cartas getCartas() {
        return cartas;
    }

    public int getEvento() {
        return evento;
    }

    public JID getFrom() {
        return from;
    }

    public String getIdCartas() {
        return idcartas;
    }

    public String getMano() {
        return mano;
    }

    public int[] getPuntos() {
        if (puntos[0] == -1 || puntos[0] == -1)
            return null;
        return puntos;
    }

    public int getPuntosPor() {
        return puntospor;
    }

    public int getSena() {
        return senyas;
    }

    public boolean getWake() {
        return wakeup;
    }

    public void setAccion(int a) {
        accion = a;
    }

    public void setAddMe(boolean o) {
        addme = o;
    }

    public void setAposta(int a, int val) {
        aposta = a;
        apostaval = val;
    }

    public void setCartas(Cartas c, String ic) {
        cartas = c;
        idcartas = ic;
    }

    public void setEvento(int e) {
        evento = e;
    }

    public void setMano(String m) {
        mano = m;
    }

    public void setPuntos(int p0, int p1) {
        if (p0 == -1 || p1 == -1)
            return;

        puntos[0] = p0;
        puntos[1] = p1;
    }

    public void setPuntosPor(int pp) {
        puntospor = pp;
    }

    public void setSena(int s) {
        senyas = s;
    }

    public void setWake(boolean w) {
        wakeup = w;
    }

}
