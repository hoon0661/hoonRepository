/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

import com.sg.orderbook.dao.OrderbookAuditDao;
import com.sg.orderbook.dao.OrderbookDao;
import com.sg.orderbook.dao.OrderbookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Trade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author hoon
 */
@Component
public class OrderBookServiceLayerImpl implements OrderBookServiceLayer {
   
    private OrderbookDao dao;
    private OrderbookAuditDao auditDao;
    
    @Autowired
    public OrderBookServiceLayerImpl(OrderbookDao dao, OrderbookAuditDao auditDao) throws OrderbookPersistenceException {
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    @Override
    public Order addBuyOrder(Order order) throws OrderbookPersistenceException {
        return dao.addBuyOrder(order);
    }

    @Override
    public Order addSellOrder(Order order) throws OrderbookPersistenceException {
        return dao.addSellOrder(order);
    }
    
    @Override
    public Trade matchOneOrder() throws OrderbookPersistenceException, OrderbookEmptyListException {
        if(dao.getTopBuyOrder() == null){
            throw new OrderbookEmptyListException("Buy order list is empty.\n");
        }
        if(dao.getTopSellOrder() == null){
            throw new OrderbookEmptyListException("Sell order list is empty.\n");
        }
        
        Order buyOrder = dao.getTopBuyOrder();
        Order sellOrder = dao.getTopSellOrder();
        int buyOrderQuantity = buyOrder.getQuantity();
        int sellOrderQuantity = sellOrder.getQuantity();

        if(buyOrderQuantity <= sellOrderQuantity){
            return buyLessThanOrEqualToSell();
        }
        
        else{
            return buyGreaterThanSell();
        }
    }
    
    private Trade buyLessThanOrEqualToSell() throws OrderbookPersistenceException{
            Order buyOrder = dao.getTopBuyOrder();
            Order sellOrder = dao.getTopSellOrder();
            int buyOrderQuantity = buyOrder.getQuantity();
            int sellOrderQuantity = sellOrder.getQuantity();
            BigDecimal buyOrderPrice = buyOrder.getPrice();
            BigDecimal sellOrderPrice = sellOrder.getPrice();

            Trade trade = new Trade();
            BigDecimal tradePrice;
            int tradeQuantity;
            
            tradePrice = buyOrderPrice.add(sellOrderPrice).divide(new BigDecimal("2")).setScale(2, RoundingMode.HALF_EVEN);
            tradeQuantity = buyOrderQuantity;
            setTrade(trade, tradePrice, tradeQuantity); 

            if(buyOrderQuantity == sellOrderQuantity){
                fillFullBuyOrder(buyOrder);
                fillFullSellOrder(sellOrder);
            }

            else{
                fillPartialSellOrder(sellOrder, buyOrder);
            }
            trade.setTradeId(dao.tradeId());
            trade.setOrderStatus(2);
            dao.addTrade(trade);
            auditDao.writeAuditEntry(trade.getTradeId() + " has been processed.");
            return trade;
    }
    
    private Trade buyGreaterThanSell() throws OrderbookPersistenceException{  
        Order buyOrder = dao.getTopBuyOrder();
        Order sellOrder = dao.getTopSellOrder();
        int buyOrderQuantity = buyOrder.getQuantity();
        int sellOrderQuantity = sellOrder.getQuantity();
        BigDecimal buyOrderPrice = buyOrder.getPrice();
        BigDecimal sellOrderPrice = sellOrder.getPrice();
        
        Trade trade = new Trade();
        BigDecimal tradePrice = null;
        int tradeQuantity;
        
        List<BigDecimal> prices = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        int initialBuyOrderQuantity = buyOrderQuantity;
        BigDecimal buyTotalPrice = buyOrderPrice.multiply(new BigDecimal(Integer.toString(buyOrderQuantity)));


        while(buyOrderQuantity > sellOrderQuantity && !isSellOrderEmpty()){
            prices.add(sellOrder.getPrice());
            quantities.add(sellOrder.getQuantity());
            fillPartialBuyOrder(sellOrder, buyOrder);
            buyOrderQuantity = buyOrder.getQuantity();

            sellOrder = dao.getTopSellOrder();
            if(sellOrder == null){
                break;
            }
            sellOrderQuantity = sellOrder.getQuantity();
        }
        
        if(sellOrder != null){
            trade.setQuantityFilled(initialBuyOrderQuantity);
            trade.setOrderStatus(2);
            prices.add(sellOrder.getPrice());
            quantities.add(buyOrder.getQuantity());
            if(buyOrderQuantity == sellOrderQuantity){
                fillFullBuyOrder(buyOrder);
                fillFullSellOrder(sellOrder);
            }
            else{
                fillPartialSellOrder(sellOrder, buyOrder);
            }
        }

        else{   
            initialBuyOrderQuantity = 0;
            for(var quantity : quantities){                     
                initialBuyOrderQuantity += quantity;           
            }                                                 
            trade.setQuantityFilled(initialBuyOrderQuantity);  
            trade.setOrderStatus(1);                                                                           
        }

        tradePrice = calculateAvgPrice(buyTotalPrice, buyOrderPrice, tradePrice, prices, quantities, trade);
        trade.setExecutedPrice(tradePrice);
        trade.setTradeId(dao.tradeId());

        dao.addTrade(trade);   
        auditDao.writeAuditEntry(trade.getTradeId() + " has been processed.");

        return trade;
    }
    
    private BigDecimal calculateAvgPrice(BigDecimal buyTotalPrice, BigDecimal buyOrderPrice, BigDecimal tradePrice, List<BigDecimal> prices, List<Integer> quantities, Trade trade){
        BigDecimal sellTotalPrice = new BigDecimal("0");
        buyTotalPrice = buyOrderPrice.multiply(new BigDecimal(Integer.toString(trade.getQuantityFilled()))).setScale(2, RoundingMode.HALF_EVEN);
        for(int j = 0; j < prices.size(); j++){
            sellTotalPrice = sellTotalPrice.add(prices.get(j).multiply(new BigDecimal(Integer.toString(quantities.get(j)))));      
        }

        tradePrice = sellTotalPrice.add(buyTotalPrice).divide(new BigDecimal("2"), 2, RoundingMode.HALF_EVEN).divide(new BigDecimal(Integer.toString(trade.getQuantityFilled())), 2, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN);
        return tradePrice;
    }

    private void fillPartialSellOrder(Order sellOrder, Order buyOrder) throws OrderbookPersistenceException {
        dao.removeBuyOrder(buyOrder.getOrderId());
        int remainder = sellOrder.getQuantity() - buyOrder.getQuantity();
        sellOrder.setQuantity(remainder);
        dao.editSellOrder(sellOrder);
    }
    
    private void fillFullBuyOrder(Order buyOrder) throws OrderbookPersistenceException{
        dao.removeBuyOrder(buyOrder.getOrderId());
    }
    
    private void fillFullSellOrder(Order sellOrder) throws OrderbookPersistenceException{
        dao.removeSellOrder(sellOrder.getOrderId());
    }
    
    private void fillPartialBuyOrder(Order sellOrder, Order buyOrder) throws OrderbookPersistenceException {
        int remainder = buyOrder.getQuantity() - sellOrder.getQuantity();
        dao.removeSellOrder(sellOrder.getOrderId());
        buyOrder.setQuantity(remainder);
        dao.editBuyOrder(buyOrder);
    }
 
    @Override
    public List<Object> displayStats() throws OrderbookPersistenceException{
        int numOfBuyOrders = 0;
        int numOfSellOrders = 0;
        int overallBuyQuantity = 0;
        int overallSellQuantity = 0;
        BigDecimal avgBuyPrice = new BigDecimal("0");
        BigDecimal avgSellPrice = new BigDecimal("0");
        List<Object> stat = new ArrayList<>();

        
        if(isBuyOrderEmpty() && isSellOrderEmpty()){
            throw new OrderbookPersistenceException("No order exists.");
        }
        else if(isBuyOrderEmpty()){
            numOfBuyOrders = 0;
            overallBuyQuantity = 0;

            avgBuyPrice = new BigDecimal("0"); 
            List<Order> sellOrders = dao.getSellOrderList();
            numOfSellOrders = sellOrders.size();
            avgSellPrice = totalPrice(sellOrders, avgSellPrice);
            overallSellQuantity = totalQuantity(sellOrders, overallSellQuantity);
            avgSellPrice = avgSellPrice.divide(new BigDecimal(String.valueOf(numOfSellOrders)), 2, RoundingMode.HALF_EVEN);

        }
        else if(isSellOrderEmpty()){
            numOfSellOrders = 0;
            overallSellQuantity = 0;

            avgSellPrice = new BigDecimal("0");    
            List<Order> buyOrders = dao.getBuyOrderList();
            numOfBuyOrders = buyOrders.size();
            avgBuyPrice = totalPrice(buyOrders, avgBuyPrice);
            overallBuyQuantity = totalQuantity(buyOrders, overallBuyQuantity);
            avgBuyPrice = avgBuyPrice.divide(new BigDecimal(String.valueOf(numOfBuyOrders)), 2, RoundingMode.HALF_EVEN);
        }
        else{
            List<Order> buyOrders = dao.getBuyOrderList();
            List<Order> sellOrders = dao.getSellOrderList();

            numOfBuyOrders = buyOrders.size();
            numOfSellOrders = sellOrders.size();
            overallBuyQuantity = 0;
            overallSellQuantity = 0;
            avgSellPrice = new BigDecimal("0");
            avgBuyPrice = new BigDecimal("0");

            avgBuyPrice = totalPrice(buyOrders, avgBuyPrice);
            overallBuyQuantity = totalQuantity(buyOrders, overallBuyQuantity);
            avgSellPrice = totalPrice(sellOrders, avgSellPrice);
            overallSellQuantity = totalQuantity(sellOrders, overallSellQuantity);

            if(numOfBuyOrders == 0){
                avgBuyPrice = new BigDecimal("0");
            } else {
                avgBuyPrice = avgBuyPrice.divide(new BigDecimal(String.valueOf(numOfBuyOrders)), 2, RoundingMode.HALF_EVEN);
            } 
            if(numOfSellOrders == 0){
                avgSellPrice = new BigDecimal("0");
            } else{
                avgSellPrice = avgSellPrice.divide(new BigDecimal(String.valueOf(numOfSellOrders)), 2, RoundingMode.HALF_EVEN);
            }
        }

        stat.add(numOfBuyOrders);
        stat.add(numOfSellOrders);

        stat.add(overallBuyQuantity);
        stat.add(overallSellQuantity);

        stat.add(avgBuyPrice);
        stat.add(avgSellPrice);

        return stat;
        
    }
    
    private int totalQuantity(List<Order> orders, int quantity){
        for(int i = 0; i < orders.size(); i++){
            quantity += orders.get(i).getQuantity();
        }
        return quantity;
    }
    
    private BigDecimal totalPrice(List<Order> orders, BigDecimal price){
        for(int i = 0; i < orders.size(); i++){
            price = price.add(orders.get(i).getPrice());
        }
        return price;
    }
    
    @Override
    public Trade getTrade(String tradeId) throws OrderbookPersistenceException {
        return dao.getTrade(tradeId);
    }
    
    @Override
    public List<Trade> getAllTradesByExecutionTime() throws OrderbookPersistenceException {
        List<Trade> tradesList = dao.getAllTrades();
        
        Comparator<Trade> executionTimeComparator = (t1, t2) -> t1.getExecutionTime().compareTo(t2.getExecutionTime());
        List<Trade> tradesListSortedByExecutionTime = tradesList.stream()
                .sorted(executionTimeComparator)
                .collect(Collectors.toList());
        
        return tradesListSortedByExecutionTime;
    }
    
    @Override
    public List<Trade> getAllTradesByQuantity(int min, int max) throws OrderbookPersistenceException {
        List<Trade> tradesList = dao.getAllTrades();
        List<Trade> queriedList = new ArrayList<>();
        
        queriedList = tradesList.stream()
                .filter(t -> t.getQuantityFilled() >= min && t.getQuantityFilled() <= max)
                .collect(Collectors.toList());
        
        Comparator<Trade> quantityComparator = (t1, t2) -> ((Integer)t1.getQuantityFilled()).compareTo(t2.getQuantityFilled());
        List<Trade> queriedListByQuantity = queriedList.stream()
                .sorted(quantityComparator)
                .collect(Collectors.toList());
        
        return queriedListByQuantity;
    }
    
    @Override
    public List<Trade> getAllTradesByDateTime(LocalDateTime start, LocalDateTime end) throws OrderbookPersistenceException {
        List<Trade> tradesList = dao.getAllTrades();
        List<Trade> queriedList = new ArrayList<>();
        
        for(Trade currentTrade : tradesList) {
            LocalDateTime executionTime = currentTrade.getExecutionTime();
            if((executionTime.isEqual(start) || executionTime.isAfter(start)) && (executionTime.isEqual(end) || executionTime.isBefore(end))) {
               queriedList.add(currentTrade);
            }
        }
        
        Comparator<Trade> dateTimeComparator = (t1, t2) -> (t1.getExecutionTime().compareTo(t2.getExecutionTime()));
        
        List<Trade> queriedListByDateTime = queriedList.stream()
                .sorted(dateTimeComparator)
                .collect(Collectors.toList());
        
        return queriedListByDateTime;
    }
    
    @Override
    public boolean isBuyOrderEmpty() throws OrderbookPersistenceException{
        if(dao.getBuyOrderList() == null){
            return true;
        }
        return false;
    }

    @Override
    public boolean isSellOrderEmpty() throws OrderbookPersistenceException{
        if(dao.getSellOrderList() == null){
            return true;
        }
        return false;
    }
    
    @Override
    public Order removeBuyOrder(String orderId) {
        return dao.removeBuyOrder(orderId);
    }
    
    @Override
    public Order removeSellOrder(String orderId) {
        return dao.removeSellOrder(orderId);
    }
    
    @Override
    public Order getBuyOrder(String orderId) throws NoOrderExistsException {
        Order retrievedOrder = dao.getBuyOrderById(orderId);
        if(retrievedOrder != null) {
            return retrievedOrder;
        } else {
            throw new NoOrderExistsException("This buy order does not exist.");
        }
    }
    
    @Override
    public Order getSellOrder(String orderId) throws NoOrderExistsException {
        Order retrievedOrder = dao.getSellOrderById(orderId);
        if(retrievedOrder != null) {
            return retrievedOrder;
        } else {
            throw new NoOrderExistsException("This sell order does not exist.");
        }
    }
    
    @Override
    public Order editBuyOrder(Order order) throws OrderbookPersistenceException, NoOrderExistsException {
        Order editedBuyOrder = dao.editBuyOrder(order);
        if(editedBuyOrder != null) {
            return editedBuyOrder;
        } else {
            throw new NoOrderExistsException("The order you are trying to edit does not exist.");
        }
    }
    
    @Override
    public Order editSellOrder(Order order) throws OrderbookPersistenceException, NoOrderExistsException {
        Order editedSellOrder = dao.editSellOrder(order);
        if(editedSellOrder != null) {
            return editedSellOrder;
        } else {
            throw new NoOrderExistsException("The order you are trying to edit does not exist.");
        }
    }

    @Override
    public List<List<Order>> getAllOrders() throws OrderbookPersistenceException{
       return dao.getAllOrders();
    }
    
    private void setTrade(Trade trade, BigDecimal price, int quantity){
        trade.setExecutedPrice(price);
        trade.setQuantityFilled(quantity);
    }
 
    @Override
    public String newBuyOrderId() throws OrderbookPersistenceException{
        return dao.newBuyOrderId();
    }
    
    @Override
    public String newSellOrderId() throws OrderbookPersistenceException{
        return dao.newSellOrderId();
    }
    
    @Override
    public void setAnotherOrderBook(String orderBook) throws OrderbookPersistenceException{
        dao.setAnotherOrderBook(orderBook);
    }

    @Override
    public String getOrderBookName() {
        return dao.getOrderBookName();
    }
        
}
