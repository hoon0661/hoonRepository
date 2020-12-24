/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.service;

import com.hoon.flooringmastery.dao.FlooringMasteryDao;
import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.dto.Product;
import com.hoon.flooringmastery.dto.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Hoon
 */
public class FlooringMasteryDaoStubImpl implements FlooringMasteryDao{
    private static final String DELIMITER = ",";
    public FMOrder onlyOrder;
    private static final String TAX_FILE = "SampleFileData/Data/Taxes.txt";
    private static final String PRODUCT_FILE = "SampleFileData/Data/Products.txt";
    Map<String, State> states = new HashMap<>();
    Map<String, Product> products = new HashMap<>();
            
    public FlooringMasteryDaoStubImpl() {
        onlyOrder = new FMOrder("Hoon Kim");
        onlyOrder.setOrderNumber(1);
        onlyOrder.setState("TX");
        onlyOrder.setTaxRate(new BigDecimal("4.45"));
        onlyOrder.setProductType("Laminate");
        onlyOrder.setArea(new BigDecimal("100"));
        onlyOrder.setCostPerSquareFoot(new BigDecimal("1.75"));
        onlyOrder.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        onlyOrder.setMaterialCost(new BigDecimal("175.00"));
        onlyOrder.setLaborCost(new BigDecimal("210.00"));
        onlyOrder.setTax(new BigDecimal("17.13"));
        onlyOrder.setTotal(new BigDecimal("402.13"));
    }

    public FlooringMasteryDaoStubImpl(FMOrder testOrder) {
        this.onlyOrder = testOrder;
    }
    
    @Override
    public FMOrder addOrder(FMOrder order, String date) throws FlooringMasteryPersistenceException {
        if(order.getOrderNumber() == onlyOrder.getOrderNumber()){
            return onlyOrder;
        } else{
            return null;
        }
    }

    @Override
    public List<FMOrder> getAllOrder(String date) throws FlooringMasteryPersistenceException {
        List<FMOrder> orderList = new ArrayList<>();
        orderList.add(onlyOrder);
        return orderList;
    }

    @Override
    public FMOrder getOrder(int orderNumber, String date) throws FlooringMasteryPersistenceException {
        if(orderNumber == onlyOrder.getOrderNumber()){
            return onlyOrder;
        } else{
            return null;
        }
    }

    @Override
    public FMOrder editOrder(FMOrder newOrder, String date) throws FlooringMasteryPersistenceException {
        if(newOrder.getOrderNumber() == onlyOrder.getOrderNumber()){
            return onlyOrder;
        } else {
            return null;
        }   
    }

    @Override
    public FMOrder removeOrder(int orderNumber, String date) throws FlooringMasteryPersistenceException {
        if(orderNumber == onlyOrder.getOrderNumber()){
            return onlyOrder;
        }
        else{
            return null;
        }
    }

    @Override
    public void exportOrder(String path) throws FlooringMasteryPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, State> stateBook() throws FlooringMasteryPersistenceException {
        loadState();
        return states;
    }

    @Override
    public Map<String, Product> productBook() throws FlooringMasteryPersistenceException {
        loadProduct();
        return products;
    }
    
    private void loadState() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch(FileNotFoundException e){
            throw new FlooringMasteryPersistenceException("Could not load flooring tax data into memory.", e);
        }
        
        String currentLine = scanner.nextLine();
        State currentState;
        
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);
            states.put(currentState.getState(), currentState);
        }
        
        scanner.close();
    }
    
    private void loadProduct() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch(FileNotFoundException e){
            throw new FlooringMasteryPersistenceException("Could not load flooring product data into memory.", e);
        }
        
        String currentLine = scanner.nextLine();
        Product currentProduct;
        
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }
        
        scanner.close();
    }
    
    private State unmarshallState(String stateAsText){
        String[] stateTokens = stateAsText.split(DELIMITER);
        State state = new State(stateTokens[0]);
        state.setStateName(stateTokens[1]);
        state.setTaxRate(new BigDecimal(stateTokens[2]));
        return state;
    }
    
    private Product unmarshallProduct(String productAsText){
        String[] productTokens = productAsText.split(DELIMITER);
        Product product = new Product(productTokens[0]);
        product.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        product.setLaborcostPerSquareFoot(new BigDecimal(productTokens[2]));
        return product;
    }
    
}
