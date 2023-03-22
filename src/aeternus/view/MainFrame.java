/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author User
 */
public class MainFrame extends javax.swing.JFrame {
    
    private AeternusGUI ae;
    public MainFrame(AeternusGUI ae) {
        initComponents();
        this.ae = ae;
    }
    
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SplashScreen = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        MainMenu = new javax.swing.JPanel();
        transitionOut = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        logo = new javax.swing.JLabel();
        background = new javax.swing.JLabel();
        Main = new javax.swing.JPanel();
        Prototype1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Prototype = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aeternus");
        setBackground(new java.awt.Color(102, 102, 102));
        setExtendedState(6);
        setLocation(new java.awt.Point(0, 0));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1920, 1080));
        setResizable(false);
        getContentPane().setLayout(null);

        SplashScreen.setBackground(new java.awt.Color(102, 102, 102));
        SplashScreen.setName("SplashScreen"); // NOI18N
        SplashScreen.setPreferredSize(new java.awt.Dimension(1920, 1080));
        SplashScreen.setLayout(null);

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/splashScreen.gif"))); // NOI18N
        jLabel5.setToolTipText("");
        SplashScreen.add(jLabel5);
        jLabel5.setBounds(0, 0, 1920, 1080);

        getContentPane().add(SplashScreen);
        SplashScreen.setBounds(0, 0, 1920, 1080);

        MainMenu.setBackground(new java.awt.Color(102, 102, 102));
        MainMenu.setName("MainMenu"); // NOI18N
        MainMenu.setPreferredSize(new java.awt.Dimension(1920, 1080));
        MainMenu.setLayout(null);

        transitionOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/transitionToGame.gif"))); // NOI18N
        MainMenu.add(transitionOut);
        transitionOut.setBounds(0, 0, 1920, 1080);
        transitionOut.setVisible(false);

        startButton.setBackground(new java.awt.Color(0, 0, 0));
        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/startIcon.png"))); // NOI18N
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });
        MainMenu.add(startButton);
        startButton.setBounds(0, 900, 1920, 180);

        logo.setBackground(new Color(0, 0, 0, 120));
        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/titleLogo.png"))); // NOI18N
        logo.setOpaque(true);
        MainMenu.add(logo);
        logo.setBounds(0, 330, 1920, 380);

        background.setBackground(new java.awt.Color(0, 0, 0));
        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/betaMenuBackground.png"))); // NOI18N
        background.setToolTipText("");
        MainMenu.add(background);
        background.setBounds(0, 0, 1920, 1087);

        getContentPane().add(MainMenu);
        MainMenu.setBounds(0, 0, 1920, 1080);

        Main.setBackground(new java.awt.Color(0, 0, 0));
        Main.setMinimumSize(new java.awt.Dimension(1920, 1080));
        Main.setName("Main"); // NOI18N
        Main.setPreferredSize(new java.awt.Dimension(1920, 1080));
        Main.setLayout(null);
        getContentPane().add(Main);
        Main.setBounds(0, 0, 1920, 1080);

        Prototype1.setBackground(new java.awt.Color(0, 0, 0));
        Prototype1.setMinimumSize(new java.awt.Dimension(1920, 1080));
        Prototype1.setName("Prototype"); // NOI18N
        Prototype1.setLayout(null);

        jLabel9.setBackground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("weapon");
        jLabel9.setOpaque(true);
        Prototype1.add(jLabel9);
        jLabel9.setBounds(460, 460, 140, 140);

        jLabel11.setBackground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("xp bar");
        jLabel11.setOpaque(true);
        Prototype1.add(jLabel11);
        jLabel11.setBounds(140, 620, 460, 60);

        jLabel12.setBackground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("stats");
        jLabel12.setOpaque(true);
        Prototype1.add(jLabel12);
        jLabel12.setBounds(140, 700, 460, 240);

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("helmet");
        jLabel13.setOpaque(true);
        Prototype1.add(jLabel13);
        jLabel13.setBounds(460, 140, 140, 140);

        jLabel10.setBackground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("img");
        jLabel10.setOpaque(true);
        Prototype1.add(jLabel10);
        jLabel10.setBounds(140, 140, 300, 300);

        jLabel14.setBackground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("magic item 2");
        jLabel14.setOpaque(true);
        Prototype1.add(jLabel14);
        jLabel14.setBounds(300, 460, 140, 140);

        jLabel15.setBackground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("magic item 1");
        jLabel15.setOpaque(true);
        Prototype1.add(jLabel15);
        jLabel15.setBounds(140, 460, 140, 140);

        jLabel16.setBackground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("chestplate");
        jLabel16.setOpaque(true);
        Prototype1.add(jLabel16);
        jLabel16.setBounds(460, 300, 140, 140);

        jLabel8.setBackground(new java.awt.Color(102, 102, 102));
        jLabel8.setOpaque(true);
        Prototype1.add(jLabel8);
        jLabel8.setBounds(100, 100, 1720, 880);

        getContentPane().add(Prototype1);
        Prototype1.setBounds(0, 0, 1920, 1080);

        Prototype.setBackground(new java.awt.Color(0, 0, 0));
        Prototype.setMinimumSize(new java.awt.Dimension(1920, 1080));
        Prototype.setName("Prototype"); // NOI18N
        Prototype.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Agency FB", 0, 60)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(">");
        Prototype.add(jLabel6);
        jLabel6.setBounds(1830, 990, 70, 70);

        jLabel2.setOpaque(true);
        Prototype.add(jLabel2);
        jLabel2.setBounds(20, 720, 340, 340);

        jLabel3.setFont(new java.awt.Font("Agency FB", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("asd");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel3.setOpaque(true);
        jLabel3.setVerifyInputWhenFocusTarget(false);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        Prototype.add(jLabel3);
        jLabel3.setBounds(390, 800, 1510, 260);

        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 36)); // NOI18N
        jLabel4.setText("asd");
        jLabel4.setOpaque(true);
        Prototype.add(jLabel4);
        jLabel4.setBounds(390, 720, 1510, 60);

        logo.setBackground(new Color(0, 0, 0, 120));
        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setOpaque(true);
        Prototype.add(jLabel1);
        jLabel1.setBounds(0, 700, 1920, 380);

        jLabel7.setBackground(new java.awt.Color(102, 102, 102));
        jLabel7.setFont(new java.awt.Font("Agency FB", 0, 48)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("InfoText");
        jLabel7.setOpaque(true);
        Prototype.add(jLabel7);
        jLabel7.setBounds(610, 460, 700, 150);

        getContentPane().add(Prototype);
        Prototype.setBounds(0, 0, 1920, 1080);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        ae.initiateGame();
    }//GEN-LAST:event_startButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Main;
    private javax.swing.JPanel MainMenu;
    private javax.swing.JPanel Prototype;
    private javax.swing.JPanel Prototype1;
    private javax.swing.JPanel SplashScreen;
    private javax.swing.JLabel background;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel logo;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel transitionOut;
    // End of variables declaration//GEN-END:variables
}
