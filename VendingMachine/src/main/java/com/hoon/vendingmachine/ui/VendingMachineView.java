/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.ui;

import static com.hoon.vendingmachine.controller.Controller.money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class VendingMachineView {
    private UserIO io;
    
    @Autowired
    public VendingMachineView(UserIO io) {
        this.io = io;
    }
    
    public int printMenuAndGetSelection(){
        io.print("Menu");
        io.print("1. cheetos - $1");
        io.print("2. lays - $0.5");
        io.print("3. snickers - $1.5");
        io.print("4. mixnuts - $2.0");
        io.print("5. exit");
            
        return io.readInt("Please select a number (1 - 5)", 1, 5);
    }
    
    public double displayInsertMoney(){
        return Double.parseDouble(io.readString("Please insert money:"));        
    }
    
    public void displayChange(){
        BigDecimal a = new BigDecimal(Double.toString(money));
        a = a.setScale(2, RoundingMode.DOWN);
        io.print("Your change is: $" + a.toString());
    }
    
    public void unknownCommand(){
        io.print("Unknown Command");
    }
    
    public void displayExit(){
        io.print("good bye!");
    }
    
    public void displayException(Exception e){
        io.print(e.getMessage());
    }
    
    public void displayChangeBanner(){
        io.print("Here is your change");
    }
    
    public void displayChangeInCoins(List<Integer> a){
        io.print("# of Quarter : " + a.get(0));
        io.print("# of Dime : " + a.get(1));
        io.print("# of Nickel : " + a.get(2));
        io.print("# of Penny : " + a.get(3));
    }
    
    public void displayBar(){
        io.print("\n================================\n");
    }
    
    public double addMoney(double money){
        money += io.readDouble("Add more money");
        return money;
    }
}
