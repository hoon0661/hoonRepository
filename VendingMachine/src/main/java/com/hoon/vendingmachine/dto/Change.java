/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.dto;

import java.math.BigDecimal;

/**
 *
 * @author Hoon
 */
public class Change {
    private BigDecimal quarter;
    private BigDecimal dime;
    private BigDecimal nickel;
    private BigDecimal penny;
    
    public Change(){
        quarter = new BigDecimal("0.25");
        dime = new BigDecimal("0.10");
        nickel = new BigDecimal("0.05");
        penny = new BigDecimal("0.01");
    }

    public BigDecimal getQuarter() {
        return quarter;
    }

    public BigDecimal getDime() {
        return dime;
    }

    public BigDecimal getNickel() {
        return nickel;
    }

    public BigDecimal getPenny() {
        return penny;
    }
    
}
