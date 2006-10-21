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

import ist.mus.baraja.Carta;
import ist.mus.baraja.Cartas;
import ist.mus.comm.JID;

import java.util.Comparator;

/**
 * @author Pau Rodriguez-Estivill
 * @author Arnau Guell-Soler
 */
public class Jugador implements Comparator, Comparable {

    private Cartas pila;

    private JID id;

    public Jugador() {
        this("");
    }

    public Jugador(String jid) {
        this(new JID(jid));
    }

    public Jugador(JID jid) {
        id = jid;
    }

    public void setCarta(Carta naipe) {
        this.pila.add(naipe);
    }

    public void dropCarta(Carta carta) {
        pila.remove(carta);
    }

    public JID getJID() {
        return id;
    }

    public String getNick() {
        return id.getNick();
    }

    public void setNick(String s) {
        id.setNick(s);
    }

    public String getFullJID() {
        return id.getFullJID();
    }

    public Cartas getCartas() {
        return this.pila;
    }

    public void setCartas(Cartas c) {
        pila = c;
    }

    public int compareTo(Object arg0) {
        return compare(this, arg0);
    }

    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof Jugador && arg1 instanceof Jugador)
            return ((Jugador) arg0).getNick().compareToIgnoreCase(
                    ((Jugador) arg1).getNick());
        else
            throw new ClassCastException();
    }

}
