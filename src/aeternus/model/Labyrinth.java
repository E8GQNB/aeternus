/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class Labyrinth {
    
    private final int BLOCK_WIDTH = 120;
    private final int BLOCK_HEIGHT = 120;
    private final int ACROSS = 29;
    private final int DOWN = 30;
    ArrayList<Cell> cells;
    ArrayList<Cell> outer;
    ArrayList<Cell> floorTiles;

    public Labyrinth(String levelPath) throws IOException {
        genLevel();
    }

    public void loadLevel(String levelPath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(levelPath));
        cells = new ArrayList<>();
        int y = 0;
        String line;
        while ((line = br.readLine()) != null) {
            int x = 0;
            for (char blockType : line.toCharArray()) {
                if (blockType == '1') {
                    Image image = new ImageIcon("src/images/betaSERVANTIcon.png").getImage();
                    cells.add(new Cell(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, image));
                }
                x++;
            }
            y++;
        }
    }
    private int[][] maze = new int[ACROSS][DOWN];
    private ArrayList<int[]> fronts = new ArrayList<int[]>();
    private int[][] floor = new int[ACROSS][DOWN];
    
    public void genLevel() throws IOException {
        cells = new ArrayList<>();
        outer = new ArrayList<>();
        floorTiles = new ArrayList<>();
        //Fill labyrinth with walls
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                maze[i][j] = 1;
            }
        }
        //random starting point for generation
        int x = ((int) (Math.random() * ACROSS/2) + 10);
        int y = ((int) (Math.random() * DOWN/2) + 10);
        if(x % 2 == 0){
            x++;
        }
        if(y % 2 == 0){
            y++;
        }
        int[] temp = {x+2, y};
        fronts.add(temp);
        int[] temp2 = {x, y-2};
        fronts.add(temp2);
        int[] temp3 = {x-2,y};
        fronts.add(temp3);
        int[] temp4 = {x, y+2};
        fronts.add(temp4);
        maze[x][y] = 0;
        while(fronts.size() > 0){
            int r = (int) (Math.random() * fronts.size());
            int i = fronts.get(r)[0];
            int j = fronts.get(r)[1];
            int [] passage = randomValidNeighbor(i,j);
            maze[passage[0]][passage[1]] = 0;
            maze[i][j] = 0;
            markFrontiers(i, j);
            fronts.remove(r);
        }
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                if(maze[i][j] == 0){
                    floor[i][j] = 1;
                }
            }
        }
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                if(j < DOWN-1){
                    if(j == DOWN-2){
                        Image image = new ImageIcon("src/images/castlewall.png").getImage();
                        cells.add(new Cell(i * BLOCK_WIDTH, j * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, image));
                    }else{
                        if(maze[i][j] == 1 && maze[i][j+1] != 1){
                            Random rnd = new Random();
                            Image image;
                                image = new ImageIcon("src/images/Labyrinth/Castle/Walls/" + rnd.nextInt(5) + ".png").getImage();
                            cells.add(new Cell(i * BLOCK_WIDTH, j * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, image));
                        }else if(maze[i][j] == 1 && maze[i][j+1] == 1){
                            Image image = new ImageIcon("src/images/castledark.png").getImage();
                            cells.add(new Cell(i * BLOCK_WIDTH, j * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, image));
                        }else{
                            Image image = new ImageIcon("src/images/floor.png").getImage();
                            floorTiles.add(new Cell(i * BLOCK_WIDTH, j * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, image));
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < ACROSS*4; i++){
            for(int j = 0; j < DOWN*4; j++){
                if((j < 10 || i < 10) || (j > 37 || i > 38)){
                    Image image = new ImageIcon("src/images/castledark.png").getImage();
                    outer.add(new Cell((i * BLOCK_WIDTH)-(BLOCK_WIDTH * 10), (j * BLOCK_HEIGHT)-(BLOCK_WIDTH * 10), BLOCK_WIDTH, BLOCK_HEIGHT, image));
                }
            }
        }
        //adjustCenter();
    }
    
    private void adjustCenter(){
        for(Cell c : cells){
            c.setX(c.getX()-((ACROSS/2)*BLOCK_WIDTH)+940);
            c.setY(c.getY()-((DOWN/2)*BLOCK_HEIGHT)+540);
        }
    }
    
    public void shiftField(int x, int y){
        for(Cell c : floorTiles){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
        
        for(Cell c : cells){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
        
        for(Cell c : outer){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
    }

    public int[] randomValidNeighbor(int x, int y){
        int[] res = new int[2];
        boolean run = true;
        while(run){
            int rnd = (int) ((Math.random() * 4));
            //Check if too close to the edge to avoid out of bounds checks
            if(!((rnd == 0 && x > ACROSS-3) || (rnd == 1 && x < 2) || (rnd == 2 && y > DOWN-3) || (rnd == 3 && y < 2))){
                switch(rnd){
                    case 0:
                        if(maze[x+2][y] == 0){
                            res[0] = x+1;
                            res[1] = y;
                            run = false;
                        }
                    break;
                    case 1:
                        if(maze[x-2][y] == 0){
                            res[0] = x-1;
                            res[1] = y;
                            run = false;
                        }
                    break;
                    case 2:
                        if(maze[x][y+2] == 0){
                            res[0] = x;
                            res[1] = y+1;
                            run = false;
                        }

                    break;
                    case 3:
                        if(maze[x][y-2] == 0){
                            res[0] = x;
                            res[1] = y-1;
                            run = false;
                        }
                    break;
                }
            }
        }
        return res;
    }
    
    public boolean neighbors(int x, int y){
        int cnt = 0;
        if(maze[x+1][y] == 0){
            cnt++;
        }if(maze[x-1][y] == 0){
            cnt++;
        }
        if(maze[x][y+1] == 0){
            cnt++;
        }
        if(maze[x][y-1] == 0){
            cnt++;
        }
        if(cnt == 1){
            return true;
        }
        return false;
    }
    
    public void markFrontiers(int x, int y){
        if(x+2 < ACROSS-1){
            if(maze[x+2][y] == 1){
            int[] temp = {x+2, y};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(x-2 > 0){
            if(maze[x-2][y] == 1){
            int[] temp = {x-2, y};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(y+2 < DOWN){
            if(maze[x][y+2] == 1){
            int[] temp = {x, y+2};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(y-2 > 0){
            if(maze[x][y-2] == 1){
            int[] temp = {x, y-2};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
    }
    
    public boolean collides(Monster m) {
        Cell collidedWith = null;
        for (Cell cell : cells) {
            if (m.collides(cell)) {
                collidedWith = cell;
                break;
            }
        }
        if (collidedWith != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean playerCollides(Player player) {
        Cell collidedWith = null;
        for (Cell cell : cells) {
            if (player.collides(cell)) {
                collidedWith = cell;
                break;
            }
        }
        if (collidedWith != null) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics g) {
        for (Cell c : floorTiles) {
            c.draw(g);
        }
        
        for (Cell c : cells) {
            c.draw(g);
        }
        for(Cell c : outer){
            c.draw(g);
        }
    }
}
