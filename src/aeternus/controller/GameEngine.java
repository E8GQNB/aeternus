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
import aeternus.model.Weapon;
import aeternus.view.AeternusGUI;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class GameEngine {
    public enum characters {
    PLAYER, SERVANT, WEAPONMERCHANT, MAGICMERCHANT
}
    public enum locations{
        SQUARE("/images/menuBackground.png", readIn("locations/Square"));
        
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
        
        public Boolean addOptions(int idx, String[] s){
            if(idx == -1){
                data.add(s);
                return false;
            }else{
                data.add(idx, s);
                return true;
            }
            
        }
        
        static private ArrayList<String[]> readIn(String name){
            File file = new File("src/locations/" + name + ".txt");
            ArrayList<String[]> data = new ArrayList<>();
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
    private ArrayList<String[]> general;
    private static ArrayList<String[]> idList;
    private Map<String, ArrayList<Item>> shopStocks = new HashMap();
    private ArrayList<String[]> stats;
    private Item[] equipped;
    private ArrayList<Item> inventory;
    private AeternusGUI aeg;
    private LabyrinthEngine la;
    private double soulPower = 1.0;
    private double souls = 0;
    private int points = 0;
    private int worldSeed = -1;
    
    public GameEngine(AeternusGUI aeg, int seed, Boolean newGame){
        this.idList = readIn("items/idlist");
        if(newGame){
            this.flags = readIn("changes/eventflags");
            this.unlocks = readIn("changes/Unlocks");
            this.lookupTable = readIn("changes/connections");
            this.engineUpgrades = readIn("upgrades/engine");
            this.shopStocks.put("MAGICSHOP", readInv("items/MAGICSHOPstock"));
            this.shopStocks.put("WEAPONSHOP", readInv("items/WEAPONSHOPstock"));
            this.stats = readIn("playerData/playerStats");
            this.equipped = readGear("playerData/playerGear");
            this.inventory = readInv("playerData/playerInventory");
            this.genericStats = readIn("playerData/genericStats");
        }else{
            this.flags = readIn("saves/eventflags");
            this.unlocks = readIn("saves/Unlocks");
            this.lookupTable = readIn("saves/connections");
            this.engineUpgrades = readIn("saves/engine");
            this.shopStocks.put("MAGICSHOP", readInv("saves/MAGICSHOPstock"));
            this.shopStocks.put("WEAPONSHOP", readInv("saves/WEAPONSHOPstock"));
            this.stats = readIn("saves/playerStats");
            this.equipped = readGear("saves/playerGear");
            this.inventory = readInv("saves/playerInventory");
            this.genericStats = readIn("saves/genericStats");
            this.general = readIn("saves/general");
            loadGeneral();
        }
        this.aeg = aeg;
        this.worldSeed = seed;
    }
    
    public String getFlag(interactables i){
        for(String[] s : flags){
            if(s[0].equals(i.name()) && s.length > 1){
                return s[1];
            }
        }
        return null;
    }
    
    private void loadGeneral(){
        souls = Double.parseDouble(general.get(0)[0]);
        worldSeed = Integer.parseInt(general.get(1)[0]);
    }
    
    public String getFlag(String i){
        for(String[] s : flags){
            if(s[0].equals(i) && s.length > 1){
                return s[1];
            }
        }
        return null;
    }
    
    public int getSeed(){
        return worldSeed;
    }
    
    public Boolean setFlag(String f, String value){
        for(String[] s : flags){
            if(s[0].equals(f)){
                if(s.length > 1){
                   s[1] = value; 
                }else{
                    s = new String[]{s[0], value};
                }
                return true;
            }
        }
        return false;
    }
    
    public int getInvSpaces(){
        return 32-inventory.size();
    }
    
    public double calcBurn(Item x){
        double price = 1000 * ((double)getGenericStat("ItemsBurned")/10+1) * Integer.parseInt(getStat("lvl"));
        alterSouls(price);
        setGenericStat("ItemsBurned", getGenericStat("ItemsBurned")+1);
        return price;
    }
    
    public ArrayList<String[]> itemToList(ArrayList<Item> items){
        ArrayList<String[]> out = new ArrayList<>();
        for(Item i : items){
            out.add(new String[]{String.valueOf(i.getId()), String.valueOf(i.getRarity())});
        }
        return out;
    }
    
    public void WriteSave(){
        try {
            FileWriter fw = new FileWriter("src/saves/general.txt");
            fw.write(String.valueOf(getSouls()) + System.getProperty( "line.separator" ));
            fw.write(String.valueOf(worldSeed));
            fw.close();
            
            ArrayList<Item> gear = new ArrayList<>();
            for(Item x : equipped){
                if(x != null){
                    gear.add(x);
                }
            }
            var files = Arrays.asList("Unlocks", "eventflags", "engine", "connections", "playerStats", "genericStats", "MAGICSHOPstock", "WEAPONSHOPstock", "playerGear", "playerInventory");
            var data = Arrays.asList(unlocks, flags, engineUpgrades, lookupTable, stats, genericStats, itemToList(shopStocks.get("MAGICSHOP")), itemToList(shopStocks.get("WEAPONSHOP")), itemToList(gear), itemToList(inventory));
            for(int i = 0; i < files.size(); i++){
                try {
                    FileWriter f = new FileWriter("src/saves/" + files.get(i) + ".txt");
                    for(int j = 0; j < data.get(i).size(); j++){
                        String out = "";
                        for(String s : data.get(i).get(j)){
                            out += ";" + s;
                        }
                        out = out.substring(1);
                        f.write(out);
                        if(j + 1 < data.get(i).size()){
                            f.write(System.getProperty( "line.separator" ));
                        }
                    }
                    f.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
    }
    
    public int getArmor(){
        int out = 0;
        if(equipped[0] != null){
            out += ((Helmet)equipped[0]).getDefense();
        }
        if(equipped[1] != null){
            out += ((Chestpiece)equipped[1]).getDefense();
        }
        return out;
    }
    
    public void setGear(Item[] items){
        equipped = items;
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
            newsp *= 5;
        }
        soulPower = newsp;
    }
    
    public double getSouls(){
        return souls;
    }
    
    public Boolean endReq(){
        for(Item i : equipped){
            if(i == null || !i.getRarity().equals("legendary")){
                return false;
            }
        }
        return true;
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
            stats.get(idx)[1] = String.valueOf(x);
            refreshShops();
        }else{
            points--;
        }
    }
    
    public void refreshShops(){
        Random rnd = new Random();
        shopStocks.entrySet().forEach(shop -> {
            ArrayList<Item> newStock = new ArrayList<>();
            int amount = (int)(31 * Math.pow(rnd.nextDouble(), 2))+1;
            for(int i = 0; i < amount; i++){
                newStock.add(genRandomItem(shop.getKey(), rnd));
            }
            setStock(shop.getKey(), newStock);
        });
    }
    
    public Item genRandomItem(String shop, Random rnd){
        Item x = null;
        while(x == null){
            int id = rnd.nextInt(idList.size());
            int quality = (int)(5 * Math.pow(rnd.nextDouble(), 3));
            
            if(!idList.get(id)[2].equals("charm") && shop.equals("MAGICSHOP")){
                continue;
            }
            
            if(idList.get(id)[2].equals("charm") && !shop.equals("MAGICSHOP")){
                continue;
            }
            
            if(Integer.parseInt(getStat("lvl")) <= 5 && quality > 0){
                quality--;
            }
            
            String itemQuality = "";
            switch(quality){
                case 0:
                    itemQuality += "common";
                break;
                case 1:
                    itemQuality += "uncommon";
                break;
                case 2:
                    itemQuality += "rare";
                break;
                case 3:
                    itemQuality += "epic";
                break;
                case 4:
                    itemQuality += "legendary";
                break;
            }
            x = createItem(new String[]{idList.get(id)[0], itemQuality});
        }
        return x;
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
            if(s.length > 1 && s[1].equals(name)){
                s[1] = "";
            }
        }
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
                    case "setFlag":
                        setFlag(line[2], line[3]);
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
        la = new LabyrinthEngine(this, aeg);
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
    
    public ArrayList<Item> readInv(String name){
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
                while ((st = br.readLine()) != null){
                    String[] line = st.split(";");
                    for(String[] s : idList){
                        int idx = -1;
                        Boolean chrm = false;
                        if(line[0].equals(s[0])){
                            switch(s[2]){
                                case "helmet":
                                    idx = 0;
                                break;
                                case "chestpiece":
                                    idx = 1;
                                break;
                                case "weapon":
                                    idx = 2;
                                break;
                                case "charm":
                                    if(!chrm){
                                        chrm = true;
                                        idx = 3;
                                    }else{
                                        idx = 4;
                                    }
                                break;
                            }
                            inv[idx] = createItem(new String[]{line[0], line[1]});
                        }
                        
                    }
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
}
