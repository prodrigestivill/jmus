/*
 * Created on May 13, 2005
 * 
 * Copyright 2005 Pau Rodriguez-Estivill
 *                Arnau Guell-Soler
 * 
 * This software is licensed under the GNU General Public License. See
 * http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.jugadors;

import ist.mus.MathMus;
import ist.mus.baraja.Carta;
import ist.mus.baraja.Cartas;
import ist.mus.comm.JID;
import ist.mus.gui.GUIcode;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Pau Rodriguez-Estivill
 * @author Arnau Guell-Soler
 */
public class Jugadors {

    private static Jugadors instancia = null;

    private Jugador jo = null;

    private LinkedList jugadors = null;

    private int mano;

    private int puntos[] = { 0, 0 };

    private int juegos[] = { 0, 0 };

    private int vacas[] = { 0, 0 };

    private Jugadors() {
        super();
        jugadors = new LinkedList();
        jo = new Jugador();
    }

    public void clear() {
        jugadors.clear();
        mano = 0;
        for (int i = 0; i < 2; i++) {
            puntos[i] = 0;
            vacas[i] = 0;
            juegos[i] = 0;
        }
        GUIcode.getInstance().resetNicks();
        GUIcode.getInstance().resetPuntos();
    }

    /**
     * Cojer Jugador segun ID de GUI (el cual yo=0)
     *  
     */
    public Jugador get(int i) {
        if (i == 0)
            return jo;
        else
            return (Jugador) jugadors.get(i - 1);
    }

    public void setMyJugador(Jugador j) {
        if (j instanceof Jugador)
            jo = j;
    }

    public synchronized void sort() {
        int ioy;
        jugadors.add(jo);
        Collections.sort(jugadors, this.jo);
        ioy = jugadors.indexOf(jo);
        Collections.rotate(jugadors, -ioy);
        jugadors.removeFirst();
        setMano(MathMus.Mod(-ioy, 4));
    }

    public void setMano(String p) {
        setMano(getID(p));
    }

    public void setMano(int num) {
        if (num < 0 || num > (jugadors.size() + 1))
            return;
        GUIcode.getInstance().setMano(num);
        mano = num;
    }

    public int getMano() {
        return mano;
    }

    public int getPostre() {
        return MathMus.Mod(mano + 1, 4);
    }

    public void setCartas(Cartas mano, JID p) {
        setCartas(mano, getID(p));
    }

    public void setCartas(Cartas mano, String p) {
        setCartas(mano, getID(p));
    }

    public void setCartas(Cartas mano, int num) {
        int i = 0;
        if (num < 0 || num > jugadors.size())
            return;

        get(num).setCartas(mano);

        GUIcode.getInstance().resetCartas();
    }

    public void showAllCartas() {
        GUIcode.getInstance().resetCartas(true);
    }
    
    public void hideAllCartas() {
        GUIcode.getInstance().hideCartas();
    }

    public void hideOtherCartas() {
        GUIcode.getInstance().resetCartas(false);
    }

    public void hideCartas(int j) {
        int i;
        for (i = 0; i < 4; i++)
            GUIcode.getInstance().setCarta(j, i);
    }

    public static Jugadors getInstance() {
        if (instancia == null)
            instancia = new Jugadors();
        return instancia;
    }

    public void addJugador(Jugador home) {
        this.jugadors.add(home);
        if (size() == 4)
            sort();
        GUIcode.getInstance().resetNicks();
    }

    public Cartas descarta(int num, Cartas descart, Cartas mazo,
            Cartas descartades) {
        int i, j;
        boolean devolver = false;
        Carta naipe;
        Cartas descart2 = (Cartas) descart.clone();

        devolver = (descart2.size() >= mazo.size());

        if (devolver)
            j = mazo.size();
        else
            j = descart2.size();

        for (i = 0; i < j; i++) {
            naipe = (Carta) descart2.getRemCarta();
            descartades.add(naipe);
            get(num).dropCarta(naipe);
            get(num).setCarta(mazo.getRemCarta());
        }

        if (num == 0)
            GUIcode.getInstance().resetCartas();

        if (devolver)
            return descart2;
        else
            return null;
    }

    public boolean hasJugador(int num) {
        return (jugadors.size() >= num);
    }

    public boolean hasJugador(JID j) {
        return (getID(j) != -1);
    }

    public boolean hasJugador(String j) {
        return (getID(j) != -1);
    }

    public int size() {
        return jugadors.size() + 1;
    }

    public int getID(JID jid) {
        return getID(jid.getNick());
    }

    public int getID(String nick) {
        int i;
        if (jo.getNick().compareToIgnoreCase(nick) == 0)
            return 0;
        for (i = 0; i < jugadors.size(); i++)
            if (((Jugador) jugadors.get(i)).getNick().compareToIgnoreCase(nick) == 0)
                return i + 1;
        return -1;
    }

    /**
     * Borra los puntos y los pasa a juegos
     *  
     */
    public void setPuntos() {
        boolean rsp = false;

        if (puntos[0] > 30) {
            juegos[0]++;
            rsp = true;
        }

        if (puntos[1] > 30) {
            juegos[1]++;
            rsp = true;
        }

        if (juegos[0] > 2){
            vacas[0]++;
            juegos[0] = 0;
            juegos[1] = 0;
        }            

        if (juegos[1] > 2){
            vacas[1]++;
            juegos[0] = 0;
        	juegos[1] = 0;
        }
        
        if (vacas[0] > 2){
            vacas[0] = 0;
            vacas[1] = 0;
            juegos[0] = 0;
            juegos[1] = 0;
        }            

        if (vacas[1] > 2){
            vacas[0] = 0;
            vacas[1] = 0;
            juegos[0] = 0;
        	juegos[1] = 0;
        }

        if (rsp) {
            setPuntos(0, 0);
            setPuntos(1, 0);
        }
    }

    /**
     * Pone los puntos
     * 
     * @param j
     *            equipo jugador (par/impar)
     * @param val
     *            valor de puntos
     */
    public void setPuntos(int j, int val) {
        puntos[MathMus.Mod(j,2)] = val;
        GUIcode.getInstance().resetPuntos();
    }

    public void setJuegos(int j, int val) {
        juegos[MathMus.Mod(j,2)] = val;
        GUIcode.getInstance().resetPuntos();
    }

    public void setVacas(int j, int val) {
        vacas[MathMus.Mod(j,2)] = val;
        GUIcode.getInstance().resetPuntos();
    }

    public int getPuntos(int j) {
        return puntos[MathMus.Mod(j,2)];
    }

    public int getJuegos(int j) {
        return juegos[MathMus.Mod(j,2)];
    }

    public int getVacas(int j) {
        return vacas[MathMus.Mod(j,2)];
    }

}
