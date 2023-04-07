/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Monster;

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
    
    public CombatEngine(Monster m){
        renderMenu();
    }
    
    private void renderMenu(){
        
    }
}
