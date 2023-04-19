/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Charm;
import aeternus.model.Chestpiece;
import aeternus.model.Helmet;
import aeternus.model.Item;
import aeternus.model.Labyrinth;
import aeternus.model.Weapon;
import aeternus.view.AeternusGUI;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameEngine {
    public enum characters {
    PLAYER, SERVANT, WEAPONMERCHANT, MAGICMERCHANT
}
    public enum locations{
        SQUARE("/images/menuBackground.png", readIn("locations/Square")); 
        //BASE("/images/betaLab.png", readIn("Lab"));
        
        private final String path;
        private ArrayList<String[]> POI = new ArrayList<String[]>();
        
        locations(String path, ArrayList<String[]> POI){
            this.path = path;
            this.POI = POI;
        }
        
        public String getPath(){
            return path;
        }
        
        public ArrayList<String[]> getPOI(){
            return POI;
        }
    }
    
    public enum interactables{
        MAGICSHOP(readIn("MagicShop")),
        WEAPONSHOP(readIn("WeaponShop")),
        BASE(readIn("Base")),
        PORTAL(readIn("Portal"));
        
        private ArrayList<String[]> data = new ArrayList<String[]>();
        
        interactables(ArrayList<String[]> data){
            this.data = data;
        }
        
        public ArrayList<String[]> getOptions(){
            return data;
        }
        
        static private ArrayList<String[]> readIn(String name){
            File file = new File("src/locations/" + name + ".txt");
            ArrayList<String[]> data = new ArrayList<String[]>();
            data.clear();
            BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null){
                        String[] line = st.split(";");
                        data.add(line);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            return data;
        }
    }
    
    private ArrayList<String[]> flags;
    private ArrayList<String[]> unlocks;
    private ArrayList<String[]> lookupTable;
    private ArrayList<String[]> engineUpgrades;
    private ArrayList<String[]> genericStats;
    private static ArrayList<String[]> idList;
    private Map<String, ArrayList<Item>> shopStocks = new HashMap();
    private ArrayList<String[]> stats;
    private Item[] equipped;
    private ArrayList<Item> inventory;
    private AeternusGUI aeg;
    private LabyrinthEngine la;
    private double soulPower = 1.0;
    private double souls = 1;
    private int points = 0;
    
    public GameEngine(AeternusGUI aeg){
       this.flags = readIn("locations/eventflags");
       this.unlocks = readIn("locations/Unlocks");
       this.lookupTable = readIn("locations/connections");
       this.engineUpgrades = readIn("upgrades/engine");
       this.idList = readIn("items/idlist");
       this.shopStocks.put("MAGICSHOP", readInv("items/MAGICSHOPstock"));
       this.shopStocks.put("WEAPONSHOP", readInv("items/WEAPONSHOPstock"));
       this.stats = readIn("saves/playerStats");
       this.equipped = readGear("saves/playerGear");
       this.inventory = readInv("saves/playerInventory");
       this.genericStats = readIn("saves/genericStats");
       this.aeg = aeg;
    }
    
    public String getFlag(interactables i){
        for(String[] s : flags){
            if(s[0].equals(i.name())){
                return s[1];
            }
        }
        return null;
    }
    
    public String getFlag(String i){
        for(String[] s : flags){
            if(s[0].equals(i)){
                return s[1];
            }
        }
        return null;
    }
    
    public void setFlag(String f, String value){
        for(String[] s : flags){
            if(s[0].equals(f)){
                s[1] = value;
            }
        }
    }
    
    public void calcBurn(Item x){
        alterSouls(1000 * ((double)getGenericStat("ItemsBurned")/10+1) * Integer.parseInt(getStat("lvl")));
        setGenericStat("ItemsBurned", getGenericStat("ItemsBurned")+1);
    }
    
    public void calculateStats(){
        stats.forEach(s ->{
            s[1] = s[2];
        });
        stats.forEach(s -> {
            for(Item i : equipped){
                if(i != null && i.getStat().equals(s[0]) && !(i instanceof Weapon)){
                    double x = Integer.parseInt(s[1]);
                    switch(i.getRarity()){
                        case "common":
                            x *= 1.1;
                        break;
                        case "uncommon":
                            x *= 1.2;
                        break;
                        case "rare":
                            x *= 1.3;
                        break;
                        case "epic":
                            x *= 1.4;
                        break;
                        case "legendary":
                            x *= 1.5;
                        break;
                    }
                    s[1] = String.valueOf((int)x);
                }
            }
        });
    }
    
    public void soulCountThread(){
        Runnable newThread = () -> {
            addSouls();
            aeg.refreshSouls();
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newThread, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    public void alterSouls(double x){
        souls += x;
    }
    
    public double getSoulPower(){
        return soulPower;
    }
    
    public void refreshSoulPower(){
        double newsp = 1;
        for(int i = 0; i < Integer.parseInt(getStat("lvl"))-1;i++){
            newsp *= 1.5;
        }
        soulPower = newsp;
    }
    
    public double getSouls(){
        return souls;
    }
    
    public String getStat(String name){
        for(String[] s : stats){
            if(s[0].equals(name)){
                return s[1];
            }
        }
        return "10";
    }
    
    public void addStat(int idx){
        int x = Integer.parseInt(stats.get(idx)[2]);
        x += 1;
        stats.get(idx)[2] = String.valueOf(x);
        if(idx == 5){
            points += 2;
        }else{
            points--;
        }
    }
    
    public int getGenericStat(String stat){
        int out = 0;
        for(String[] s : genericStats){
            if(s[0].equals(stat)){
                out = Integer.parseInt(s[1]);
            }
        }
        return out;
    }
    
    public void setGenericStat(String stat, int value){
        for(String[] s : genericStats){
            if(s[0].equals(stat)){
                s[1] = String.valueOf(value);
            }
        }
    }
    
    public int getPoints(){
        return points;
    }
    
    public double getDamage(){
        double div = 1;
        if(equipped[2] != null){
            switch(equipped[2].getRarity()){
                case "common":
                    div *= 2;
                break;
                case "uncommon":
                    div *= 1.8;
                break;
                case "rare":
                    div *= 1.5;
                break;
                case "epic":
                    div *= 1.2;
                break;
                case "legendary":
                   //
                break;
            }
            return Double.parseDouble(getStat(((Weapon)equipped[2]).getStat()))/div;
        }
        return (Double.parseDouble(getStat("str"))/10);
    }
    
    public ArrayList<Item> getInv(){
        return inventory;
    }

    public Item[] getEquipped() {
        return equipped;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void setEquipped(Item[] equipped) {
        this.equipped = equipped;
    }
    
    public void addSouls(){
        double total = 0;
        for(String[] row : engineUpgrades){
            if(Double.parseDouble(row[1]) > 0){
                total += (Double.parseDouble(row[3])/10) * Double.parseDouble(row[1]);
            }
        }
        alterSouls(total);
    }
    
    public ArrayList<String[]> getEngineUpgrades(){
        return engineUpgrades;
    }
    
    public ArrayList<Item> getStock(String shop){
        return shopStocks.get(shop);
    }
    
    public void setStock(String shop, ArrayList<Item> stock){
        shopStocks.replace(shop, stock);
    }
    
    public ArrayList<String[]> getIds(){
        return idList;
    }
    
    public void removeFlag(String name){
        for(String[] s : flags){
            if(s[1].equals(name)){
                s[1] = "";
            }
        }
        //writeFlags("eventflags");
    }
    
    public boolean getLockState(String tst){
        for(String[] s : unlocks){
            if(s[0].equals(tst)){
                return Boolean.parseBoolean(s[1]);
            }
        }
        return false;
    }
    
    public void checkConnections(String event){
        for(String[] line : lookupTable){
            if(line[0].equals(event)){
                switch(line[1]){
                    case "unlock":
                        setLockState(interactables.valueOf(line[2]), "true");
                    break;
                }
            }
        }
    }
    
    public void setLockState(interactables i, String lockstate){
        for(String[] s : unlocks){
            if(s[0].equals(i.name())){
                s[1] = lockstate;
            }
        }
        //writeUnlocks("Unlocks");
    }
    
    public Item createItem(String[] in){
        Item x = null;
        for(String[] s : idList){
            if(s[0].equals(in[0])){
                Image img;
                switch(s[2]){
                    case "weapon":
                        img = new ImageIcon("src/images/Equipment/Weapon/" + s[1] + ".png").getImage();
                        x = new Weapon(s[0], s[1], in[1], this, img, s[3]);
                    break;
                    case "helmet":
                        img = new ImageIcon("src/images/Equipment/Helmet/" + s[1] + ".png").getImage();
                        x = new Helmet(s[0], s[1], in[1], Integer.parseInt(s[3]), this, img, s[4]);
                    break;
                    case "chestpiece":
                        img = new ImageIcon("src/images/Equipment/Chestplate/" + s[1] + ".png").getImage();
                        x = new Chestpiece(s[0], s[1], in[1], Integer.parseInt(s[3]), this, img, s[4]);
                    break;
                    case "charm":
                        img = new ImageIcon("src/images/Equipment/Charm/" + s[1] + ".png").getImage();
                        x = new Charm(s[0], s[1], in[1], s[3], this, img);
                    break;
                }
            }
        }
        return x;
    }
    
    public void enterPortal() throws IOException{
        la = new LabyrinthEngine(this);
        la.setBounds(0, 0, 1920, 1080);
        aeg.getFrame().getContentPane().add(la, 0);
        la.requestFocus();
        //aeg.findPanel("subMenu").getParent().add(la, 0);
    }
    
    public void exitPortal(Boolean win){
        la.setVisible(false);
        aeg.getFrame().getContentPane().remove(la);
        la = null;
        aeg.setOptions(true);
        aeg.setOptionsVisibility(true);
        aeg.setActiveLabyrinth(false);
        aeg.afterPortalExit(win);
        refreshSoulPower();
    }
    
    private ArrayList<Item> readInv(String name){
        File file = new File("src/" + name + ".txt");
        ArrayList<Item> inv = new ArrayList<>();
        BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null){
                    String[] line = st.split(";");
                    inv.add(createItem(line));
                }
            } catch (Exception ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        return inv;
    }
    
    private Item[] readGear(String name){
        File file = new File("src/" + name + ".txt");
        Item[] inv = new Item[5];
        BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(file));
                String st;
                int cnt = 0;
                while ((st = br.readLine()) != null){
                    String[] line = st.split(";");
                    if(line.length > 1){
                        inv[cnt] = createItem(new String[]{line[1], line[2]});
                    }
                    cnt++;
                }
            } catch (Exception ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        return inv;
    }
    
    static private ArrayList<String[]> readIn(String name){
        File file = new File("src/" + name + ".txt");
        ArrayList<String[]> data = new ArrayList<String[]>();
        BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null){
                    String[] line = st.split(";");
                    data.add(line);
                }
            } catch (Exception ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        return data;
    }
    
    private void writeFlags(String name){
            try {
                FileWriter fw = new FileWriter("src/locations/" + name + ".txt");
                for(int i = 0; i < flags.size(); i++){
                    fw.write(flags.get(i)[0] + ";" + flags.get(i)[1]);
                    if(i + 1 < flags.size()){
                        fw.write(System.getProperty( "line.separator" ));
                    }
                }
                fw.close();
                readIn("locations/" + name);
              } catch (IOException e) {
                e.printStackTrace();
              }
            
        }
    
    
}
