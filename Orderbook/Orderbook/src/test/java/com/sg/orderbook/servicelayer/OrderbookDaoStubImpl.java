/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

import com.sg.orderbook.dao.OrderbookDao;
import com.sg.orderbook.dao.OrderbookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Trade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author hoon
 */
public class OrderbookDaoStubImpl implements OrderbookDao {
    public List<Order> buyOrders;
    public List<Order> sellOrders;
    public Map<String, Trade> trades;
    
    public OrderbookDaoStubImpl(){
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
        trades = new HashMap<>();
        Order buyOrder1 = new Order("BORD1", new BigDecimal("191.00"), 20);
        Order buyOrder2 = new Order("BORD2", new BigDecimal("190.50"), 45);
        Order buyOrder3 = new Order("BORD3", new BigDecimal("190.00"), 30);
        
        Order sellOrder1 = new Order("SORD1", new BigDecimal("190.75"), 40);
        Order sellOrder2 = new Order("SORD2", new BigDecimal("190.50"), 30);
        Order sellOrder3 = new Order("SORD3", new BigDecimal("190.25"), 20);
        
        buyOrders.add(buyOrder1);
        buyOrders.add(buyOrder2);
        buyOrders.add(buyOrder3);
        
        sellOrders.add(sellOrder1);
        sellOrders.add(sellOrder2);
        sellOrders.add(sellOrder3);
        
        buyOrders = sortOrders(buyOrders);
        sellOrders = sortOrders(sellOrders);
        
        trades = new HashMap<>();
    }

    @Override
    public List<Order> getBuyOrderList() throws OrderbookPersistenceException {
        if(buyOrders.isEmpty()){
            return null;
        }
        return buyOrders;
    }

    @Override
    public List<Order> getSellOrderList() throws OrderbookPersistenceException {
        if(sellOrders.isEmpty()){
            return null;
        }
        return sellOrders;
    }

    @Override
    public Order getTopBuyOrder() throws OrderbookPersistenceException {
        if(buyOrders.isEmpty()){
            return null;
        }
        return buyOrders.get(0);
    }

    @Override
    public Order getTopSellOrder() throws OrderbookPersistenceException {
        if(sellOrders.isEmpty()){
            return null;
        }
        return sellOrders.get(0);
    }

    @Override
    public Order getBuyOrderById(String orderId) {
        for(int i = 0; i < buyOrders.size(); i++) {
            if(buyOrders.get(i).getOrderId().equals(orderId)) {
                return buyOrders.get(i);
            }
        }
        return null;
    }

    @Override
    public Order getSellOrderById(String orderId) {
        for(int i = 0; i < sellOrders.size(); i++) {
            if(sellOrders.get(i).getOrderId().equals(orderId)) {
                return sellOrders.get(i);
            }
        }
        return null;
    }

    @Override
    public Order editBuyOrder(Order newOrder) throws OrderbookPersistenceException {
        Order prevOrder = null;
        for(Order oldOrder : buyOrders){
            if(oldOrder.getOrderId().equals(newOrder.getOrderId())){
                prevOrder = buyOrders.set(buyOrders.indexOf(oldOrder), newOrder);
            }
        }
        buyOrders = sortOrders(buyOrders);
        return prevOrder;
    }

    @Override
    public Order editSellOrder(Order newOrder) throws OrderbookPersistenceException {
        Order prevOrder = null;
        for(Order oldOrder : sellOrders){
            if(oldOrder.getOrderId().equals(newOrder.getOrderId())){
                prevOrder = sellOrders.set(sellOrders.indexOf(oldOrder), newOrder);
            }
        }
        sellOrders = sortOrders(sellOrders);

        return prevOrder;
    }

    private List<Order> sortOrders(List<Order> orders) {
        Comparator<Order> priceComparator = (o1, o2) -> o1.getPrice().compareTo(o2.getPrice());
        
        List<Order> sortedOrders = orders.stream()
                .sorted(priceComparator.reversed())
                .collect(Collectors.toList());
        
        return sortedOrders;
    }
    
    @Override
    public Order addBuyOrder(Order order) throws OrderbookPersistenceException {
        if(buyOrders.add(order)){
            buyOrders = sortOrders(buyOrders);
            
            return order;
        }         
        return null;
    }

    @Override
    public Order addSellOrder(Order order) throws OrderbookPersistenceException {
        if(sellOrders.add(order)){
            sellOrders = sortOrders(sellOrders);
            return order;
        }         
        return null;
    }

    @Override
    public Order removeSellOrder(String orderId) {
        Order removedOrder;
        for(int i = 0; i < sellOrders.size(); i++){
            if(sellOrders.get(i).getOrderId().equals(orderId)){
                removedOrder = sellOrders.get(i);
                sellOrders.remove(i);
                return removedOrder;
            }
        }
        return null;
    }

    @Override
    public Order removeBuyOrder(String orderId) {
        Order removedOrder;
        for(int i = 0; i < buyOrders.size(); i++){
            if(buyOrders.get(i).getOrderId().equals(orderId)){
                removedOrder = buyOrders.get(i);
                buyOrders.remove(i);
                return removedOrder;
            }
        }     
        return null;
    }

    @Override
    public Trade addTrade(Trade trade) throws OrderbookPersistenceException {
        trade.setTradeId(tradeId());
        Trade prevTrade = trades.put(trade.getTradeId(), trade);
        return prevTrade;
    }

    @Override
    public Trade getTrade(String tradeId) throws OrderbookPersistenceException {
        return trades.get(tradeId);
    }

    @Override
    public List<Trade> getAllTrades() throws OrderbookPersistenceException {
        return new ArrayList<Trade>(trades.values());
    }

    @Override
    public List<List<Order>> getAllOrders() throws OrderbookPersistenceException {
        List<List<Order>> allOrders = new ArrayList<>();
        allOrders.add(getBuyOrderList());
        allOrders.add(getSellOrderList());
        return allOrders;
    }

    @Override
    public String newBuyOrderId() throws OrderbookPersistenceException {
        String id;
        try {
            id = "BORD" + setBuyOrderNumber();
        } catch (OrderbookPersistenceException ex) {
            throw new OrderbookPersistenceException("Cannot Open orderbook.");
        }
//        buyOrderCount++;
        return id;
    }
    
    private String setBuyOrderNumber() throws OrderbookPersistenceException{
        if(buyOrders.isEmpty()){
            return "1";
        } else {
            int max = getMaxNum(buyOrders);
            return Integer.toString(max + 1);
        }
    }
    
    private int getMaxNum(List<Order> orders){
        int max = 0;
        String id;
        String strNum;
        int num;
        for(Order order : orders){
            id = order.getOrderId();
            strNum = id.substring(4);
            num = Integer.parseInt(strNum);
            if(num > max){
                max = num;
            }
        }
        return max;
    }

    @Override
    public String newSellOrderId() throws OrderbookPersistenceException {
        String id;
        try {
            id = "SORD" + setSellOrderNumber();
        } catch (OrderbookPersistenceException ex) {
            throw new OrderbookPersistenceException("Cannot Open orderbook.");
        }
//        sellOrderCount++;
        return id;
    }
    
    private String setSellOrderNumber() throws OrderbookPersistenceException{
        if(sellOrders.isEmpty()){
            return "1";
        } else {
            int max = getMaxNum(sellOrders); 
            return Integer.toString(max + 1);
        }
    }

    @Override
    public String tradeId() throws OrderbookPersistenceException {
        return "TRADE" + tradeNumber();
    }
    
    private int tradeNumber() throws OrderbookPersistenceException{        
        return trades.isEmpty() ? 1 : trades.size() + 1;
    }

    @Override
    public void setAnotherOrderBook(String orderBook) throws OrderbookPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getOrderBookName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
