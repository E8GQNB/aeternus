/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.controller.CombatEngine.enemy;
import aeternus.model.CustomLabel;
import aeternus.model.Item;
import aeternus.model.Labyrinth;
import aeternus.model.Monster;
import aeternus.model.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author User
 */
public class LabyrinthEngine extends JPanel{
    private final int FPS = 240;
    private final int PLAYER_WIDTH = 25;
    private final int PLAYER_HEIGHT = 55;
    private final int PLAYER_MOVEMENT = 1;
    private final int MONSTER_SIZE = 120;

    private boolean paused = false;
    private static int levelNum = 0;
    private Labyrinth level;
    private Monster monster;
    private Player vignette;
    private Player player;
    private Boolean activeCombat = false;
    private ArrayList<Player> playerHitbox = new ArrayList<Player>();
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private ArrayList<Monster> monsterHitboxes = new ArrayList<Monster>();
    private Timer newFrameTimer;
    private CombatEngine c;
    private GameEngine game;

    public LabyrinthEngine(GameEngine game) {
        super();
        this.game = game;
        this.setLayout(null);
        
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setX(-PLAYER_MOVEMENT);

            }
        });
        
        this.getInputMap().put(KeyStroke.getKeyStroke('A', 0, true), "stopped left");
        this.getActionMap().put("stopped left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setX(0);
            }
        });
        
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setX(PLAYER_MOVEMENT);

            }
        });
        
        this.getInputMap().put(KeyStroke.getKeyStroke('D', 0, true), "stopped right");
        this.getActionMap().put("stopped right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setX(0);
            }
        });
        
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setY(-PLAYER_MOVEMENT);

            }
        });
        
        this.getInputMap().put(KeyStroke.getKeyStroke('W', 0, true), "stopped up");
        this.getActionMap().put("stopped up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setY(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setY(PLAYER_MOVEMENT);

            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke('S', 0, true), "stopped down");
        this.getActionMap().put("stopped down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                setY(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                paused = !paused;
            }
        });
        levelNum = 0;
        restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
    }
    
    public void setX(int x){
        player.setVelx(x);
        for(Player p : playerHitbox){
            p.setVelx(x);
        }
        vignette.setVelx(x);
    }
    
    public void setY(int y){
        player.setVely(y);
        for(Player p : playerHitbox){
            p.setVely(y);
        }
        vignette.setVely(y);
    }
    
    public void restart() {
        try {
            level = new Labyrinth("data/levels/lvl0" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Image playerImage = new ImageIcon("src/images/betaSERVANTIcon.png").getImage();
        Image vignetteImage = new ImageIcon("src/images/vignette.png").getImage();
        int playerSpawnX = 152*4+10;
        int playerSpawnY = 152*4+5;
        int[] stats = new int[]{Integer.parseInt(game.getStat("str")), Integer.parseInt(game.getStat("con")), Integer.parseInt(game.getStat("dex")), Integer.parseInt(game.getStat("int")), Integer.parseInt(game.getStat("lck")), Integer.parseInt(game.getStat("lvl"))};
        player = new Player(playerSpawnX, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage, Integer.parseInt(game.getStat("con")), game.getDamage(), stats);
        Player Up = new Player(playerSpawnX, playerSpawnY-5, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Player Right = new Player(playerSpawnX+5, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Player Down = new Player(playerSpawnX, playerSpawnY+5, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Player Left = new Player(playerSpawnX-5, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        List<Player> newHB = Arrays.asList(Up, Right, Down, Left);
        playerHitbox.addAll(newHB);
        vignette = new Player(-1600, -440, 4537, 2190, vignetteImage);
        spawnMonster(1);
    }
    
    public void spawnMonster(int amount){
        for(int i = 0; i < amount; i++){
            int random = (int) (Math.random() * 3);
            int monsterX = (int) ((Math.random() * 28) + 1);
            int monsterY = (int) ((Math.random() * 28) + 1);
            Image monsterImage = new ImageIcon("src/images/Foes/Castle/" + enemy.values()[random].name() + ".png").getImage();
            Image monsterToken = new ImageIcon("src/images/Foes/Castle/Tokens/" + enemy.values()[random].name() + ".png").getImage();
            int hitbox = 10;
            int ms = 120;
            monsterX *= ms;
            monsterY *= ms;
            
            Monster m = new Monster(monsterX, monsterY, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random], monsterToken);
            while(level.collides(m) || player.collides(m)){
                monsterX = (int) ((Math.random() * 10) + 1);
                monsterY = (int) ((Math.random() * 5) + 1);
                monsterX *= ms;
                monsterY *= ms;
                m = new Monster(monsterX, monsterY, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random], monsterToken);
            }
            Monster up = new Monster(monsterX, monsterY-hitbox, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random]);
            Monster right = new Monster(monsterX+hitbox, monsterY, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random]);
            Monster down = new Monster(monsterX, monsterY+hitbox, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random]);
            Monster left = new Monster(monsterX-hitbox, monsterY, MONSTER_SIZE, MONSTER_SIZE, monsterImage, enemy.values()[random]);
            List<Monster> newMHB = Arrays.asList(up, right, down, left);
            monsterHitboxes.addAll(newMHB);
            monsters.add(m);
        }
    }

    public static int getPoints(){
        return levelNum;
    }
    
    private boolean moveViewport(){
        int xmin = 480;
        int xmax = 1440;
        int ymin = 370;
        int ymax = 710;
        int newx = 0;
        int newy = 0;
        if(player.getX() < xmin){
            newx = +1;
        }
        if(player.getX() > xmax){
            newx = -1;
        }
        if(player.getY() < ymin){
            newy = +1;
        }
        if(player.getY() > ymax){
            newy = -1;
        }
        level.shiftField(newx, newy);
        vignette.setX(vignette.getX() + newx);
        vignette.setY(vignette.getY() + newy);
        player.setX(player.getX() + newx);
        player.setY(player.getY() + newy);
        
        for(Player p : playerHitbox){
            p.setX(p.getX() + newx);
            p.setY(p.getY() + newy);
        }
        
        for(Monster m : monsters){
            m.setX(m.getX() + newx);
            m.setY(m.getY() + newy);
        }
        for(Monster m : monsterHitboxes){
            m.setX(m.getX() + newx);
            m.setY(m.getY() + newy);
        }
        if(newx != 0 || newy != 0){
            return true;
        }
        return false;
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        level.draw(grphcs);
        player.draw(grphcs);
        
        for(Player p : playerHitbox){
            p.draw(grphcs);
        }
        
        for(Monster m : monsters){
            m.draw(grphcs);
        }
        
        /*for(Monster m : monsterHitboxes){
            m.draw(grphcs);
        }*/
        
        //vignette.draw(grphcs);
    }
    
    private void createCombatEngine(Monster m){
        c = new CombatEngine(m, this, player, this);
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public void deleteMonster(){
        monsters.remove(monsterIndx);
        for(int i = 0; i < 4; i++){
            monsterHitboxes.remove(monsterIndx*4);
        }
    }
    
    public void resume(){
        if(monsters.size() > 0){
            paused = false;
        }else{
            paused = false;
            endScreen();
        }
    }
    
    public void deleteCombat(){
        c = null;
    }
    
    public void rollLvl(){
        Random rnd = new Random();
        double lvlupchance = Math.pow(0.5, Double.parseDouble(game.getStat("lvl")));
        if(rnd.nextInt(100)+1 < lvlupchance*100){
            game.addStat(5);
        }
    }
    
    public void exit(Boolean win){
        game.exitPortal(win);
    }
    
    public void labelFactory(JLabel j, boolean opacity, boolean vis, int[] alignment, Color fG, Color bG, Font f){
        j.setOpaque(opacity);
        j.setVisible(vis);
        j.setHorizontalAlignment(alignment[0]);
        j.setVerticalAlignment(alignment[1]);
        j.setForeground(fG);
        j.setBackground(bG);
        j.setFont(f);
    }
    
    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    
    private int getChance(int boost){
        Random rnd = new Random();
        int chance = rnd.nextInt(100);
        chance += boost;
        if(isBetween(chance, 0, 39)){
            return 0;
        }else if(isBetween(chance, 40, 69)){
            return 1;
        }else if(isBetween(chance, 70, 89)){
            return 2;
        }else if(isBetween(chance, 90, 98)){
            return 3;
        }else if(isBetween(chance, 99, Integer.MAX_VALUE)){
            return 4;
        }
        return 0;
    }
    
    public void endScreen(){
        Random rnd = new Random();
        ArrayList<Item> loot = new ArrayList<>();
        ArrayList<JLabel> menu = new ArrayList<>();
        
        //generate loot
        int amount = getChance(0)+1;
        for(int i = 0; i < amount; i++){
            int quality = rnd.nextInt(100);
            quality += player.getStat(5);
            String itemQuality = "";
            switch(getChance(player.getStat(5))){
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
            String[] item = game.getIds().get(rnd.nextInt(game.getIds().size()-1));
            loot.add(game.createItem(new String[]{item[0], itemQuality}));
            JLabel newl = new JLabel();
            newl.setName(String.valueOf(i));
            menu.add(newl);
        }
        
        //Add items to inv
        ArrayList<Item> newInv = game.getInv();
        for(Item i : loot){
            newInv.add(i);
        }
        
        game.setInventory(newInv);
        
        //build menu
        JLabel bg, button, title;
        bg = new JLabel();
        button = new JLabel();
        title = new JLabel();
        menu.add(bg);
        menu.add(button);
        menu.add(title);
        bg.setBounds(100, 255, 1720, 570);
        button.setBounds(730, 740, 460, 70);
        title.setBounds(130, 280, 1660, 70);
        
        for(JLabel l : menu){
            labelFactory(l, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 30, 150),
                new Font("Agency FB", 1, 36));
            this.add(l, 0);
        }
        
        title.setText("You have acquired the following items");
        button.setText("Leave Labyrinth");
        int max = loot.size();
        for(JLabel l : menu){
            if(l.getName() != null){
                if(loot.size() > 0){
                    CustomLabel cl = new CustomLabel();
                    cl.setBounds(150 + ((max - loot.size())*340), 380, 240, 240);
                    cl.setIcon(new javax.swing.ImageIcon(loot.get(0).getImg().getScaledInstance(240, 240, Image.SCALE_DEFAULT)));
                    cl.setToolTipText("<html>" + loot.get(0).getName() + "<br><b><em style='color: #" 
                                    + getColor(loot.get(0).getRarity()) + "'>" 
                                    +  loot.get(0).getRarity()
                                    + "</em></b><br>" + "<html>");
                    this.add(cl, 0);
                    loot.remove(0);
                }
            }
        }
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exit(true);
            }
        });
    }
    
    private String getColor(String input){
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
    
    private int monsterIndx = -1;

    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                moveViewport();
                int rnd = (int) (Math.random() * 2);
                monsterController(rnd);
                //check for potential combat
                int cnt = 0;
                for(Monster m : monsters){
                    if(m.collides(player)){
                        paused = true;
                        createCombatEngine(m);
                        monsterIndx = cnt;
                        activeCombat = true;
                    }
                    cnt++;
                }
                if((!level.playerCollides(playerHitbox.get(0)) && player.getVely() < 0) || (!level.playerCollides(playerHitbox.get(2)) && player.getVely() > 0)){
                    player.moveY();
                    for(Player p : playerHitbox){
                        p.moveY();
                    }
                    vignette.moveY();
                }
                if((!level.playerCollides(playerHitbox.get(3)) && player.getVelx() < 0) || (!level.playerCollides(playerHitbox.get(1)) && player.getVelx() > 0)){
                    player.moveX();
                    for(Player p : playerHitbox){
                        p.moveX();
                    }
                    vignette.moveX();
                }
                repaint();
            }
            
        }

        private void monsterController(int rnd){
            for(int i = 0; i < monsters.size(); i++){
                int j = i*4;
                if(rnd == 1){
                    if(!level.collides(monsterHitboxes.get(j)) && monsters.get(i).gety() == 0){
                        monsters.get(i).changeDirection(1);
                        monsterHitboxes.get(j).changeDirection(1);
                        monsterHitboxes.get(j+1).changeDirection(1);
                        monsterHitboxes.get(j+2).changeDirection(1);
                        monsterHitboxes.get(j+3).changeDirection(1);
                    }else if(!level.collides(monsterHitboxes.get(j+1)) && monsters.get(i).getx() == 0){
                        monsters.get(i).changeDirection(2);
                        monsterHitboxes.get(j).changeDirection(2);
                        monsterHitboxes.get(j+1).changeDirection(2);
                        monsterHitboxes.get(j+2).changeDirection(2);
                        monsterHitboxes.get(j+3).changeDirection(2);
                    }else if(!level.collides(monsterHitboxes.get(j+2)) && monsters.get(i).gety() == 0){
                        monsters.get(i).changeDirection(3);
                        monsterHitboxes.get(j).changeDirection(3);
                        monsterHitboxes.get(j+1).changeDirection(3);
                        monsterHitboxes.get(j+2).changeDirection(3);
                        monsterHitboxes.get(j+3).changeDirection(3);
                    }else if(!level.collides(monsterHitboxes.get(j+3)) && monsters.get(i).getx() == 0){
                        monsters.get(i).changeDirection(4);
                        monsterHitboxes.get(j).changeDirection(4);
                        monsterHitboxes.get(j+1).changeDirection(4);
                        monsterHitboxes.get(j+2).changeDirection(4);
                        monsterHitboxes.get(j+3).changeDirection(4);
                    }
                }
                
                monsters.get(i).move();
                monsterHitboxes.get(j).move();
                monsterHitboxes.get(j+1).move();
                monsterHitboxes.get(j+2).move();
                monsterHitboxes.get(j+3).move();
                if (level.collides(monsters.get(i))) {
                    monsters.get(i).stepBack();
                    monsterHitboxes.get(j).stepBack();
                    monsterHitboxes.get(j+1).stepBack();
                    monsterHitboxes.get(j+2).stepBack();
                    monsterHitboxes.get(j+3).stepBack();
                    rnd = (int) ((Math.random() * 4) + 1);
                    monsters.get(i).changeDirection(rnd);
                    monsterHitboxes.get(j).changeDirection(rnd);
                    monsterHitboxes.get(j+1).changeDirection(rnd);
                    monsterHitboxes.get(j+2).changeDirection(rnd);
                    monsterHitboxes.get(j+3).changeDirection(rnd);
                }
            }
        }
    }
}
