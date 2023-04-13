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
public class Charm extends Item{
    private final String stat;
    public Charm(String id, String name, String rarity, String stat, GameEngine gm, Image image){
        super(id, name, rarity, gm, image);
        this.stat = stat;
    }

    public String getStat() {
        return stat;
    }
    
    @Override
    public void equip(int slot){
        Item[] newGear = gm.getEquipped();
        ArrayList<Item> inv = gm.getInv();
        Item current = null;
        if(newGear[3] != null){
            current = newGear[3];
        }
        newGear[3] = this;
        gm.setEquipped(newGear);
        if(current != null){
            inv.set(slot, current);
        }else{
            inv.remove(slot);
        }
        gm.setInventory(inv);
    }
}
