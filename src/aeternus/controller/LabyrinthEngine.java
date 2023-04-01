/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Labyrinth;
import aeternus.model.Monster;
import aeternus.model.Player;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
    private final int DRAGON_SIZE = 80;

    private boolean paused = false;
    private Image background;
    private static int levelNum = 0;
    private Labyrinth level;
    private Monster monster;
    private Monster up;
    private Monster down;
    private Monster left;
    private Monster right;
    private Player player;
    private Player vignette;
    private Player Up;
    private Player Right;
    private Player Down;
    private Player Left;
    private Timer newFrameTimer;

    public LabyrinthEngine() {
        super();
        background = new ImageIcon("src/images/betaCaveBackground.png").getImage();
        
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
        Up.setVelx(x);
        Right.setVelx(x);
        Down.setVelx(x);
        Left.setVelx(x);
        vignette.setVelx(x);
    }
    
    public void setY(int y){
        player.setVely(y);
        Up.setVely(y);
        Right.setVely(y);
        Down.setVely(y);
        Left.setVely(y);
        vignette.setVely(y);
    }
    
    public void restart() {
        try {
            level = new Labyrinth("data/levels/lvl0" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Image playerImage = new ImageIcon("src/images/betaSERVANTIcon.png").getImage();
        Image vignetteImage = new ImageIcon("src/images/betaSERVANTIcon.png").getImage();
        int playerSpawnX = 152*4+10;
        int playerSpawnY = 152*4+5;
        player = new Player(playerSpawnX, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Up = new Player(playerSpawnX, playerSpawnY-5, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Right = new Player(playerSpawnX+5, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Down = new Player(playerSpawnX, playerSpawnY+5, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        Left = new Player(playerSpawnX-5, playerSpawnY, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        vignette = new Player(-2130, -150, 4537, 2190, vignetteImage);
        Image dragonImage = new ImageIcon("src/images/betaMAGIC MERCHANTIcon.png").getImage();
        int dragonX = (int) ((Math.random() * 30) + 1);
        int dragonY = (int) ((Math.random() * 15) + 1);
        monster = new Monster(60*dragonX, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        up = new Monster(60*dragonX, (60*dragonY)-10, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        right = new Monster((60*dragonX)+10, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        down = new Monster(60*dragonX, (60*dragonY)+10, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        left = new Monster((60*dragonX)-10, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        while(level.collides(monster)){
            dragonX = (int) ((Math.random() * 30) + 1);
            dragonY = (int) ((Math.random() * 15) + 1);
            monster = new Monster(60*dragonX, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
            up = new Monster(60*dragonX, (60*dragonY)-10, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
            right = new Monster((60*dragonX)+10, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
            down = new Monster(60*dragonX, (60*dragonY)+10, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
            left = new Monster((60*dragonX)-10, 60*dragonY, DRAGON_SIZE, DRAGON_SIZE, dragonImage);
        }
    }

    public static int getPoints(){
        return levelNum;
    }
    
    private boolean moveViewport(){
        int xmin = 480;
        int xmax = 1440;
        int ymin = 270;
        int ymax = 810;
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
        player.setX(player.getX() + newx);
        player.setY(player.getY() + newy);
        
        Up.setX(Up.getX() + newx);
        Up.setY(Up.getY() + newy);
        
        Right.setX(Right.getX() + newx);
        Right.setY(Right.getY() + newy);
        
        Down.setX(Down.getX() + newx);
        Down.setY(Down.getY() + newy);
        
        Left.setX(Left.getX() + newx);
        Left.setY(Left.getY() + newy);
        if(newx != 0 || newy != 0){
            return true;
        }
        return false;
    }
    
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 1920, 1080, null);
        level.draw(grphcs);
        player.draw(grphcs);
        Up.draw(grphcs);
        Down.draw(grphcs);
        Left.draw(grphcs);
        Right.draw(grphcs);
        monster.draw(grphcs);
        //vignette.draw(grphcs);
    }

    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                boolean viewMoved = moveViewport();
                int rnd = (int) (Math.random() * 2);
                if(!level.collides(up) && monster.gety() == 0){
                    if(rnd == 1){
                        monster.changeDirection(1);
                        up.changeDirection(1);
                        down.changeDirection(1);
                        left.changeDirection(1);
                        right.changeDirection(1);
                    }
                }else if(!level.collides(right) && monster.getx() == 0){
                    if(rnd == 1){
                        monster.changeDirection(2);
                        up.changeDirection(2);
                        down.changeDirection(2);
                        left.changeDirection(2);
                        right.changeDirection(2);
                    }
                }else if(!level.collides(down) && monster.gety() == 0){
                    if(rnd == 1){
                        monster.changeDirection(3);
                        up.changeDirection(3);
                        down.changeDirection(3);
                        left.changeDirection(3);
                        right.changeDirection(3);
                    }
                }else if(!level.collides(left) && monster.getx() == 0){
                    if(rnd == 1){
                        monster.changeDirection(4);
                        up.changeDirection(4);
                        down.changeDirection(4);
                        left.changeDirection(4);
                        right.changeDirection(4);
                    }
                }
                monster.move();
                up.move();
                down.move();
                right.move();
                left.move();
                if (level.collides(monster)) {
                    monster.stepBack();
                    up.stepBack();
                    left.stepBack();
                    right.stepBack();
                    down.stepBack();
                    rnd = (int) ((Math.random() * 4) + 1);
                    monster.changeDirection(rnd);
                    up.changeDirection(rnd);
                    down.changeDirection(rnd);
                    left.changeDirection(rnd);
                    right.changeDirection(rnd);
                }
                if (player.collides(monster)) {
                    paused = true;
                    //ded
                }
                if((!level.playerCollides(Up) && player.getVely() < 0) || (!level.playerCollides(Down) && player.getVely() > 0)){
                    player.moveY();
                    Up.moveY();
                    Left.moveY();
                    Right.moveY();
                    Down.moveY();
                    vignette.moveY();
                }
                if((!level.playerCollides(Left) && player.getVelx() < 0) || (!level.playerCollides(Right) && player.getVelx() > 0)){
                    player.moveX();
                    Up.moveX();
                    Left.moveX();
                    Right.moveX();
                    Down.moveX();
                    vignette.moveX();
                }
                if(monster.getY() <= 0){
                    monster.changeDirection(3);
                    up.changeDirection(3);
                    down.changeDirection(3);
                    left.changeDirection(3);
                    right.changeDirection(3);
                }
            }
            if (player.isOut()) {
                levelNum++;
                restart();
            }
            repaint();
        }

    }
}
