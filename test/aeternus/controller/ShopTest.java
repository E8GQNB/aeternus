/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author User
 */
public class ShopTest {
    ShopManager s;
    
    @Test
    public void testLocation(){
        assertNotNull(GameEngine.locations.SQUARE);
        assertNotNull(GameEngine.locations.SQUARE.getPOI());
        assertEquals("/images/menuBackground.png", GameEngine.locations.SQUARE.getPath());
    };
}
