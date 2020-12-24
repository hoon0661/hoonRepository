/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.service;

import com.hoon.vendingmachine.dao.VendingMachineAuditDao;
import com.hoon.vendingmachine.dao.VendingMachineAuditDaoFileImpl;
import com.hoon.vendingmachine.dao.VendingMachineDao;
import com.hoon.vendingmachine.dao.VendingMachineDaoFileImpl;
import com.hoon.vendingmachine.dto.Item;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Hoon
 */
public class VendingMachineServiceLayerImplTest {
//    VendingMachineDao testDao;
//    VendingMachineAuditDao testAuditDao;
    VendingMachineServiceLayer service;
    public VendingMachineServiceLayerImplTest() {
        VendingMachineDao dao = new VendingMachineDaoStubImpl();
        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();    
        service = new VendingMachineServiceLayerImpl(dao, auditDao);
    }
//    @BeforeEach
//    public void setUp() {
//        String testFile = "testVendingMachine.txt";
////      new FileWriter(testFile);
////      testDao = new VendingMachineDaoFileImpl(testFile);
//        testDao = new VendingMachineDaoFileImpl(testFile);
//        testAuditDao = new VendingMachineAuditDaoFileImpl();
//        testService = new VendingMachineServiceLayerImpl(testDao, testAuditDao); 
//    }
    
    @BeforeEach
    public void setUP() {
        
    }
    
    @Test
    public void testVendItem() throws Exception{
        double refund = service.vendItem("cheetos", 3);
        assertTrue(refund == 2.0);
        assertTrue(service.getItem("cheetos").getQuantity() == 9);
//        assertTrue(service.getItem("cheetos").getQuantity() == 2);
    }

//    @Test()
//    public void testVend1() throws Exception{
//        String name1 = "cheetos";
//        double money1 = 0.5;
//        InsufficientFundsException exception1 = assertThrows(InsufficientFundsException.class, () -> {testService.vendItem(name1, money1);});
//        assertEquals("Error, Insufficient fund, you balance is $" + money1, exception1.getMessage());
//        assertNotEquals("Error, this item is not available", exception1.getMessage());
//        assertNotEquals("Could not load inventory data into memory.", exception1.getMessage());
//        
//        double money2 = 100;
//        assertDoesNotThrow(() -> testService.vendItem(name1, money2)); 
//        assertEquals(98, testDao.getItem(name1).getQuantity());
//              
//        //====================================================================//
//        
//        String name2 = "snickers";
//        NoItemInventoryException exception2 = assertThrows(NoItemInventoryException.class, () -> {testService.vendItem(name2, money2);});
//        assertNotEquals("Error, Insufficient fund, you balance is $" + money2, exception2.getMessage());
//        assertEquals("Error, this item is not available", exception2.getMessage());
//        assertNotEquals("Could not load inventory data into memory.", exception2.getMessage());
//        
//        String name3 = "lays";
//        assertDoesNotThrow(() -> testService.vendItem(name3, money2));
//        assertEquals(5, testDao.getItem(name3).getQuantity());
//        
//    }
//    
    @Test
    public void testChange() {
        
        List<Integer> expectedChangeOfList = new ArrayList<>();
        
        expectedChangeOfList.add(12);
        expectedChangeOfList.add(1);
        expectedChangeOfList.add(1);
        expectedChangeOfList.add(2);
        
        int[] arrExpected = new int[expectedChangeOfList.size()]; 
        for (int i =0; i < expectedChangeOfList.size(); i++){
            arrExpected[i] = expectedChangeOfList.get(i);
        } 
            
        double money = 3.17;
        List<Integer> methodChangeOfList = new ArrayList<>();
        methodChangeOfList = service.change(money);
        
        int[] arrMethod = new int[methodChangeOfList.size()]; 
        for (int i =0; i < methodChangeOfList.size(); i++){
            arrMethod[i] = methodChangeOfList.get(i);
        } 
             
        assertArrayEquals(arrExpected, arrMethod);
    }   
}
