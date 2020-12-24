/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.service;

import com.hoon.vendingmachine.dao.VendingMachineAuditDao;
import com.hoon.vendingmachine.dao.VendingMachineDao;
import com.hoon.vendingmachine.dao.VendingMachineDaoFileImpl;
import com.hoon.vendingmachine.dao.VendingMachinePersistenceException;
import com.hoon.vendingmachine.dto.Change;
import com.hoon.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {
    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;
    
    @Autowired
    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao auditDao){
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    @Override
    public double vendItem(String itemName, double money) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException {
        Item item = getItem(itemName);
        if(money >= item.getPrice()){
            money -= item.getPrice();
            if(item.getQuantity() > 0){
                item.setQuantity(item.getQuantity() - 1);
            }else{
                throw new NoItemInventoryException("Error, this item is not available"); 
            }
        }else{
                throw new InsufficientFundsException("Error, Insufficient fund, you balance is $" + money);
        }
        Item newItem = item;
        dao.editItem(itemName, newItem);
        auditDao.writeAuditEntry("The item " + item.getItemName() + " was vended");
        return money;
    }   
    
    public List<Integer> change(double money){
        Change change = new Change();
        String strMoney = Double.toString(money);
        List<Integer> coins = new ArrayList<>();
        
        BigDecimal total = new BigDecimal(strMoney);
        total = total.setScale(2, RoundingMode.DOWN);
        BigDecimal remainder;
        BigDecimal quarterCount;
        BigDecimal dimeCount;
        BigDecimal nickelCount;
        BigDecimal pennyCount;
        
        quarterCount = total.divide(change.getQuarter(), 0, RoundingMode.FLOOR);
        remainder = total.subtract(change.getQuarter().multiply(quarterCount));
        
        dimeCount = remainder.divide(change.getDime(), 0, RoundingMode.FLOOR);
        remainder = remainder.subtract(change.getDime().multiply(dimeCount));
        
        nickelCount = remainder.divide(change.getNickel(), 0, RoundingMode.FLOOR);
        remainder = remainder.subtract(change.getNickel().multiply(nickelCount));       
        
        pennyCount = remainder.divide(change.getPenny(), 0, RoundingMode.FLOOR);
        
        int quarterCountInt = quarterCount.intValue();
        int dimeCountInt = dimeCount.intValue();
        int nickelCountInt = nickelCount.intValue();
        int pennyCountInt = pennyCount.intValue();
        
        coins.add(quarterCountInt);
        coins.add(dimeCountInt);
        coins.add(nickelCountInt);
        coins.add(pennyCountInt);
        
        return coins;
    }

    @Override
    public Item getItem(String itemName) throws VendingMachinePersistenceException {
        return dao.getItem(itemName);
    }

    @Override
    public Item editItem(String name, Item item) throws VendingMachinePersistenceException {
        return dao.editItem(name, item);
    }
    
    
}
