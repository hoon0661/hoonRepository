/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.dto;

import java.math.BigDecimal;

/**
 *
 * @author Hoon
 */
public class Product {
    private String productType;
    private BigDecimal CostPerSquareFoot;
    private BigDecimal LaborcostPerSquareFoot;

    public Product(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return CostPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal CostPerSquareFoot) {
        this.CostPerSquareFoot = CostPerSquareFoot;
    }

    public BigDecimal getLaborcostPerSquareFoot() {
        return LaborcostPerSquareFoot;
    }

    public void setLaborcostPerSquareFoot(BigDecimal LaborcostPerSquareFoot) {
        this.LaborcostPerSquareFoot = LaborcostPerSquareFoot;
    }


   
}


