/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.service;

import com.hoon.flooringmastery.dao.FlooringMasteryDao;
import com.hoon.flooringmastery.dao.FlooringMasteryDaoFileImpl;
import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.dto.Product;
import com.hoon.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class FlooringMasteryServiceLayerImpl implements FlooringMasteryServiceLayer {
    
    FlooringMasteryDao dao;
    
    @Autowired
    public FlooringMasteryServiceLayerImpl(FlooringMasteryDao dao) throws FlooringMasteryPersistenceException {
        this.dao = dao;
    }

    @Override
    public FMOrder addOrder(FMOrder order, String date) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException {
        
        validateCustomerData(order);
        setUpOrderInfoWithCalculation(order);
        
//        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMddYYYY"));
        
        String path = "SampleFileData/Orders/Orders_" + date + ".txt";
        
        return dao.addOrder(order, path);
    }

    @Override
    public List<FMOrder> getAllOrder(String date) throws FlooringMasteryPersistenceException {
        String path = "SampleFileData/Orders/Orders_" + date + ".txt";
        return dao.getAllOrder(path);
        
    }

    @Override
    public FMOrder editOrder(int orderNumber, FMOrder newOrder, String date) throws FlooringMasteryPersistenceException, FlooringMasteryNonExistOrderNumberException, FlooringMasteryDataValidationException {

        FMOrder oldOrder = getOrder(orderNumber, date);
        if(oldOrder == null){
            throw new FlooringMasteryNonExistOrderNumberException("ERROR: Could not edit order. Order #" + orderNumber + " does not exists.");
        }
        validateCustomerData(newOrder);       
        setUpOrderInfoWithCalculation(newOrder);
        String path = "SampleFileData/Orders/Orders_" + date + ".txt";
        return dao.editOrder(newOrder, path);
    
    }

    @Override
    public FMOrder removeOrder(int orderNumber, String date) throws FlooringMasteryPersistenceException {
        String path = "SampleFileData/Orders/Orders_" + date + ".txt";
        return dao.removeOrder(orderNumber, path);
        
    }
    
    private void validateCustomerData(FMOrder order) throws FlooringMasteryDataValidationException{
        
        if(order.getCustomerName() == null
                || order.getCustomerName().trim().length() == 0
                || order.getArea() == null
                || order.getArea().doubleValue() < 100){
            
            throw new FlooringMasteryDataValidationException("Error: All fields [Customer name, Area] are required.");
        }
        
        char[] chars = order.getCustomerName().toCharArray();
        for(char c : chars){
            if(Character.isLetterOrDigit(c) || c == '.' || c == ',' || c == ' '){
                continue;
            }
            else{
                throw new FlooringMasteryDataValidationException("Error: the allowed inputs are letters, numbers, commas, periods and spaces.");
            } 
        }
   
    }
    
    @Override
    public List<String> returnStateList() throws FlooringMasteryPersistenceException{
        Map<String, State> states = dao.stateBook();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, State> entry : states.entrySet()){
            list.add(entry.getValue().getState());
        }
        return list;
    }
    
    @Override
    public List<String> returnProductList() throws FlooringMasteryPersistenceException{
        Map<String, Product> products = dao.productBook();
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, Product> entry : products.entrySet()){
            list.add(entry.getValue().getProductType());
        }
        return list;
    }
    
    @Override
    public FMOrder getOrder(int orderNum, String date) throws FlooringMasteryPersistenceException{
        String path = "SampleFileData/Orders/Orders_" + date + ".txt";
        return dao.getOrder(orderNum, path);
    }

    @Override
    public void exportOrder() throws FlooringMasteryPersistenceException {
        String path = "SampleFileData/Backup/DataExport.txt";
        dao.exportOrder(path);
    }
    
    @Override
    public void setUpOrderInfoWithCalculation(FMOrder order) throws FlooringMasteryPersistenceException{
        Map<String, State> states = dao.stateBook();
        Map<String, Product> products = dao.productBook();
        order.setTaxRate(states.get(order.getState()).getTaxRate().setScale(2, RoundingMode.HALF_EVEN));
        order.setCostPerSquareFoot(products.get(order.getProductType()).getCostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));
        order.setLaborCostPerSquareFoot(products.get(order.getProductType()).getLaborcostPerSquareFoot().setScale(2, RoundingMode.HALF_EVEN));
        
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
        BigDecimal tax = (materialCost.add(laborCost)).multiply((order.getTaxRate().divide(new BigDecimal("100"))));
        BigDecimal total = materialCost.add(laborCost.add(tax));
        
        order.setMaterialCost(materialCost.setScale(2, RoundingMode.HALF_EVEN));
        order.setLaborCost(laborCost.setScale(2, RoundingMode.HALF_EVEN));
        order.setTax(tax.setScale(2, RoundingMode.HALF_EVEN));
        order.setTotal(total.setScale(2, RoundingMode.HALF_EVEN));
        
    }
}
