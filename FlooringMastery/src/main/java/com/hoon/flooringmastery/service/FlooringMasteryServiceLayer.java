/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.service;

import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.dto.FMOrder;
import java.util.List;

/**
 *
 * @author Hoon
 */
public interface FlooringMasteryServiceLayer {
    FMOrder addOrder(FMOrder order, String date) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException;
    List<FMOrder> getAllOrder(String date) throws FlooringMasteryPersistenceException;
    FMOrder editOrder(int orderNumber, FMOrder order, String date) throws FlooringMasteryPersistenceException, FlooringMasteryNonExistOrderNumberException, FlooringMasteryDataValidationException;
    FMOrder removeOrder(int orderNumber, String date) throws FlooringMasteryPersistenceException;
    void exportOrder() throws FlooringMasteryPersistenceException;
    List<String> returnStateList() throws FlooringMasteryPersistenceException;
    List<String> returnProductList() throws FlooringMasteryPersistenceException;
    FMOrder getOrder(int orderNum, String date) throws FlooringMasteryPersistenceException;
    void setUpOrderInfoWithCalculation(FMOrder order) throws FlooringMasteryPersistenceException;
}
