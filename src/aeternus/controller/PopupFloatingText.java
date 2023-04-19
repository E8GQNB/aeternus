/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class PopupFloatingText {
    private AeternusGUI gui;
    private GameEngine game;
    private JPanel jp;
    private ArrayList<JLabel> effects = new ArrayList<JLabel>();
    private ArrayList<Integer> effectLifespan = new ArrayList<Integer>();
    
    public PopupFloatingText(AeternusGUI gui, GameEngine game, JPanel jp, int speed){
        this.gui = gui;
        this.game = game;
        this.jp = jp;
        effectThread(speed);
    }
    
    
    public void spawnEffect(String s, Boolean positive){
        JLabel j = new JLabel();
        Color c = ((positive) ? new Color(200,200,200) : new Color(200,0,0));
        gui.labelFactory(j, false, true, new int[]{SwingConstants.LEFT, SwingConstants.CENTER}, 
                c, 
                null, 
                s, 
                new Font("Agency FB", 1, 30));
        Point p = MouseInfo.getPointerInfo().getLocation();
        Random rnd = new Random();
        j.setBounds(p.x + rnd.nextInt(50)-30, p.y - rnd.nextInt(50), 300, 50);
        jp.add(j);
        jp.setComponentZOrder(j, 0);
        effects.add(j);
        effectLifespan.add(40);
    }
    
    private void effectThread(int speed){
        Runnable newThread = () -> {
            for(int i = 0; i < effects.size(); i++){
                effects.get(i).setLocation(effects.get(i).getLocation().x, effects.get(i).getLocation().y-1);
                if(effectLifespan.get(i) > 1){
                    effectLifespan.set(i, effectLifespan.get(i)-1);
                }else{
                    effects.get(i).getParent().remove(effects.get(i));
                    effects.remove(i);
                    effectLifespan.remove(i);
                }
                
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newThread, 0, speed, TimeUnit.MILLISECONDS);
    }
}