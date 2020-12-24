/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.controller;

import com.hoon.vendingmachine.dao.VendingMachinePersistenceException;
import com.hoon.vendingmachine.service.InsufficientFundsException;
import com.hoon.vendingmachine.service.NoItemInventoryException;
import com.hoon.vendingmachine.service.VendingMachineServiceLayer;
import com.hoon.vendingmachine.ui.VendingMachineView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class Controller {
    private VendingMachineServiceLayer service;
    private VendingMachineView view;
    
    @Autowired
    public Controller(VendingMachineServiceLayer service, VendingMachineView view){
        this.service = service;
        this.view = view;
    }
    public static double money;
    public void run(){
        boolean keepGoing = true;
        int menuSelection = 0;
        money = insertMoney();
        displayBar();
        while (keepGoing){
            menuSelection = getMenuSelection();       
            switch (menuSelection){
                case 1:
                    try{
                        cheetos(money);
                        keepGoing = false;
                    }catch (VendingMachinePersistenceException | NoItemInventoryException e){
                        displayException(e);
                    }catch (InsufficientFundsException e){
                        displayException(e);
                        money = addMoney(money);
                        displayBar();
                    }
                    
                    break;
                case 2:
                    try{
                        lays(money);
                        keepGoing = false;
                    }catch (VendingMachinePersistenceException | NoItemInventoryException e){
                        displayException(e);
                    }catch (InsufficientFundsException e){
                        displayException(e);
                        money = addMoney(money);
                        displayBar();
                    }
                    break;
                case 3:
                    try{
                        snickers(money);
                        keepGoing = false;
                    }catch (VendingMachinePersistenceException | NoItemInventoryException e){
                        displayException(e);
                    }catch (InsufficientFundsException e){
                        displayException(e);
                        money = addMoney(money);
                        displayBar();
                    }
                    break;
                case 4:
                    try{
                        mixednuts(money);
                        keepGoing = false;
                    }catch (VendingMachinePersistenceException | NoItemInventoryException e){
                        displayException(e);
                    }catch (InsufficientFundsException e){
                        displayException(e);
                        money = addMoney(money);
                        displayBar();
                    }
                    break;
                case 5:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        displayBar();
        change();
        coins(money);
        displayExit();
    }
    
    private int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }
    
    private double insertMoney(){
        return view.displayInsertMoney();
    }
    
    private void cheetos(double money1) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException{
        money = service.vendItem("cheetos", money1);
    }
    
    private void lays(double money1) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException{
        money = service.vendItem("lays", money1);
    }
    
    private void snickers(double money1) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException{
        money = service.vendItem("snickers", money1);
    }
    
    private void mixednuts(double money1) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException{
        money = service.vendItem("mixednuts", money1);
    }
    
    private void change(){
        view.displayChange();
    }
    
    private void unknownCommand(){
        view.unknownCommand();
    }
    
    private void displayExit(){
        view.displayExit();
    }
    
    private void displayException(Exception e){
        view.displayException(e);
    }
    
    private void coins(double money1){
        view.displayChangeBanner();
        List<Integer> coins = service.change(money1);
        view.displayChangeInCoins(coins);
    }
    
    private void displayBar(){
        view.displayBar();
    }
    
    private double addMoney(double money){
        return view.addMoney(money);
    }
}
