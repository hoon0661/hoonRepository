/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

import com.sg.orderbook.dao.OrderbookAuditDao;
import com.sg.orderbook.dao.OrderbookDao;
import com.sg.orderbook.dao.OrderbookPersistenceException;
import com.sg.orderbook.dto.Trade;
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
public class OrderBookServiceLayerImplTest {
    OrderBookServiceLayer testService;
    public OrderBookServiceLayerImplTest() throws OrderbookPersistenceException {
        OrderbookDao dao = new OrderbookDaoStubImpl();
        OrderbookAuditDao auditDao = new OrderbookAuditDaoStubImpl();
        testService = new OrderBookServiceLayerImpl(dao, auditDao);
    }
    
    @BeforeEach
    public void setUp() {
    }
    


    @Test
    public void testMatchOneOrderAndOrderEmpty() throws OrderbookPersistenceException, OrderbookEmptyListException {
        assertFalse(testService.isBuyOrderEmpty());
        assertFalse(testService.isSellOrderEmpty());
        
        Trade trade1 = testService.matchOneOrder();
        assertEquals(trade1.getExecutedPrice(), new BigDecimal("190.88"));
        assertEquals(trade1.getQuantityFilled(), 20);
        assertEquals(trade1.getTradeId(), "TRADE1");
        assertFalse(testService.isBuyOrderEmpty());
        assertFalse(testService.isSellOrderEmpty());
        
        Trade trade2 = testService.matchOneOrder();
        assertEquals(trade2.getExecutedPrice(), new BigDecimal("190.56"));
        assertEquals(trade2.getQuantityFilled(), 45);
        assertEquals(trade2.getTradeId(), "TRADE2");
        assertFalse(testService.isBuyOrderEmpty());
        assertFalse(testService.isSellOrderEmpty());
        
        Trade trade3 = testService.matchOneOrder();
        assertEquals(trade3.getExecutedPrice(), new BigDecimal("190.15"));
        assertEquals(trade3.getQuantityFilled(), 25);
        assertEquals(trade3.getTradeId(), "TRADE3");
        assertFalse(testService.isBuyOrderEmpty());
        assertTrue(testService.isSellOrderEmpty());
    }
    
    @Test
    public void testDisplayStat() throws OrderbookPersistenceException{
        List<Object> stat = new ArrayList<>();
        stat = testService.displayStats();
        assertEquals(stat.size(), 6);
        assertEquals(stat.get(0), 3);
        assertEquals(stat.get(1), 3);
        assertEquals(stat.get(2), 95);
        assertEquals(stat.get(3), 90);
        assertEquals(stat.get(4), new BigDecimal("190.50"));
        assertEquals(stat.get(5), new BigDecimal("190.50"));
    }
    
    @Test
    public void testGetAllTradesByExecutionTime() throws OrderbookPersistenceException, OrderbookEmptyListException{
        List<Trade> trades = new ArrayList<>();
        
        testService.matchOneOrder();
        testService.matchOneOrder();  
        testService.matchOneOrder();
        
        trades = testService.getAllTradesByExecutionTime();
        
        System.out.println(trades.size());
        for(int i = 0; i < trades.size() - 1; i++){
            assertTrue(trades.get(i).getExecutionTime().isBefore(trades.get(i+1).getExecutionTime()) || trades.get(i).getExecutionTime().isEqual(trades.get(i+1).getExecutionTime()));
        }
    }
    
    @Test
    public void testGellAllTradesByQuantity() throws OrderbookPersistenceException, OrderbookEmptyListException{
        List<Trade> trades = new ArrayList<>();
        
        testService.matchOneOrder();
        testService.matchOneOrder();  
        testService.matchOneOrder();
        
        trades = testService.getAllTradesByQuantity(25, 50);
        assertEquals(trades.size(), 2);
        assertEquals(trades.get(0).getQuantityFilled(), 25);
        assertEquals(trades.get(1).getQuantityFilled(), 45);
        
    }

}
