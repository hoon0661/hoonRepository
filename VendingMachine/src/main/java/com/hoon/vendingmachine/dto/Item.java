/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.dto;

/**
 *
 * @author Hoon
 */
public class Item {
    private String itemName;
    private double price;
    private int quantity;

    public Item(){
       itemName = "";
       price = 0;
    }
    
    public Item(String itemName, double price){
        this.itemName = itemName;
        this.price = price;
    }
    
    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    
}
