/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import java.awt.Color;
import javax.swing.JLabel;
import aeternus.controller.GameEngine;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JPanel;

public class DialougeSystem {
    private JLabel dialougeTextBox = new javax.swing.JLabel();
    private JLabel dialougeBackground = new javax.swing.JLabel();
    private volatile boolean flag = true;
    
    public DialougeSystem(GameEngine.characters c1, GameEngine.characters c2, JPanel destination){
        createTextBox(destination);
        createBackground(destination);
    }
    
    private void createTextBox(JPanel destination){
        dialougeTextBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dialougeTextBox.setForeground(new java.awt.Color(204, 204, 204));
        dialougeTextBox.setOpaque(false);
        dialougeTextBox.setVisible(false);
        dialougeTextBox.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dialougeTextBox.setFont(new java.awt.Font("Agency FB", 0, 36));
        destination.add(dialougeTextBox);
        dialougeTextBox.setBounds(390, 800, 1510, 260);
        dialougeTextBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SkipButtonMouseClicked(evt);
            }
        });
    }
    
    private void SkipButtonMouseClicked(java.awt.event.MouseEvent evt) {                                         
        flag = false;
    }  
    
    private void createBackground(JPanel destination){
        fadeIn(dialougeBackground, 5);
        dialougeBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dialougeBackground.setOpaque(true);
        destination.add(dialougeBackground);
        dialougeBackground.setBounds(0, 700, 1920, 380);
    }
    
    private void fadeIn(JLabel l, int length){
        Thread one = new Thread() {
        public void run() {
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(30, 30, 30, i));
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
        }  
    };

    one.start();
    }
    
    public void play(String id) throws Exception{
        ArrayList<String[]> dialouge = readIn(id);
        dialougeTextBox.setVisible(true);
            Thread one = new Thread() {
                public void run() {
                    try {
                        for(String[] s : dialouge){
                            //Speaker image
                        
                            //speaker name
                        
                            //Dialouge
                            String text = "";
                            for(char ch : s[2].toCharArray()){
                                text += ch;
                                dialougeTextBox.setText(text);
                                Thread.sleep(Integer.parseInt(s[1]));
                            }
                            waitForClick();
                            dialougeTextBox.getParent().validate();
                            dialougeTextBox.getParent().repaint();
                            dialougeBackground.validate();
                            dialougeBackground.repaint();
                            System.out.println(dialougeBackground.getBackground());
                        }
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
