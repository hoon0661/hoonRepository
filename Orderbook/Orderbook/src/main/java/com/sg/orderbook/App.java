package com.sg.orderbook;

import com.sg.orderbook.controller.OrderbookController;
import com.sg.orderbook.dao.OrderbookPersistenceException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.sg.orderbook.servicelayer.OrderbookEmptyListException;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hoon    
 */
public class App {

    public static void main(String[] args) throws OrderbookPersistenceException, OrderbookEmptyListException {       
        
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.orderbook");
        appContext.refresh();

        OrderbookController controller = appContext.getBean("orderbookController", OrderbookController.class);
        controller.run();
    }
}
