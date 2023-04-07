/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.CombatEngine.enemy;
import java.awt.Image;

/**
 *
 * @author User
 */
public class Monster extends Sprite{
    private double velx;
    private double vely;
    private int maxHP;
    private int hp;
    private int damage;

    public Monster(int x, int y, int width, int height, Image image, enemy e) {
        super(x, y, width, height, image);
        velx = 1;
        vely = 0;
        this.maxHP = e.getHp();
        this.hp = e.getHp();
        this.damage = e.getDmg();
    }
    
    public int getMaxHp(){
        return maxHP;
    }
    
    public int getHp(){
        return hp;
    }
    
    public int getDamage(){
        return damage;
    }

    public void move() {
            x += velx;
            y += vely;
    }
    
    public void stepBack(){
        x += -velx;
        y += -vely;
    }

    public double getx(){
        return velx;
    }
    
    public double gety(){
        return vely;
    }
    
    public void changeDirection(int d){
        switch(d){
            case 1:
                vely = -1;
                velx = 0;
            break;
            case 2:
                vely = 0;
                velx = 1;
            break;
            case 3:
                vely = 1;
                velx = 0;
            break;
            case 4:
                vely = 0;
                velx = -1;
            break;
        }
    }
}
