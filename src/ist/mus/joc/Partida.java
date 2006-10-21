/*
 * Created on May 13, 2005
 * 
 * Copyright 2005 Pau Rodriguez-Estivill
 *                Arnau Guell-Soler
 * 
 * This software is licensed under the GNU General Public License. See
 * http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.joc;

import ist.mus.baraja.Carta;
import ist.mus.baraja.Cartas;
import ist.mus.jugadors.Jugadors;

/**
 * @author Pau Rodriguez-Estivill
 * @author Arnau Guell-Soler
 */
public class Partida {

    public final static int GRANDES = 0;

    public final static int CHICAS = 1;

    public final static int PARES = 2;

    public final static int JUEGO = 3;

    public final static int PUNTO = 4;

    private int apuestas[] = { 0, 0, 0, 0, 0 };

    private int envites[] = { 0, 0, 0, 0, 0 };

    private int punts[] = { 0, 0, 0, 0 };

    private int vamos = 0;

    private boolean hordago;

    Partida() {
        super();
        clear();
    }

    /**
     * Define a que vamos si a GRANDES, CHICAS, PARES, ...
     * 
     * @param vamos
     */
    public void setVamos(int v) {
        if (v > -1 && v < 5)
            vamos = v;
    }

    public int getVamos() {
        return vamos;
    }

    /**
     * A?ade apuestas en el juego al cual "vamos"
     * 
     * @param valor
     * @param ordago
     */
    public void setApuesta(int a, boolean b) {
        //MusDebug.DebugMsg("hola hoy vamos a: " + vamos + " y apuestas" + a);
        apuestas[vamos] = a;
        hordago = b;
        vamos++;
    }

    private boolean jugadorValido(int num) {
        if (!Jugadors.getInstance().hasJugador(num))
            return false;
        if (Jugadors.getInstance().get(num).getCartas().size() != 4)
            return false;
        return true;
    }

    /**
     * Algo con los pares
     * 
     * @param num
     * @return
     */
    public int contaPar(int num) {
        Cartas naipe = new Cartas();
        int i = 0, punts = 0;
        // Si no es valid, fora.
        if (!jugadorValido(num))
            return punts;
        // Copiem la Llista
        naipe = (Cartas) Jugadors.getInstance().get(num).getCartas().clone();
        // Ordanem
        naipe.sortByMusNum();
        // Busquem parelles
        if ((((Carta) naipe.get(0)).getMusNum() == ((Carta) naipe.get(1))
                .getMusNum())
                && (((Carta) naipe.get(2)).getMusNum() == ((Carta) naipe.get(3))
                        .getMusNum())) {
            punts = 30000 + (((Carta) naipe.get(0)).getMusNum()) * 100
                    + ((Carta) naipe.get(2)).getMusNum();
            return punts;
        }
        if (((Carta) naipe.get(0)).getMusNum() == ((Carta) naipe.get(1))
                .getMusNum()
                && ((Carta) naipe.get(2)).getMusNum() == ((Carta) naipe.get(1))
                        .getMusNum()) {
            punts = 200 + ((Carta) naipe.get(0)).getMusNum();
            return punts;
        }
        if (((Carta) naipe.get(2)).getMusNum() == ((Carta) naipe.get(3))
                .getMusNum()
                && ((Carta) naipe.get(2)).getMusNum() == ((Carta) naipe.get(1))
                        .getMusNum()) {
            punts = 200 + ((Carta) naipe.get(1)).getMusNum();
            return punts;
        }
        for (i = 3; i > 0; i--) {
            if (((Carta) naipe.get(i)).getMusNum() == ((Carta) naipe.get(i - 1))
                    .getMusNum()) {
                punts = ((Carta) naipe.get(i)).getMusNum();
                return punts;
            }
        }
        return 0;
    }

    private int contaJoc(int num) {
        int i = 0, valor = 0;

        // Si no es valid, fora.
        if (!jugadorValido(num))
            return valor;

        for (i = 0; i < 4; i++) {
            valor += (((Carta) Jugadors.getInstance().get(num).getCartas().get(
                    i)).getMusVal());
        }
        return valor;

    }

    public int contaPuntsJoc(int num) {
        if (this.contaJoc(num) == 31) {
            return 3;
        } else if (this.contaJoc(num) > 31) {
            return 2;
        }
        return 0;
    }

    private int comparar_puntos(int num1, int num2, int n0, int n1, int n2) {
        if (num1 > num2)
            return n1;
        if (num1 < num2)
            return n2;
        return n0;
    }

    public int gana_puntos(int[] num) {
        int val1, val2, val3, val4;
        /* Quien tine las mas grandes o pequenyas */
        val1 = comparar_puntos(num[0], num[2], 2, 0, 2);
        val2 = comparar_puntos(num[1], num[3], 3, 1, 3);
        val3 = comparar_puntos(num[val1], num[val2], -1, val1, val2);
        if (val3 != -1)
            return val3;
        /* Empate! */
        if (val1 > val2)
            return val1;
        else
            return val2;

    }

    public int jocGana() {
        int i, num[] = { 0, 0, 0, 0 };

        for (i = 3; i >-1; i--)
            switch (contaJoc(i)) {
            case 31:
                num[i] = 100;
                break;
            case 32:
                num[i] = 90;
                break;
            case 40:
                num[i] = 80;
                break;
            case 39:
                num[i] = 70;
                break;
            case 38:
                num[i] = 60;
                break;
            case 37:
                num[i] = 50;
                break;
            case 36:
                num[i] = 40;
                break;
            case 35:
                num[i] = 30;
                break;
            case 34:
                num[i] = 20;
                break;
            case 33:
                num[i] = 10;
                break;
            default:
                num[i] = 0;
                break;
            }

        return gana_puntos(num);
    }

    public int puntoGana() {
        int i, num[] = { 0, 0, 0, 0 };
        for (i = 0; i < 4; i++)
            num[i] = contaJoc(i);
        return gana_puntos(num);
    }

    public int parGana() {
        int i, num[] = { 0, 0, 0, 0 };
        for (i = 0; i < 4; i++)
            num[i] = contaPar(i);
        return gana_puntos(num);
    }

    private int contaPuntsPar(int num) {
        if (this.contaPar(num) > 30000) {
            return 3;
        } else if (this.contaPar(num) > 200) {
            return 2;
        } else if (this.contaPar(num) > 0) {
            return 1;
        }
        return 0;
    }

    private int comparar_mas(Cartas napies1, Cartas napies2, int n0, int n1,
            int n2, boolean grande) {
        int i;
        if (grande) {
            // La mas grande
            for (i = 3; i > 0; i--) {
                if (((Carta) napies1.get(i)).getMusNum() > ((Carta) napies2
                        .get(i)).getMusNum())
                    return n1;
                if (((Carta) napies2.get(i)).getMusNum() > ((Carta) napies1
                        .get(i)).getMusNum())
                    return n2;
            }
            return n0;
        } else {
            // La mas pequenya
            for (i = 0; i < 4; i++) {
                if (((Carta) napies1.get(i)).getMusNum() < ((Carta) napies2
                        .get(i)).getMusNum())
                    return n1;
                if (((Carta) napies2.get(i)).getMusNum() < ((Carta) napies1
                        .get(i)).getMusNum())
                    return n2;
            }
            return n0;
        }
    }

    public int grandespequesGana(boolean g) {
        int i = 0;
        int val1, val2, val3, val4;
        Cartas napies[] = { null, null, null, null };
        for (i = 0; i < 4; i++) {
            // Si no es valid, fora.
            if (!jugadorValido(i))
                return -1;
            // Guarda las cartas i las ordena
            napies[i] = (Cartas) Jugadors.getInstance().get(i).getCartas()
                    .clone();
            napies[i].sortByMusNum();
        }

        /* Quien tine las mas grandes o pequenyas */
        val1 = comparar_mas(napies[0], napies[2], 2, 0, 2, g);
        val2 = comparar_mas(napies[1], napies[3], 3, 1, 3, g);
        val3 = comparar_mas(napies[val1], napies[val2], -1, val1, val2, g);
        if (val3 != -1)
            return val3;
        /* Empate! */
        if (val1 > val2)
            return val1;
        else
            return val2;
    }

    public int quienGana(int juego) {
        switch (juego) {
        case GRANDES:
            return grandespequesGana(true);
        case CHICAS:
            return grandespequesGana(false);
        case PARES:
            return parGana();
        case JUEGO:
            return jocGana();
        case PUNTO:
            return puntoGana();
        }
        return 0;
    }

    /**
     * Actualiza los puntos, cojerlos con getPuntos.
     *  
     */
    public void actualizaPuntos() {
        int i, qg;
        for (i = GRANDES; i < (PUNTO + 1); i++) {
            qg = quienGana(i);
            if (envites[i] != 0)
                setPuntos(qg, apuestas[i]);
            // +Pares
            if (envites[i] != 0 && i == PARES) {
                if (qg % 2 == 0)
                    setPuntos(qg, contaPuntsPar(0) + contaPuntsPar(2));
                else
                    setPuntos(qg, contaPuntsPar(1) + contaPuntsPar(3));
            }
            // +Juego
            if (envites[i] != 0 && i == PARES) {
                if (qg % 2 == 0)
                    setPuntos(qg, contaPuntsJoc(0) + contaPuntsJoc(2));
                else
                    setPuntos(qg, contaPuntsJoc(1) + contaPuntsJoc(3));
            }
        }
    }

    public void clear() {
        int i;
        for (i = 0; i < 4; i++)
            punts[i] = 0;
        for (i = GRANDES; i < (PUNTO + 1); i++) {
            apuestas[i] = 0;
            envites[i] = 0;
        }
        vamos = GRANDES;
    }

    public void setPuntos(int j, int puntos) {
        if (j < 0 || j > 3)
            return;

        punts[j] += puntos;
    }

    public int getPuntos(int j) {
        if (j < 0 || j > 3)
            return 0;

        return punts[j];
    }

    public int getEPuntos(int j) {
        return punts[j % 2] + punts[(j % 2) + 2];
    }

}
