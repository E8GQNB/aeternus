/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import java.awt.Color;
import javax.swing.JLabel;
import aeternus.model.InfoText;
import aeternus.view.AeternusGUI;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JPanel;

public class DialougeSystem {
    private JLabel dialougeTextBox = new javax.swing.JLabel();
    private JLabel speakerName = new javax.swing.JLabel();
    private JLabel speakerImage = new javax.swing.JLabel();
    private JLabel dialougeBackground = new javax.swing.JLabel();
    private JLabel nextArrow = new javax.swing.JLabel();
    private JPanel destination;
    private AeternusGUI ae;
    private volatile boolean flag = true;
    
    public DialougeSystem(GameEngine.characters c1, GameEngine.characters c2, JPanel destination, AeternusGUI ae){
        this.destination = destination;
        this.ae = ae;
        nextArrow();
        createSpeakerName();
        createSpeakerImage();
        createTextBox();
        createBackground();
    }
    
    private void createTextBox(){
        dialougeTextBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dialougeTextBox.setForeground(new java.awt.Color(204, 204, 204));
        dialougeTextBox.setOpaque(false);
        dialougeTextBox.setVisible(false);
        dialougeTextBox.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dialougeTextBox.setFont(new java.awt.Font("Agency FB", 0, 36));
        destination.add(dialougeTextBox);
        dialougeTextBox.setBounds(390, 800, 1510, 260);
        dialougeTextBox.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SkipButtonMouseClicked(evt);
            }
        });
    }
    
    private void createSpeakerName(){
        speakerName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        speakerName.setForeground(new java.awt.Color(204, 204, 204));
        speakerName.setOpaque(false);
        speakerName.setVisible(false);
        speakerName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        speakerName.setFont(new java.awt.Font("Agency FB", 1, 36));
        destination.add(speakerName);
        speakerName.setBounds(390, 720, 1510, 60);
    }
    
    private void createSpeakerImage(){
        speakerImage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        speakerImage.setOpaque(false);
        speakerImage.setVisible(false);
        speakerImage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        destination.add(speakerImage);
        speakerImage.setBounds(20, 720, 340, 340);
    }
    
    private void nextArrow(){
        nextArrow.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nextArrow.setForeground(new java.awt.Color(204, 204, 204));
        nextArrow.setOpaque(false);
        nextArrow.setVisible(false);
        nextArrow.setText(">");
        nextArrow.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        nextArrow.setFont(new java.awt.Font("Agency FB", 0, 60));
        destination.add(nextArrow);
        nextArrow.setBounds(1830, 990, 70, 70);
    }
    
    private void SkipButtonMouseClicked(java.awt.event.MouseEvent evt) {                                         
        flag = false;
    }  
    
    private void createBackground(){
        dialougeBackground.setBackground(new Color(30, 30, 30, 0));
        dialougeBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dialougeBackground.setOpaque(true);
        dialougeBackground.setVisible(false);
        destination.add(dialougeBackground);
        dialougeBackground.setBounds(0, 700, 1920, 380);
    }
    
    public void fadeIn(JLabel l, int length){
            try {
                for(int i = 0; i < 200; i++){
                    l.setBackground(new Color(30, 30, 30, i));
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
                for(int i = 0; i < 200; i++){
                    l.setBackground(new Color(30, 30, 30, 200-i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
                ae.setDialougeState(false);
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    public void play(String id) throws Exception{
        dialougeBackground.setVisible(true);
        fadeIn(dialougeBackground, 5);
        ArrayList<String[]> dialouge = readIn(id);
        dialougeTextBox.setVisible(true);
        speakerName.setVisible(true);
        speakerImage.setVisible(true);
            Thread one = new Thread() {
                public void run() {
                    try {
                        for(String[] s : dialouge){
                            //Speaker image
                            if(s[0].length() > 0){
                                speakerImage.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/beta" + s[0].toUpperCase() + "icon.png")).getImage().getScaledInstance(340, 340, Image.SCALE_DEFAULT)));
                            }
                            
                            //speakerImage.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon("icon.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
                            //speaker name
                            speakerName.setText(s[0]);
                            //Dialouge
                            String text = "";
                            for(char ch : s[2].toCharArray()){
                                if(!flag){
                                    dialougeTextBox.setText(s[2].replaceAll("€", ""));
                                    flag = true;
                                    break;
                                }
                                if(ch != '€'){
                                    text += ch;
                                    dialougeTextBox.setText(text);
                                }else if(flag){
                                    Thread.sleep(Integer.parseInt(s[1])*8);
                                }
                                Thread.sleep(Integer.parseInt(s[1]));
                            }
                            nextArrow.setVisible(true);
                            waitForClick();
                            nextArrow.setVisible(false);
                            dialougeTextBox.getParent().validate();
                            dialougeTextBox.getParent().repaint();
                        }
                        dialougeTextBox.setVisible(false);
                        speakerName.setVisible(false);
                        speakerImage.setVisible(false);
                        
                        dialougeTextBox.setText("");
                        speakerName.setText("");
                        speakerImage.setIcon(null);
                        fadeOut(dialougeBackground, 5);
                    } catch(InterruptedException v) {
                        System.out.println(v);
                    }
                }  
            };
            one.start();
    }
    
    private void waitForClick(){
        while(flag){}
        flag = true;
    }
    
    private ArrayList<String[]> readIn(String name) throws Exception{
        File file = new File("src/dialouge/" + name + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        ArrayList<String[]> dialouge = new ArrayList<String[]>();
        while ((st = br.readLine()) != null){
            String[] line = st.split(";");
            dialouge.add(line);
        }
        return dialouge;
    }
}
