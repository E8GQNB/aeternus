/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;

import java.awt.Component;

/**
 *
 * @author User
 */
public class AeternusGUI {
    public AeternusGUI(){
        titleScreen s = new titleScreen();
        s.setVisible(true);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                s.getRootPane().getContentPane().getComponent(0).setVisible(false);
                //s.getComponent(0).setVisible(false);
            }
        }, 
        10000
);
    }
}
