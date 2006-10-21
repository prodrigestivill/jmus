/*
 * Created on May 11, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */

package ist.mus.gui;

import ist.mus.comm.JID;

import java.awt.Frame;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * @author Pau Rodriguez-Estivill
 */
public class JMusBuscar extends javax.swing.JDialog {
    private final static String[] cservers = { "conf.jabberes.org",
            "muc.jabber.upc.es", "conferencia.jabber-hispano.org",
            "conference.seunet.org", "conference.jabber.org" };

    public JID room = new JID();

    public boolean func = false;

    /** Creates new form JMusInvite */
    public JMusBuscar(Frame arg0, boolean arg1) {
        super(arg0, "Buscar sala", arg1);
        initComponents();
        cancelar.addActionListener(new HandlerActionAdapter(this));
        cancelar.setActionCommand("close");
        refresh.addActionListener(new HandlerActionAdapter(this));
        refresh.setActionCommand("refresh");
        entrar.addActionListener(new HandlerActionAdapter(this));
        entrar.setActionCommand("entrar");
        lista.addMouseListener(new HandlerActionAdapter(this, "lista"));
        room.setJID(cservers[0]);
        listServer();
        setVisible(true);
    }

    public void listServer() {
        int i;
        Vector v = new Vector();
        v.add("Anadir...");
        for (i = 1; i < 17; i++) {
            v.add("JMus_Mesa" + i);
            /*
             * + " (Ocupantes:" + Comunicacion.getComm().getRoomOcupants(r) +
             * ")");
             */
        }
        lista.setListData(v);
    }

    public void refresh_ActionPerformed() {
        load_room();
        refresh_room();
        listServer();
    }

    private void load_room() {
        room.setJID(servers.getModel().getSelectedItem().toString());
        room.setNick(nick.getText());
    }

    private void refresh_room() {
        servers.getModel().setSelectedItem(room.getJID());
    }

    public void lista_ActionPerformed() {
        load_room();
        /*
         * room.setName(lista.getSelectedValue().toString().substring(0,
         * lista.getSelectedValue().toString().indexOf(" ")));
         */
        if (lista.getSelectedIndex() == 0) {
            String jop = JOptionPane.showInputDialog(this,
                    "Nombre de la nueva mesa?");
            if (jop instanceof String)
                room.setName(jop);
        } else {
            room.setName(lista.getSelectedValue().toString());
        }
        refresh_room();
    }

    public void entrar_ActionPerformed() {
        load_room();
        if (room.getNick() != null && room.getNick().length() == 0) {
            nick.setText("guest" + (new Random()).nextInt(1000));
            return;
        }
        if (room.getName() != null && room.getName().length() == 0) {
            lista.setSelectedIndex(1);
            lista_ActionPerformed();
            return;
        }
        func = true;
        dispose();
    }

    public void close_ActionPerformed() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        lista = new javax.swing.JList();
        servers = new javax.swing.JComboBox();
        refresh = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        entrar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        nick = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        jPanel1.setLayout(null);

        jPanel1.setPreferredSize(new java.awt.Dimension(312, 440));
        lista.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        jPanel1.add(lista);
        lista.setBounds(10, 60, 290, 290);

        servers.setEditable(true);
        servers.setModel(new javax.swing.DefaultComboBoxModel(cservers));
        jPanel1.add(servers);
        servers.setBounds(10, 20, 290, 24);

        refresh.setText("Refresh");
        jPanel1.add(refresh);
        refresh.setBounds(10, 360, 81, 25);

        cancelar.setText("Cancelar");
        cancelar.setDefaultCapable(false);
        jPanel1.add(cancelar);
        cancelar.setBounds(210, 400, 87, 25);

        entrar.setText("Entrar");
        entrar.setDefaultCapable(false);
        jPanel1.add(entrar);
        entrar.setBounds(130, 400, 71, 25);

        jLabel5.setText("Nick");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(120, 360, 27, 15);

        jPanel1.add(nick);
        nick.setBounds(170, 360, 130, 19);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;

    private javax.swing.JButton entrar;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JList lista;

    private javax.swing.JTextField nick;

    private javax.swing.JButton refresh;

    private javax.swing.JComboBox servers;
    // End of variables declaration//GEN-END:variables

}