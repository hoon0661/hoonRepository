/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.service;

import com.hoon.vendingmachine.dao.VendingMachineDao;
import com.hoon.vendingmachine.dao.VendingMachinePersistenceException;
import com.hoon.vendingmachine.dto.Item;

/**
 *
 * @author Hoon
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao{
    public Item onlyItem;
    
    public VendingMachineDaoStubImpl(){
        onlyItem = new Item("cheetos", 1.0);
        onlyItem.setQuantity(10);
    }
    
    public VendingMachineDaoStubImpl(Item onlyItem){
        this.onlyItem = onlyItem;
    }
    @Override
    public Item editItem(String name, Item item) throws VendingMachinePersistenceException {
        if(name.equals(onlyItem.getItemName())){
            return onlyItem;
        } else{
            return null;
        }
    }

    @Override
    public Item getItem(String itemName) throws VendingMachinePersistenceException {
        if(itemName.equals(onlyItem.getItemName())){
            return onlyItem;
        } else{
            return null;
        }
    }
    
}
