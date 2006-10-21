/*
 * Created on May 8, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.baraja;

import java.util.Comparator;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Carta implements Comparator, Comparable {
    /* Definimos los palos en constantes */
    public static final byte paloOros = 0;

    public static final byte paloCopas = 1;

    public static final byte paloBastos = 2;

    public static final byte paloEspadas = 3;

    /* Definimos char para autocompletar el nombre de archivo */
    private static final char paloStr[] = { 'O', 'C', 'B', 'E' };

    /* Caracteristicas de la carta */
    private byte numero = 1;

    private byte palo = 0;

    public Carta(String s) {
        if ((s.length() == 2) || (s.length() == 3)) {
            char c;
            byte i;
            c = s.charAt(s.length() - 1);
            for (i = 0; i < (byte) paloStr.length; i++)
                if (s.charAt(s.length() - 1) == paloStr[i]) {
                    palo = i;
                    i = (byte) paloStr.length;
                }
            numero = new Integer(s.substring(0, s.length() - 2)).byteValue();
            if ((numero == 0) || (numero == 8) || (numero == 9)
                    || (numero > 13))
                numero = 1;
        }
    }

    public Carta(byte num, byte palo) {
        if ((num > 0) && (num < 8) || ((num < 13) && (num > 9)))
            this.numero = num;
        if (palo < 4)
            this.palo = palo;
    }

    public Carta(int num, int palo) {
        this((byte) num, (byte) palo);
    }

    public byte getPalo() {
        return palo;
    }

    public byte getNum() {
        return numero;
    }

    public int getMusNum() {
        if (this.getNum() == 3)
            return 12;
        if (this.getNum() == 2)
            return 1;
        return this.getNum();
    }

    public int getMusVal() {
        if (this.getMusNum() > 9)
            return 10;
        if (this.getNum() < 3)
            return 1;
        return this.getNum();
    }

    public String toString() {
        return String.valueOf(numero) + String.valueOf(paloStr[palo]);
    }

    public int hashCode() {
        return (((int) palo) << 4 | ((int) numero));
    }

    public int compareTo(Object arg0) {
        return compare(this, arg0);
    }

    public int compare(Object arg0, Object arg1) {
        if (arg0 instanceof Carta && arg1 instanceof Carta) {
            return (new Integer(((Carta) arg0).getMusNum()))
                    .compareTo(new Integer(((Carta) arg1).getMusNum()));
        } else
            throw new ClassCastException();
    }

    /*
     * Comparacio com a carta normal public int compare(Object arg0, Object
     * arg1) { if (arg0 instanceof Carta && arg1 instanceof Carta) return
     * ((Carta) arg0).hashCode() - ((Carta) arg0).hashCode(); else return
     * -65535; }
     */
}
