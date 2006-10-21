/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.comm;

import ist.mus.MusDebug;
import ist.mus.gui.GUIcode;
import ist.mus.joc.Joc;
import ist.mus.jugadors.Jugador;
import ist.mus.jugadors.Jugadors;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SSLXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.UserStatusListener;
import org.jivesoftware.smackx.packet.DelayInformation;

/**
 * @author Pau Rodriguez-Estivill
 */
public class Comm implements PacketListener, ParticipantStatusListener,
        UserStatusListener {
    public static final String resource = "JMus_";

    public XMPPConnection xmpp = null;

    MultiUserChat sala = null;

    private List allGameUser = new LinkedList();

    private Random resourcernd = null;

    private Object listener = null;

    private JID room = null;

    public Comm(String user, String passw, String server, int port, boolean ssl)
            throws XMPPException {
        this(server, port, ssl);
        login(user, passw);
    }

    public Comm(String server, int port, boolean ssl) throws XMPPException {
        try {
            if (ssl)
                xmpp = new SSLXMPPConnection(server, port);
            else
                xmpp = new XMPPConnection(server, port);
        } catch (Exception e) {
            throw (XMPPException) e;
        }

        PacketFilter filter = new PacketTypeFilter(Message.class);
    }

    public void setRoom(JID r) {

        Jugadors.getInstance().clear();
        Jugadors.getInstance().setMyJugador(
                new Jugador(new JID(r.getFullJID())));

        if (sala != null) {
            xmpp.removePacketListener(this);
            sala.leave();
        }
        room = r;
        sala = new MultiUserChat(xmpp, r.getJID());

        try {
            sala.create(r.getNick());
            sala.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
            sala
                    .changeSubject("Sala dedicada al juego JMus, porfavor solo entrar con dicho programa.");
        } catch (Exception e) {
        }

        if (!sala.isJoined()) {
            try {
                sala.join(r.getNick());
            } catch (Exception e) {
                sala = null;
            }
        }

        if (sala != null)
            try {
                sala.addUserStatusListener(this);
                sala.addParticipantStatusListener(this);

                PacketFilter filter = new AndFilter(new PacketTypeFilter(sala
                        .createMessage().getClass()), new FromContainsFilter(
                        sala.getRoom()));
                xmpp.createPacketCollector(filter);
                xmpp.addPacketListener(this, filter);

                jugador_sendAM();
            } catch (Exception e) {
            }
    }

    public void sendAllGameUsers(String msg) {
        if ((sala != null) && (sala.isJoined()))
            try {
                Message cs = sala.createMessage();
                cs.setBody(msg);
                sendPacket(cs);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean inaRoom() {
        return (sala != null) && sala.isJoined();
    }

    public int getRoomNumber() {
        if (!inaRoom())
            return -1;
        return sala.getOccupantsCount();
    }

    public JID getRoom() {
        if (!inaRoom())
            return new JID();
        return new JID(room.getFullJID());
    }

    public Packet createPacket() {
        if ((sala != null) && (sala.isJoined()))
            return sala.createMessage();
        else
            return new Message();
    }

    public void sendPacket(Packet pkt) {
        xmpp.sendPacket(pkt);
    }

    public void sendSena(int s) {
        GameData gd = new GameData();
        gd.setSena(s);
        sendPacket(gd.fillToPacket());
        gd = null;
    }

    public void close() {
        /*
         * Presence presence = new Presence(Presence.Type.UNAVAILABLE); try{
         * xmpp.sendPacket(presence); }catch(Exception e){};
         */
        Joc.stop();
        xmpp.close();
    }

    public void login(String n, String p) throws XMPPException {
        if (resourcernd == null)
            resourcernd = new Random(System.currentTimeMillis());
        try {
            xmpp.login(n, p, resource + resourcernd.nextInt(200));
        } catch (Exception e) {
            throw (XMPPException) e;
        }
        ;
    }

    private void jugador_sendAM() {
        if (!Comunicacion.getComm().inaRoom())
            return;
        GameData gd = new GameData();
        gd.setAddMe(true);
        sendPacket(gd.fillToPacket());
        //MusDebug.DebugMsg("Sended AddMe");
        //MusDebug.DebugMsg(gd.fillToPacket().toXML());
        gd = null;
    }

    private void jugador_addMe(String from) {
        JID jugid = new JID(from);
        Jugador jug = null;

        if (Jugadors.getInstance().size() > 3)
            return;

        jug = new Jugador(jugid);
        Jugadors.getInstance().addJugador(jug);
        jugador_sendAM();
        if (Jugadors.getInstance().size() == 4)
            Joc.start();
    }

    public void setEventListener(Object arg0) {
        listener = arg0;
    }

    public void processPacket(Packet arg0) {
        // Se descarta todo si no estas en una sala
        if (!inaRoom())
            return;
        // Si es antiguo se descarta
        if ((arg0.getExtension("x", "jabber:x:delay")) instanceof DelayInformation)
            return;
        GameData gd = new GameData(arg0);
        // Comprueva si hay mensajes
        if (arg0 instanceof Message
                && ((Message) arg0).getBody() instanceof String) {
            GUIcode.getInstance().reciveMsg(gd.getFrom().getNick(),
                    ((Message) arg0).getBody());

        }
        // Comprueva si son datos de juego
        if (GameData.isGameData(arg0)) {
            if (Jugadors.getInstance().hasJugador(gd.getFrom())) {
                if (gd.getSena() != 0) {
                    GUIcode.getInstance().setCara(
                            Jugadors.getInstance().getID(gd.getFrom()),
                            gd.getSena());
                    gd.setSena(0);
                }
            } else if (gd.getAddMe())
                jugador_addMe(arg0.getFrom());

            gd.setAddMe(false);
            // Manda una copia del paquete al listener
            if (Jugadors.getInstance().hasJugador(gd.getFrom())
                    && listener != null && !gd.isEmpty()) {

                Class argType[] = new Class[1];
                argType[0] = gd.getClass();
                //Busca el method en la class
                Method me = null;
                try {
                    me = listener.getClass().getDeclaredMethod("processData",
                            argType);
                } catch (Exception err) {
                }
                //Ejecuta el method
                try {
                    if (me != null) {
                        Object args[] = new Object[1];
                        args[0] = gd;
                        me.invoke(listener, args);
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
        gd = null;
    }

    public void joined(String arg0) {
        if (Jugadors.getInstance().size() > 3)
            try {
                sala.kickParticipant(arg0, "Full");
            } catch (Exception e) {
            }
    }

    public void left(String arg0) {
        JID j = new JID(arg0);
        if (Jugadors.getInstance().hasJugador(j)) {
            MusDebug.InfoMsg(j.getNick() + " ha salido.");
            Joc.stop();
            sala.leave();
        }
    }

    public void kicked(String arg0) {
        left(arg0);
    }

    public void voiceGranted(String arg0) {
    };

    public void voiceRevoked(String arg0) {
    };

    public void banned(String arg0) {
        left(arg0);
    }

    public void membershipGranted(String arg0) {
    };

    public void membershipRevoked(String arg0) {
    };

    public void moderatorGranted(String arg0) {
    };

    public void moderatorRevoked(String arg0) {
    };

    public void ownershipGranted(String arg0) {
    };

    public void ownershipRevoked(String arg0) {
    };

    public void adminGranted(String arg0) {
    };

    public void adminRevoked(String arg0) {
    };

    public void nicknameChanged(String arg0) {
        //TODO nose
    }

    public void kicked(String arg0, String arg1) {
        MusDebug.ErrMsg("Sala llena!");
    }

    public void voiceGranted() {

    }

    public void voiceRevoked() {

    }

    public void banned(String arg0, String arg1) {
        kicked(arg0, arg1);
    }

    public void membershipGranted() {

    }

    public void membershipRevoked() {

    }

    public void moderatorGranted() {

    }

    public void moderatorRevoked() {

    }

    public void ownershipGranted() {

    }

    public void ownershipRevoked() {

    }

    public void adminGranted() {

    }

    public void adminRevoked() {

    }
}