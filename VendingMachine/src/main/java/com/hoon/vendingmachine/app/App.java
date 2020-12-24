/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.vendingmachine.app;

import com.hoon.vendingmachine.controller.Controller;
import com.hoon.vendingmachine.dao.VendingMachinePersistenceException;
import com.hoon.vendingmachine.service.InsufficientFundsException;
import com.hoon.vendingmachine.service.NoItemInventoryException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Hoon
 */
public class App {
    public static void main(String[] args) throws VendingMachinePersistenceException, InsufficientFundsException, NoItemInventoryException{
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.hoon.vendingmachine");
        appContext.refresh();
        Controller controller = appContext.getBean("controller", Controller.class);
        controller.run();
//        UserIO io = new UserIOConsoleImpl();
//        VendingMachineView view = new VendingMachineView(io);
//        VendingMachineDao dao = new VendingMachineDaoFileImpl();
//        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoFileImpl();
//        VendingMachineServiceLayer service = new VendingMachineServiceLayerImpl(dao, auditDao);
//        Controller controller = new Controller(service, view);
//        controller.run();
    }
}
