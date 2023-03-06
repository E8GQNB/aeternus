/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;

import java.awt.Component;
import javax.swing.JPanel;
import aeternus.controller.DialougeSystem;
import aeternus.controller.GameEngine;
import aeternus.model.InfoText;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author User
 */
public class AeternusGUI {
    
    private MainFrame s = new MainFrame(this);
    private Component[] clist = s.getRootPane().getContentPane().getComponents();
    private volatile boolean dialougeState = false;
    private JLabel backgroundImage = new JLabel();
    private JLabel transitionCover = new JLabel();
    private ArrayList<JLabel> scenePoints = new ArrayList<JLabel>();
    
    public AeternusGUI(){ 
        s.setVisible(true);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                clist[0].setVisible(false);
            }
        }, 
        100
);
    }

    public void initiateGame(){
        ((JPanel) s.getRootPane().getContentPane().getComponent(1)).getComponent(0).setVisible(true);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                clist[1].setVisible(false);
                clist[2].setVisible(true);
                newGame();
            }
        }, 
        570
);
        
    }
    
    public void CreateInfo(JPanel p, String text, int length) throws InterruptedException{
        fadeIn(transitionCover, 5);
        InfoText inf = new InfoText(p, text, length);
        fadeOut(transitionCover, 5);
        
    }
    
    public void CreateInfo(JPanel p, String text, int length, String path, int speed) throws InterruptedException{
        fadeIn(transitionCover, 5);
        InfoText inf = new InfoText(p, text, length);
        setBackground(path, speed, false);
        fadeOut(transitionCover, 5);
        
    }
    
    public void setDialougeState(boolean b){
        dialougeState = b;
    }
    
    private void setBackground(String path, int speed, boolean transition){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        backgroundImage.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        if(transition){
            fadeOut(transitionCover, speed);
        }
    }
    
    public void fadeIn(JLabel l, int length){
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    public void fadeOut(JLabel l, int length){
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, 255-i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    private void createTransition(JPanel dest){
        transitionCover.setForeground(new java.awt.Color(0, 0, 0, 0));
        transitionCover.setOpaque(true);
        transitionCover.setVisible(true);
        dest.add(transitionCover);
        dest.setComponentZOrder(transitionCover, 0);
        transitionCover.setBounds(0, 0, 1920, 1080);
    }
    
    private void createBackground(JPanel dest){
        backgroundImage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        backgroundImage.setOpaque(false);
        backgroundImage.setVisible(true);
        backgroundImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dest.add(backgroundImage);
        dest.setComponentZOrder(backgroundImage, 6);
        backgroundImage.setBounds(0, 0, 1920, 1080);
    }
    
    private void loadLocale(GameEngine.locations x, JPanel destination, boolean transition){
        setBackground(x.getPath(), 5, transition);
        loadPointsOfInterest(x, destination);
        destination.getParent().validate();
        destination.getParent().repaint();
    }
    
    private void loadPointsOfInterest(GameEngine.locations x, JPanel destination){
        ArrayList<String[]> POI = x.getPOI();
        for(String[] point : POI){
            JLabel newLabel = new javax.swing.JLabel();
            newLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            newLabel.setForeground(new java.awt.Color(255, 255, 255));
            newLabel.setOpaque(false);
            newLabel.setText(point[0]);
            newLabel.setName(point[5]);
            newLabel.setVisible(true);
            newLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            newLabel.setFont(new java.awt.Font("Agency FB", 0, 30));
            destination.add(newLabel);
            destination.setComponentZOrder(newLabel, 1);
            newLabel.setBounds(Integer.parseInt(point[1]), Integer.parseInt(point[2]), Integer.parseInt(point[3]), Integer.parseInt(point[4]));
            newLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    loadPOI(evt);
                }
            });
            scenePoints.add(newLabel);
        }
    }
    
    private void removePointsOfInterest(){
        for(JLabel l : scenePoints){
            l.getParent().remove(l);
        }
        backgroundImage.getParent().revalidate();
        backgroundImage.getParent().repaint();
    }
    
    private void loadPOI(java.awt.event.MouseEvent evt){
        Thread one = new Thread() {
            public void run() {
                removePointsOfInterest();
                setBackground("/images/beta" + evt.getComponent().getName() + ".png", 2, true);
            }
        };
        one.start();
    }
    
    public void newGame(){
        dialougeState = true;
        DialougeSystem d = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.SERVANT, (JPanel)clist[2], this);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    createTransition((JPanel)clist[2]);
                    createBackground((JPanel)clist[2]);
                    setBackground("/images/betaCaveBackground.png", 5, true);
                    /*d.play("Open");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    CreateInfo((JPanel)clist[2], "An hour later...", 3000);
                    dialougeState = true;
                    d.play("Open2");
                    while(dialougeState){}
                    Thread.sleep(1000);*/
                    CreateInfo((JPanel)clist[2], "At the surface", 2500, "/images/betaMenuBackground.png", 5);
                    dialougeState = true;
                    d.play("Open3");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    loadLocale(GameEngine.locations.SQUARE, (JPanel)clist[2], false);
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
}
