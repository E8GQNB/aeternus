/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

<<<<<<< HEAD
import aeternus.view.AeternusGUI;
import javax.swing.JPanel;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
=======
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
>>>>>>> f6d5470dec48dbb5f341e6c47ae7f2649e9cf6a5
import org.junit.Test;

/**
 *
 * @author User
 */
public class ShopTest {
<<<<<<< HEAD
    AeternusGUI gui;
    GameEngine game;
    ShopManager s;
    
    @Before
    public void setUp(){
        gui = new AeternusGUI();
        game = new GameEngine(gui, 73915, false);
        s = new ShopManager(gui, game, "MAGICSHOP");
        game.alterSouls(100000000);
        gui.createNewPanel("subMenu", 0);
    }
    
=======
    ShopManager s;
    
>>>>>>> f6d5470dec48dbb5f341e6c47ae7f2649e9cf6a5
    @Test
    public void testLocation(){
        assertNotNull(GameEngine.locations.SQUARE);
        assertNotNull(GameEngine.locations.SQUARE.getPOI());
        assertEquals("/images/menuBackground.png", GameEngine.locations.SQUARE.getPath());
    };
<<<<<<< HEAD
    
    @Test
    public void purchaseTest(){
        assertTrue(s.purchaseItem(0, "MAGICSHOP"));
    }
=======
>>>>>>> f6d5470dec48dbb5f341e6c47ae7f2649e9cf6a5
}
