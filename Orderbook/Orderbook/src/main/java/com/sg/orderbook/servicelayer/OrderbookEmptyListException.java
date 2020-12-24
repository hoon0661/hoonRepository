/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

/**
 *
 * @author hoon
 */
public class OrderbookEmptyListException extends Exception{

    public OrderbookEmptyListException(String message) {
        super(message);
    }

    public OrderbookEmptyListException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
