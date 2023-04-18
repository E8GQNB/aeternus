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
import aeternus.controller.InventoryManager;
import aeternus.model.CustomLabel;
import aeternus.model.InfoText;
import aeternus.model.Item;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class AeternusGUI {
    
    private MainFrame s = new MainFrame(this);
    private Component[] clist = s.getRootPane().getContentPane().getComponents();
    private volatile boolean dialougeState = false;
    private boolean activeLabyrinth = false;
    private JLabel backgroundImage = new JLabel();
    private JLabel subBackground = new JLabel();
    private JLabel transitionCover = new JLabel();
    private JLabel subTransition = new JLabel();
    private ArrayList<JLabel> mapLocations = new ArrayList<JLabel>();
    private ArrayList<JLabel> locationOptions = new ArrayList<JLabel>();
    private ArrayList<JLabel> upgradeOptions = new ArrayList<JLabel>();
    private ArrayList<JLabel> shopMenu = new ArrayList<JLabel>();
    private InventoryManager inventory;
    private JLabel soulCount = new JLabel();
    private JLabel invLabel = new JLabel();
    private JLabel engine = new JLabel();
    private GameEngine game;
    private GameEngine.locations currentLocale;
    private LocalTime portalEntry = LocalTime.now().minusMinutes(10);
    private MouseListener ml;
    
    public AeternusGUI(){ 
        s.setVisible(true);
        s.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/gameIcon.png")));
        this.game = new GameEngine(this);
        inventory = new InventoryManager(this, game);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                findPanel("SplashScreen").setVisible(false);
            }
        }, 
        100
);
        
        engineClickEffect();
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
    
    public JFrame getFrame(){
        return s;
    }
    
    public ArrayList<JLabel> getLocationOptions() {
        return locationOptions;
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
        transitionCover.setFocusable(false);
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
    
    public void refreshPortal(){
        for(JLabel j : locationOptions){
            if(j.getName().equals("Enter Portal")){
                long minutes = Duration.between(LocalTime.now(), portalEntry.plusMinutes(5)).toMinutes();
                long seconds = Duration.between(LocalTime.now(), portalEntry.plusMinutes(5)).toSecondsPart();
                if(!activeLabyrinth){
                    if(minutes > 0 || seconds > 0){
                        j.setEnabled(false);
                        String s = "";
                        if(seconds < 10){
                            s = minutes + ":0" + seconds;
                        }else{
                            s = minutes + ":" + seconds;
                        }

                        j.setText(s);
                        j.getParent().revalidate();
                        j.getParent().repaint();
                    }else if(!j.isEnabled()){
                        j.setEnabled(true);
                        j.setText("Enter Portal");
                    }
                }
            }
        }
    }
    
    public void afterPortalExit(Boolean win){
        LocalTime l2 = portalEntry.plusMinutes(5);
        long difference = LocalTime.now().until(l2, ChronoUnit.MINUTES);
        if(win && game.getGenericStat("PortalsEntered") == 0){
            startDialouge("FirstLabyrinthWon");
        }else if(!win && game.getGenericStat("PortalsEntered") == 0){
            startDialouge("FirstLabyrinthLost");
        }
        game.setGenericStat("PortalsEntered", game.getGenericStat("PortalsEntered")+1);
        portalThread(difference);
    }
    
    private void loadPointsOfInterest(GameEngine.locations x){
        ArrayList<String[]> POI = x.getPOI();
        currentLocale = x;
        setSoulCount(findPanel("Main"));
        invLabel.setEnabled(true);
        for(String[] point : POI){
            JLabel newLabel = new javax.swing.JLabel();
            labelFactory(newLabel, false, game.getLockState(point[5]), new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(255, 255, 255), 
                new Color(0, 0, 0, 0), 
                String.valueOf(point[0]), 
                new Font("Agency FB", 0, 30));
            newLabel.setName(point[5]);
            findPanel("Main").add(newLabel);
            findPanel("Main").setComponentZOrder(newLabel, 1);
            newLabel.setBounds(Integer.parseInt(point[1]), Integer.parseInt(point[2]), Integer.parseInt(point[3]), Integer.parseInt(point[4]));
            newLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if(newLabel.isEnabled()){
                        loadLocation(evt);
                    }
                }
            });
            mapLocations.add(newLabel);
        }
        transitionCover.getParent().setComponentZOrder(transitionCover, 0);
    }
    
    private void removePointsOfInterest(){
        for(JLabel l : mapLocations){
            l.getParent().remove(l);
        }
        mapLocations.clear();
        backgroundImage.getParent().revalidate();
        backgroundImage.getParent().repaint();
    }
    
    public void setPOI(boolean b){
        for(JLabel l : mapLocations){
            l.setEnabled(b);
        }
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
        String flag = game.getFlag(GameEngine.interactables.valueOf(evt.getComponent().getName()));
        ArrayList<String[]> options = GameEngine.interactables.valueOf(evt.getComponent().getName()).getOptions();
        Thread one = new Thread() {
            @Override
            public void run() {
                setPOI(false);
                setBackground("/images/" + evt.getComponent().getName() + ".png", 2, true, subBackground, findPanel("subMenu"));
                if(flag != null && flag != ""){
                    startDialouge(flag);
                }
                int cnt = 0;
                for(String[] row : options){
                    if(row[0].equals("option")){
                        addOption(row[1], GameEngine.interactables.valueOf(evt.getComponent().getName()).toString(), cnt);
                        cnt++;
                    }
                }
                invLabel.setEnabled(true);
            }
        };
        one.start();
    }
    
    private void portalThread(long difference){
        Thread two = new Thread() {
            @Override
            public void run() {
                while(difference > 0){
                    refreshPortal();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
            };
        two.start();
    }
    
    private void addOption(String name, String id, int place){
        JLabel newL = new JLabel();
        labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(40, 40, 40), 
                name, 
                new Font("Agency FB", 0, 36));
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(610, 400 + (place*100), 700, 50);
        locationOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled()){
                    option(evt, id, name);
                }
            }
        });
    }
    
    private void option(java.awt.event.MouseEvent evt, String id, String name){
        if(!dialougeState){
            setOptions(false);
            String flag = game.getFlag(name);
            if(flag != null && !flag.equals("")){
                startDialouge(flag);
                return;
            }
            switch(name){
                case "Leave":
                    leaveSubMenu();
                break;
                case "Shop":
                    loadShop(id);
                break;
                case "Engine":
                    setOptionsVisibility(false);
                    showEngineMenu();
                break;
                case "Talk":
                    startDialouge(id + (int)(Math.random()*5));
                break;
                case "Burn Items":
                    showBurner();
                break;
                case "Enter Portal":
                {
                    try {
                        portalEntry = LocalTime.now();
                        setOptions(false);
                        setOptionsVisibility(false);
                        activeLabyrinth = true;
                        game.enterPortal();
                    } catch (IOException ex) {
                        Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            }
        }
        
    }
    
    public void setOptions(boolean b){
        for(JLabel l : locationOptions){
            l.setEnabled(b);
        }
    }
    
    public void setOptionsVisibility(boolean b){
        for(JLabel l : locationOptions){
            l.setVisible(b);
        }
    }
    
    public void labelFactory(JLabel j, boolean opacity, boolean vis, int[] alignment, Color fG, Color bG, String text, Font f){
        j.setOpaque(opacity);
        j.setVisible(vis);
        j.setHorizontalAlignment(alignment[0]);
        j.setVerticalAlignment(alignment[1]);
        j.setForeground(fG);
        j.setBackground(bG);
        j.setText(text);
        j.setFont(f);
        j.setName(text);
    }
    
    private void setSoulCount(JPanel destination){
        labelFactory(soulCount, true, true, new int[]{SwingConstants.LEFT, SwingConstants.TOP}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 100), 
                String.valueOf(game.getSouls()), 
                new Font("Agency FB", 1, 36));
        destination.add(soulCount, 0);
        soulCount.setBounds(10, 10, 120, 50);
        createInvButton(destination);
    }
    
    private void createInvButton(JPanel destination){
        invLabel.removeMouseListener(ml);
        invLabel.setEnabled(false);
        labelFactory(invLabel, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 50), 
                "Inv", 
                new Font("Agency FB", 1, 28));
        destination.add(invLabel, 0);
        invLabel.setBounds(1860, 0, 60, 60);
        ml = new MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(inventory.getInventoryMenu().size()>0){
                    inventory.hideInventory(destination);
                }else{
                    inventory.showInventory(destination, false);
                }
                
            }
        };
        invLabel.addMouseListener(ml);
    }
    
    public void refreshSouls(){
        if(!activeLabyrinth){
            if(game.getSouls() > 1000000){
                soulCount.setText(String.format("%.0f", game.getSouls()/1000000) + "M");
            }else if(game.getSouls() > 1000){
                soulCount.setText(String.format("%.0f", game.getSouls()));
            }else{
                soulCount.setText(String.format("%.1f", game.getSouls()));
            }
            soulCount.getParent().revalidate();
            soulCount.getParent().repaint();
            double souls = game.getSouls();

            for(JLabel j : upgradeOptions){
                j.setEnabled((souls - Double.parseDouble(j.getName())) >= 0);
                if(!j.isEnabled()){
                    j.setBackground(new Color(30,30,30));
                }else{
                    j.setBackground(new Color(40,40,40));
                }
            }
        }
    }
    
    private void leaveSubMenu(){
        findPanel("subMenu").getParent().remove(findPanel("subMenu"));
        removePointsOfInterest();
        loadPointsOfInterest(currentLocale);
        s.getRootPane().getContentPane().revalidate();
        s.getRootPane().getContentPane().repaint();
    }
    
    private void startDialouge(String name){
        dialougeState = true;
        setOptions(false);
        DialougeSystem d = new DialougeSystem(findPanel("subMenu"), this);
        Thread one = new Thread() {
            @Override
            public void run() {
                try {
                    d.play(name);
                    while(dialougeState){}
                    setOptions(true);
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                game.checkConnections(name);
                game.removeFlag(name);
            }
        };
        one.start();
    }
    
    private void loadShop(String id){
        if(id.equals("MAGICSHOP")){
            if(game.getSouls() == 0){
                startDialouge("NoSoulsMagic");
            }else if(game.getSouls() < 10000 && !game.getLockState("PORTAL")){
                startDialouge("BeforePortalsMagic");
            }else if(game.getSouls() >= 10000 && !game.getLockState("PORTAL")){
                startDialouge("UnlockPortalsMagic");
            }else{
                openShop(id);
            }
        }else if(id.equals("WEAPONSHOP")){
            if(!game.getLockState("PORTAL")){
                startDialouge("BeforePortalsWEAPONSHOP");
            }else{
                openShop(id);
            }
        }
    }
    
    public void showBurner(){
        inventory.showInventory(findPanel("subMenu"), true);
    }
    
    public void setActiveLabyrinth(Boolean b){
        activeLabyrinth = b;
    }
    
    private void openShop(String shop){
        JLabel shopBg = new JLabel();
        labelFactory(shopBg, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
        findPanel("subMenu").add(shopBg, 0);
        shopBg.setBounds(100, 100, 1720, 880);
        setOptions(false);
        setOptionsVisibility(false);
        shopMenu.add(shopBg);
        ArrayList<String[]> stock = game.getStock(shop);
        ArrayList<String[]> idList = game.getIds();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                CustomLabel shopSlot = new CustomLabel();
                labelFactory(shopSlot, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
                if(stock.size() >= i*8 + j + 1){
                    shopSlot.setIcon(new javax.swing.ImageIcon(
                                    new javax.swing.ImageIcon(getClass().getResource(
                                    "/items/itemImages/tempImage.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                    String id = stock.get(i*8 + j)[0];
                    for(String[] s : idList){
                        if(s[0].equals(id)){
                            shopSlot.setToolTipText("<html>" + s[1] + "<br><b><em style='color: #" 
                                    + inventory.getColor(stock.get(i*6 + j)[1]) + "'>" 
                                    +  stock.get(i*8 + j)[1] 
                                    + "</em></b><br>§" + stock.get(i*6 + j)[2] + "<br><html>");
                        }
                    }
                    int slotnum = i*8 + j;
                    shopSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            purchaseItem(slotnum, shop);
                        }
                    });
                }
                
                findPanel("subMenu").add(shopSlot, 0);
                shopSlot.setName("slot" + j*8 + i);
                shopSlot.setBounds(190 + j*200, 150 + i*200, 150, 150);
                setOptionsVisibility(false);
                shopMenu.add(shopSlot);
            }
        }
        JLabel exit = new JLabel();
        labelFactory(exit, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.white, 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 26));
        exit.setText("Leave");
        findPanel("subMenu").add(exit, 0);
        exit.setBounds(760, 915, 400, 50);
        shopMenu.add(exit);
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                for(JLabel j : shopMenu){
                    j.getParent().remove(j);
                }
                shopMenu.clear();
                setOptions(true);
                setOptionsVisibility(true);
                findPanel("subMenu").revalidate();
                findPanel("subMenu").repaint();
            }
        });
        
        findPanel("subMenu").revalidate();
        findPanel("subMenu").repaint();
    }
    
    private void purchaseItem(int slot, String shop){
        ArrayList<String[]> stock = game.getStock(shop);
        ArrayList<Item> inv = game.getInv();
        if(game.getSouls() >= Integer.parseInt(stock.get(slot)[2])){
            Item x = game.createItem(new String[]{stock.get(slot)[0], stock.get(slot)[1]});
            game.alterSouls(-Integer.parseInt(stock.get(slot)[2]));
            stock.remove(slot);
            inv.add(x);
            game.setInventory(inv);
            
            for(JLabel j : shopMenu){
                    j.getParent().remove(j);
                }
                shopMenu.clear();
                setOptions(true);
                setOptionsVisibility(true);
                findPanel("subMenu").revalidate();
                findPanel("subMenu").repaint();
            openShop(shop);
        }
    }
    
    private void showEngineMenu(){
        labelFactory(engine, false, true, new int[]{SwingConstants.LEFT, SwingConstants.TOP}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 150), 
                null, 
                new Font("Agency FB", 0, 36));
        findPanel("subMenu").add(engine,0 );
        engine.setBounds(200, 320, 500, 500);
        engine.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/ENGINE.png")).getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        engine.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                game.alterSouls(game.getSoulPower());
                refreshSouls();
                spawnEngineEffect();
            }
        });
        ArrayList<String[]> upgrades = game.getEngineUpgrades();
        int cnt = 0;
        for(String[] row : upgrades){
            addUpgradeOption(row[0], cnt, Double.parseDouble(row[2]), row[4], Double.parseDouble(row[3]));
            cnt++;
        }
    }
    
    private void addUpgradeOption(String name, int place, double price, String desc, double power){
        CustomLabel newL = new CustomLabel();
        labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                Color.lightGray, 
                new Color(40, 40, 40), 
                null, 
                new Font("Agency FB", 0, 28));
        if(name.equals("Leave")){
            newL.setText(name);
        }else{
            newL.setText("<html><div style='text-align: center'>" + name + "<br>" + String.format("%.1f", price) + "<div><html>");
        }
        newL.setName(String.valueOf(price));
        newL.setToolTipText("<html>" + power + " Souls per Second<br>" + desc + "<html>");
        newL.setEnabled((game.getSouls()- price) >= 0);
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(800, 100 + (place*120), 1000, 90);
        upgradeOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled() && !newL.getText().equals("Leave")){
                    buyUpgrade(name);
                }else if(newL.getText().equals("Leave")){
                    engine = new JLabel();
                    upgradeOptions.clear();
                    leaveSubMenu();
                }
            }
        });
    }
    
    private void buyUpgrade(String name){
        int indx = 0;
        for(String[] row : game.getEngineUpgrades()){
            if(row[0].equals(name)){
                int x = Integer.parseInt(row[1]);
                x++;
                game.alterSouls(-Double.parseDouble(row[2]));
                row[2] = String.valueOf(Double.parseDouble(row[2]) * 1.15);
                upgradeOptions.get(indx).setText("<html><div style='text-align: center'>" + name + "<br>" + String.format("%.1f", Double.parseDouble(row[2])) + "<div><html>");
                row[1] = String.valueOf(x);
                upgradeOptions.get(indx).setName(row[2]);
                upgradeOptions.get(indx).getParent().revalidate();
                upgradeOptions.get(indx).getParent().repaint();
            }
            indx++;
        }
    }
    
    private ArrayList<JLabel> engineEffects = new ArrayList<JLabel>();
    private ArrayList<Integer> engineEffectLifespan = new ArrayList<Integer>();
    private void spawnEngineEffect(){
        JLabel j = new JLabel();
        labelFactory(j, false, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 100), 
                String.valueOf(game.getSoulPower()), 
                new Font("Agency FB", 1, 30));
        Point p = MouseInfo.getPointerInfo().getLocation();
        Random rnd = new Random();
        j.setBounds(p.x + rnd.nextInt(50)-30, p.y - rnd.nextInt(50), 30, 30);
        findPanel("subMenu").add(j);
        findPanel("subMenu").setComponentZOrder(j, 0);
        engineEffects.add(j);
        engineEffectLifespan.add(40);
    }
    
    private void engineClickEffect(){
        Runnable newThread = () -> {
            for(int i = 0; i < engineEffects.size(); i++){
                engineEffects.get(i).setLocation(engineEffects.get(i).getLocation().x, engineEffects.get(i).getLocation().y-1);
                if(engineEffectLifespan.get(i) > 1){
                    engineEffectLifespan.set(i, engineEffectLifespan.get(i)-1);
                }else{
                    engineEffects.get(i).getParent().remove(engineEffects.get(i));
                    engineEffects.remove(i);
                    engineEffectLifespan.remove(i);
                }
                
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newThread, 0, 15, TimeUnit.MILLISECONDS);
    }
    
    public void newGame(){
        dialougeState = true;
        DialougeSystem d = new DialougeSystem(findPanel("Main"), this);
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
                    setBackground("/images/caveBackground.png", 5, true);
                    /*d.play("Open");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    CreateInfo(findPanel("Main"), "An hour later...", 3000);
                    dialougeState = true;
                    d.play("Open2");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    CreateInfo(findPanel("Main"), "At the surface", 2500, "/images/menuBackground.png", 5);
                    dialougeState = true;*/
                    d.play("Open3");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    d.removeAllStuff();
                    loadLocale(GameEngine.locations.SQUARE, false);
                    game.soulCountThread();
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
}