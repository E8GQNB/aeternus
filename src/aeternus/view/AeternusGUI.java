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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class AeternusGUI {
    
    private MainFrame s = new MainFrame(this);
    private Component[] clist = s.getRootPane().getContentPane().getComponents();
    private volatile boolean dialougeState = false;
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
        InfoText inf = new InfoText(p, text, length);
    }
    
    public void setDialougeState(boolean b){
        dialougeState = b;
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
                    d.play("Open");
                    while(dialougeState){}
                    Thread.sleep(2000);
                    CreateInfo((JPanel)clist[2], "An hour later...", 3000);
                    
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
}
