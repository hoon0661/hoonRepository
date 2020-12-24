/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Trade;
import java.util.List;

/**
 *
 * @author hoon
 */
public interface OrderbookDao {
    
    List<Order> getBuyOrderList() throws OrderbookPersistenceException;
    List<Order> getSellOrderList() throws OrderbookPersistenceException;
    Order getTopBuyOrder() throws OrderbookPersistenceException;
    Order getTopSellOrder() throws OrderbookPersistenceException;
    Order getBuyOrderById(String orderId);
    Order getSellOrderById(String orderId);
    
    Order editBuyOrder(Order newOrder) throws OrderbookPersistenceException;
    Order editSellOrder(Order newOrder) throws OrderbookPersistenceException;

    Order addBuyOrder(Order order) throws OrderbookPersistenceException;
    Order addSellOrder(Order order) throws OrderbookPersistenceException;
    
    Order removeSellOrder(String orderId);
    Order removeBuyOrder(String orderId);
    
    Trade addTrade(Trade trade) throws OrderbookPersistenceException;
    Trade getTrade(String tradeId) throws OrderbookPersistenceException;
    List<Trade> getAllTrades() throws OrderbookPersistenceException;
    
    List<List<Order>> getAllOrders() throws OrderbookPersistenceException;
    
    String newBuyOrderId() throws OrderbookPersistenceException;
    String newSellOrderId() throws OrderbookPersistenceException;
    
    String tradeId() throws OrderbookPersistenceException;
    
    void setAnotherOrderBook(String orderBook) throws OrderbookPersistenceException;
    String getOrderBookName();
    
}
