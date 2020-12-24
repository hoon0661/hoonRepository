/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.dao;

import com.hoon.vendingmachine.dto.Item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */
@Component
public class VendingMachineDaoFileImpl implements VendingMachineDao{
    public final String INVENTORY_FILE;
    public static final String DELIMITER = "::";
    
    private Map<String, Item> items = new HashMap<>();
    
    public VendingMachineDaoFileImpl(){
        INVENTORY_FILE = "inventory.txt";
    }
    
    public VendingMachineDaoFileImpl(String testFile){
        INVENTORY_FILE = testFile;
    }
    @Override
    public Item getItem(String itemName) throws VendingMachinePersistenceException{
        loadItems();
        return items.get(itemName);
    }
    
    @Override
    public Item editItem(String name, Item item)  throws VendingMachinePersistenceException{
        loadItems();
        Item prevItem = items.replace(name, item);
        try {
            writeItems();
        } catch (IOException ex) {
            Logger.getLogger(VendingMachineDaoFileImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prevItem;
    }
    
    private Item unmarshallItem(String itemAsText){
        String[] itemTokens = itemAsText.split(DELIMITER);
        String itemName = itemTokens[0];
        double price = Double.parseDouble(itemTokens[1]);
        Item itemFromFile = new Item(itemName, price);
        itemFromFile.setQuantity(Integer.parseInt(itemTokens[2]));
        return itemFromFile;
    }
    
    private void loadItems() throws VendingMachinePersistenceException{
        Scanner scanner;
        
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(INVENTORY_FILE)));
        } catch(FileNotFoundException e){
            throw new VendingMachinePersistenceException("Could not load inventory data into memory.", e);
        }
        String currentLine;
        Item currentItem;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentItem = unmarshallItem(currentLine);
            items.put(currentItem.getItemName(), currentItem);
        }
    }
    
    private String marshallItem(Item item){
        String itemAsText = item.getItemName() + DELIMITER;
        itemAsText += Double.toString(item.getPrice()) + DELIMITER;
        itemAsText += Integer.toString(item.getQuantity());
        
        return itemAsText;
    }
    
    private void writeItems() throws VendingMachinePersistenceException, IOException{
        PrintWriter sc;
        sc = new PrintWriter(new FileWriter(INVENTORY_FILE));
        String currentLine;
        for(Map.Entry<String, Item> item : items.entrySet()){
            sc.println(marshallItem(item.getValue()));
        }
        sc.close();
    }
}
