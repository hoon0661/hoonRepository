/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.service;

import com.hoon.flooringmastery.dao.FlooringMasteryDao;
import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.dto.FMOrder;
import java.math.BigDecimal;
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
public class FlooringMasteryServiceLayerImplTest {
    
    private FlooringMasteryServiceLayer service;
    
    public FlooringMasteryServiceLayerImplTest() throws FlooringMasteryPersistenceException {
        FlooringMasteryDao dao = new FlooringMasteryDaoStubImpl();
        service = new FlooringMasteryServiceLayerImpl(dao);
    }
    
//    @BeforeAll
//    public static void setUpClass() {
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//    }
//    
//    @BeforeEach
//    public void setUp() {
//    }
//    
//    @AfterEach
//    public void tearDown() {
//    }

    @Test
    public void testGetAllOrders() throws FlooringMasteryPersistenceException {
        FMOrder order = new FMOrder("Hoon Kim");
        order.setOrderNumber(1);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal(100.0));
        order.setCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(new BigDecimal("175.00"));
        order.setLaborCost(new BigDecimal("210.00"));
        order.setTax(new BigDecimal("17.13"));
        order.setTotal(new BigDecimal("402.13"));
        
        assertEquals(1, service.getAllOrder("").size());
        
        assertTrue(service.getAllOrder("").contains(order));
    }
    
    @Test
    public void testGetOrder() throws FlooringMasteryPersistenceException {
        FMOrder order = new FMOrder("Hoon Kim");
        order.setOrderNumber(1);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal(100.0));
        order.setCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(new BigDecimal("175.00"));
        order.setLaborCost(new BigDecimal("210.00"));
        order.setTax(new BigDecimal("17.13"));
        order.setTotal(new BigDecimal("402.13"));
        
        FMOrder methodOrder = service.getOrder(1, "");
        assertNotNull(methodOrder);
        assertEquals(order, methodOrder);
        
        FMOrder shouldBeNull = service.getOrder(2, "");
        assertNull(shouldBeNull);
    }
    
    @Test
    public void testAddOrder() throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException{
        FMOrder order = new FMOrder("Bruce Lee");
        order.setOrderNumber(1);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal(100.0));
        
        FMOrder AddedOrder = service.addOrder(order, "");
        assertNotNull(AddedOrder);
        assertEquals(order.getOrderNumber(), AddedOrder.getOrderNumber());
        
        assertTrue(AddedOrder.getCustomerName() == "Hoon Kim");
        FMOrder shouldBeNull = service.getOrder(2, "");
        assertNull(shouldBeNull);
        
    }
    
    @Test
    public void testEditOrder() throws FlooringMasteryPersistenceException, FlooringMasteryNonExistOrderNumberException, FlooringMasteryDataValidationException{
        FMOrder order = new FMOrder("Bruce Lee");
        order.setOrderNumber(1);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal(100.0));
        
        FMOrder edittedOrder = service.editOrder(1, order, "");
        
        assertNotNull(edittedOrder);
        assertEquals(order.getOrderNumber(), edittedOrder.getOrderNumber());
        
        assertTrue(edittedOrder.getCustomerName() == "Hoon Kim");
        FMOrder shouldBeNull = service.getOrder(2, "");
        assertNull(shouldBeNull);
    }
    
    @Test
    public void testRemoveOrder() throws FlooringMasteryPersistenceException {
        FMOrder order = new FMOrder("Hoon Kim");
        order.setOrderNumber(1);
        order.setState("TX");
        order.setTaxRate(new BigDecimal("4.45"));
        order.setProductType("Laminate");
        order.setArea(new BigDecimal(100.0));
        order.setCostPerSquareFoot(new BigDecimal("1.75"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(new BigDecimal("175.00"));
        order.setLaborCost(new BigDecimal("210.00"));
        order.setTax(new BigDecimal("17.13"));
        order.setTotal(new BigDecimal("402.13"));
        
        FMOrder shouldBeHoon = service.removeOrder(1, "");
        assertNotNull(shouldBeHoon);
        assertEquals(order, shouldBeHoon);
        
        FMOrder shouldBeNull = service.removeOrder(2, "");
        assertNull(shouldBeNull);
    }
}
