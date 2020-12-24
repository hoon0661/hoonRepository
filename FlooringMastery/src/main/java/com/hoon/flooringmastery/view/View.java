/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.view;

import com.hoon.flooringmastery.dto.FMOrder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */

@Component
public class View {
    private UserIO io;
    
    @Autowired
    public View(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection(){
        io.print("*****************************");
        io.print("* <<Flooring Program>>      *");
        io.print("* 1. Display Orders         *");
        io.print("* 2. Add an Order           *");
        io.print("* 3. Edit an Order          *");
        io.print("* 4. Remove an Order        *");
        io.print("* 5. Export All Data        *");
        io.print("* 6. Quit                   *");
        io.print("*****************************");
        return io.readInt("Please select from above choices", 1, 6);
    }
    
    public void displayOrder(FMOrder order) {
        if (order != null) {
            io.print(order.toString());
        } else {
            io.print("No such order number.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    public void displayOrderForAdding(FMOrder order) {
       
       io.print("Order to be placed: "); 
       io.print(order.toStringForAddingOrder());
        
        io.readString("Please hit enter to continue.");
    }
    
    public void displayOrders(List<FMOrder> orders){
        if(orders.isEmpty()){
            io.print("There is currently no data");
        }
        else{
            for(FMOrder order : orders){
                io.print(order.toString());
            }
        } 
        io.readString("Please hit enter to continue.");
    }
    
    public void displayAddOrderBanner(){
        io.print("=== Add Order ===");
    }
    
    public FMOrder getOrderInfo(List<String> stateList, List<String> productList){
        FMOrder order = new FMOrder(io.readString("Please enter customer's name"));
        
        io.print("=== State Selection ===");
        for(int i = 0; i < stateList.size(); i++){
            io.print((i+1) + ". " + stateList.get(i));
        }
        io.print("========================");
        int state = io.readInt("Please choose state", 1, stateList.size());
        for(int i = 1; i <= stateList.size(); i++){
            if(state == i){
                order.setState(stateList.get(i-1));
                break;
            }
        }
        
        io.print("=== Floor Type Selection ===");
        for(int i = 0; i < productList.size(); i++){
            io.print((i+1) + ". " + productList.get(i));
        }
        io.print("============================");
        int productType = io.readInt("Please choose productType", 1, productList.size());
        for(int i = 1; i <= productList.size(); i++){
            if(productType == i){
                order.setProductType(productList.get(i-1));
                break;
            }
        }
        
        order.setArea(new BigDecimal(Double.toString(io.readDouble("please enter area", 100, 100000))));
        return order;
    }
    
    public FMOrder getOrderInfoForEdit(List<String> stateList, List<String> productList, FMOrder beforeEdit){
        String newName = io.readString("Please enter customer's name (current: " + beforeEdit.getCustomerName() + ")");
        FMOrder order;
        if(newName.isEmpty() || newName.length() == 0){
            order = new FMOrder(beforeEdit.getCustomerName());
        } else {
            order = new FMOrder(newName);
        }
        

        io.print("=== State Selection ===");
        for(int i = 0; i < stateList.size(); i++){
            io.print((i+1) + ". " + stateList.get(i));
        }
        io.print("========================");
        int state = io.readInt("Please choose state (current: " + beforeEdit.getState() + ")", 1, stateList.size());
        for(int i = 1; i <= stateList.size(); i++){
            if(state == i){
                order.setState(stateList.get(i-1));
                break;
            }
        }
        
        io.print("=== Floor Type Selection ===");
        for(int i = 0; i < productList.size(); i++){
            io.print((i+1) + ". " + productList.get(i));
        }
        io.print("============================");
        int productType = io.readInt("Please choose productType (current: " + beforeEdit.getProductType() + ")", 1, productList.size());
        for(int i = 1; i <= productList.size(); i++){
            if(productType == i){
                order.setProductType(productList.get(i-1));
                break;
            }
        }
        
        order.setArea(new BigDecimal(Double.toString(io.readDouble("please enter area (current: " + beforeEdit.getArea() + ")", 100, 100000))));
        order.setOrderNumber(beforeEdit.getOrderNumber());
        return order;
    }
    
    public void displayAddSuccessBanner(){
        io.readString("Order successfuly added. Please hit enter to continue");
    }
    
    public void displayEditOrderBanner(){
        io.print("=== Edit Order ===");
    }
    
    public void displayEditSuccessBanner(){
        io.readString("Order successfuly Editted. Please hit enter to continue");
    }
    
    public void displayRemoveOrderBanner(){
        io.print("=== Remove Order ===");
    }
    
    public String getCustomerName(){
        return io.readString("Please enter customer name");
    }
    
    public void displayRemoveResultBanner(FMOrder removedOrder){
        if(removedOrder != null){
            io.print("Order successfully removed");
        }else{
            io.print("No such order");
        }
        io.readString("Please hit enter to continue");
    }
    
    public void unknownCommand(){
        io.print("Unknown Command!!!");
    }
    
    public void exitMessage(){
        io.print("Good Bye!");
    }
    
    public void displayError(Exception e){
        io.print(e.getMessage());
    }
    
    public char askToPlaceOrder(){
        String ans;
        do{
            ans = io.readString("Would you like to place this order? (Y/N)");
        }while(!(((ans.toLowerCase().equals("n")) || (ans.toLowerCase().equals("y"))) && (ans.length() == 1)));
        
        
        return ans.charAt(0);
    }
    
    public void displayNotAddOrder(){
        io.readString("The order has not been placed. Please hit enter to continue");
    }
    
    public String askDate(){
        String date = io.readString("please enter date of order (yyyy-MM-dd)");
        return LocalDate.parse(date).format(DateTimeFormatter.ofPattern("MMddYYYY"));
    }
    
    public void invalidDate(){
        io.print("********************************************************");
        io.print("ERROR: Invalid Date. Please enter date in correct format");
        io.print("********************************************************");
    }
    
    public int askOrderNumber(){
        return io.readInt("Please enter order number");
    }
    
    public char askToRemoveOrder(){
        String ans;
        do{
            ans = io.readString("Would you like to remove this order? (Y/N)");
        }while(!(((ans.toLowerCase().equals("n")) || (ans.toLowerCase().equals("y"))) && (ans.length() == 1)));
        
        
        return ans.charAt(0);
    }
    
    public void displayNotRemoveOrder(){
        io.readString("Order have not been removed. Please hit enter to continue");
    }
    
    public void displayExportSuccessBanner(){
        io.readString("All order data has been exported. Please hit enter to continue");
    }
    
    public void displayErrorDate(){
        io.print("The date should be from today. Cannot place an order for the past");
    }
}
