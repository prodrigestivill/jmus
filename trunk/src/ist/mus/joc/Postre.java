/*
 * Created on May 21, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *                Alberto Guirao-Villalonga <wakboth@gmail.com>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.joc;

import ist.mus.MathMus;
import ist.mus.baraja.Baraja40;
import ist.mus.baraja.Cartas;
import ist.mus.comm.GameData;
import ist.mus.jugadors.Jugadors;

import java.util.Random;

/**
 * @author Pau Rodriguez-Estivill
 * @author Alberto Guirao-Villalonga
 */
public class Postre implements Runnable {

    Random rnd = new Random(System.currentTimeMillis());

    Cartas repartir = null;

    Cartas descartado = null;

    public Partida partida = null;

    private boolean running = false;

    private int accion = 0;

    private int accionfrom;

    private boolean apuestaordago;

    private int apuestaval;

    private int apuestavista;

    private int firstenvidado;

    private int z;

    private int puntos[] = new int[5];

    private int jug[] = new int[4];

    private Cartas desc[] = new Cartas[4];

    Postre() {
        super();
        partida = new Partida();
    }

    /**
     * Sortir quan !running
     */
    public void run() {
        running = true;
        partida.clear();
        accion = 0;
        int i, k, j, resultado;
        Cartas c = null;

        partida.clear();

        Joc.getInstance().sendEventoAll(Joc.evento_barajar);
        Jugadors.getInstance().hideOtherCartas();
        descartado = new Cartas();
        repartir = Baraja40.getAllCartas();

        // como Baraja40 nos devuelve una baraja ordenada....
        j = rnd.nextInt(4);
        for (i = -2; i < j; i++) {
            //Minimo 2 maximo j+2 [rnd(j)]
            repartir.shuffle();
        }
        
        Jugadors.getInstance().hideAllCartas();
        
        if (haveToClose())
            return;
        for (i = 3; i > -1; i--) {
            c = new Cartas();

            for (j = 0; j < 4; j++)
                c.add(repartir.getRemCarta());

            Jugadors.getInstance().setCartas(c, i);
            sendCartas(i);
        }
        
        Jugadors.getInstance().hideOtherCartas();

        do {
            if (haveToClose())
                return;
        } while (mus_nomus());//no se sale de este bucle hasta que alguien
        // corte el mus.
        z = 0;
        Joc.getInstance().sendEventoAll(Joc.evento_grandes);
        resultado = grandes_chicas();//se juega a grandes
        if (haveToClose())
            return;
        if (resultado == 0) {
            puntos[z] = 1;
        }
        
        z = 1;
        Joc.getInstance().sendEventoAll(Joc.evento_pequenyas);
        resultado = grandes_chicas();//se juega a chicas
        if (haveToClose())
            return;
        if (resultado == 0) {
            puntos[z] = 1;
        }

        for (k = 3; k > -1; k--) {
            if ((partida.contaPar(k) != 0)) {
                Joc.getInstance().sendEventoAll(
                        Joc.evento_haspares + Joc.GUIid2Manoid(k));
                jug[k] = 1;
                //System.err.println("Usuario " + k + " tiene pares");
            } else {
                jug[k] = 0;
                //System.err.println("Usuario " + k + " no tiene pares");
            }
        }
        if (haveToClose())
            return;
        z = 2;
        Joc.getInstance().sendEventoAll(Joc.evento_pares);
        resultado = pares_juego();//se juega a pares
        if (haveToClose())
            return;
        if (resultado == 0) {
            puntos[z] = 0;
        }
        for (k = 3; k > -1; k--) {
            if ((partida.contaPuntsJoc(k) != 0)) {
                Joc.getInstance().sendEventoAll(
                        Joc.evento_hasjuego + Joc.GUIid2Manoid(k));
                jug[k] = 1;
                //System.err.println("Usuario " + k + " tiene juego");
            } else {
                jug[k] = 0;
               // System.err.println("Usuario " + k + " no tiene juego");
            }
        }
        if (haveToClose())
            return;
        z = 3;
        Joc.getInstance().sendEventoAll(Joc.evento_juego);
        resultado = pares_juego();//se juega a juego
        if (haveToClose())
            return;
        if (resultado == 0) {
            puntos[z] = 0;

        }
        puntos[z + 1] = 0;
        if ((jug[0] == 0) && (jug[1] == 0) && (jug[2] == 0) && (jug[3] == 0)) {
            //se juega al punto
            z = 4;
            Joc.getInstance().sendEventoAll(Joc.evento_punto);
            resultado = grandes_chicas();//se juega al punto
            if (haveToClose())
                return;
            if (resultado == 0) {
                puntos[z] = 1;
            }
            puntos[z - 1] = 0;

        }
        /* Evento Final (el desenlace) */
        puntosPaTos();//mas claro el agua...
        fin();
    }
    
    public void fin(){
    	int k;
        sendCartasToAll();
        
        Joc.getInstance().sendEventoAll(Joc.evento_final);
        Joc.getInstance().sendWakeAll();
        for (k = 0; k < 4; k++) {
            if (haveToClose())
                return;
            accion_wait(Usuario.accion_veo);
        }
        pasaMano();
    }

    public void pasaMano() {
        GameData gd = new GameData();
        gd.setMano(Jugadors.getInstance().get(
                        MathMus.Mod(Jugadors.getInstance().getMano() - 1, 4))
                        .getNick());
        Joc.getInstance().sendGameData(gd);
        para();
    }

    private boolean mus_nomus() {//Si los 4 jugadores quieren mus, se
        int k;
        Joc.getInstance().sendEventoAll(Joc.evento_mus);
        // descartan, sino se empieza
        for (k = 3; k > -1; k--) {
            if (haveToClose())
                return false;
            Joc.getInstance().sendWake(k);
            int a = mus_corto_wait(k);//En teoria esto es un "espero recibir
            // pakete del jugador k"
            if (a == Usuario.accion_corto) {
                return false;
            }
        }
        Joc.getInstance().sendEventoAll(Joc.evento_descartar);
        Joc.getInstance().sendWakeAll();

        for (k = 0; k < 4; k++) {
            accion_wait(Usuario.accion_descartar);
            if (haveToClose())
                return false;
        }
        descarteYa();

        return true;
    }

    private int grandes_chicas() {
        int valor = 0, res;

        apuestavista = 0;
        for (int k = 3; k > -1; k--) {
            if (haveToClose())
                return 0;
            Joc.getInstance().sendWake(k);
            valor = paso_envido_veo_wait(k);
            if (valor != Usuario.accion_paso) {
                switch (valor) {
                case Usuario.accion_envido: {
                    //System.err.println("Usuario " + k + " envida " + apuestaval);
                    res = envidar(k, apuestaval);

                    while (res >= 0) {
                        res = envidar(res, apuestaval);
                    }

                    return 1;
                }
                case Usuario.accion_veo: {
                    k++;
                    break;
                }
                }

            } else {
                //System.err.println("Usuario " + k + " pasa");
                //Mensajito por pantalla: "Usuario k pasa"
            }
        }
        //System.err.println("Se queda en paso");

        return 0;//todos pasan

    }

    private int pares_juego() {
        int valor = 0, res;
        apuestavista = 0;
        if ((jug[0] == 1) && (jug[1] == 0) && (jug[2] == 0) && (jug[3] == 0)
                || (jug[0] == 0) && (jug[1] == 1) && (jug[2] == 0)
                && (jug[3] == 0) || (jug[0] == 0) && (jug[1] == 0)
                && (jug[2] == 1) && (jug[3] == 0) || (jug[0] == 0)
                && (jug[1] == 0) && (jug[2] == 0) && (jug[3] == 1)
                || (jug[0] == 1) && (jug[1] == 0) && (jug[2] == 1)
                && (jug[3] == 0) || (jug[0] == 0) && (jug[1] == 1)
                && (jug[2] == 0) && (jug[3] == 1)) {
            //no se juega a pares o juego
           //System.err.println("\n\n");

            partida.setApuesta(0, false);
            return -1;
        }
        for (int k = 3; k > -1; k--) {
            if (haveToClose())
                return 0;
            if (jug[k] == 1) {
                Joc.getInstance().sendWake(k);
                valor = paso_envido_veo_wait(k);
                if (valor != Usuario.accion_paso) {
                    switch (valor) {
                    case Usuario.accion_envido: {
                       // System.err.println("Usuario " + k + " envida "+ apuestaval);
                        res = envidar2(k, apuestaval);

                        while (res >= 0) {
                            res = envidar2(res, apuestaval);
                        }

                        return 1;
                    }
                    case Usuario.accion_veo: {
                        k++;
                        break;
                    }
                    }

                } else {
                    //System.err.println("Usuario " + k + " pasa");
                    //Mensajito por pantalla: "Usuario k pasa"
                }
            }

        }

        //System.err.println("Se queda en paso");
        //partida.setApuesta(1, false);
        return 0;//todos pasan
    }

    private int envidar2(int k, int apuesta) {
        int j = 0;
        int resp;
        if (k == 1 || k == 3) {//Ha envidado el mano o su companyero
            j = 0;//responde el equipo del usuario 0
        } else if (k == 0 || k == 2) {//Ha envidado el postre o su companyero
            j = 1;//responde el equipo del usuario 1
        }
        if ((jug[j] == 1) && (jug[j + 2] == 1)) {
            resp = envidar((j + 1), apuesta);
            return resp;
        }
        if ((jug[j] == 1) && (jug[j + 2]) == 0) {
            resp = subenvite(j, apuesta);
            return resp;
        }
        if ((jug[j] == 0) && (jug[j + 2]) == 1) {
            resp = subenvite((j + 2), apuesta);
            return resp;
        }
        return -3;

    }

    private int subenvite(int k, int apuesta) {
        int resp;
        Joc.getInstance().sendWake(k);
        resp = paso_envido_veo_wait(k);
        apuestavista += apuestaval;

        switch (resp) {

        case Usuario.accion_paso: {
            apuestavista -= apuestaval;
            if (apuestavista == 0) {
                apuestavista = 1;
            }

            //System.err.println("Usuario " + k + " pasa");
           // System.err.println("PONER " + apuestavista+ " PUNTOS al equipo del usuario " + ((k + 1) % 2));
            //poner apuestavista al otro equipo
            sendPuntosMas(((k + 1) % 2), apuestavista);
            puntos[z] = 0;
            return -1;
        }
        case Usuario.accion_veo: {
            //println usuario j+2 ve la apuesta
            //System.err.println("Usuario " + k + " ve la apuesta, se ven "   + apuestavista);
            if (apuestaval >= 31) {
                ordago();
            } else {
                puntos[z] = apuestavista;//poner
                // la
                // apuesta
                return -2;
            }
        }
        case Usuario.accion_envido: {
            //System.err.println("Usuario " + k + " envida " + apuestaval + " mas");

            return k;
        }
        }
        return -3;
    }

    private int envidar(int k, int apuesta) {
        int j = 0;
        int resp1, resp2;

        apuestavista += apuestaval;

        if (k == 1 || k == 3) {//Ha envidado el mano o su companyero
            j = 0;//responde el equipo del usuario 0
        } else if (k == 0 || k == 2) {//Ha envidado el postre o su companyero
            j = 1;//responde el equipo del usuario 1
        }

        Joc.getInstance().sendWake(j + 2);
        resp1 = paso_envido_veo_wait(j + 2);

        switch (resp1) {
        case Usuario.accion_paso: {
            //System.err.println("Usuario " + (j + 2) + " pasa");
            //println usuario j pasa
            Joc.getInstance().sendWake(j);
            resp2 = paso_envido_veo_wait(j);
            switch (resp2) {
            case Usuario.accion_paso: {
                apuestavista -= apuestaval;
                if (apuestavista == 0) {
                    apuestavista = 1;
                }

               // System.err.println("Usuario " + j + " pasa");
                //println usuario j+2 pasa
                //System.err.println("PONER " + apuestavista + " PUNTOS al equipo del usuario " + k);
                //poner apuestavista al otro equipo
                sendPuntosMas(k, apuestavista);
                puntos[z] = 0;
                return -1;

            }
            case Usuario.accion_veo: {
                //println usuario j+2 ve la apuesta
                //System.err.println("Usuario " + j + " ve la apuesta, se ven "+ apuestavista);
                if (apuestaval >= 31) {
                    ordago();

                } else {
                    puntos[z] = apuestavista;
                    return -2;
                }
            }
            case Usuario.accion_envido: {
                //println usuario j+2 envida apuestaval m?s
                //System.err.println("Usuario " + j + " envida " + apuestaval+ " mas");

                return j;
            }
            }
            break;
        }
        case Usuario.accion_veo: {
            //println usuario j quiere ver la apuesta
            //System.err.println("Usuario " + (j + 2) + " quiere ver la apuesta");
            Joc.getInstance().sendWake(j);
            resp2 = paso_envido_veo_wait(j);
            switch (resp2) {
            case Usuario.accion_paso: {
                //println usuario j+2 pasa
                //System.err.println("Usuario " + j + " pasa");
                //println usuario j ve la apuesta
                //System.err.println("Usuario " + (j + 2)+ " ve la apuesta, se ven " + apuestavista);
                if (apuestaval >= 31) {
                    ordago();
                } else {
                    puntos[z] = apuestavista;
                }
                return -1;
            }
            case Usuario.accion_veo: {
                //println usuario j+2 ve la apuesta
               //System.err.println("Usuario " + j + " ve la apuesta, se ven " + apuestavista);
                //poner la apuesta
                if (apuestaval >= 31) {
                    ordago();

                } else {
                    puntos[z] = apuestavista;
                }
                return -2;
            }

            case Usuario.accion_envido: {
                //System.err.println("Usuario " + j + " envida " + apuestaval+ " mas");

                return j;
            }
            }
            break;
        }
        case Usuario.accion_envido: {

            //System.err.println("Usuario " + (j + 2) + " envida " + apuestaval+ " mas");

            return j;
        }
        }
        return -3;
    }

    public void puntosMas(int e, int puntosmas) {
        Jugadors.getInstance().setPuntos(e,
                Jugadors.getInstance().getPuntos(e) + puntosmas);
    }
    
    public void sendPuntosPor(int e, int puntosmas, int por){
        puntosMas(e, puntosmas);
        GameData gd = new GameData();
        gd.setPuntosPor(por);
        if ((e % 2)==1)
            gd.setPuntos(puntosmas, 0);
        else
            gd.setPuntos(0, puntosmas);
        Joc.getInstance().sendGameData(gd);
    }
    

    public void sendPuntos() {
        GameData gd = new GameData();
        gd.setPuntos(Jugadors.getInstance().getPuntos(1), Jugadors
                .getInstance().getPuntos(0));
        Joc.getInstance().sendGameData(gd);
    }

    public void sendPuntosMas(int e, int puntosmas) {
        puntosMas(e, puntosmas);
        sendPuntos();
        if(Jugadors.getInstance().getPuntos(e)>30) fin();
    }

    public boolean haveToClose() {
        return !running;
    }

    public void accion_wait(int a) {
        accion = 0;
        try {
            while (accion != a)
                synchronized (this) {
                    wait();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCartas(int i) {
        if (i == 0)
            return;

        GameData gd = new GameData();
        gd.setCartas(Jugadors.getInstance().get(i).getCartas(), Jugadors
                .getInstance().get(i).getNick());
        Joc.getInstance().sendGameData(gd, i);
    }

    public void sendCartasToAll() {
        int i;
        GameData gd = new GameData();
        for (i = 3; i > -1; i--) {
            gd.setCartas(Jugadors.getInstance().get(i).getCartas(), Jugadors
                    .getInstance().get(i).getNick());
            Joc.getInstance().sendGameData(gd);
        }
    }
    
    private int mus_corto_wait(int user) {
        accion = -1;
        accionfrom = -1;
        try {
            while (true) {
                synchronized (this) {
                    wait();
                }
                if ((accionfrom == user)
                        && (accion == Usuario.accion_mus || accion == Usuario.accion_corto))
                    return accion;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int paso_envido_veo_wait(int user) {
        accion = -1;
        accionfrom = -1;
        try {
            while (true) {
                synchronized (this) {
                    wait();
                }
                if ((accionfrom == user)
                        && (accion == Usuario.accion_paso
                                || accion == Usuario.accion_envido || accion == Usuario.accion_veo))
                    return accion;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void descarte(int j, Cartas c) {
        if (j > -1 && j < 4)
            desc[j] = c;
    }

    public void descarteYa() {
        int j;
        Cartas c2;
        for (j = 3; j > -1; j--)
            if (desc[j] != null) {
                c2 = Jugadors.getInstance().descarta(j, desc[j], repartir,
                        descartado);
                if (c2 != null) {
                    repartir = descartado;
                    descartado = new Cartas();
                    Joc.getInstance().sendEventoAll(Joc.evento_barajar);
                    repartir.shuffle();
                    if (c2.size() > 0)
                        Jugadors.getInstance().descarta(j, desc[j], repartir,
                                descartado);
                }
                sendCartas(j);
                desc[j] = null;
            }
    }

    /**
     * 
     * @param id
     *            jugador
     * @param aposta
     *            valor de la apuesta
     * @param ordago
     */
    public void setApuesta(int id, int apostaVal, boolean ordago) {
        this.apuestaordago = ordago;
        this.apuestaval = apostaVal;
    }

    public void rcv_accion(int i, int a) {
        accion = a;
        accionfrom = i;

        synchronized (this) {
            notify();
        }
    }

    private void puntosPaTos() {
        int jugador, ret, pares = 0, juegos = 0;
        jugador=partida.grandespequesGana(true);
        sendPuntosPor(jugador, puntos[0], Partida.GRANDES);//grandes
        if (Jugadors.getInstance().getPuntos(jugador % 2) > 30){
            sendPuntos();
            return;
        }
        jugador=partida.grandespequesGana(false);
        sendPuntosPor(jugador, puntos[1], Partida.CHICAS);//chicas
        if (Jugadors.getInstance().getPuntos(jugador % 2) > 30){
            sendPuntos();
            return;
        }
        jugador = partida.parGana();
        ret = partida.contaPar((jugador % 2));
        if (ret > 3000) {
            pares += 3;
        } else if (ret > 199) {
            pares += 2;
        } else if ((ret < 199) && (ret != 0)) {
            pares += 1;
        }
        ret = partida.contaPar((jugador % 2) + 2);
        if (ret > 3000) {
            pares += 3;
        } else if (ret > 150) {
            pares += 2;
        } else if ((ret < 150) && (ret != 0)) {
            pares += 1;
        }
        sendPuntosPor(jugador, puntos[2] + pares, Partida.PARES);//par + puntos por pares
        if (Jugadors.getInstance().getPuntos(jugador % 2) > 30){
            sendPuntos();
            return;
        }
        jugador = partida.jocGana();
        ret = partida.contaPuntsJoc((jugador % 2));
        juegos += ret;
        ret = partida.contaPuntsJoc((jugador % 2) + 2);
        juegos += ret;
        sendPuntosPor(jugador, puntos[3] + juegos, Partida.JUEGO);//juego
        if (Jugadors.getInstance().getPuntos(jugador % 2) > 30){
            sendPuntos();
            return;
        }
        jugador=partida.puntoGana();
        sendPuntosPor(jugador, puntos[4], Partida.PUNTO); //alpunto
        if (Jugadors.getInstance().getPuntos(jugador % 2) > 30){
            sendPuntos();
            return;
        }

        //System.err.println(partida.grandespequesGana(true) + " " + puntos[0]+ "\n");
        //System.err.println(partida.grandespequesGana(false) + " " + puntos[1]+ "\n");
        //System.err.println(partida.parGana() + " " + puntos[2] + "\n");
        //System.err.println(pares + " puntos por pares al equipo del jugador "+ partida.parGana() + "\n");
        //System.err.println(partida.jocGana() + " " + puntos[3] + "\n");
        //System.err.println(juegos + " puntos por juegos al equipo del jugador "+ partida.jocGana() + "\n");
        //System.err.println(partida.puntoGana() + " " + puntos[4] + "\n");

        sendPuntos();
    }

    private void ordago() {
        int ganador = 0;
        switch (z) {
        case 0:
            ganador = partida.grandespequesGana(true);
            break;
        case 1:
            ganador = partida.grandespequesGana(false);
            break;
        case 2:
            ganador = partida.parGana();
            break;
        case 3:
            ganador = partida.jocGana();
            break;
        case 4:
            ganador = partida.puntoGana();
            break;
        }
        //System.err.println("Gana el equipo del jugador" + ganador);
        sendPuntosMas(ganador, 31);
    }

    public void empieza(){
        Thread tpostre = new Thread(this);
        tpostre.setName("Joc_Postre_Thread");
        tpostre.start();
    }

    public void para() {
        running = false;
    }
}