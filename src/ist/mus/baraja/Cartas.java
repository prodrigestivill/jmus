/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.baraja;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Cartas {

    private LinkedList cartas = null;

    public Cartas() {
        this(new LinkedList());
    }

    private Cartas(LinkedList c) {
        super();
        cartas = c;
    }

    public void add(Carta carta) {
        cartas.add(carta);
    }

    public void add(Cartas cs, int j) {
        for (int i = 0; i < j; i++)
            add(cs.getCarta());
    }

    public void addAll(Collection c) {
        cartas.addAll(c);
    }

    public Object clone() {
        return (new Cartas((LinkedList) cartas.clone()));
    }

    public Carta get(int i) {
        return (Carta) cartas.get(i);
    }

    /**
     * Canvia el orden de las cartas!! y despues devuelve la primera carta.
     * Mejor iterar en this.size() vezes para no desordenarlo
     * 
     * @return Carta
     */
    public Carta getCarta() {
        Collections.rotate(cartas, -1);
        return (Carta) cartas.getFirst();
    }

    /**
     * Devuelve una carta y despues la borra!!!
     * 
     * @return Carta
     */
    public Carta getRemCarta() {
        return (Carta) cartas.removeFirst();
    }

    /*
     * public Cartas move(Cartas cs, int j){ Cartas cs2 = new Cartas(); for (int
     * i=0;(i <j) && (i < size());i++){ cs2.add(cs.getRemCarta()); add((Carta)
     * cs2.cartas.getLast()); } return cs2; }
     */

    public boolean remove(Carta c) {
        if (!cartas.contains(c))
            return false;
        cartas.remove(c);
        return true;
    }

    public Carta remove(int i) {
        return (Carta) cartas.remove(i);
    }

    public void shuffle() {
        Collections.shuffle(cartas, new Random(System.currentTimeMillis()));
    }

    public int size() {
        return cartas.size();
    }

    public void sortByMusNum() {
        Collections.sort(cartas);
    }

    public int sumaVals() {
        int i, j = 0;
        for (i = 0; i < this.size(); i++)
            j += ((Carta) this.get(i)).getMusVal();
        return j;
    }
}