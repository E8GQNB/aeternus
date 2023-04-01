/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Image;

/**
 *
 * @author User
 */
public class Player extends Sprite{
    private double velx;
    private double vely;
    
    public Player(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
    
    
    public void moveX() {
        x += velx;
    }
    
    public void moveY(){
        y += vely;
    }
    
    public void stepBack(){
        x += -velx;
        y += -vely;
    }

    public double getVelx() {
        return velx;
    }
    
    public double getVely() {
        return vely;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }
    
    public void setVely(double vely) {
        this.vely = vely;
    }
    
    public boolean isOut(){
        if(y < -50){
            return true;
        }
        return false;
    }
}
