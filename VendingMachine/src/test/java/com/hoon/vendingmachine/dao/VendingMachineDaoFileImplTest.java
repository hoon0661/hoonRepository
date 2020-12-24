/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.dao;

import com.hoon.vendingmachine.dto.Item;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Hoon
 */
public class VendingMachineDaoFileImplTest {
    VendingMachineDao testDao;
    public VendingMachineDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testVendingMachine.txt";
//        new FileWriter(testFile);
//        testDao = new VendingMachineDaoFileImpl(testFile);
        testDao = new VendingMachineDaoFileImpl(testFile);
    }
    
        @Test
        public void testEditItem() throws Exception{
        String name = "cheetos";
        double price = 1;
        Item item = new Item(name, price);
        item.setQuantity(99);
        
        testDao.editItem(name, item);
//        assertEquals(item, newItem);
        assertNotNull(testDao.getItem(name));
        assertEquals(item.getQuantity(), testDao.getItem(name).getQuantity());
    }
    
        @Test
        public void testGetItem() throws Exception{
        assertNotNull(testDao.getItem("cheetos"));
        assertNotNull(testDao.getItem("lays"));
        assertNotNull(testDao.getItem("snickers"));
        assertNotNull(testDao.getItem("mixednuts"));

        assertNull(testDao.getItem("pringles"));
        
        Item item1 = new Item("snickers", 1.5);
        item1.setQuantity(0);
        
        Item item2 = new Item("mixednuts", 2.0);
        item2.setQuantity(9);
        
        Item item3 = new Item("cheetos", 1.0);
        item3.setQuantity(99);
        
        Item item4 = new Item("lays", 0.5);
        item4.setQuantity(6);
        
        assertEquals(item1.getPrice(), testDao.getItem("snickers").getPrice());
        assertEquals(item1.getItemName(), testDao.getItem("snickers").getItemName());
        assertEquals(item1.getQuantity(), testDao.getItem("snickers").getQuantity());
        
        assertEquals(item2.getPrice(), testDao.getItem("mixednuts").getPrice());
        assertEquals(item2.getItemName(), testDao.getItem("mixednuts").getItemName());
        assertEquals(item2.getQuantity(), testDao.getItem("mixednuts").getQuantity());
        
        assertEquals(item3.getPrice(), testDao.getItem("cheetos").getPrice());
        assertEquals(item3.getItemName(), testDao.getItem("cheetos").getItemName());
        assertEquals(item3.getQuantity(), testDao.getItem("cheetos").getQuantity());
        
        assertEquals(item4.getPrice(), testDao.getItem("lays").getPrice());
        assertEquals(item4.getItemName(), testDao.getItem("lays").getItemName());
        assertEquals(item4.getQuantity(), testDao.getItem("lays").getQuantity());
        
    }
    

}
