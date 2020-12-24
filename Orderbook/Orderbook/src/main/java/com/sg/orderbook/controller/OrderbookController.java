/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.controller;

import com.sg.orderbook.dao.OrderbookPersistenceException;
import com.sg.orderbook.dto.Trade;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.servicelayer.NoOrderExistsException;
import com.sg.orderbook.servicelayer.OrderBookServiceLayer;
import com.sg.orderbook.servicelayer.OrderbookEmptyListException;
import com.sg.orderbook.ui.OrderbookView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author hoon
 */
@Component
public class OrderbookController {

    private OrderbookView view;
    private OrderBookServiceLayer service;

    @Autowired
    public OrderbookController(OrderBookServiceLayer service, OrderbookView view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() throws OrderbookEmptyListException {
        boolean keepGoing = true;
        int menuSelection = 0;

        while(keepGoing) {
            
            displayCurrentOrderBook();
            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                {
                    try {
                        viewOrderBook();
                    } catch (OrderbookPersistenceException e) {
                        displayException(e);
                    }
                }
                    break;

                case 2:
                {
                    try {
                        matchOneOrder();
                    } catch (OrderbookPersistenceException e) {
                        displayException(e);
                    }
                }
                    break;

                case 3:
                {
                    try {
                        matchAllOrders();
                    } catch (OrderbookPersistenceException | OrderbookEmptyListException e) {
                        displayException(e);
                    }
                }
                    break;

                case 4:
                    tradeMenu();
                    break;
                case 5:
                    orderMenu();
                    break;
                case 6:
                {
                    try {
                        readAnotherOrderBook();
                    } catch (OrderbookPersistenceException e) {
                        displayException(e);
                        enterToContinue();;
                    }
                }
                    break;

                case 7:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
    }
    
    public void tradeMenu() {
        boolean keepGoing = true;
        int menuSelection = 0;
        
        try {
            
            while (keepGoing) {                
                
                menuSelection = getTradeMenuSelection();
                
                switch (menuSelection) {
                    case 1:
                        viewTrade();
                        break;
                    case 2:
                        listTradesByExecutionTime();
                        break;
                    case 3:
                        queryTradesByDateTime();
                        break;
                    case 4:
                        queryTradesByQuantity();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            backToMainMenuMessage();
        } catch (OrderbookPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    public void orderMenu() throws OrderbookEmptyListException {
        boolean keepGoing = true;
        int menuSelection = 0;
        
        try {
            
            while (keepGoing) {                
                
                menuSelection = getOrderMenuSelection();
                
                switch (menuSelection) {
                    case 1:
                        createBuyOrder();
                        break;
                    case 2:
                        createSellOrder();
                        break;
                    case 3:
                        editBuyOrder();
                        break;
                    case 4:
                        editSellOrder();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            backToMainMenuMessage();
        } catch (OrderbookPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (NoOrderExistsException e) {
            view.displayErrorMessage(e.getMessage());
        } 
    }
    
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    private int getTradeMenuSelection() {
        return view.printTradeMenuAndGetSelection();
    }
    
    private int getOrderMenuSelection() {
        return view.printOrderMenuAndGetSelection();
    }
    
    private void matchAllOrders() throws OrderbookPersistenceException, OrderbookEmptyListException {
        Random rng = new Random();
        List<List<Order>> orderLists = service.getAllOrders();
        List<Order> buyOrdersList = orderLists.get(0);
        List<Order> sellOrdersList = orderLists.get(1);
        
        Trade currentTrade;
        
        try {
            while(!(buyOrdersList == null || sellOrdersList == null)) {
                int delay = rng.nextInt(2) + 1;
//                TimeUnit.SECONDS.sleep(delay);
                currentTrade = service.matchOneOrder();
                view.displayTickerTrade(currentTrade);
            }            
        } catch (OrderbookEmptyListException e) {
            view.printEmptyOrdersListMessage();
            enterToContinue();
        }
    }
    
    private void matchOneOrder() throws OrderbookPersistenceException, OrderbookEmptyListException{
        try {
            Trade trade = service.matchOneOrder();
            view.displayTrade(trade);
            List<Object> stat = service.displayStats();
            view.displayStats(stat); 
            enterToContinue();        
        } catch (OrderbookEmptyListException e) {
            view.displayErrorMessage(e.getMessage());
            enterToContinue();
        }
    }
    
    private void viewTrade() throws OrderbookPersistenceException {
        view.displayDisplayTradeBanner();
        String tradeId = view.getTradeIdChoice();
        Trade trade = service.getTrade(tradeId);
        view.displayTrade(trade);
    }
    
    private void listTradesByExecutionTime() throws OrderbookPersistenceException {
        view.displayDisplayAllBanner();
        List<Trade> tradeListSortedByExecutionTime = service.getAllTradesByExecutionTime();
        view.displayTradeList(tradeListSortedByExecutionTime);
    }
    

    private void viewOrderBook() throws OrderbookPersistenceException, OrderbookEmptyListException{
        List<Object> stat = service.displayStats();
        view.displayStats(stat);
        List<List<Order>> allOrders = service.getAllOrders();
        view.displayAllOrders(allOrders); 
    }
    
    private void createBuyOrder() throws OrderbookPersistenceException {
        view.displayCreateBuyOrderBanner();
        Order newOrder = view.getNewOrderInfo();
        
        int choice = orderSummaryAndSelection(newOrder);
        switch (choice) {
            case 1:
                newOrder.setOrderId(service.newBuyOrderId());
                view.printOrderId(newOrder.getOrderId());
                service.addBuyOrder(newOrder);
                view.displayCreateOrderSuccessBanner();
                break;
            case 2:
                cancelCreateOrder();
                enterToContinue();
                break;
        }
    }
    
    private void createSellOrder() throws OrderbookPersistenceException {
        view.displayCreateSellOrderBanner();
        Order newOrder = view.getNewOrderInfo();

        int choice = orderSummaryAndSelection(newOrder);
        switch (choice) {
            case 1:
                newOrder.setOrderId(service.newSellOrderId());
                view.printOrderId(newOrder.getOrderId());
                service.addSellOrder(newOrder);
                view.displayCreateOrderSuccessBanner();
                break;
            case 2:
                cancelCreateOrder();
                enterToContinue();
                break;
        }
    }
    
    private void editBuyOrder() throws OrderbookPersistenceException, NoOrderExistsException {
        view.displayEditBuyOrderBanner();
        try {
            String orderId = view.getOrderIdChoice();
            Order retrievedOrder = service.getBuyOrder(orderId); 
            Order editedOrder = view.getEditOrderInfo(retrievedOrder);
            
            int choice = orderSummaryAndEditSelection(editedOrder);
            switch (choice) {
                case 1:
                    service.editBuyOrder(editedOrder);
                    view.displayEditSuccessBanner();
                    break;
                case 2:
                    cancelEditOrder();
                    enterToContinue();
                    break;
            }
        } catch (NoOrderExistsException e) {
            view.displayErrorMessage(e.getMessage());
            enterToContinue();
        } catch (OrderbookPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
            enterToContinue();
        }
    }
    
    private void editSellOrder() {
        view.displayEditSellOrderBanner();
        try {
            String orderId = view.getOrderIdChoice();
            Order retrievedOrder = service.getSellOrder(orderId);
            Order editedOrder = view.getEditOrderInfo(retrievedOrder);
            
            int choice = orderSummaryAndEditSelection(editedOrder);
            switch (choice) {
                case 1:
                    service.editSellOrder(editedOrder);
                    view.displayEditSuccessBanner();
                    break;
                case 2:
                    cancelEditOrder();
                    enterToContinue();
                    break;
            }
        } catch (NoOrderExistsException e) {
            view.displayErrorMessage(e.getMessage());
            enterToContinue();
        } catch (OrderbookPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
            enterToContinue();
        }
    }
    
    public void queryTradesByQuantity() throws OrderbookPersistenceException {
        view.displayQueryTradesByQuantityBanner();
        List<Integer> range = view.getQueryTradeQuantities();
        int min = range.get(0);
        int max = range.get(1);
        List<Trade> queriedListByQuantity = service.getAllTradesByQuantity(min, max);
        view.displayTradeList(queriedListByQuantity);
    }
    
    public void queryTradesByDateTime() throws OrderbookPersistenceException {
        view.displayQueryTradesByDateTimeBanner();
        List<LocalDateTime> timeRange = view.getQueryTradeDateTimes();
        LocalDateTime start = timeRange.get(0);
        LocalDateTime end = timeRange.get(1);
        List<Trade> queriedListByDateTime = service.getAllTradesByDateTime(start, end);
        view.displayTradeList(queriedListByDateTime);
    }
    
    private int orderSummaryAndSelection(Order order) {
        view.printOrderSummary(order);
        return view.getOrderPlacementChoice();
    }
    
    private int orderSummaryAndEditSelection(Order order) {
        view.printOrderSummary(order);
        return view.getConfirmEditChoice();
    }
    
    private void cancelCreateOrder() {
        view.displayCancelCreateOrderBanner();
    }
    
    private void cancelEditOrder() {
        view.displayCancelEditOrderBanner();
    }
    
    private void enterToContinue() {
        view.printEnterToContinue();
    }
    
    private void exitMessage() {
        view.displayExitBanner();
    }
    
    private void backToMainMenuMessage() {
        view.displayBackToMainMenuBanner();
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    private void readAnotherOrderBook() throws OrderbookPersistenceException, OrderbookEmptyListException{
        String orderFile = view.askBuyOrderBookFile();
        service.setAnotherOrderBook(orderFile);
    }
    
    private void displayException(Exception e){
        view.displayException(e);
    }
    
    private void displayCurrentOrderBook(){
        String name = service.getOrderBookName();
        view.displayOrderBookName(name);
    }
}
