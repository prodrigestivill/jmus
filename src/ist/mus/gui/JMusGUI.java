/*
 * Created on May 6, 2005
 *
 * Copyright 2005 Pau Rodriguez-Estivill <prodrigestivill@yahoo.es>
 *
 * This software is licensed under the GNU General Public License.
 * See http://www.gnu.org/copyleft/gpl.html for details.
 */

package ist.mus.gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * @author Pau Rodriguez-Estivill
 */
public class JMusGUI extends javax.swing.JFrame {

    JCard cartas_juego[][] = new JCard[4][4];

    JCard cartas_barajas[] = new JCard[4];

    JSmile smiles[] = new JSmile[3];

    JBolas puntos[] = new JBolas[4];

    javax.swing.JLabel nombres[] = new javax.swing.JLabel[3];

    javax.swing.JLabel cequipos[] = new javax.swing.JLabel[2];

    javax.swing.JLabel cpuntos[] = new javax.swing.JLabel[2];

    javax.swing.JLabel cjuegos[] = new javax.swing.JLabel[2];

    javax.swing.JLabel cvacas[] = new javax.swing.JLabel[2];

    /** Creates new form JMusGUI */
    public JMusGUI() {
        initComponents();
        initCenter();
        initCards();
    }

    /**
     * This method is called from within the constructor to initialize the
     * centerpane.
     */
    private void initCenter() {
        int i;

        /* Panel Central */
        taulacentral.setLocation(
                (taula.getWidth() - taulacentral.getWidth()) / 2, (taula
                        .getHeight() - taulacentral.getHeight()) / 2);

        for (i = 0; i < 2; i++) {
            cequipos[i] = new JLabel();
            cpuntos[i] = new JLabel();
            cjuegos[i] = new JLabel();
            cvacas[i] = new JLabel();
        }

        i = 0;
        cequipos[i].setForeground(new java.awt.Color(204, 204, 204));
        cequipos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cequipos[i].setText("Mio");
        ctop.add(cequipos[i]);

        cpuntos[i].setForeground(new java.awt.Color(204, 204, 204));
        cpuntos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cpuntos[i].setText("0");
        ctop.add(cpuntos[i]);

        cjuegos[i].setForeground(new java.awt.Color(204, 204, 204));
        cjuegos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cjuegos[i].setText("0");
        ctop.add(cjuegos[i]);

        cvacas[i].setForeground(new java.awt.Color(204, 204, 204));
        cvacas[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cvacas[i].setText("0");
        ctop.add(cvacas[i]);

        i = 1;
        cequipos[i].setForeground(new java.awt.Color(204, 204, 204));
        cequipos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cequipos[i].setText("Otro");
        ctop.add(cequipos[i]);

        cpuntos[i].setForeground(new java.awt.Color(204, 204, 204));
        cpuntos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cpuntos[i].setText("0");
        ctop.add(cpuntos[i]);

        cjuegos[i].setForeground(new java.awt.Color(204, 204, 204));
        cjuegos[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cjuegos[i].setText("0");
        ctop.add(cjuegos[i]);

        cvacas[i].setForeground(new java.awt.Color(204, 204, 204));
        cvacas[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cvacas[i].setText("0");
        ctop.add(cvacas[i]);
    }

    /**
     * This method is called from within the constructor to initialize the
     * cards.
     */
    private void initCards() {
        int i, j;
        final int cartawidth = 8; /* Espacio real entre cartas */
        final int cartamargin = 2; /* Espacio real entre cartas */
        final int smilesmargin = 3; /* Espacio real entre cartas */

        /* Cartas Jugadores */
        for (j = 0; j < 4; j++)
            for (i = 0; i < 4; i++) {
                cartas_juego[j][i] = new JCard(j);
                taula.add(cartas_juego[j][i]);
            }
        /* Cartas baraja */
        for (j = 0; j < 4; j++) {
            cartas_barajas[j] = new JCard(null, (j + 1) % 4, true);
            cartas_barajas[j].setVisible(false);
            taula.add(cartas_barajas[j]);
        }

        /* Smiles 3 Jugadors */
        for (j = 0; j < 3; j++) {
            smiles[j] = new JSmile();
            taula.add(smiles[j]);
        }

        /* Nombres Jugadores */
        for (j = 0; j < 3; j++) {
            nombres[j] = new javax.swing.JLabel();
            nombres[j].setForeground(Color.white);
            nombres[j].setHorizontalAlignment(SwingConstants.LEFT);
            taula.add(nombres[j]);
        }

        /* Puntos */
        puntos[0] = new JBolas(0, 5); /* Amarracos */
        puntos[1] = new JBolas(1, 5); /* Amarracos */
        puntos[2] = new JBolas(2, 1); /* Piedras */
        puntos[3] = new JBolas(3, 1); /* Piedras */
        for (j = 0; j < 4; j++)
            taula.add(puntos[j]);

        j = 2; /* Top Cartas */
        for (i = 0; i < 4; i++) {
            cartas_juego[j][i]
                    .setLocation(
                            (int) ((taula.getWidth() - 4
                                    * cartas_juego[j][i].getWidth() - 3 * cartawidth) / 2 + (3 - i)
                                    * (cartas_juego[j][i].getWidth() + 0.75 * cartawidth)),
                            cartamargin);
        }
        cartas_barajas[j]
                .setLocation(
                        ((taula.getWidth() - cartas_juego[j][0].getX() - cartas_juego[j][0]
                                .getWidth()) / 2 - cartas_barajas[j].getWidth() / 2)
                                + cartas_juego[j][0].getX()
                                + cartas_juego[j][0].getWidth(),
                        cartas_juego[j][i - 1].getY()
                                + cartas_juego[j][i - 1].getHeight() / 2
                                - cartas_barajas[j].getHeight() / 2);
        smiles[j - 1].setLocation(cartas_juego[j][0].getX()
                + cartas_juego[j][0].getWidth() + smilesmargin,
                cartas_juego[j][0].getY());
        i = smiles[j - 1].getX() + smiles[j - 1].getWidth() + smilesmargin;
        nombres[j - 1].setBounds(i, smiles[j - 1].getY(), taula.getWidth() - i,
                smiles[j - 1].getHeight());

        j = 1; /* Left Cartas */
        for (i = 0; i < 4; i++) {
            cartas_juego[j][i]
                    .setLocation(
                            cartamargin,
                            (int) ((taula.getHeight() - 4
                                    * cartas_juego[j][i].getHeight() - 3 * cartawidth) / 2 + i
                                    * (cartas_juego[j][i].getHeight() + 0.75 * cartawidth)));
        }
        /*
         * cartas_barajas[j].setLocation(cartas_juego[j][i - 1].getX() +
         * cartas_juego[j][i - 1].getWidth() / 2 - cartas_barajas[j].getWidth() /
         * 2, cartas_juego[j][0].getY() / 2 - cartas_barajas[j].getHeight() /
         * 2);
         */
        cartas_barajas[j].setLocation(cartas_juego[j][i - 1].getX()
                + cartas_juego[j][i - 1].getWidth() / 2
                - cartas_barajas[j].getWidth() / 2, smilesmargin);
        smiles[j - 1].setLocation(cartas_juego[j][0].getX(), cartas_juego[j][0]
                .getY()
                - smiles[j - 1].getHeight() - smilesmargin);
        i = smiles[j - 1].getX() + smiles[j - 1].getWidth() + smilesmargin;
        nombres[j - 1].setBounds(i, smiles[j - 1].getY(), cartas_juego[j][0]
                .getX()
                + cartas_juego[j][0].getWidth() - i - cartamargin,
                smiles[j - 1].getHeight());

        j = 3; /* Right Cartas */
        for (i = 0; i < 4; i++) {
            cartas_juego[j][i]
                    .setLocation(
                            taula.getWidth() - cartas_juego[j][i].getWidth()
                                    - cartamargin,
                            (int) ((taula.getHeight() - 4
                                    * cartas_juego[j][i].getHeight() - 3 * cartawidth) / 2 + (3 - i)
                                    * (cartas_juego[j][i].getHeight() + 0.75 * cartawidth)));
        }
        cartas_barajas[j]
                .setLocation(cartas_juego[j][i - 1].getX()
                        + cartas_juego[j][i - 1].getWidth() / 2
                        - cartas_barajas[j].getWidth() / 2, ((taula.getHeight()
                        - cartas_juego[j][0].getY() - cartas_juego[j][0]
                        .getHeight()) / 2 - cartas_barajas[j].getHeight() / 2)
                        + cartas_juego[j][0].getY()
                        + cartas_juego[j][0].getHeight());
        smiles[j - 1].setLocation(taula.getWidth() - smiles[j - 1].getWidth()
                - cartamargin, cartas_juego[j][3].getY()
                - smiles[j - 1].getHeight() - smilesmargin);
        nombres[j - 1].setBounds(cartas_juego[j][3].getX(), smiles[j - 1]
                .getY(), smiles[j - 1].getX() - cartas_juego[j][3].getX()
                - smilesmargin, smiles[j - 1].getHeight());
        nombres[j - 1].setHorizontalAlignment(SwingConstants.RIGHT);

        j = 0; /* Bottom Cartas */
        for (i = 0; i < 4; i++) {
            cartas_juego[j][i].setLocation((int) ((taula.getWidth() - 4
                    * cartas_juego[j][i].getWidth() - 3 * cartawidth) / 2 + i
                    * (cartas_juego[j][i].getWidth() + 0.75 * cartawidth)),
                    taula.getHeight() - cartas_juego[j][i].getHeight()
                            - cartamargin);
        }
        cartas_barajas[j].setLocation(cartas_juego[j][0].getX() / 2
                - cartas_barajas[j].getWidth() / 2, cartas_juego[j][i - 1]
                .getY()
                + cartas_juego[j][i - 1].getHeight()
                / 2
                - cartas_barajas[j].getHeight() / 2);
        centerPuntos();
        /*
         * taulacentral.setBounds(puntos[1].getX()+puntos[1].getWidth(),
         * puntos[2].getY()+puntos[2].getHeight(),
         * puntos[3].getX()-puntos[1].getX()-puntos[1].getWidth(),puntos[0].getY()-puntos[2].getY()-puntos[2].getHeight()-cartamargin);
         */
    }

    /**
     * Centra los objetos de puntos segun sus puntos
     *  
     */
    public void centerPuntos() {
        int j, cartamargin = 3;
        j = 2;
        puntos[j].setLocation((taula.getWidth() - puntos[j].getWidth()) / 2,
                cartas_juego[j][0].getY() + cartas_juego[j][0].getHeight()
                        + cartamargin);
        j = 1;
        puntos[j].setLocation(cartas_juego[j][0].getX()
                + cartas_juego[j][0].getWidth() + cartamargin, (taula
                .getHeight() - puntos[j].getHeight()) / 2);
        j = 3;
        puntos[j].setLocation(cartas_juego[j][0].getX() - puntos[j].getWidth()
                - cartamargin, (taula.getHeight() - puntos[j].getHeight()) / 2);
        j = 0;
        puntos[j]
                .setLocation((taula.getWidth() - puntos[j].getWidth()) / 2,
                        cartas_juego[j][0].getY() - puntos[j].getHeight()
                                - cartamargin);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        estat = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        taulabottom = new javax.swing.JPanel();
        entradaChat = new javax.swing.JTextField();
        textChatScroll = new javax.swing.JScrollPane();
        textChat = new javax.swing.JTextPane();
        taula = new javax.swing.JPanel();
        taulacentral = new javax.swing.JPanel();
        cbottom = new javax.swing.JPanel();
        cmdApuesta = new javax.swing.JSpinner();
        cmdEnvido = new javax.swing.JButton();
        cmdPaso = new javax.swing.JButton();
        cmdVeo = new javax.swing.JButton();
        cmdOrdago = new javax.swing.JButton();
        cmdCorto = new javax.swing.JButton();
        cmdMus = new javax.swing.JButton();
        cmdDescartar = new javax.swing.JButton();
        cmdSena = new javax.swing.JComboBox();
        ctop = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmdLabel = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menu_conect = new javax.swing.JMenuItem();
        menu_disconect = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menu_nou = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        menu_sortir = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menu_como = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        menu_acerca = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("JMus");
        setResizable(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 20));
        estat.setText("JMus");
        jPanel1.add(estat);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        taulabottom.setLayout(new java.awt.BorderLayout());

        taulabottom.setPreferredSize(new java.awt.Dimension(0, 75));
        taulabottom.add(entradaChat, java.awt.BorderLayout.SOUTH);

        textChatScroll
                .setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textChat.setEditable(false);
        textChatScroll.setViewportView(textChat);

        taulabottom.add(textChatScroll, java.awt.BorderLayout.CENTER);

        jPanel2.add(taulabottom, java.awt.BorderLayout.SOUTH);

        taula.setLayout(null);

        taula.setBackground(new java.awt.Color(0, 102, 0));
        taula.setPreferredSize(new java.awt.Dimension(510, 510));
        taulacentral.setLayout(new java.awt.BorderLayout());

        taulacentral.setOpaque(false);
        cbottom.setLayout(new java.awt.GridBagLayout());

        cbottom.setOpaque(false);
        cmdApuesta.setModel(new SpinnerNumberModel(2, 2, 30, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdApuesta, gridBagConstraints);

        cmdEnvido.setText("E");
        cmdEnvido.setToolTipText("Envido");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdEnvido, gridBagConstraints);

        cmdPaso.setText("P");
        cmdPaso.setToolTipText("Paso");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdPaso, gridBagConstraints);

        cmdVeo.setText("V");
        cmdVeo.setToolTipText("Veo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdVeo, gridBagConstraints);

        cmdOrdago.setText("O");
        cmdOrdago.setToolTipText("Ordago");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdOrdago, gridBagConstraints);

        cmdCorto.setText("C");
        cmdCorto.setToolTipText("Corto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdCorto, gridBagConstraints);

        cmdMus.setText("M");
        cmdMus.setToolTipText("Mus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdMus, gridBagConstraints);

        cmdDescartar.setText("D");
        cmdDescartar.setToolTipText("Descartar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdDescartar, gridBagConstraints);

        cmdSena.setModel(new javax.swing.DefaultComboBoxModel(
                JSmile.smiles_array));
        cmdSena.setToolTipText("Senyas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        cbottom.add(cmdSena, gridBagConstraints);

        taulacentral.add(cbottom, java.awt.BorderLayout.SOUTH);

        ctop.setLayout(new java.awt.GridLayout(3, 4));

        ctop.setOpaque(false);
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Equipo");
        ctop.add(jLabel3);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Puntos");
        ctop.add(jLabel4);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Juegos");
        ctop.add(jLabel5);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Vacas");
        ctop.add(jLabel6);

        taulacentral.add(ctop, java.awt.BorderLayout.NORTH);

        cmdLabel.setEditable(false);
        cmdLabel.setForeground(new java.awt.Color(255, 255, 255));
        cmdLabel.setBorder(null);
        cmdLabel.setFocusable(false);
        cmdLabel.setOpaque(false);
        taulacentral.add(cmdLabel, java.awt.BorderLayout.CENTER);

        taula.add(taulacentral);
        taulacentral.setBounds(150, 80, 220, 200);

        jPanel2.add(taula, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Juego");
        menu_conect.setText("Conectarse...");
        jMenu1.add(menu_conect);

        menu_disconect.setText("Desconectarse");
        menu_disconect.setEnabled(false);
        jMenu1.add(menu_disconect);

        jMenu1.add(jSeparator1);

        menu_nou.setText("Empezar...");
        menu_nou.setEnabled(false);
        jMenu1.add(menu_nou);

        jMenu1.add(jSeparator3);

        menu_sortir.setText("Sortir");
        jMenu1.add(menu_sortir);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ayuda");
        menu_como.setText("Como jugar...");
        jMenu2.add(menu_como);

        jMenu2.add(jSeparator2);

        menu_acerca.setText("Acerca de...");
        jMenu2.add(menu_acerca);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cbottom;

    public javax.swing.JSpinner cmdApuesta;

    public javax.swing.JButton cmdCorto;

    public javax.swing.JButton cmdDescartar;

    public javax.swing.JButton cmdEnvido;

    public javax.swing.JTextArea cmdLabel;

    public javax.swing.JButton cmdMus;

    public javax.swing.JButton cmdOrdago;

    public javax.swing.JButton cmdPaso;

    public javax.swing.JComboBox cmdSena;

    public javax.swing.JButton cmdVeo;

    private javax.swing.JPanel ctop;

    javax.swing.JTextField entradaChat;

    javax.swing.JLabel estat;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    javax.swing.JMenuItem menu_acerca;

    javax.swing.JMenuItem menu_como;

    javax.swing.JMenuItem menu_conect;

    javax.swing.JMenuItem menu_disconect;

    javax.swing.JMenuItem menu_nou;

    javax.swing.JMenuItem menu_sortir;

    javax.swing.JPanel taula;

    private javax.swing.JPanel taulabottom;

    private javax.swing.JPanel taulacentral;

    javax.swing.JTextPane textChat;

    javax.swing.JScrollPane textChatScroll;
    // End of variables declaration//GEN-END:variables

}
