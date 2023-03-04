/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class InfoText {
    private JLabel infoText = new javax.swing.JLabel();
    private JLabel infoBackground = new javax.swing.JLabel();
    private JPanel destination;
    private int length;
    public InfoText(JPanel destination, String x, int length) throws InterruptedException{
        this.destination = destination;
        this.length = length;
        createText(x, length);
        //createBackground(destination);
    }
    
    private void createText(String x, int length) throws InterruptedException{
        infoText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoText.setForeground(new java.awt.Color(204, 204, 204));
        infoText.setOpaque(true);
        infoText.setVisible(true);
        infoText.setText(x);
        infoText.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        infoText.setFont(new java.awt.Font("Agency FB", 0, 48));
        infoText.setBackground(new Color(30, 30, 30));
        destination.add(infoText);
        infoText.setBounds(610, 465, 700, 150);
        Thread.sleep(length);
        destination.remove(infoText);
        destination.revalidate();
        destination.repaint();
    }
    
    private void createBackground(){
        //fadeIn(dialougeBackground, 5);
        infoBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoBackground.setOpaque(true);
        destination.add(infoBackground);
        infoBackground.setBounds(610, 465, 700, 150);
    }
    
    
}
