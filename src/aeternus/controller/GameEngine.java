/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEngine {
    public enum characters {
    PLAYER, SERVANT, WEAPONMERCHANT, MAGICMERCHANT
}
    public enum locations{
        SQUARE("/images/betaMenuBackground.png", readIn("Square")); 
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
        BASE(readIn("Base"));
        
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
    
    public GameEngine(){
       this.flags = readIn("eventflags");
       this.unlocks = readIn("Unlocks");
       this.lookupTable = readIn("connections");
    }
    
    public String getFlag(interactables i){
        for(String[] s : flags){
            if(s[0].equals(i.name())){
                return s[1];
            }
        }
        return null;
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
        File file = new File("src/locations/" + name + ".txt");
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
                readIn(name);
              } catch (IOException e) {
                e.printStackTrace();
              }
            
        }
    
    
}
