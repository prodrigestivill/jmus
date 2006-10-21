/*
 * Created on May 26, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.joc;

import ist.mus.MathMus;
import ist.mus.gui.GUIcode;
import ist.mus.jugadors.Jugadors;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Usuario {

    public static final int accion_mus = 1;

    public static final int accion_descartar = 2;

    public static final int accion_corto = 3;

    public static final int accion_paso = 4;

    public static final int accion_veo = 5;

    public static final int accion_envido = 6;

    private boolean running = false;

    private boolean dormir;

    private int lastevento=0;
    
    private boolean lastordago=false;
    
    private boolean lastapuesta=false;

    Usuario() {
        super();
    }

    /**
     * Sortir quan !running
     */
    public void empieza() {
        running = true;
        dormir = true;
        lastevento=0;
        lastordago=false;
        lastapuesta=false;
        GUIcode.getInstance().textCentral_add("Empieza la partida!");
        Jugadors.getInstance().setPuntos();
    }

    public void descartar_ActionPerformed() {
        if (running && !dormir){
            if (Joc.getInstance().sendDecartarse())
                dormir();
        }else
            dormir();
    }

    public void mus_ActionPerformed() {
        if (running && !dormir) {
            Joc.getInstance().sendAccion(accion_mus);
        }
        dormir();
    }

    public void corto_ActionPerformed() {
        if (running && !dormir) {
            Joc.getInstance().sendAccion(accion_corto);
        }
        dormir();
    }

    public void paso_ActionPerformed() {
        if (running && !dormir) {
            Joc.getInstance().sendAccion(accion_paso);
        }
        dormir();
    }

    public void veo_ActionPerformed() {
        if (!dormir) {
            Joc.getInstance().sendAccion(accion_veo);
            if (lastevento==Joc.evento_final)
            	Jugadors.getInstance().hideAllCartas();
        }
        dormir();
    }

    public void envido_ActionPerformed() {
        if (running && !dormir) {
            Joc.getInstance().sendAposta(GUIcode.getInstance().getApuesta(),
                    false);
        }
        dormir();
    }

    public void ordago_ActionPerformed() {
        if (running && !dormir) {
            Joc.getInstance().sendAposta(31, true);
        }
        dormir();
    }

    public void para() {
        running = false;
        dormir();
        GUIcode.getInstance().textCentral_add("Fin de partida.");
    }

    public void despertar() {
        dormir = false;
        despertar_acciones();
    }

    public void dormir() {
        dormir = true;
        GUIcode.getInstance().setActivateButtons(-1, -1, -1, -1, -1, -1, -1);
    }

    public void evento(int e) {
        String msg = new String();
        switch (e) {
        case Joc.evento_barajar:
            msg = "Barajando...";
            break;
        case Joc.evento_descartar:
            msg = "Ahora descartate.";
            break;
        case Joc.evento_grandes:
            msg = "Vamos a grandes.";
            break;
        case Joc.evento_pequenyas:
            msg = "Vamos a pequenyas.";
            break;
        case Joc.evento_pares:
            msg = "Vamos a pares.";
            break;
        case Joc.evento_juego:
            msg = "Vamos a juego.";
            break;
        case Joc.evento_punto:
            msg = "Vamos a punto.";
            break;
        case Joc.evento_final:
            msg = "Pulsa Veo para continuar.";
            break;
        default:
            if (e >= Joc.evento_haspares && e <= Joc.evento_hasjuego) {
                msg = Jugadors.getInstance().get(
                        Joc.Manoid2GUIid(e - Joc.evento_haspares)).getNick()
                        + " tiene pares.";
            } else if (e > Joc.evento_hasjuego) {
                msg = Jugadors.getInstance().get(
                        Joc.Manoid2GUIid(e - Joc.evento_hasjuego)).getNick()
                        + " tiene juego.";
            }
        }
        lastevento = e;
        lastordago=false;
        lastapuesta=false;
        if (msg.length() > 0)
            GUIcode.getInstance().textCentral_add(msg);
    }

    public void despertar_acciones() {
        int l,p;
        switch (lastevento) {
	        case Joc.evento_barajar:
	        case Joc.evento_mus:
	            GUIcode.getInstance().setActivateButtons(-1, -1, -1, -1, 1, 1, -1);
	            break;
	        case Joc.evento_descartar:
	            GUIcode.getInstance().setActivateButtons(-1, -1, -1, -1, -1, -1, 1);
	            break;
	        case Joc.evento_grandes:
	        case Joc.evento_pequenyas:
	        case Joc.evento_pares:
	        case Joc.evento_juego:
	        case Joc.evento_punto:
	            l = MathMus.booleanToInt(!lastordago);
	        	p = MathMus.booleanToInt(lastapuesta || lastordago);
	            GUIcode.getInstance().setActivateButtons(l, l, 1, p, -1, -1, -1);
	            break;
	        case Joc.evento_final:
	            GUIcode.getInstance().setActivateButtons(-1, -1, -1, 1, -1, -1, -1);
	            Jugadors.getInstance().showAllCartas();
	            running = false;
	            break;
        }
    }

    /**
     * @param f
     * @param accion
     */
    public void rcv_accion(int f, int accion, int envido, boolean ordago) {
        String msg = new String(Jugadors.getInstance().get(f).getNick());
        switch (accion) {
        case accion_mus:
            msg += " dice mus.";
            break;
        case accion_corto:
            msg += " dice corto.";
            break;
        case accion_paso:
            msg += " dice paso.";
            break;
        case accion_veo:
            msg += " dice veo.";
            break;
        case accion_envido:
            if (ordago){
                msg += " lanza ordago.";
                lastordago = true;
            }else{
                msg += " dice envido " + envido + ".";
                if (!lastordago) lastapuesta= true;
            }
            break;
        default:
            msg = null;
        }
        if (msg != null)
            GUIcode.getInstance().textCentral_add(msg);
    }

    public void puntospor(int puntosPor, int p_mios, int p_otros) {
        if ((p_mios != 0 && p_otros != 0) || (p_mios == 0 && p_otros == 0))
            return;
        String msg;
        switch (puntosPor){
        	case Partida.GRANDES:
        	    msg = "grandes";
        	break;
        	case Partida.CHICAS:
        	    msg = "chicas";
        	break;
        	case Partida.PARES:
        	    msg = "pares";
        	break;
        	case Partida.JUEGO:
        	    msg = "juego";
        	break;
        	case Partida.PUNTO:
        	    msg = "punto";
        	break;
        	default:
        	    msg = "otros";
        }
        
        if (p_mios == 0)
            msg = "Ellos ganan "+ p_otros + " puntos en " + msg;
        if (p_otros == 0)
            msg = "Vosotros ganais " + p_mios + " puntos en " + msg;

        GUIcode.getInstance().textCentral_add(msg);
    }
}
