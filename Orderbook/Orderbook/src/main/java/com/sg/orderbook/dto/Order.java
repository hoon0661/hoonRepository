/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author hoon
 */
public class Order {
    
    private String orderId;
    private BigDecimal price;
    private int quantity;
    private int side;

    public Order() {
        
    }
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.price = generateRandomPrice();
        this.quantity = generateRandomQuantity();
    }
    
    public Order(String orderId, BigDecimal price, int quantity){
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    private BigDecimal generateRandomPrice() {
        Random rng = new Random();
        Double priceAsDouble = rng.nextDouble() + 190;
        String priceAsString = String.valueOf(priceAsDouble);
        BigDecimal price = new BigDecimal(priceAsString).setScale(2, RoundingMode.HALF_UP);
        return price;
    }

    private int generateRandomQuantity() {
        Random rng = new Random();
        int quantity = rng.nextInt(31) + 20;
        return quantity;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.orderId);
        hash = 97 * hash + Objects.hashCode(this.price);
        hash = 97 * hash + this.quantity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.quantity != other.quantity) {
            return false;
        }
        if (!Objects.equals(this.orderId, other.orderId)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return orderId + "   " + price + "   " + quantity;
    }
}
