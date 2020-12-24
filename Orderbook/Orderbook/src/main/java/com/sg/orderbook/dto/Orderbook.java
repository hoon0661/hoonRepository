/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoon
 */
public class Orderbook {
    
    private List<Order> buyOrders;
    private List<Order> sellOrders;

    public Orderbook() {
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
        buyOrders = generateBuyOrders();
        sellOrders = generateSellOrders();
    }
    
    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<Order> buyOrders) {
        this.buyOrders = buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<Order> sellOrders) {
        this.sellOrders = sellOrders;
    }
    
    public List<Order> generateBuyOrders() {

        for(int i = 0; i < 1000; i++) {
            String buyString = "BORD";
            Order currentOrder = new Order(buyString + (i + 1));
            buyOrders.add(currentOrder);
        }
        return buyOrders;
    }
    
    public List<Order> generateSellOrders() {
        
        for(int i = 0; i < 1000; i++) {

            String sellString = "SORD";
            Order currentOrder = new Order(sellString + (i + 1));
            sellOrders.add(currentOrder);
        }
        return sellOrders;
    }
}
