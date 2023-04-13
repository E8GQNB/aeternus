/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.CustomLabel;
import aeternus.model.Item;
import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class InventoryManager {
    private AeternusGUI aeg;
    private ArrayList<JLabel> inventoryMenu = new ArrayList<JLabel>();
    private GameEngine game;
    
    public InventoryManager(AeternusGUI aeg, GameEngine game){
        this.aeg = aeg;
        this.game = game;
    }
    
    public ArrayList<JLabel> getInventoryMenu() {
        return inventoryMenu;
    }
    
    public void showInventory(JPanel dest){
        JLabel invBg = new JLabel();
        JLabel invAvatar = new JLabel();
        CustomLabel invHelmet = new CustomLabel();
        CustomLabel invChestplate = new CustomLabel();
        CustomLabel invWeapon = new CustomLabel();
        CustomLabel invMagicItem1 = new CustomLabel();
        CustomLabel invMagicItem2 = new CustomLabel();
        JLabel invXp = new JLabel();
        JLabel invStatBox = new JLabel();
        CustomLabel strLabel = new CustomLabel();
        CustomLabel conLabel = new CustomLabel();
        CustomLabel dexLabel = new CustomLabel();
        CustomLabel intLabel = new CustomLabel();
        CustomLabel lckLabel = new CustomLabel();
        inventoryMenu.add(invBg);
        inventoryMenu.add(invAvatar);
        inventoryMenu.add(invHelmet);
        inventoryMenu.add(invChestplate);
        inventoryMenu.add(invWeapon);
        inventoryMenu.add(invMagicItem1);
        inventoryMenu.add(invMagicItem2);
        inventoryMenu.add(invXp);
        inventoryMenu.add(invStatBox);
        inventoryMenu.add(strLabel);
        inventoryMenu.add(conLabel);
        inventoryMenu.add(dexLabel);
        inventoryMenu.add(intLabel);
        inventoryMenu.add(lckLabel);
        
        for(JLabel j : inventoryMenu){
            aeg.labelFactory(j, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
            dest.add(j, 0);
        }
        
        //background
        invBg.setBounds(100, 100, 1720, 880);
        dest.add(invBg, 0);
        
        //player img
        invAvatar.setBounds(140, 140, 300, 300);
        invAvatar.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/PLAYER.png")).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT)));
        invAvatar.getParent().setComponentZOrder(invAvatar, 0);
        
        //equipment
        invHelmet.setBounds(460, 140, 140, 140);
        invChestplate.setBounds(460, 300, 140, 140);
        invWeapon.setBounds(460, 460, 140, 140);
        invMagicItem1.setBounds(140, 460, 140, 140);
        invMagicItem2.setBounds(300, 460, 140, 140);
        ArrayList<CustomLabel> gear = new ArrayList<CustomLabel>();
        gear.add(invHelmet);
        gear.add(invChestplate);
        gear.add(invWeapon);
        gear.add(invMagicItem1);
        gear.add(invMagicItem2);
        int cnt = 0;
        for(CustomLabel custom : gear){
            custom.getParent().setComponentZOrder(custom, 0);
            if(game.getEquipped()[cnt] != null){
                Item x = game.getEquipped()[cnt];
                custom.setIcon(new ImageIcon(game.getEquipped()[cnt].getImg().getScaledInstance(140, 140, Image.SCALE_DEFAULT)));
                for(String[] s : game.getIds()){
                        if(s[0].equals(game.getEquipped()[cnt].getId())){
                            custom.setToolTipText("<html>" + s[1] + "<br><b><em style='color: #" 
                                    + getColor(game.getEquipped()[cnt].getRarity()) + "'>" 
                                    +  game.getEquipped()[cnt].getRarity() 
                                    + "</em></b><br>" + "<html>");
                        }
                }
                custom.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        removeItem(x, dest);
                    }
                });
            }else{
                custom.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/items/itemImages/emptyGear.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                String type = "";
                switch(cnt){
                    case 0:
                        type += "Helmet";
                    break;
                    case 1:
                        type += "Chestplate";
                    break;
                    case 2:
                        type += "Weapon";
                    break;
                    case 3:
                        type += "#1 Charm";
                    break;
                    case 4:
                        type += "#2 Charm";
                    break;
                }
                custom.setToolTipText("<html>" + type + " Slot" +"<html>");
            }
            cnt++;
        }
        
        //xp bar
        invXp.setBounds(140, 620, 460, 60);
        invXp.setText("Level: " + game.getStat("lvl") + ((game.getPoints() > 0) ? "(Avalable points: " + game.getPoints() + ")" : ""));
        invXp.getParent().setComponentZOrder(invXp, 0);
        
        //stats
        invStatBox.setBounds(140, 700, 460, 240);
        invStatBox.getParent().setComponentZOrder(invStatBox, 0);
        
        for(int i = 0; i < 5; i++){
            inventoryMenu.get(i+9).setBounds(140, 700+(i*50), 460, 40);
            inventoryMenu.get(i+9).getParent().setComponentZOrder(inventoryMenu.get(i+9), 0);
        }
        
        String points = "";
        if(game.getPoints() > 0){
            points += "+";
            for(int i = 0; i < 5; i++){
                AtomicInteger idx = new AtomicInteger();
                idx.set(i);
                inventoryMenu.get(i+9).addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        game.addStat(idx.get());
                        hideInventory(dest);
                        showInventory(dest);
                    }
                });
            }
        }
        
        strLabel.setText("Strength: " + game.getStat("str") + points);
        conLabel.setText("Constitution: " + game.getStat("con") + points);
        dexLabel.setText("Dexterity: " + game.getStat("dex") + points);
        intLabel.setText("Intelligence: " + game.getStat("int") + points);
        lckLabel.setText("Luck: " + game.getStat("lck") + points);
        
        strLabel.setToolTipText("<html>" + "Your strength determines your damage. The formula is as follows:<br> (Strength score / 10) * Weapon damage" + "<html>");
        conLabel.setToolTipText("<html>" + "Your constitution directly correlates with your hp. It is a 1:1 ratio." +"<html>");
        dexLabel.setToolTipText("<html>" + "Your dexterity determines how well you can dodge attacks. The formula for evasion is as follows:<br> Dex / 5" +"<html>");
        intLabel.setToolTipText("<html>" + "Your intelligence determines how you pick your attack spots. The higher it is the bigger the critical damage you deal. Each point correlates to a %" +"<html>");
        lckLabel.setToolTipText("<html>" + "Your luck determines the chance at a critical strike on your opponent. The formula is as follows:<br> Luck / Monster level." +"<html>");
        
        //inventory
        ArrayList<String[]> idList = game.getIds();
        ArrayList<Item> inv = game.getInv();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                CustomLabel invSlot = new CustomLabel();
                aeg.labelFactory(invSlot, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
                if(inv.size() >= i*6 + j + 1){
                    invSlot.setIcon(new ImageIcon(inv.get(i*6 + j).getImg().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    String id = inv.get(i*6 + j).getId();
                    for(String[] s : idList){
                        if(s[0].equals(id)){
                            invSlot.setToolTipText("<html>" + s[1] + "<br><b><em style='color: #" 
                                    + getColor(inv.get(i*6 + j).getRarity()) 
                                    + "'>" +  inv.get(i*6 + j).getRarity() 
                                    + "</em></b><br>" + "<html>");
                        }
                    }
                }
                invSlot.setName(String.valueOf(i*6 + j));
                invSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if(invSlot.getIcon() != null){
                            equipItem(Integer.parseInt(invSlot.getName()), dest);
                        }
                    }
                });
                
                dest.add(invSlot, 0);
                invSlot.setBounds(620 + j*200, 160 + i*200, 150, 150);
                inventoryMenu.add(invSlot);
            }
        }
        
        if(aeg.getLocationOptions().size() > 0){
            aeg.setOptions(false);
        }else{
            aeg.setPOI(false);
        }
        dest.revalidate();
        dest.repaint();
    }
    
    public void hideInventory(JPanel origin){
        for(JLabel j : getInventoryMenu()){
            j.getParent().remove(j);
        }
        if(aeg.getLocationOptions().size() > 0){
            aeg.setOptions(true);
        }else{
            aeg.setPOI(true);
        }
        getInventoryMenu().clear();
        origin.revalidate();
        origin.repaint();
    }
    
    public void removeItem(Item gear, JPanel destination){
        ArrayList<Item> inv = game.getInv();
        inv.add(gear);
        Item[] gearSetup = game.getEquipped();
        for(int i = 0; i < gearSetup.length; i++){
            if(gearSetup[i] != null && gearSetup[i].equals(gear)){
                gearSetup[i] = null;
            }
        }
        game.setEquipped(gearSetup);
        hideInventory(destination);
        showInventory(destination);
    }
    
    public void equipItem(int slot, JPanel destination){
        Item x = game.getInv().get(slot);
        x.equip(slot);
        hideInventory(destination);
        showInventory(destination);
    }
    
    public String getColor(String input){
        String out = "";
        switch(input){
            case "common":
                out = "ffffff";
            break;
            case "uncommon":
                out = "0000ff";
            break;
            case "rare":
                out = "00ff00";
            break;
            case "epic":
                out = "ff00ff";
            break;
            case "legendary":
                out = "ffff00";
            break;
        }
        return out;
    }
}
