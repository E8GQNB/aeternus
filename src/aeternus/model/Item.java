/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.GameEngine;
import java.awt.Image;

/**
 *
 * @author User
 */
public class Item {
    private String id;
    private String rarity;
    private String name;
    private Image image;
    private String stat;
    GameEngine gm;
    
    public Item(String id, String name, String rarity, GameEngine gm, Image image, String stat){
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.gm = gm;
        this.image = image;
        this.stat = stat;
    }

    public String getId() {
        return id;
    }
    
    public Image getImg(){
        return image;
    }

    public String getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }
    
    public String getStat(){
        return stat;
    }
    
    public void equip(int slot){}
}
