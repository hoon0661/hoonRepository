/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Orderbook;
import com.sg.orderbook.dto.Trade;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author hoon
 */
@Component
public class OrderbookDaoFileImpl implements OrderbookDao {
    
    private List<Order> buyOrders;
    private List<Order> sellOrders;

    private Map<String, Order> buyOrdersAsMap;
    private Map<String, Order> sellOrdersAsMap;
    private Map<String, Trade> trades = new HashMap<>();
    
    public final String TRADE_FILE;
    public String ORDER_FILE;
    public String DELIMITER = "|";

    
    private int initialBuyOrderMaxNum;
    private int initialSellOrderMaxNum;

    public OrderbookDaoFileImpl() throws OrderbookPersistenceException {

        TRADE_FILE = "trade.txt";
        ORDER_FILE = "orderbook.txt";
        Orderbook newOrderbook = new Orderbook();
        buyOrders = newOrderbook.getBuyOrders();
        sellOrders = newOrderbook.getSellOrders();
        buyOrders = sortOrders(buyOrders);
        sellOrders = sortOrders(sellOrders);
        initialBuyOrderMaxNum = getMaxNum(buyOrders);
        initialSellOrderMaxNum = getMaxNum(sellOrders);
        writeOrders();
    }
    
    public OrderbookDaoFileImpl(String tradeTextFile, String orderTextFile) throws OrderbookPersistenceException {

        TRADE_FILE = tradeTextFile;
        ORDER_FILE = orderTextFile;
        Orderbook newOrderbook = new Orderbook();
        buyOrders = newOrderbook.getBuyOrders();
        sellOrders = newOrderbook.getSellOrders();
        buyOrders = sortOrders(buyOrders);
        sellOrders = sortOrders(sellOrders);
        initialBuyOrderMaxNum = getMaxNum(buyOrders);
        initialSellOrderMaxNum = getMaxNum(sellOrders);
        writeOrders();
    }
    
    @Override
    public List<Order> getBuyOrderList() throws OrderbookPersistenceException{
        loadOrders();
        
        if(buyOrders.isEmpty()){
            return null;
        }
        return buyOrders;
    }
        
    @Override
    public List<Order> getSellOrderList() throws OrderbookPersistenceException{
        loadOrders();
        if(sellOrders.isEmpty()){
            return null;
        }
        return sellOrders;
    }
    
    @Override
    public Order getTopBuyOrder() throws OrderbookPersistenceException{
        loadOrders();
        if(buyOrders.isEmpty()){
            return null;
        }
        return buyOrders.get(0);
    }
    
    @Override
    public Order getTopSellOrder() throws OrderbookPersistenceException{
        loadOrders();
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
    public Order editBuyOrder(Order newOrder) throws OrderbookPersistenceException{
        loadOrders();

        Order prevOrder = null;
        for(Order oldOrder : buyOrders){
            if(oldOrder.getOrderId().equals(newOrder.getOrderId())){
                prevOrder = buyOrders.set(buyOrders.indexOf(oldOrder), newOrder);
            }
        }
        buyOrders = sortOrders(buyOrders);
        writeOrders();
        return prevOrder;
    }

    @Override
    public Order editSellOrder(Order newOrder) throws OrderbookPersistenceException{
        loadOrders();

        Order prevOrder = null;
        for(Order oldOrder : sellOrders){
            if(oldOrder.getOrderId().equals(newOrder.getOrderId())){
                prevOrder = sellOrders.set(sellOrders.indexOf(oldOrder), newOrder);
            }
        }
        sellOrders = sortOrders(sellOrders);
        writeOrders();

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
            writeOrders();
            
            return order;
        }         
        return null;
    }

    @Override
    public Order addSellOrder(Order order) throws OrderbookPersistenceException {
        if(sellOrders.add(order)){
            sellOrders = sortOrders(sellOrders);
            writeOrders();
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
                try {
                    writeOrders();
                } catch (OrderbookPersistenceException ex) {
                    Logger.getLogger(OrderbookDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                try {
                    writeOrders();
                } catch (OrderbookPersistenceException ex) {
                    Logger.getLogger(OrderbookDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                return removedOrder;
            }
        }     
        return null;
    }
    
    @Override
    public Trade addTrade(Trade trade) throws OrderbookPersistenceException {
        trade.setTradeId(tradeId());
        Trade prevTrade = trades.put(trade.getTradeId(), trade);
        writeTrade();
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
    
    private String marshallTrade(Trade aTrade) {
        String delimiter = "";
        char[] delimiterArr = DELIMITER.toCharArray();
        for(var ch : delimiterArr) {
            if(ch != '\\') {
                delimiter = String.valueOf(ch);
            }
        }
        DELIMITER = delimiter;
        
        String tradeAsText = "35=8" + DELIMITER;
        tradeAsText += "52=" + aTrade.getExecutionTime().format(DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS")).toString() + DELIMITER;
        tradeAsText += "6=" + aTrade.getExecutedPrice() + DELIMITER;
        tradeAsText += "14=" + aTrade.getQuantityFilled() + DELIMITER;
        tradeAsText += "20=0" + DELIMITER;
        tradeAsText += "37=" + aTrade.getTradeId() + DELIMITER;
        tradeAsText += "39=" + aTrade.getOrderStatus();
        return tradeAsText;
    }
    
    private void writeTrade() throws OrderbookPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(TRADE_FILE));
        } catch (FileNotFoundException e) {
            throw new OrderbookPersistenceException("Could not load data into file, trade.txt file does not exist.", e);
        } catch (IOException e) {
            throw new OrderbookPersistenceException("Could not write to file.", e);
        }
        out.flush();
        
        try {
            out = new PrintWriter(new FileWriter(TRADE_FILE));
        } catch (FileNotFoundException e) {
            throw new OrderbookPersistenceException("Could not load trade data into file; trade.txt file does not exist.", e);
        } catch (IOException e) {
            throw new OrderbookPersistenceException("Could not write to file.", e);
        }
        for(Map.Entry<String,Trade> currentTrade : trades.entrySet()) {
            out.println(marshallTrade(currentTrade.getValue()));
            out.flush();
        }
        out.close();
    }
    
    private String marshallOrder(Order order) {

        String orderAsText = 37 + "=" + order.getOrderId() + DELIMITER;
        orderAsText += 44 + "=" + order.getPrice() + DELIMITER;
        orderAsText += 38 + "=" + order.getQuantity() + DELIMITER;
        if(order.getOrderId().contains("BORD")) {
            orderAsText += 54 + "=" + 1;
        } else {
            orderAsText += 54 + "=" + 2;
        }
        return orderAsText;
    }
    
    private Order unmarshallOrder(String orderAsText){
        
        Order tempOrder = new Order("***");
        String[] orderTokens = orderAsText.split(DELIMITER);
        
        for(var token : orderTokens){
          String[] tokenArray = token.split("=");
          if(tokenArray[0].equals("37")) {
              tempOrder.setOrderId(tokenArray[1]);
          }
          if(tokenArray[0].equals("44")) {
              tempOrder.setPrice(new BigDecimal(tokenArray[1]));
          }
          if(tokenArray[0].equals("38")) {
              tempOrder.setQuantity(Integer.parseInt(tokenArray[1]));
          }
          if(tokenArray[0].equals("54")) {
              tempOrder.setSide(Integer.parseInt(tokenArray[1]));
          }
        }
        return tempOrder;
    }
    
    private void loadOrders() throws OrderbookPersistenceException {
        buyOrders.clear();
        sellOrders.clear();
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderbookPersistenceException("Could not load trade data into memory.", e);
        }
        String currentLine;
        Order currentOrder;
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            DELIMITER = findDelimiter(currentLine);
            currentOrder = unmarshallOrder(currentLine);
            if(currentOrder.getSide() == 1){
                buyOrders.add(currentOrder);
            } else {
                sellOrders.add(currentOrder);
            }
        }
        scanner.close();
    }

    private void writeOrders() throws OrderbookPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (FileNotFoundException e) {
            throw new OrderbookPersistenceException("Could not load trade data into file.", e);
        } catch (IOException e) {
            throw new OrderbookPersistenceException("Could not write to file.", e);
        }
        
        String delimiter = "";
        char[] delimiterArr = DELIMITER.toCharArray(); 
        for(var ch : delimiterArr){
            if(ch != '\\'){
                delimiter = String.valueOf(ch);
            }
        }
        DELIMITER = delimiter;
        for(Order order : buyOrders) {
            out.println(marshallOrder(order));
            out.flush();
        }
        for(Order order : sellOrders) {
            out.println(marshallOrder(order));
            out.flush();
        }
    }
    
    @Override
    public List<List<Order>> getAllOrders() throws OrderbookPersistenceException {
        List<List<Order>> allOrders = new ArrayList<>();
        allOrders.add(getBuyOrderList());
        allOrders.add(getSellOrderList());
        return allOrders;
    }
    
    private int tradeNumber() throws OrderbookPersistenceException {        
        return trades.isEmpty() ? 1 : trades.size() + 1;
    }
    
    @Override
    public String tradeId() throws OrderbookPersistenceException {
        return "TRADE" + tradeNumber();
    }
        
    @Override
    public String newBuyOrderId() throws OrderbookPersistenceException {
        String id = null;
        try {
            id = "BORD" + setBuyOrderNumber();
        } catch (OrderbookPersistenceException ex) {
            throw new OrderbookPersistenceException("Cannot Open orderbook.");
        }
        return id;
    }
    
    @Override
    public String newSellOrderId() throws OrderbookPersistenceException {
        String id = null;
        try {
            id = "SORD" + setSellOrderNumber();
        } catch (OrderbookPersistenceException ex) {
            throw new OrderbookPersistenceException("Cannot Open orderbook.");
        }
        return id;
    }
    
    private String setBuyOrderNumber() throws OrderbookPersistenceException {
        loadOrders();
        if(buyOrders.isEmpty()){
            int orderNum = initialBuyOrderMaxNum + 1;
            initialBuyOrderMaxNum++;
            return Integer.toString(orderNum);
        } else {
            int max = initialBuyOrderMaxNum;
            initialBuyOrderMaxNum++;
            return Integer.toString(max + 1);
        }
    }
    
    private String setSellOrderNumber() throws OrderbookPersistenceException {
        loadOrders();
        if(sellOrders.isEmpty()){
            int orderNum = initialSellOrderMaxNum + 1;
            initialSellOrderMaxNum++;
            return Integer.toString(orderNum);
        } else {
            int max = initialSellOrderMaxNum;
            initialSellOrderMaxNum++;
            return Integer.toString(max + 1);
        }
    }
    
    private int getMaxNum(List<Order> orders) {
        int max = 0;
        String orderbookName = getOrderBookName();
        String id;
        String strNum;
        int num;
        for(Order order : orders){
            id = order.getOrderId();
//            if(orderbookName.equals("orderbook")) {
                strNum = id.substring(4);
//            } else {
//                strNum = id.substring(orderbookName.length() + 4);
//            }
            num = Integer.parseInt(strNum);
            if(num > max){
                max = num;
            }
        }
        return max;
    }
    
    private String findDelimiter(String currentLine) {
       String delimiters = ":*&^%$#@!;|/\\";
       char delimiter = 0;
       for(var del : delimiters.toCharArray()){
           if(currentLine.contains(String.valueOf(del))){
               delimiter = del;
               break;
           }
       }
       int count = 0;
       for(var ch : currentLine.toCharArray()){
           if(ch == delimiter){
               count++;
           }
           if(count > 0 && ch != delimiter){
               break;
           }
       }
       String specialCharacters = "|?*+\\^$&";
       String strDelimiter = String.valueOf(delimiter);
       if(specialCharacters.contains(strDelimiter)){
           strDelimiter = "\\" + strDelimiter;
       } 
       String repeated = strDelimiter.repeat(count);
       return repeated;

    }
    
    @Override
    public void setAnotherOrderBook(String orderBook) throws OrderbookPersistenceException {
        File orderBookFile = new File(orderBook);
        if(!orderBookFile.exists()){
            throw new OrderbookPersistenceException("File name " + orderBook + " does not exist.\n");
        }
        ORDER_FILE = orderBook;
        
        loadOrders();
        initialBuyOrderMaxNum = getMaxNum(buyOrders);
        initialSellOrderMaxNum = getMaxNum(sellOrders);
        
        trades.clear();
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(TRADE_FILE));
        } catch (FileNotFoundException e) {
            throw new OrderbookPersistenceException("Could not load data into file, trade.txt file does not exist.", e);
        } catch (IOException e) {
            throw new OrderbookPersistenceException("Could not write to file.", e);
        }
        out.flush();
        out.close();
    }
    
    @Override
    public String getOrderBookName() {
        String[] name = ORDER_FILE.split("\\.");
        return name[0];
    }
}
