/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;

import java.awt.Component;
import javax.swing.JPanel;
import aeternus.controller.DialougeSystem;
import aeternus.controller.GameEngine;
import aeternus.model.InfoText;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author User
 */
public class AeternusGUI {
    
    private MainFrame s = new MainFrame(this);
    private Component[] clist = s.getRootPane().getContentPane().getComponents();
    private volatile boolean dialougeState = false;
    private JLabel backgroundImage = new JLabel();
    private JLabel subBackground = new JLabel();
    private JLabel transitionCover = new JLabel();
    private JLabel subTransition = new JLabel();
    private ArrayList<JLabel> scenePoints = new ArrayList<JLabel>();
    private ArrayList<JLabel> locationOptions = new ArrayList<JLabel>();
    private JLabel soulCount = new JLabel();
    private GameEngine game;
    private GameEngine.locations currentLocale;
    
    public AeternusGUI(){ 
        s.setVisible(true);
        this.game = new GameEngine();
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                findPanel("SplashScreen").setVisible(false);
            }
        }, 
        100
);
    }

    public void initiateGame(){
        ((JPanel) s.getRootPane().getContentPane().getComponent(1)).getComponent(0).setVisible(true);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                findPanel("MainMenu").setVisible(false);
                findPanel("Main").setVisible(true);
                //clist[2].setVisible(true);
                newGame();
            }
        }, 
        570
);
        
    }
    
    public JPanel findPanel(String name){
        for(Component c : clist){
            if(c.getName().equals(name)){
                return (JPanel)c;
            }
        }
        return null;
    }
    
    public void CreateInfo(JPanel p, String text, int length) throws InterruptedException{
        fadeIn(transitionCover, 5);
        InfoText inf = new InfoText(p, text, length);
        fadeOut(transitionCover, 5);
        
    }
    
    public void CreateInfo(JPanel p, String text, int length, String path, int speed) throws InterruptedException{
        fadeIn(transitionCover, 5);
        InfoText inf = new InfoText(p, text, length);
        setBackground(path, speed, false);
        fadeOut(transitionCover, 5);
        
    }
    
    public void setDialougeState(boolean b){
        dialougeState = b;
    }
    
    private void setBackground(String path, int speed, boolean transition){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        backgroundImage.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        if(transition){
            fadeOut(transitionCover, speed);
        }
    }
    
    private void setBackground(String path, int speed, boolean transition, JLabel bg){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        bg.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        if(transition){
            fadeOut(transitionCover, speed);
        }
    }
    
    private void setBackground(String path, int speed, boolean transition, JLabel bg, JPanel to){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        bg.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        
        to.setVisible(true);
        to.getParent().revalidate();
        to.getParent().repaint();
        transitionCover.setBackground(new Color(0,0,0,0));
        if(transition){
            fadeOut(subTransition, speed);
        }
    }
    
    public void fadeIn(JLabel l, int length){
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    public void fadeOut(JLabel l, int length){
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, 255-i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    private void createTransition(JPanel dest, JLabel tr){
        tr.setForeground(new java.awt.Color(0, 0, 0, 0));
        tr.setOpaque(true);
        tr.setVisible(true);
        dest.add(tr);
        dest.setComponentZOrder(tr, 0);
        tr.setBounds(0, 0, 1920, 1080);
    }
    
    private void createBackground(JPanel dest, JLabel bg, int z){
        bg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bg.setOpaque(false);
        bg.setVisible(true);
        bg.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dest.add(bg);
        dest.setComponentZOrder(bg, z);
        bg.setBounds(0, 0, 1920, 1080);
    }
    
    private void loadLocale(GameEngine.locations x, boolean transition){
        currentLocale = x;
        setBackground(x.getPath(), 5, transition);
        loadPointsOfInterest(x);
        findPanel("Main").getParent().validate();
        findPanel("Main").getParent().repaint();
    }
    
    private void loadPointsOfInterest(GameEngine.locations x){
        ArrayList<String[]> POI = x.getPOI();
        currentLocale = x;
        setSoulCount(findPanel("Main"));
        for(String[] point : POI){
            JLabel newLabel = new javax.swing.JLabel();
            newLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            newLabel.setForeground(new java.awt.Color(255, 255, 255));
            newLabel.setOpaque(false);
            newLabel.setText(point[0]);
            newLabel.setName(point[5]);
            newLabel.setVisible(game.getLockState(point[5]));
            newLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            newLabel.setFont(new java.awt.Font("Agency FB", 0, 30));
            findPanel("Main").add(newLabel);
            findPanel("Main").setComponentZOrder(newLabel, 1);
            newLabel.setBounds(Integer.parseInt(point[1]), Integer.parseInt(point[2]), Integer.parseInt(point[3]), Integer.parseInt(point[4]));
            newLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    loadLocation(evt);
                }
            });
            scenePoints.add(newLabel);
        }
    }
    
    private void removePointsOfInterest(){
        for(JLabel l : scenePoints){
            l.getParent().remove(l);
        }
        scenePoints.clear();
        backgroundImage.getParent().revalidate();
        backgroundImage.getParent().repaint();
    }
    
    private void createNewPanel(String name, int z){
        JPanel newP = new JPanel();
        newP.setBackground(new java.awt.Color(0, 0, 0));
        newP.setMinimumSize(new java.awt.Dimension(1920, 1080));
        createBackground(newP, subBackground, 0);
        createTransition(newP, subTransition);
        newP.setName(name);
        newP.setPreferredSize(new java.awt.Dimension(1920, 1080));
        newP.setLayout(null);
        s.getRootPane().getContentPane().add(newP);
        newP.setBounds(0, 0, 1920, 1080);
        s.getRootPane().getContentPane().setComponentZOrder(newP, z);
        clist = s.getRootPane().getContentPane().getComponents();
        findPanel(name).setVisible(false);
        newP.getParent().revalidate();
        newP.getParent().repaint();
    }
    
    private void loadLocation(java.awt.event.MouseEvent evt){
        createNewPanel("subMenu", 0);
        setSoulCount(findPanel("subMenu"));
        DialougeSystem event = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.MAGICMERCHANT, findPanel("subMenu"), this);
        String flag = game.getFlag(GameEngine.interactables.valueOf(evt.getComponent().getName()));
        System.out.println(flag);
        ArrayList<String[]> options = GameEngine.interactables.valueOf(evt.getComponent().getName()).getOptions();
        Thread one = new Thread() {
            public void run() {
                setBackground("/images/beta" + evt.getComponent().getName() + ".png", 2, true, subBackground, findPanel("subMenu"));
                if(flag != ""){
                    dialougeState = true;
                    try {
                        event.play(flag);
                        while(dialougeState){}
                    } catch (Exception ex) {
                        Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    while(dialougeState){}
                    game.checkConnections(flag);
                    game.removeFlag(flag);
                }
                int cnt = 0;
                for(String[] row : options){
                    if(row[0].equals("option")){
                        addOption(row[1], GameEngine.interactables.valueOf(evt.getComponent().getName()).toString(), cnt);
                        cnt++;
                    }
                }
            }
        };
        one.start();
    }
    
    private void addOption(String name, String id, int place){
        JLabel newL = new JLabel();
        newL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newL.setForeground(new java.awt.Color(204, 204, 204));
        newL.setOpaque(true);
        newL.setVisible(true);
        newL.setText(name);
        newL.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        newL.setFont(new java.awt.Font("Agency FB", 0, 36));
        newL.setBackground(new Color(40, 40, 40));
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(610, 400 + (place*100), 700, 50);
        locationOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled()){
                    loadShop(evt, id, name);
                }
            }
        });
    }
    
    private void setOptions(boolean b){
        for(JLabel l : locationOptions){
            l.setEnabled(b);
        }
    }
    
    private void setOptionsVisibility(boolean b){
        for(JLabel l : locationOptions){
            l.setVisible(b);
        }
    }
    
    private void setSoulCount(JPanel destination){
        soulCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        soulCount.setForeground(new java.awt.Color(204, 204, 204));
        soulCount.setBackground(new java.awt.Color(0, 0, 0, 50));
        soulCount.setOpaque(true);
        soulCount.setVisible(true);
        soulCount.setText(String.valueOf(game.getSouls()));
        soulCount.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        soulCount.setFont(new java.awt.Font("Agency FB", 1, 36));
        destination.add(soulCount, 0);
        soulCount.setBounds(10, 10, 100, 50);
    }
    
    private void refreshSouls(){
        soulCount.setText(String.valueOf(game.getSouls()));
        soulCount.getParent().revalidate();
        soulCount.getParent().repaint();
    }
    
    private void loadShop(java.awt.event.MouseEvent evt, String id, String name){
        setOptions(false);
        if(name.equals("Leave")){
            findPanel("subMenu").getParent().remove(findPanel("subMenu"));
            removePointsOfInterest();
            loadPointsOfInterest(currentLocale);
            s.getRootPane().getContentPane().revalidate();
            s.getRootPane().getContentPane().repaint();
        }else if(name.equals("Shop")){
            if(game.getSouls() == 0){
                dialougeState = true;
                DialougeSystem d = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.MAGICMERCHANT, findPanel("subMenu"), this);
                Thread one = new Thread() {
                    public void run() {
                        try {
                            d.play("NoSoulsMagic");
                            while(dialougeState){}
                            setOptions(true);
                        } catch (Exception ex) {
                            Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                one.start();
            }
        }else if(name.equals("Engine")){
            setOptionsVisibility(false);
            showEngineMenu();
        }
    }
    
    private void showEngineMenu(){
        JLabel engine = new JLabel();
        engine.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        engine.setOpaque(false);
        engine.setVisible(true);
        engine.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        findPanel("subMenu").add(engine,0 );
        engine.setBounds(200, 320, 500, 500);
        engine.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/betaENGINE.png")).getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        engine.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                game.alterSouls(1);
                refreshSouls();
            }
        });
        ArrayList<String[]> upgrades = game.getEngineUpgrades();
        int cnt = 0;
        for(String[] row : upgrades){
            addUpgrade(row[0], cnt, row[3]);
            cnt++;
        }
    }
    
    private void addUpgrade(String name, int place, String state){
        JLabel newL = new JLabel();
        newL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newL.setForeground(new java.awt.Color(204, 204, 204));
        newL.setOpaque(true);
        newL.setVisible(true);
        newL.setText(name);
        newL.setEnabled(Boolean.parseBoolean(state));
        newL.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        newL.setFont(new java.awt.Font("Agency FB", 0, 28));
        newL.setBackground(new Color(40, 40, 40));
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(800, 300 + (place*50), 1000, 40);
        //locationOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled()){
                    buyUpgrade(name);
                }
            }
        });
    }
    
    private void buyUpgrade(String name){
        
    }
    
    public void newGame(){
        dialougeState = true;
        DialougeSystem d = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.SERVANT, findPanel("Main"), this);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    createTransition(findPanel("Main"), transitionCover);
                    createBackground(findPanel("Main"), backgroundImage, 6);
                    setBackground("/images/betaCaveBackground.png", 5, true);
                    /*d.play("Open");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    CreateInfo(findPanel("Main"), "An hour later...", 3000);
                    dialougeState = true;
                    d.play("Open2");
                    while(dialougeState){}
                    Thread.sleep(1000);*/
                    CreateInfo(findPanel("Main"), "At the surface", 2500, "/images/betaMenuBackground.png", 5);
                    dialougeState = true;
                    d.play("Open3");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    d.removeAllStuff();
                    loadLocale(GameEngine.locations.SQUARE, false);
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
}
