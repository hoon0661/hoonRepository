/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.dao;

import com.hoon.flooringmastery.dto.FMOrder;
import com.hoon.flooringmastery.dto.Product;
import com.hoon.flooringmastery.dto.State;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hoon
 */
public interface FlooringMasteryDao {
    FMOrder addOrder(FMOrder order, String path) throws FlooringMasteryPersistenceException;
    List<FMOrder> getAllOrder(String path) throws FlooringMasteryPersistenceException;
    FMOrder getOrder(int orderNumber, String path) throws FlooringMasteryPersistenceException;
    FMOrder editOrder(FMOrder newOrder, String path) throws FlooringMasteryPersistenceException;
    FMOrder removeOrder(int orderNumber, String path) throws FlooringMasteryPersistenceException;
    void exportOrder(String path) throws FlooringMasteryPersistenceException;
    Map<String, State> stateBook() throws FlooringMasteryPersistenceException;
    Map<String, Product> productBook() throws FlooringMasteryPersistenceException;
}
