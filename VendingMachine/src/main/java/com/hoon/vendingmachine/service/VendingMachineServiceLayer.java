/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.service;

import com.hoon.vendingmachine.dao.VendingMachinePersistenceException;
import com.hoon.vendingmachine.dto.Item;
import java.util.List;

/**
 *
 * @author Hoon
 */
public interface VendingMachineServiceLayer {
    double vendItem(String itemName, double money) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException;
    Item editItem(String name, Item item) throws VendingMachinePersistenceException;
    Item getItem(String itemName) throws VendingMachinePersistenceException;
    public List<Integer> change(double money);
}
