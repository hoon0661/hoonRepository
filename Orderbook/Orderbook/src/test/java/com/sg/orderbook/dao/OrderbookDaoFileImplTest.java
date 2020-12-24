/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Trade;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hoon
 */
public class OrderbookDaoFileImplTest {
    
    OrderbookDao testDao;
    
    public OrderbookDaoFileImplTest() {
    }
    
    
    @BeforeEach
    public void setUp() throws IOException, OrderbookPersistenceException {
        String testTrade = "testtrade.txt";
        String testOrders = "testorderbook.txt";
        new FileWriter(testTrade);
        new FileWriter(testOrders);
        testDao = new OrderbookDaoFileImpl(testTrade, testOrders);
    }
    

    @Test
    public void testInitialOrders() throws OrderbookPersistenceException {
        List<Order> buyOrders = testDao.getBuyOrderList();
        List<Order> sellOrders = testDao.getSellOrderList();
        
        assertEquals(buyOrders.size(), 1000);
        assertEquals(sellOrders.size(), 1000);
        
    }
    
    @Test
    public void testAddOrderAndSortAndGetOrder() throws OrderbookPersistenceException {
        Order buyOrder = new Order();
        buyOrder.setOrderId("TestBORD");
        buyOrder.setPrice(new BigDecimal("200"));
        buyOrder.setQuantity(100);
        testDao.addBuyOrder(buyOrder);
        
        Order sellOrder = new Order();
        sellOrder.setOrderId("TestSORD");
        sellOrder.setPrice(new BigDecimal("2000"));
        sellOrder.setQuantity(1000);
        testDao.addSellOrder(sellOrder);
        
        List<Order> buyOrders = testDao.getBuyOrderList();
        List<Order> sellOrders = testDao.getSellOrderList();

        assertEquals(buyOrders.get(0), buyOrder);
        assertEquals(sellOrders.get(0), sellOrder);
        
        Order retrievedBuyOrder = testDao.getBuyOrderById("TestBORD");
        Order retrievedSellOrder = testDao.getSellOrderById("TestSORD");
        
        assertEquals(buyOrder, retrievedBuyOrder);
        assertEquals(sellOrder, retrievedSellOrder);

    }
    
    @Test
    public void testRemoveOrder() throws OrderbookPersistenceException{
        Order buyOrder = new Order();
        buyOrder.setOrderId("TestBORD");
        buyOrder.setPrice(new BigDecimal("200"));
        buyOrder.setQuantity(100);
        testDao.addBuyOrder(buyOrder);
        
        Order sellOrder = new Order();
        sellOrder.setOrderId("TestSORD");
        sellOrder.setPrice(new BigDecimal("2000"));
        sellOrder.setQuantity(1000);
        testDao.addSellOrder(sellOrder);
        
        List<Order> buyOrders = testDao.getBuyOrderList();
        List<Order> sellOrders = testDao.getSellOrderList();
        
        assertEquals(buyOrders.size(), 1001);
        assertEquals(sellOrders.size(), 1001);
        
        Order removedBuyOrder = testDao.removeBuyOrder(buyOrder.getOrderId());
        Order removedSellOrder = testDao.removeSellOrder(sellOrder.getOrderId());
        
        assertEquals(buyOrders.size(), 1000);
        assertEquals(sellOrders.size(), 1000);
        
        assertNotNull(removedBuyOrder);
        assertNotNull(removedSellOrder);
        
        assertEquals(removedBuyOrder, buyOrder);
        assertEquals(removedSellOrder, sellOrder);
    }
    
    @Test
    public void testEditOrder() throws OrderbookPersistenceException{
        Order buyOrder = new Order();
        buyOrder.setOrderId("TestBORD");
        buyOrder.setPrice(new BigDecimal("200"));
        buyOrder.setQuantity(100);
        testDao.addBuyOrder(buyOrder);
        
        Order sellOrder = new Order();
        sellOrder.setOrderId("TestSORD");
        sellOrder.setPrice(new BigDecimal("2000"));
        sellOrder.setQuantity(1000);
        testDao.addSellOrder(sellOrder);
        
        Order newBuyOrderForEdit = new Order();
        newBuyOrderForEdit.setOrderId("TestBORD");
        newBuyOrderForEdit.setPrice(new BigDecimal("4000"));
        newBuyOrderForEdit.setQuantity(100000);
 
        Order newSellOrderForEdit = new Order();
        newSellOrderForEdit.setOrderId("TestSORD");
        newSellOrderForEdit.setPrice(new BigDecimal("5000"));
        newSellOrderForEdit.setQuantity(10000);

        Order prevBuyOrder = testDao.editBuyOrder(newBuyOrderForEdit);
        Order prevSellOrder = testDao.editSellOrder(newSellOrderForEdit);
        
        Order newBuyOrder = testDao.getBuyOrderById("TestBORD");
        Order newSellOrder = testDao.getSellOrderById("TestSORD");
        
        assertEquals(buyOrder, prevBuyOrder);
        assertEquals(sellOrder, prevSellOrder);
        
        assertEquals(newBuyOrderForEdit, newBuyOrder);
        assertEquals(newSellOrderForEdit, newSellOrder);

    }
    
    @Test
    public void testAddandGetTrade() throws OrderbookPersistenceException{
        Trade trade1 = new Trade();
        trade1.setExecutedPrice(new BigDecimal("1000"));
        trade1.setQuantityFilled(5000);
        testDao.addTrade(trade1);
        
        Trade trade2 = new Trade();
        trade2.setExecutedPrice(new BigDecimal("10000"));
        trade2.setQuantityFilled(50000);
        testDao.addTrade(trade2);
        
        List<Trade> allTrades = testDao.getAllTrades(); 
        assertEquals(allTrades.size(), 2);
        
        Trade retrievedTrade1 = testDao.getTrade("TRADE1");
        Trade retrievedTrade2 = testDao.getTrade("TRADE2");
        
        assertEquals(trade1.getExecutedPrice(), retrievedTrade1.getExecutedPrice());
        assertEquals(trade1.getQuantityFilled(), retrievedTrade1.getQuantityFilled()); 
        assertEquals(trade1.getExecutionTime(), retrievedTrade1.getExecutionTime());
        
        assertEquals(trade2.getExecutedPrice(), retrievedTrade2.getExecutedPrice());
        assertEquals(trade2.getQuantityFilled(), retrievedTrade2.getQuantityFilled()); 
        assertEquals(trade2.getExecutionTime(), retrievedTrade2.getExecutionTime());
    }
    
    @Test
    public void testGetAllOrdersAtOnce() throws OrderbookPersistenceException{
        List<List<Order>> allOrders = new ArrayList<>();
        allOrders = testDao.getAllOrders();
        assertEquals(allOrders.get(0).size(), 1000);
        assertEquals(allOrders.get(1).size(), 1000);
    }
    
    @Test
    public void testNewOrderId() throws OrderbookPersistenceException{
        String newBuyOrderId = testDao.newBuyOrderId();
        assertEquals(newBuyOrderId, "BORD1001");
        
        String newSellOrderId = testDao.newSellOrderId();
        assertEquals(newSellOrderId, "SORD1001");
    }
    
}
