/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class FullPageInfo extends InfoText{
    private JLabel infoText = new javax.swing.JLabel();
    private JLabel infoBackground = new javax.swing.JLabel();
    public FullPageInfo(JPanel destination, String x, int length) throws InterruptedException{
        super(destination, x, length);
        
    }
    
    
    private void createBackground(JPanel destination){
        //fadeIn(dialougeBackground, 5);
        infoBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        infoBackground.setOpaque(true);
        destination.add(infoBackground);
        infoBackground.setBounds(610, 330, 700, 150);
    }
}
