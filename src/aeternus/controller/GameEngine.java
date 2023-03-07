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
    }
    
    public enum interactables{
        MAGICSHOP(readIn("MagicShop"), "SQUARE"),
        /*WEAPONSHOP(readIn("WeaponShop")),
        BLACKSMITH(readIn("Blacksmith")),
        GENERATOR(readIn("Generator"))*/;
        
        private ArrayList<String[]> data = new ArrayList<String[]>();
        private String l;
        
        interactables(ArrayList<String[]> data, String l){
            this.data = data;
            this.l = l;
        }
        
        public ArrayList<String[]> getOptions(){
            return data;
        }
        
        public String getParentLocation(){
            return l;
        }
        
        public String getFlag(){
            return data.get(data.size()-1)[1];
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
    }
    
    public GameEngine(){
        
    }
}
