/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author hoon
 */
public class Trade {
    
    private String tradeId;
    private LocalDateTime executionTime;
    private int quantityFilled;
    private BigDecimal executedPrice;
    private int orderStatus;
    
    public Trade() {
        this.executionTime = LocalDateTime.now();
    }
    
    public Trade(int quantityFilled, BigDecimal executedPrice) {
        this.quantityFilled = quantityFilled;
        this.executedPrice = executedPrice;
    }

    public String getTradeId() {
        return tradeId;
    }
    
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public int getQuantityFilled() {
        return quantityFilled;
    }

    public void setQuantityFilled(int quantityFilled) {
        this.quantityFilled = quantityFilled;
    }

    public BigDecimal getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(BigDecimal executedPrice) {
        this.executedPrice = executedPrice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.tradeId);
        hash = 97 * hash + Objects.hashCode(this.executionTime);
        hash = 97 * hash + this.quantityFilled;
        hash = 97 * hash + Objects.hashCode(this.executedPrice);
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
        final Trade other = (Trade) obj;
        if (this.quantityFilled != other.quantityFilled) {
            return false;
        }
        if (!Objects.equals(this.tradeId, other.tradeId)) {
            return false;
        }
        if (!Objects.equals(this.executionTime, other.executionTime)) {
            return false;
        }
        if (!Objects.equals(this.executedPrice, other.executedPrice)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Trade{" + "tradeId=" + tradeId + ", executionTime=" + executionTime + ", quantityFilled=" + quantityFilled + ", executedPrice=" + executedPrice + '}';
    }

}
