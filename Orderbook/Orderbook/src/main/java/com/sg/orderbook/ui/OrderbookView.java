/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.ui;

import com.sg.orderbook.dto.Trade;
import java.time.format.DateTimeFormatter;
import com.sg.orderbook.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author hoon
 */
@Component
public class OrderbookView {
    
    private UserIO io;
    
    @Autowired
    public OrderbookView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection() {
        io.print("Main Menu");
        io.print("1. View orderbook");
        io.print("2. Match one order");
        io.print("3. Match all orders");
        io.print("4. Trade query menu");
        io.print("5. Create & update order menu");
        io.print("6. Read another order book");
        io.print("7. Exit");
        
        int choice = io.readInt("Please select from the above choices.", 1, 7);
        io.readString("");
        return choice;
    }
    
    public int printTradeMenuAndGetSelection() {
        io.print("Trade Menu");
        io.print("1. View a trade by ID");
        io.print("2. View all trades by execution time");
        io.print("3. Query trades by date & time");
        io.print("4. Query trades by quantity");
        io.print("5. Return to Main Menu");
        
        int choice = io.readInt("Please select from the above choices.", 1, 5);
        io.readString("");
        return choice;
    }
    
    public int printOrderMenuAndGetSelection() {
        io.print("Order Menu");
        io.print("1. Create buy order");
        io.print("2. Create sell order");
        io.print("3. Update existing buy order");
        io.print("4. Update existing sell order");
        io.print("5. Return to Main Menu");
        
        int choice = io.readInt("Please select from the above choices.", 1, 5);
        io.readString("");
        return choice;
    }
    
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
    
    public void displayDisplayTradeBanner() {
        io.print("=== Display Trade ===");
    }
    
    public String getTradeIdChoice() {
        return io.readString("Please enter the Trade ID.");
    }
    
    public void displayTrade(Trade trade) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSS");
        if(trade != null) {
            io.print("=== Trade Summary ===");
            io.print("Trade ID: " + trade.getTradeId());
            io.print("Execution time: " + trade.getExecutionTime().format(formatter));
            io.print("Quantity filled: " + trade.getQuantityFilled());
            io.print("Executed price: $" + trade.getExecutedPrice());
        } else {
            io.print("No such trade exists.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayTickerTrade(Trade trade) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSS");
        String tradeInfo = String.format("ID: %s - Execution time: %s | Quantity filled: %s | Executed price: $%s",
                trade.getTradeId(),
                trade.getExecutionTime().format(formatter),
                trade.getQuantityFilled(),
                trade.getExecutedPrice());
        io.print(tradeInfo);
//        io.readString("");
    }
    
    public void displayTradeList(List<Trade> tradeList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss.SSS");
        int counter = 0;
        String str;
        boolean cont = true;
        for(Trade currentTrade : tradeList) {
            String tradeInfo = String.format("ID: %s - Execution time: %s | Quantity filled: %s | Executed price: $%s",
                currentTrade.getTradeId(),
                currentTrade.getExecutionTime().format(formatter),
                currentTrade.getQuantityFilled(),
                currentTrade.getExecutedPrice());
            io.print(tradeInfo);
            counter++;
            if(counter % 10 == 0 && counter != 0) {
                do{
                    str = io.readString("Enter - NEXT, n - STOP");
                    if(str.equals("")){
                        cont = true;
                        break;
                    }
                    else if(str.toLowerCase().equals("n")){
                        io.print("Stop viewing orders....\n");
                        cont = false;
                        break;
                    } else {
                        io.print("Error - Please hit enter or n");
                    }
                } while(!(str.equals("") || str.toLowerCase().equals("n")));
                if(cont){
                    continue;
                } else {
                    break;
                }
            }
        }
        io.readString("You have finished viewing trades; please hit enter to continue.");
    }
    
    public void displayDisplayAllBanner() {
        io.print("=== Display All Trades by Execution Time ===");
    }
        
    public void displayAllOrders(List<List<Order>> allOrders){
        
        List<Order> buyOrders = allOrders.get(0);
        List<Order> sellOrders = allOrders.get(1);
        int buyOrderSize;
        int sellOrderSize;
        
        if(buyOrders == null && sellOrders == null){
            io.print("There is no order.\n");
            return;
        }
        int size;
        if(buyOrders == null){
            size = sellOrders.size();
            buyOrderSize = 0;
            sellOrderSize = sellOrders.size();
        }
        else if(sellOrders == null){
            size = buyOrders.size();
            sellOrderSize = 0;
            buyOrderSize = buyOrders.size();
        }
        else{
            size = Math.max(buyOrders.size(), sellOrders.size());   
            buyOrderSize = buyOrders.size();
            sellOrderSize = sellOrders.size();
        }
        
        io.print("================= Order Book ==================");

        while(true){
            io.print("===== Buy Order =====    ===== Sell Order =====");
            io.print("OrderID Price Quantity   OrderID Price Quantity");
            io.print("===============================================");
            boolean cont = true;
            String str;
            for(int i = 0; i < size; i++){
                if(i >= buyOrderSize){
                    io.print("                          " + sellOrders.get(i).toString());
                }
                else if(i >= sellOrderSize){
                    io.print(buyOrders.get(i).toString());
                }
                else{
                    io.print(buyOrders.get(i).toString() + "      " + sellOrders.get(i).toString());
                }
                if((i+1) % 20 == 0){
                    do{
                        str = io.readString("Enter - NEXT, n - STOP");
                        if(str.equals("")){
                            cont = true;
                            break;
                        }
                        else if(str.toLowerCase().equals("n")){
                            io.print("Stop viewing orders....\n");
                            cont = false;
                            break;
                        } else {
                            io.print("Error - Please hit enter or n");
                        }
                    } while(!(str.equals("") || str.toLowerCase().equals("n")));
                }
                if(cont){
                    continue;
                } else{
                    break;
                }
            }
            break;
        }
        io.print("**************End of OrderBook**************\n");
        printEnterToContinue();
    }

    public void displayStats(List<Object> stat) {
        int numBuyOrders = (int) stat.get(0);
        int numSellOrders = (int) stat.get(1);
        int overallBuyQuantity = (int) stat.get(2);
        int overallSellQuantity = (int) stat.get(3);
        String avgBuyPrice = stat.get(4).toString();
        String avgSellPrice = stat.get(5).toString();
        io.print("==================== Orderbook Statistics ======================");
        io.print("                 ===== Buy Order =====    ===== Sell Order =====");
        io.print("# of sell orders:         " + numBuyOrders + "                       " + numSellOrders);

        io.print("Overall Quantity:         " + overallBuyQuantity + "                       " + overallSellQuantity);
        io.print("Avg. price      :         " + avgBuyPrice + "                  " + avgSellPrice);
        io.print("================================================================\n");
    }
    
    
    public void displayEditSellOrderBanner() {
        io.print("=== Edit Sell Order ===");
    }
    
    public void displayEditBuyOrderBanner() {
        io.print("=== Edit Buy Order ===");
    }
    
    public String getOrderIdChoice() {
        return io.readString("Please enter the order ID of the order you want to edit.");
    }
    
    public Order getEditOrderInfo(Order order) {
        Double doublePrice;
        do {            
            doublePrice = io.readDouble("Please enter updated price.");
            if(doublePrice <= 0) {
                io.print("Invalid input - price must be positive amount.");
            }
        } while (doublePrice <= 0);
        
        int quantity;
        do {            
            quantity = io.readInt("Please enter updated quantity.");
            if(quantity < 1) {
                io.print("Invalid input - quantity must be positive amount.");
            }
        } while (quantity < 1);
        
        order.setPrice(new BigDecimal(doublePrice.toString()));
        order.setQuantity(quantity);
        return order;
    }
    
    public void displayEditSuccessBanner() {
        io.readString("Successfully edited order. Please hit enter to continue.");
    }
    
    public void printOrderSummary(Order order) {
        io.print("=== Order Summary ===");
        if(order.getOrderId() != null) {
            io.print("Order ID: " + order.getOrderId());
        }
        io.print("Price: $" + order.getPrice().setScale(2, RoundingMode.HALF_UP));
        io.print("Quantity: " + order.getQuantity());
    }
    
    public void printOrderId(String orderId) {
        io.print("Your order ID: " + orderId);
    }
    
    public int getConfirmEditChoice() {
        int choice = io.readInt("Would you like to save this edited order? [1] Yes or [2] No.", 1, 2);
        io.readString("");
        return choice;
    }
    
    public void displayCancelEditOrderBanner() {
        io.print("You have canceled your edited order.");
    }
    
    public void displayCreateBuyOrderBanner() {
        io.print("=== Create Buy Order ===");
    }
    
    public void displayCreateSellOrderBanner() {
        io.print("=== Create Sell Order ===");
    }
    
    public Order getNewOrderInfo() {
        Double doublePrice;
        do {            
            doublePrice = io.readDouble("Please enter price.");
            if(doublePrice <= 0) {
                io.print("Invalid input - price must be positive amount.");
            }
        } while (doublePrice <= 0);
        
        int quantity;
        do {            
            quantity = io.readInt("Please enter quantity.");
            if(quantity < 1) {
                io.print("Invalid input - quantity must be positive amount.");
            }
        } while (quantity < 1);
        
        Order currentOrder = new Order();
        currentOrder.setPrice(new BigDecimal(doublePrice.toString()));
        currentOrder.setQuantity(quantity);
        return currentOrder;
    }
    
    public void displayCreateOrderSuccessBanner() {
        io.readString("Order successfully created. Please hit enter to continue.");
    }
    
    public int getOrderPlacementChoice() {
        int choice = io.readInt("Would you like to place this order? [1] Yes or [2] No.", 1, 2);
        io.readString("");
        return choice;
    }
    
    public void displayCancelCreateOrderBanner() {
        io.print("Your order has been discarded and will not be created.");
    }
    
    public void displayQueryTradesByQuantityBanner() {
        io.print("=== Query Trades by Quantity ===");
    }
    
    public List<Integer> getQueryTradeQuantities() {
        List<Integer> range = new ArrayList<>();
        
        io.print("Please enter number for the minimum.");
        int min = getQuantity();
        
        int max;
        do {            
            io.print("Please enter number for the maximum.");
            max = getQuantity();
            if(max <= min) {
                io.print("Invalid input - maximum must be greater than minimum.");
            }
        } while (max <= min);
        io.readString("");
        
        range.add(min);
        range.add(max);
        return range;
    }
    
    public int getQuantity() {
        int quantity;
        do {            
            quantity = io.readInt("");
            if(quantity <= 0) {
                io.print("Invalid input - must be positive number.");
            }
        } while (quantity <= 0);
        return quantity;
    }
    
    public void displayQueryTradesByDateTimeBanner() {
        io.print("=== Query Trades by Time ===");
    }
    
    public List<LocalDateTime> getQueryTradeDateTimes() {
        List<LocalDateTime> timeRange = new ArrayList<>();
        
        io.print("Please enter the time to start showing trades from - Use military time (HH:mm:ss).");
        LocalDateTime start = getTime();
        
        LocalDateTime end;
        do {            
            io.print("Please enter the time to stop showing trades at - Use military time (HH:mm:ss).");
            end = getTime(); 
            if(!end.isAfter(start)) {
                io.print("End time must be after start time.");
            }
        } while (!end.isAfter(start));
        
        timeRange.add(start);
        timeRange.add(end);
        return timeRange;
    }
    
    public LocalDateTime getTime() {
        boolean isValid = false;
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String ldtString = null;
        
        LocalDateTime ldt = null;
        String strTime;
        do {
            strTime = io.readString("");
            if(strTime.matches("([0-9]{2}):([0-9]{2}:([0-9]{2}))")) {
                ldtString = today.toString() + " " + strTime;
                if(isValidDateTime(ldtString)) {
                    ldt = LocalDateTime.parse(ldtString, formatter);
                    isValid = true;                
                } else {
                    io.print("The time input is an invalid time.");
                }
            } else {
                io.print("Invalid input - please enter time in military format (HH:mm:ss).");
            }
        } while(!isValid);
        
        return ldt;
    }
    
    public boolean isValidDateTime(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        
        try {
            format.parse(dateTime);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    public void printEnterToContinue() {
        io.readString("Please hit enter to continue.");
    }
    
    public void displayExitBanner() {
        io.print("You have exited the Orderbook program.");
    }
    
    public void displayBackToMainMenuBanner() {
        io.readString("You have returned to the Main Menu. Hit enter to continue.");
    }
    
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void printEmptyOrdersListMessage() {
        io.print("All possible orders have been fulfilled.");
    }
    
    public void displayBuyIsEmpty(){
        io.print("Buy order is empty.\n");
    }
    
    public void displaySellIsEmpty(){
        io.print("Sell order is empty.\n");
    }
    
    public String askBuyOrderBookFile(){
        return io.readString("Enter name of orderbook file.");
    }
    
    public void displayException(Exception e){
        io.print(e.getMessage());
    }
    
    public void displayOrderBookName(String name){
        io.print("\n======================================================");
        io.print("           Current Orderbook: " + name);
        io.print("======================================================");
    }
}
