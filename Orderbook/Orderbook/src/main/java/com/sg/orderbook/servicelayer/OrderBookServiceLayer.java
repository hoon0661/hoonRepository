/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

import com.sg.orderbook.dao.OrderbookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Trade;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author hoon
 */
public interface OrderBookServiceLayer {

    Trade matchOneOrder() throws OrderbookPersistenceException, OrderbookEmptyListException;
    List<Object> displayStats() throws OrderbookPersistenceException;
    Order addBuyOrder(Order order) throws OrderbookPersistenceException;
    Order addSellOrder(Order order) throws OrderbookPersistenceException;
    Order removeBuyOrder(String orderId);
    Order removeSellOrder(String orderId);
    Order editBuyOrder(Order order) throws OrderbookPersistenceException, NoOrderExistsException;
    Order editSellOrder(Order order) throws OrderbookPersistenceException, NoOrderExistsException;
    Order getBuyOrder(String orderId) throws NoOrderExistsException;
    Order getSellOrder(String orderId) throws NoOrderExistsException;
    Trade getTrade(String tradeId) throws OrderbookPersistenceException;
    List<Trade> getAllTradesByExecutionTime() throws OrderbookPersistenceException;
    List<Trade> getAllTradesByQuantity(int min, int max) throws OrderbookPersistenceException;
    List<Trade> getAllTradesByDateTime(LocalDateTime start, LocalDateTime end) throws OrderbookPersistenceException;
    boolean isBuyOrderEmpty() throws OrderbookPersistenceException;
    boolean isSellOrderEmpty() throws OrderbookPersistenceException;
    List<List<Order>> getAllOrders() throws OrderbookPersistenceException;
    String newBuyOrderId() throws OrderbookPersistenceException;
    String newSellOrderId() throws OrderbookPersistenceException;
    void setAnotherOrderBook(String orderBook) throws OrderbookPersistenceException;
    String getOrderBookName();
        
}
