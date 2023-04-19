/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.CustomLabel;
import aeternus.model.Item;
import aeternus.model.Weapon;
import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.LabelUI;

/**
 *
 * @author User
 */
public class ShopManager {
    private AeternusGUI gui;
    private GameEngine game;
    private ArrayList<JLabel> shopMenu = new ArrayList<>();
    private String shopID = "";
    private PopupFloatingText pop;
    
    public ShopManager(AeternusGUI gui, GameEngine game, String shopID){
        this.gui = gui;
        this.game = game;
        this.shopID = shopID;
    }
    
    public void openShop(){
        pop = new PopupFloatingText(gui, game, gui.findPanel("subMenu"), 35);
        JLabel shopBg = new JLabel();
        gui.labelFactory(shopBg, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
        gui.findPanel("subMenu").add(shopBg, 0);
        shopBg.setBounds(100, 100, 1720, 880);
        gui.setOptions(false);
        gui.setOptionsVisibility(false);
        shopMenu.add(shopBg);
        ArrayList<Item> stock = game.getStock(shopID);
        ArrayList<String[]> idList = game.getIds();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                CustomLabel shopSlot = new CustomLabel();
                JLabel slotGlow = new JLabel();
                gui.labelFactory(shopSlot, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
                if(stock.size() >= i*8 + j + 1){
                    shopSlot.setIcon(new ImageIcon(stock.get(i*8 + j).getImg().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    slotGlow.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/rarity/" + stock.get(i*8 + j).getRarity() + ".png")).getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
                    String id = stock.get(i*8 + j).getId();
                    for(String[] s : idList){
                        if(s[0].equals(id)){
                            String stat = decodeStat(stock.get(i*8 + j).getStat());
                            if(stock.get(i*8 + j) instanceof Weapon && stat.length() > 0){
                                shopSlot.setToolTipText("<html>" + s[1] + "<br>§" + stock.get(i*8 + j).getPrice() + "<br><b><em style='color: #" 
                                    + getColor(stock.get(i*8 + j).getRarity()) 
                                    + "'>" +  stock.get(i*8 + j).getRarity() 
                                    + "</em></b><br>" + "Scales from " + stat + "<html>");
                            }else if(stat.length() > 0){
                                shopSlot.setToolTipText("<html>" + s[1] + "<br>§" + stock.get(i*8 + j).getPrice() + "<br><b><em style='color: #" 
                                    + getColor(stock.get(i*8 + j).getRarity()) 
                                    + "'>" +  stock.get(i*8 + j).getRarity() 
                                    + "</em></b><br>" + "Increases your " + stat + "<html>");
                            }else{
                                shopSlot.setToolTipText("<html>" + s[1] + "<br>§" + stock.get(i*8 + j).getPrice() + "<br><b><em style='color: #" 
                                    + getColor(stock.get(i*8 + j).getRarity()) 
                                    + "'>" +  stock.get(i*8 + j).getRarity() 
                                    + "</em></b><br>" + "<html>");
                            }
                        }
                    }
                    int slotnum = i*8 + j;
                    shopSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            purchaseItem(slotnum, shopID);
                        }
                    });
                }
                
                gui.findPanel("subMenu").add(shopSlot, 0);
                gui.findPanel("subMenu").add(slotGlow, 0);
                shopSlot.setName("slot" + j*8 + i);
                shopSlot.setBounds(190 + j*200, 150 + i*200, 150, 150);
                slotGlow.setBounds(190 + j*200, 150 + i*200, 150, 150);
                gui.setOptionsVisibility(false);
                shopMenu.add(shopSlot);
                shopMenu.add(slotGlow);
            }
        }
        JLabel exit = new JLabel();
        gui.labelFactory(exit, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.white, 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 26));
        exit.setText("Leave");
        gui.findPanel("subMenu").add(exit, 0);
        exit.setBounds(760, 915, 400, 50);
        shopMenu.add(exit);
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                for(JLabel j : shopMenu){
                    j.getParent().remove(j);
                }
                shopMenu.clear();
                gui.setOptions(true);
                gui.setOptionsVisibility(true);
                gui.findPanel("subMenu").revalidate();
                gui.findPanel("subMenu").repaint();
            }
        });
        
        gui.findPanel("subMenu").revalidate();
        gui.findPanel("subMenu").repaint();
    }
    
    private void purchaseItem(int slot, String shop){
        ArrayList<Item> stock = game.getStock(shop);
        ArrayList<Item> inv = game.getInv();
        if(game.getSouls() >= stock.get(slot).getPrice()){
            game.alterSouls(-stock.get(slot).getPrice());
            inv.add(stock.get(slot));
            stock.remove(slot);
            game.setInventory(inv);
            
            for(JLabel j : shopMenu){
                    j.getParent().remove(j);
                }
                shopMenu.clear();
                gui.setOptions(true);
                gui.setOptionsVisibility(true);
                gui.findPanel("subMenu").revalidate();
                gui.findPanel("subMenu").repaint();
            openShop();
        }else{
            pop.spawnEffect("You cannot afford this.", false);
        }
    }
    
    public String getColor(String input){
        String out = "";
        switch(input){
            case "common":
                out = "ffffff";
            break;
            case "uncommon":
                out = "00ff00";
            break;
            case "rare":
                out = "0000ff";
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
    
    private String decodeStat(String st){
        String stat = "";
        switch(st){
            case "str":
                stat += "strength";
            break;
            case "con":
                stat += "constitution";
            break;
            case "dex":
                stat += "dexterity";
            break;
            case "int":
                stat += "intelligence";
            break;
            case "lck":
                stat += "luck";
            break;
        }
        return stat;
    }
    
    public void setAll(Boolean state){
        for(JLabel j : shopMenu){
            j.setVisible(state);
        }
    }
}
