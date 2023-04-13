/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.GameEngine;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Helmet extends Item{
    private final int defense;
    public Helmet(String id, String name, String rarity, int defense, GameEngine gm, Image image){
        super(id, name, rarity, gm, image);
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }
    
    @Override
    public void equip(int slot){
        Item[] newGear = gm.getEquipped();
        ArrayList<Item> inv = gm.getInv();
        Item current = null;
        if(newGear[0] != null){
            current = newGear[0];
        }
        newGear[0] = this;
        gm.setEquipped(newGear);
        if(current != null){
            inv.set(slot, current);
        }else{
            inv.remove(slot);
        }
        gm.setInventory(inv);
    }
}
