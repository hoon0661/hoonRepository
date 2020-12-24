/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.dao;

import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.dto.Product;
import com.hoon.flooringmastery.dto.State;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */

@Component
public class FlooringMasteryDaoFileImpl implements FlooringMasteryDao {
    
    private static final String DELIMITER = ",";
    private static final String DELIMITER_ORDER = "::";
    private String ORDER_FILE;
    private static final String TAX_FILE = "SampleFileData/Data/Taxes.txt";
    private static final String PRODUCT_FILE = "SampleFileData/Data/Products.txt";
    
    Map<String, State> states = new HashMap<>();
    Map<String, Product> products = new HashMap<>();
    List<FMOrder> ordersAsList = new ArrayList<>();
    
    @Override
    public FMOrder addOrder(FMOrder order, String path) throws FlooringMasteryPersistenceException {
        ORDER_FILE = path;
        try {
            loadOrdersAsListForAddingOrder();
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        order.setOrderNumber(setOrderNumber());
        FMOrder prevOrder = null;
        if(ordersAsList.add(order)){
            prevOrder = order;
        }
        
        try {
            writeOrderFromList(ordersAsList);
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        return prevOrder;
    }
    
    @Override
    public FMOrder getOrder(int orderNumber, String path) throws FlooringMasteryPersistenceException{
        ORDER_FILE = path;
        try {
            loadOrdersAsList();
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        
        for(FMOrder order : ordersAsList){
            if(order.getOrderNumber() == orderNumber){
                return order;
            }
        }
        return null;
    }

    @Override
    public List<FMOrder> getAllOrder(String path) throws FlooringMasteryPersistenceException {
        ORDER_FILE = path;
        try {
            loadOrdersAsList();
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        List<FMOrder> orderList = new ArrayList<>();
        for(FMOrder order : ordersAsList){
            orderList.add(order);
        }

        return orderList;
    }

    @Override
    public FMOrder editOrder(FMOrder newOrder, String path) throws FlooringMasteryPersistenceException{
        ORDER_FILE = path;
        try {
            loadOrdersAsList();
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        
        FMOrder prevOrder = null;
        for(FMOrder oldOrder : ordersAsList){
            if(oldOrder.getOrderNumber() == newOrder.getOrderNumber()){
                prevOrder = ordersAsList.set(ordersAsList.indexOf(oldOrder), newOrder);
            }
        }

        try {
            writeOrderFromList(ordersAsList);
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        return prevOrder;
    }

    @Override
    public FMOrder removeOrder(int orderNumber, String path) throws FlooringMasteryPersistenceException {
        ORDER_FILE = path;
        try {
            loadOrdersAsList();
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        FMOrder removedOrder = null;

        for(int i = 0; i < ordersAsList.size(); i++){
            if(ordersAsList.get(i).getOrderNumber() == orderNumber){
                removedOrder = ordersAsList.get(i);
                ordersAsList.remove(i);
            }
        }

        try {
            writeOrderFromList(ordersAsList);
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
        return removedOrder;
    }
    
    private FMOrder unmarshallOrder(String orderAsText){
        String[] orderTokens = orderAsText.split(DELIMITER_ORDER);
        FMOrder order = new FMOrder(orderTokens[1]);
        order.setOrderNumber(Integer.parseInt(orderTokens[0]));
        order.setState(orderTokens[2]);
        order.setTaxRate(new BigDecimal(orderTokens[3]));
        order.setProductType(orderTokens[4]);
        order.setArea(new BigDecimal(orderTokens[5]));
        order.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        order.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        order.setMaterialCost(new BigDecimal(orderTokens[8]));
        order.setLaborCost(new BigDecimal(orderTokens[9]));
        order.setTax(new BigDecimal(orderTokens[10]));
        order.setTotal(new BigDecimal(orderTokens[11]));
        return order;
    }
    
    private void loadOrdersAsList() throws FlooringMasteryPersistenceException, IOException {
        ordersAsList.clear();
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        } catch(FileNotFoundException e){
            throw new FlooringMasteryPersistenceException("Error: there is no file for the date");
        }
//        scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        String currentLine;
        FMOrder currentOrder;
        
        try{
            currentLine = scanner.nextLine();
        } catch(NoSuchElementException e){
            emptyFileHeader();
        }
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            ordersAsList.add(currentOrder);
        }
        
        scanner.close();
    }
    
    private void loadOrdersAsListForAddingOrder() throws FlooringMasteryPersistenceException, IOException {
        ordersAsList.clear();
        Scanner scanner;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        } catch(FileNotFoundException e){
            emptyFileHeader();
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        }
//        scanner = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        FMOrder currentOrder;
        String currentLine;
        try{
            currentLine = scanner.nextLine();
        } catch(NoSuchElementException e){
            emptyFileHeader();
        }
      
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            ordersAsList.add(currentOrder);
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
    
    private String marshallOrder(FMOrder order) throws FlooringMasteryPersistenceException{

        String orderAsText = order.getOrderNumber() + DELIMITER_ORDER;

        orderAsText += order.getCustomerName() + DELIMITER_ORDER;

        orderAsText += order.getState() + DELIMITER_ORDER;

        orderAsText += order.getTaxRate() + DELIMITER_ORDER;
        
        orderAsText += order.getProductType() + DELIMITER_ORDER;
        
        orderAsText += order.getArea() + DELIMITER_ORDER;
        
        orderAsText += order.getCostPerSquareFoot() + DELIMITER_ORDER;
        
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER_ORDER;
        
        orderAsText += order.getMaterialCost() + DELIMITER_ORDER;
        
        orderAsText += order.getLaborCost() + DELIMITER_ORDER;
        
        orderAsText += order.getTax() + DELIMITER_ORDER;
        
        orderAsText += order.getTotal();
        
        return orderAsText;
    }
    
    private String marshallOrderForExport(FMOrder order, String date) throws FlooringMasteryPersistenceException{

        String orderAsText = order.getOrderNumber() + DELIMITER_ORDER;

        orderAsText += order.getCustomerName() + DELIMITER_ORDER;

        orderAsText += order.getState() + DELIMITER_ORDER;

        orderAsText += order.getTaxRate() + DELIMITER_ORDER;
        
        orderAsText += order.getProductType() + DELIMITER_ORDER;
        
        orderAsText += order.getArea() + DELIMITER_ORDER;
        
        orderAsText += order.getCostPerSquareFoot() + DELIMITER_ORDER;
        
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMITER_ORDER;
        
        orderAsText += order.getMaterialCost() + DELIMITER_ORDER;
        
        orderAsText += order.getLaborCost() + DELIMITER_ORDER;
        
        orderAsText += order.getTax() + DELIMITER_ORDER;
        
        orderAsText += order.getTotal() + DELIMITER_ORDER;
        
        orderAsText += date;
        
        return orderAsText;
    }
    
    private void writeOrderFromList(List<FMOrder> orders) throws FlooringMasteryPersistenceException, IOException {
        PrintWriter scanner;
        try {
            scanner = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException( "-_- Could not load order data into memory.", e);
        }

        scanner.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
        for(FMOrder order : ordersAsList){
            scanner.println(marshallOrder(order));
        }
        scanner.close();
    }
    
    private void writeOrderFromListForExport(List<String> textData) throws FlooringMasteryPersistenceException, IOException {
        PrintWriter scanner;
        try {
            scanner = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (FileNotFoundException e) {
            throw new FlooringMasteryPersistenceException( "-_- Could not load order data into memory.", e);
        }

        scanner.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total::OrderDate");
        for(String order : textData){
            scanner.println(order);
        }
        scanner.close();
    }
    
    private int setOrderNumber() throws FlooringMasteryPersistenceException{
        if(ordersAsList.isEmpty()){
            return 1;
        }
        else{
            int maxNum = 0;
            for(FMOrder order : ordersAsList){
                if(order.getOrderNumber() > maxNum){
                    maxNum = order.getOrderNumber();
                }
            } 
            return maxNum + 1;
        }
    }
    
    @Override
    public Map<String, State> stateBook() throws FlooringMasteryPersistenceException{
        loadState();
        return states;
    }
    
    @Override
    public Map<String, Product> productBook() throws FlooringMasteryPersistenceException{
        loadProduct();
        return products;
    }

    
    private void emptyFileHeader() throws FlooringMasteryPersistenceException, IOException{
        PrintWriter writeFile;
        writeFile = new PrintWriter(new FileWriter(ORDER_FILE));
        writeFile.println("OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total");
        writeFile.flush();
        writeFile.close();

    }

    @Override
    public void exportOrder(String path) throws FlooringMasteryPersistenceException {
        String orderPath = "C:\\Users\\19178\\Documents\\MThree\\classwork\\FlooringMastery\\SampleFileData\\Orders";
        String javaOrderPath = orderPath.replace("\\", "/");
        File directoryPath = new File(javaOrderPath);
        String[] orderFile = directoryPath.list();
        ordersAsList.clear();
        List<String> textData = new ArrayList<>();
        
        for(int i = 0; i < orderFile.length; i++){
            String[] fileName1 = orderFile[i].split("_");
            String str1 = fileName1[1];
            String[] fileName2 = str1.split("\\.");
            String date = fileName2[0];
            String formattedDate = dateFormat(date);
            ORDER_FILE = "SampleFileData/Orders/Orders_" + date + ".txt";
            try {
                loadOrdersAsList();
            } catch (IOException ex) {
                throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
            }
            for(FMOrder order : ordersAsList){
                String textOrder = marshallOrderForExport(order, formattedDate);
                textData.add(textOrder);
            }
            
        }

        ORDER_FILE = path;
        try {
            writeOrderFromListForExport(textData);
        } catch (IOException ex) {
            throw new FlooringMasteryPersistenceException("Error: Cannot open file.");
        }
    }
    
    private String dateFormat(String date){
        //06022013
        String newDate = date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4);
        return newDate;
    }
    
}
