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
public class Weapon extends Item{
    private final int damage;
    public Weapon(String id, String name, String rarity, int damage, GameEngine gm, Image image){
        super(id, name, rarity, gm, image);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
    
    @Override
    public void equip(int slot){
        Item[] newGear = gm.getEquipped();
        ArrayList<Item> inv = gm.getInv();
        Item current = null;
        if(newGear[2] != null){
            current = newGear[2];
        }
        newGear[2] = this;
        gm.setEquipped(newGear);
        if(current != null){
            inv.set(slot, current);
        }else{
            inv.remove(slot);
        }
        gm.setInventory(inv);
    }
}
