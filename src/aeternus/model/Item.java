/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.GameEngine;

/**
 *
 * @author User
 */
public class Item {
    private String id;
    private String rarity;
    private String name;
    GameEngine gm;
    
    public Item(String id, String name, String rarity, GameEngine gm){
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.gm = gm;
    }

    public String getId() {
        return id;
    }

    public String getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }
    
    public void equip(int slot){}
}