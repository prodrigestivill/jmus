/*
 * Created on May 16, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */
package ist.mus.comm;

/**
 * @author Pau Rodriguez-Estivill
 */
public class JID implements Comparable {

    String server = null;

    String name = null;

    String nick = null;

    public JID() {
        super();
    }

    public JID(JID j) {
        this(j.getFullJID());
    }

    public JID(String j) {
        this();
        setJID(j);
    }

    public void setJID(String jid) {
        String j = new String(jid);
        int a /* arroba */, b /* barra */;
        a = j.indexOf("@");
        b = j.indexOf("/");
        //Pillem el Server del JID
        if (a != -1) {
            name = j.substring(0, a);
            if (b == -1) {
                server = j.substring(a + 1);
                nick = new String("");
            } else {
                server = j.substring(a + 1, b);
                nick = j.substring(b + 1, j.length());
            }
        } else {
            nick = new String("");
            name = new String("");
            server = j;
        }
    }

    public void setName(String n) {
        name = new String(n);
    }

    public void setServer(String s) {
        server = new String(s);
    }

    public void setNick(String n) {
        nick = new String(n);
    }

    public String getName() {
        return new String(name);
    }

    public String getServer() {
        return new String(server);
    }

    public String getNick() {
        return new String(nick);
    }

    public String getJID() {
        String j = new String("");
        if ((name != null) && (name.length() != 0))
            j += name + "@";
        if ((server != null) && (server.length() != 0))
            j += server;
        return j;
    }

    public String getFullJID() {
        String j = new String("");
        if ((name != null) && (name.length() != 0))
            j += name + "@";
        if ((server != null) && (server.length() != 0))
            j += server;
        if ((nick != null) && (nick.length() != 0))
            j += "/" + nick;
        return j;
    }

    public String toString() {
        return getFullJID();
    }

    public int compareTo(Object arg0) {
        if (arg0 instanceof JID) {
            return getFullJID().compareToIgnoreCase(((JID) arg0).getFullJID());
        } else
            return -65535;
    }

}
