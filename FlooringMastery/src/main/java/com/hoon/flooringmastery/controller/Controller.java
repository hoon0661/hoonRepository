/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.controller;

import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.service.FlooringMasteryDataValidationException;
import com.hoon.flooringmastery.service.FlooringMasteryNonExistOrderNumberException;
import com.hoon.flooringmastery.service.FlooringMasteryServiceLayer;
import com.hoon.flooringmastery.view.View;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class Controller {
    View view;
    FlooringMasteryServiceLayer service;
    
    @Autowired
    public Controller(FlooringMasteryServiceLayer service, View view) {
        this.service = service;
        this.view = view;
    }
    
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        
            while(keepGoing){
            menuSelection = getMenuSelection();

            switch(menuSelection){
                case 1:
                {
                    try {
                        displayOrders();
                    } catch (FlooringMasteryPersistenceException ex) {
                        displayError(ex);
                    }
                }
                    break;

                case 2:
                {
                    try {
                        addOrder();
                    } catch (FlooringMasteryPersistenceException | FlooringMasteryDataValidationException ex) {
                        displayError(ex);
                    } 
                }
                    break;

                case 3:
                {
                    try {
                        editOrder();
                    } catch (FlooringMasteryPersistenceException | FlooringMasteryNonExistOrderNumberException | FlooringMasteryDataValidationException ex) {
                        displayError(ex);
                    } 
                }
                    break;

                case 4:
                {
                    try {
                        removeOrder();
                    } catch (FlooringMasteryPersistenceException ex) {
                        displayError(ex);
                    }
                }
                    break;

                case 5:
                {
                    try {
                        exportData();
                    } catch (FlooringMasteryPersistenceException ex) {
                        displayError(ex);
                    }
                }
                    break;

                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();    
    }
    
    public int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }
    
    public void displayOrders() throws FlooringMasteryPersistenceException{
        String date = null;
        while(true){
            try{
                date = view.askDate();
                break;
            } catch(Exception e){
                view.invalidDate();
            }
        }
        List<FMOrder> orders = service.getAllOrder(date);
        view.displayOrders(orders);
    }
    
    public void addOrder() throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException{
        view.displayAddOrderBanner();
        List<String> stateList = service.returnStateList();
        List<String> productList = service.returnProductList();
        FMOrder order = view.getOrderInfo(stateList, productList);
        service.setUpOrderInfoWithCalculation(order);
        
        
        String date = null;
        while(true){
            try{
                date = view.askDate();
//                break;
            } catch(Exception e){
                view.invalidDate();
            }
       
            if(date.compareTo(LocalDate.now().format(DateTimeFormatter.ofPattern("MMddYYYY"))) < 0){
                view.displayErrorDate();
                continue;
            }
            break;
        }
        
        view.displayOrderForAdding(order);
        
        char ch = view.askToPlaceOrder();
        if(ch == 'y'){
            service.addOrder(order, date);
            view.displayAddSuccessBanner();
        } else{
            view.displayNotAddOrder();
        }
        
    }
    
    public void editOrder() throws FlooringMasteryPersistenceException, FlooringMasteryNonExistOrderNumberException, FlooringMasteryDataValidationException{
        view.displayEditOrderBanner();
//        String customerName = view.getCustomerName();
        int orderNumber = view.askOrderNumber();
        List<String> stateList = service.returnStateList();
        List<String> productList = service.returnProductList();
        
        String date = null;
        while(true){
            try{
                date = view.askDate();
                break;
            } catch(Exception e){
                view.invalidDate();
            }
        }
        
        FMOrder beforeEdit = service.getOrder(orderNumber, date);
        view.displayOrder(beforeEdit);
        if(beforeEdit != null){
            FMOrder order = view.getOrderInfoForEdit(stateList, productList, beforeEdit);
            
            System.out.println(order.toString());
            
            service.editOrder(orderNumber, order, date);
            view.displayEditSuccessBanner();
        }     
    }
    
    public void removeOrder() throws FlooringMasteryPersistenceException{
        view.displayRemoveOrderBanner();
        int orderNumber = view.askOrderNumber();
        String date = null;
        while(true){
            try{
                date = view.askDate();
                break;
            } catch(Exception e){
                view.invalidDate();
            }
        }
        FMOrder beforeRemove = service.getOrder(orderNumber, date);
        view.displayOrder(beforeRemove);
        if(beforeRemove != null){
            char ch = view.askToRemoveOrder();
            if(ch == 'y'){
                FMOrder removedOrder = service.removeOrder(orderNumber, date);
                view.displayRemoveResultBanner(removedOrder);
            } else{
                view.displayNotRemoveOrder();
            }
        }  
    }
    
    public void exportData() throws FlooringMasteryPersistenceException{
        service.exportOrder();
        view.displayExportSuccessBanner();
    }
    
    public void unknownCommand(){
        view.unknownCommand();
    }
    
    public void exitMessage(){
        view.exitMessage();
    }
    
    public void displayError(Exception e){
        view.displayError(e);
    }
    
}
