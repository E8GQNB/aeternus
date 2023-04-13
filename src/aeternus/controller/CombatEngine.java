/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Monster;
import aeternus.model.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class CombatEngine {
    public enum enemy{
        KNIGHT(10, 2), MAGE(5, 5), BRUTE(20, 2), KING(15, 5);
        private int hp;
        private int dmg;
        
        enemy(int hp, int dmg){
            this.hp = hp;
            this.dmg = dmg;
        }
        
        public int getHp(){
            return hp;
        }
        
        public int getDmg(){
            return dmg;
        }
    }
    
    private ArrayList<JLabel> combatMenu = new ArrayList<JLabel>();
    private Player p;
    private Monster m;
    private LabyrinthEngine lab;
    
    public CombatEngine(Monster mon, JPanel dest, Player pl, LabyrinthEngine lab){
        this.lab = lab;
        this.p = pl;
        this.m = mon;
        renderMenu(dest, m);
    }
    
    private void renderMenu(JPanel dest, Monster m){
        JLabel bg, img1, img2, hp1, hpBox1, hp2, hpBox2, stats, str, dex, con, lck, inT;
        bg = new JLabel();
        img1 = new JLabel();
        img2 = new JLabel();
        hp1 = new JLabel();
        hp2 = new JLabel();
        hpBox1 = new JLabel();
        hpBox2 = new JLabel();
        stats = new JLabel();
        str = new JLabel();
        con = new JLabel();
        dex = new JLabel();
        lck = new JLabel();
        inT = new JLabel();
        combatMenu = new ArrayList<>(List.of(bg, img1, img2, hpBox1, hpBox2, hp1, hp2, stats, str, dex, inT, con, lck));
        for(JLabel l : combatMenu){
            labelFactory(l, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 50, 150), 
                null, 
                new Font("Agency FB", 1, 36));
            dest.add(l, 0);
        }
        bg.setBounds(100, 100, 1720, 880);
        img1.setBounds(180, 140, 460, 460);
        img1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                                   "/images/betaPLAYERIcon.png")).getImage().getScaledInstance(460, 460, Image.SCALE_DEFAULT)));
        img2.setBounds(1280, 140, 460, 460);
        img2.setIcon(new ImageIcon(m.getImg().getScaledInstance(460, 460, Image.SCALE_DEFAULT)));
        
        hp1.setBounds(180, 620, (int)(460*(p.getHp()/p.getMaxHp())), 60);
        hp1.setBackground(new Color(0, 90, 0));
        hp1.setName("hp1");
        hpBox1.setBounds(180, 620, 460, 60);
        
        hp2.setBounds(1280, 620, 460, 60);
        hp2.setBackground(new Color(0, 90, 0));
        hp2.setName("hp2");
        hpBox2.setBounds(1280, 620, 460, 60);
        
        stats.setBounds(180, 700, 460, 240);
        str.setBounds(180, 700, 460, 40);
        dex.setBounds(180, 750, 460, 40);
        con.setBounds(180, 800, 460, 40);
        lck.setBounds(180, 850, 460, 40);
        inT.setBounds(180, 900, 460, 40);
        
        dest.revalidate();
        dest.repaint();
        simulateCombat();
    }
    
    private void simulateCombat(){
        Thread one = new Thread() {
            public void run() {
                while(p.getHp() > 0 && m.getHp() > 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CombatEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    attack(true);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CombatEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(m.getHp() > 0){
                        attack(false);
                    }
                }
                if(p.getHp() > 0){
                    lab.getPlayer().setHp(p.getHp());
                    lab.deleteMonster();
                    lab.rollLvl();
                    lab.resume();
                }else{
                    lab.exit();
                }
                for(JLabel j : combatMenu){
                    j.getParent().remove(j);
                }
                lab.deleteCombat();
            }
        };
        one.start();
        
    }
    
    private void attack(Boolean dir){
        Random rnd = new Random();
        if(dir){
            if(rnd.nextInt(100)+1 < (p.getStat(4)/p.getStat(5))){
                m.setHp(m.getHp()-p.getDamage()*(p.getStat(3)/100 + 2));
            }
            m.setHp(m.getHp()-p.getDamage());
        }else{
            
            if(rnd.nextInt(100)+1 > (p.getStat(2) / 5)){
                p.setHp(p.getHp()-m.getDamage());
            }else{
                //miss
            }
        }
        for(JLabel j : combatMenu){
            if(j.getName() != null && j.getName().equals("hp1")){
                j.setBounds(180, 620, (int)(460*(p.getHp()/p.getMaxHp())), 60);
                j.getParent().revalidate();
                j.getParent().repaint();
            }else if(j.getName() != null && j.getName().equals("hp2")){
                j.setBounds(1280, 620, (int)(460*(m.getHp()/m.getMaxHp())), 60);
                j.getParent().revalidate();
                j.getParent().repaint();
            }
        }
    }
        
    public ArrayList<JLabel> getMenu(){
        return combatMenu;
    }
    
    private void labelFactory(JLabel j, boolean opacity, boolean vis, int[] alignment, Color fG, Color bG, String text, Font f){
        j.setOpaque(opacity);
        j.setVisible(vis);
        j.setHorizontalAlignment(alignment[0]);
        j.setVerticalAlignment(alignment[1]);
        j.setForeground(fG);
        j.setBackground(bG);
        j.setText(text);
        j.setFont(f);
    }
}
