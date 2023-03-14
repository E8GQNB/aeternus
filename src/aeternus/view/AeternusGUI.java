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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;

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
    private ArrayList<JLabel> upgradeOptions = new ArrayList<JLabel>();
    private JLabel soulCount = new JLabel();
    private JLabel engine = new JLabel();
    private GameEngine game;
    private GameEngine.locations currentLocale;
    
    public AeternusGUI(){ 
        s.setVisible(true);
        this.game = new GameEngine(this);
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                findPanel("SplashScreen").setVisible(false);
            }
        }, 
        100
);
        
        engineClickEffect(510);
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
            labelFactory(newLabel, false, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER});
            newLabel.setForeground(new java.awt.Color(255, 255, 255));
            newLabel.setText(point[0]);
            newLabel.setName(point[5]);
            newLabel.setVisible(game.getLockState(point[5]));
            newLabel.setFont(new java.awt.Font("Agency FB", 0, 30));
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
            scenePoints.add(newLabel);
        }
        transitionCover.getParent().setComponentZOrder(transitionCover, 0);
    }
    
    private void removePointsOfInterest(){
        for(JLabel l : scenePoints){
            l.getParent().remove(l);
        }
        scenePoints.clear();
        backgroundImage.getParent().revalidate();
        backgroundImage.getParent().repaint();
    }
    
    private void setPOI(boolean b){
        for(JLabel l : scenePoints){
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
        DialougeSystem event = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.MAGICMERCHANT, findPanel("subMenu"), this);
        String flag = game.getFlag(GameEngine.interactables.valueOf(evt.getComponent().getName()));
        System.out.println(flag);
        ArrayList<String[]> options = GameEngine.interactables.valueOf(evt.getComponent().getName()).getOptions();
        Thread one = new Thread() {
            public void run() {
                setPOI(false);
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
        labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER});
        newL.setForeground(new java.awt.Color(204, 204, 204));
        newL.setText(name);
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
                    option(evt, id, name);
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
    
    private void labelFactory(JLabel j, boolean opacity, boolean vis, int[] alignment){
        j.setOpaque(opacity);
        j.setVisible(vis);
        j.setHorizontalAlignment(alignment[0]);
        j.setVerticalAlignment(alignment[1]);
    }
    
    private void setSoulCount(JPanel destination){
        labelFactory(soulCount, true, true, new int[]{SwingConstants.LEFT, SwingConstants.TOP});
        soulCount.setForeground(new java.awt.Color(204, 204, 204));
        soulCount.setBackground(new java.awt.Color(0, 0, 0, 50));
        soulCount.setText(String.valueOf(game.getSouls()));
        soulCount.setFont(new java.awt.Font("Agency FB", 1, 36));
        destination.add(soulCount, 0);
        soulCount.setBounds(10, 10, 100, 50);
    }
    
    public void refreshSouls(){
        if(game.getSouls() > 1000000){
            soulCount.setText(String.format("%.1f", game.getSouls()/1000000) + "M");
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
    
    private void leaveSubMenu(){
        findPanel("subMenu").getParent().remove(findPanel("subMenu"));
        removePointsOfInterest();
        loadPointsOfInterest(currentLocale);
        s.getRootPane().getContentPane().revalidate();
        s.getRootPane().getContentPane().repaint();
    }
    
    private void startDialouge(String name){
        dialougeState = true;
        DialougeSystem d = new DialougeSystem(GameEngine.characters.PLAYER, GameEngine.characters.MAGICMERCHANT, findPanel("subMenu"), this);
        Thread one = new Thread() {
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
                openShop();
            }
        }else if(id.equals("SOULSMITH")){
            if(!game.getLockState("PORTAL")){
                startDialouge("lockedSmith");
            }else{
                openShop();
            }
        }
    }
    
    private void option(java.awt.event.MouseEvent evt, String id, String name){
        setOptions(false);
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
        }
    }
    
    private void openShop(){
        
    }
    
    private void showEngineMenu(){
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
                game.alterSouls(game.getSoulPower());
                refreshSouls();
                growing = 1;
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
        labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER});
        newL.setForeground(Color.lightGray);
        if(name.equals("Leave")){
            newL.setText(name);
        }else{
            newL.setText("<html><div style='text-align: center'>" + name + "<br>" + String.format("%.1f", price) + "<div><html>");
        }
        newL.setName(String.valueOf(price));
        newL.setToolTipText("<html>" + power + " Souls per Second<br>" + desc + "<html>");
        newL.setEnabled((game.getSouls()- price) >= 0);
        newL.setFont(new java.awt.Font("Agency FB", 0, 28));
        newL.setBackground(new Color(40, 40, 40));
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(800, 300 + (place*120), 1000, 90);
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
    
    private int growing = 0;
    private void engineClickEffect(int maxSize){
        Runnable newThread = new Runnable() {
            @Override
            public void run() {
                if(growing > 0){
                    engine.setBounds(engine.getLocation().x-1, engine.getLocation().y-1, engine.getHeight()+2, engine.getWidth()+2);
                    engine.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/betaENGINE.png")).getImage().getScaledInstance(engine.getHeight()+2, engine.getWidth()+2, Image.SCALE_DEFAULT)));
                    engine.getParent().revalidate();
                    engine.getParent().repaint();
                    if(engine.getWidth() >= maxSize){
                        growing = -1;
                    }
                }else if(growing < 0){
                    engine.setBounds(engine.getLocation().x+1, engine.getLocation().y+1, engine.getHeight()-2, engine.getWidth()-2);
                    engine.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(
                                                "/images/betaENGINE.png")).getImage().getScaledInstance(engine.getHeight()-2, engine.getWidth()-2, Image.SCALE_DEFAULT)));
                    engine.getParent().revalidate();
                    engine.getParent().repaint();
                    if(engine.getWidth() <= maxSize-20){
                        growing = 0;
                    }
                }
            }
            
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(newThread, 0, 5, TimeUnit.MILLISECONDS);
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
                    Thread.sleep(1000);
                    CreateInfo(findPanel("Main"), "At the surface", 2500, "/images/betaMenuBackground.png", 5);
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

class CustomLabel extends JLabel {

   public CustomLabel() {
   }

   @Override
   public JToolTip createToolTip() {
      return (new MyCustomToolTip(this));
   }
}

class MyCustomToolTip extends JToolTip {
   public MyCustomToolTip(JComponent component) {
      super();
      setComponent(component);
      setBackground(Color.darkGray);
      setForeground(Color.lightGray);
      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
   }
}