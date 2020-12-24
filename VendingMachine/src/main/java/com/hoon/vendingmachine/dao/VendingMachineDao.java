/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.dao;

import com.hoon.vendingmachine.dto.Item;

/**
 *
 * @author Hoon
 */
public interface VendingMachineDao {
    Item editItem(String name, Item item) throws VendingMachinePersistenceException;
    Item getItem(String itemName) throws VendingMachinePersistenceException;
}
