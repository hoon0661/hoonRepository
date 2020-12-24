/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.dao;

import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.service.FlooringMasteryServiceLayer;
import com.hoon.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Order;

/**
 *
 * @author Hoon
 */
public class FlooringMasteryDaoFileImplTest {
    FlooringMasteryDao testDao;
    
    public FlooringMasteryDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws IOException {
//        String testFile = "SampleFileData/Orders/Orders_06012013.txt";
//        new FileWriter(testFile);
        testDao = new FlooringMasteryDaoFileImpl();
        new FileWriter("SampleFileData/UnitTesting/Orders_" + "UnitTest" + ".txt");
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAddAndGetOrder() throws FlooringMasteryPersistenceException {
        FMOrder expectedOrder = new FMOrder("Bruce Lee");
        expectedOrder.setOrderNumber(1);
        expectedOrder.setState("CA");
        expectedOrder.setTaxRate(new BigDecimal("25.00"));
        expectedOrder.setProductType("Carpet");
        expectedOrder.setArea(new BigDecimal("60000.0"));
        expectedOrder.setCostPerSquareFoot(new BigDecimal("2.25"));
        expectedOrder.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        expectedOrder.setMaterialCost(new BigDecimal("135000.00"));
        expectedOrder.setLaborCost(new BigDecimal("126000.00"));
        expectedOrder.setTax(new BigDecimal("65250.00"));
        expectedOrder.setTotal(new BigDecimal("326250.00"));
        String path = "SampleFileData/UnitTesting/Orders_" + "UnitTest" + ".txt";
        testDao.addOrder(expectedOrder, path);
        
        FMOrder retrievedOrder = testDao.getOrder(1, path);
        
        assertEquals(expectedOrder, retrievedOrder);
        
        assertTrue(retrievedOrder != null);
        assertEquals(retrievedOrder.getOrderNumber(), expectedOrder.getOrderNumber());
        assertEquals(retrievedOrder.getCustomerName(), expectedOrder.getCustomerName());
        assertEquals(retrievedOrder.getProductType(), expectedOrder.getProductType());
        assertEquals(retrievedOrder.getState(), expectedOrder.getState());
        assertEquals(retrievedOrder.getTaxRate() , expectedOrder.getTaxRate());
        assertEquals(retrievedOrder.getArea() , expectedOrder.getArea());
        assertEquals(retrievedOrder.getCostPerSquareFoot() , expectedOrder.getCostPerSquareFoot());
        assertEquals(retrievedOrder.getLaborCostPerSquareFoot() , expectedOrder.getLaborCostPerSquareFoot());
        assertEquals(retrievedOrder.getMaterialCost() , expectedOrder.getMaterialCost());
        assertEquals(retrievedOrder.getLaborCost() , expectedOrder.getLaborCost());
        assertEquals(retrievedOrder.getTax(), expectedOrder.getTax());
        assertEquals(retrievedOrder.getTotal(), expectedOrder.getTotal());
    }
    
    @Test
    public void testGetAllOrders() throws FlooringMasteryPersistenceException {
        FMOrder order1 = new FMOrder("Bruce Lee");
        order1.setOrderNumber(1);
        order1.setState("CA");
        order1.setTaxRate(new BigDecimal("25.00"));
        order1.setProductType("Carpet");
        order1.setArea(new BigDecimal("60000.0"));
        order1.setCostPerSquareFoot(new BigDecimal("2.25"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order1.setMaterialCost(new BigDecimal("135000.00"));
        order1.setLaborCost(new BigDecimal("126000.00"));
        order1.setTax(new BigDecimal("65250.00"));
        order1.setTotal(new BigDecimal("326250.00"));
        
        FMOrder order2 = new FMOrder("Hoon Kim");
        order2.setOrderNumber(2);
        order2.setState("TX");
        order2.setTaxRate(new BigDecimal("15.00"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("62300.0"));
        order2.setCostPerSquareFoot(new BigDecimal("55.12"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.32"));
        order2.setMaterialCost(new BigDecimal("1300.00"));
        order2.setLaborCost(new BigDecimal("16000.00"));
        order2.setTax(new BigDecimal("650.00"));
        order2.setTotal(new BigDecimal("3250.00"));
        String path = "SampleFileData/UnitTesting/Orders_" + "UnitTest" + ".txt";
        testDao.addOrder(order1, path);
        testDao.addOrder(order2, path);
        
        List<FMOrder> allOrders = testDao.getAllOrder(path);
        
        assertNotNull(allOrders);
        assertEquals(2, allOrders.size());
        
        assertTrue(allOrders.contains(order1));
        assertTrue(allOrders.contains(order2));
    }
    
    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException {
        FMOrder order1 = new FMOrder("Bruce Lee");
        order1.setOrderNumber(1);
        order1.setState("CA");
        order1.setTaxRate(new BigDecimal("25.00"));
        order1.setProductType("Carpet");
        order1.setArea(new BigDecimal("60000.0"));
        order1.setCostPerSquareFoot(new BigDecimal("2.25"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order1.setMaterialCost(new BigDecimal("135000.00"));
        order1.setLaborCost(new BigDecimal("126000.00"));
        order1.setTax(new BigDecimal("65250.00"));
        order1.setTotal(new BigDecimal("326250.00"));
        
        FMOrder order2 = new FMOrder("Hoon Kim");
        order2.setOrderNumber(1);
        order2.setState("TX");
        order2.setTaxRate(new BigDecimal("15.00"));
        order2.setProductType("Tile");
        order2.setArea(new BigDecimal("62300.0"));
        order2.setCostPerSquareFoot(new BigDecimal("55.12"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("4.32"));
        order2.setMaterialCost(new BigDecimal("1300.00"));
        order2.setLaborCost(new BigDecimal("16000.00"));
        order2.setTax(new BigDecimal("650.00"));
        order2.setTotal(new BigDecimal("3250.00"));
        
        String path = "SampleFileData/UnitTesting/Orders_" + "UnitTest" + ".txt";
        testDao.addOrder(order1, path);
        FMOrder prevOrder = testDao.editOrder(order2, path);
        
        List<FMOrder> allOrders = testDao.getAllOrder(path);
        
        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        assertEquals(order1, prevOrder);
        assertTrue(allOrders.contains(order2));
        
    }
    
    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException {
        FMOrder order1 = new FMOrder("Bruce Lee");
        order1.setOrderNumber(1);
        order1.setState("CA");
        order1.setTaxRate(new BigDecimal("25.00"));
        order1.setProductType("Carpet");
        order1.setArea(new BigDecimal("60000.0"));
        order1.setCostPerSquareFoot(new BigDecimal("2.25"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order1.setMaterialCost(new BigDecimal("135000.00"));
        order1.setLaborCost(new BigDecimal("126000.00"));
        order1.setTax(new BigDecimal("65250.00"));
        order1.setTotal(new BigDecimal("326250.00"));
        String path = "SampleFileData/UnitTesting/Orders_" + "UnitTest" + ".txt";
        testDao.addOrder(order1, path);
        
        
        List<FMOrder> allOrders = testDao.getAllOrder(path);
        
        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        assertTrue(allOrders.contains(order1));
        
        FMOrder removedOrder = testDao.removeOrder(1, path);
        assertEquals(order1, removedOrder);
        
        allOrders = testDao.getAllOrder(path);

        assertTrue(allOrders.isEmpty());

        
    }
    
    @Test
    public void testExportFile() throws FlooringMasteryPersistenceException {
        String path = "SampleFileData/ExportTesting/DataExportTestForUnitTesting.txt";
        File file = new File(path);
//        assertFalse(file.exists());
        testDao.exportOrder(path);
        assertTrue(file.exists());
    }
    
    
}
