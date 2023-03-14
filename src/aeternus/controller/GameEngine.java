/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.view.AeternusGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEngine {
    public enum characters {
    PLAYER, SERVANT, WEAPONMERCHANT, MAGICMERCHANT
}
    public enum locations{
        SQUARE("/images/betaMenuBackground.png", readIn("locations/Square")); 
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
        SOULSMITH(readIn("Soulsmith")),
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
    private AeternusGUI aeg;
    private double soulPower = 1.0;
    private double souls = 0;
    
    public GameEngine(AeternusGUI aeg){
       this.flags = readIn("locations/eventflags");
       this.unlocks = readIn("locations/Unlocks");
       this.lookupTable = readIn("locations/connections");
       this.engineUpgrades = readIn("upgrades/engine");
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
    
    public void soulCountThread(){
        int x = 0;
        Runnable newThread = new Runnable() {
            @Override
            public void run() {
                addSouls();
                aeg.refreshSouls();
            }
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
    
    public double getSouls(){
        return souls;
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
