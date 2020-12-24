/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.servicelayer;

import com.sg.orderbook.dao.OrderbookAuditDao;
import com.sg.orderbook.dao.OrderbookPersistenceException;

/**
 *
 * @author hoon
 */
public class OrderbookAuditDaoStubImpl implements OrderbookAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws OrderbookPersistenceException {
        //Do nothing
    }
    
}
