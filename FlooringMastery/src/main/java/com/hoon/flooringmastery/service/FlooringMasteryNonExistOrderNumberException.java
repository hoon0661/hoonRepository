/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.service;

/**
 *
 * @author Hoon
 */
public class FlooringMasteryNonExistOrderNumberException extends Exception{

    public FlooringMasteryNonExistOrderNumberException(String message) {
        super(message);
    }

    public FlooringMasteryNonExistOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
