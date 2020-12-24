/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

/**
 *
 * @author hoon
 */
public class OrderbookPersistenceException extends Exception {
    
    public OrderbookPersistenceException(String message) {
        super(message);
    }
    
    public OrderbookPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
