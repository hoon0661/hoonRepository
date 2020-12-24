/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery;

import com.hoon.flooringmastery.controller.Controller;
import com.hoon.flooringmastery.dao.FlooringMasteryDao;
import com.hoon.flooringmastery.dao.FlooringMasteryDaoFileImpl;
import com.hoon.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.hoon.flooringmastery.service.FlooringMasteryDataValidationException;
import com.hoon.flooringmastery.service.FlooringMasteryNonExistOrderNumberException;
import com.hoon.flooringmastery.service.FlooringMasteryServiceLayer;
import com.hoon.flooringmastery.service.FlooringMasteryServiceLayerImpl;
import com.hoon.flooringmastery.view.UserIO;
import com.hoon.flooringmastery.view.UserIOConsoleImpl;
import com.hoon.flooringmastery.view.View;
import java.io.IOException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Hoon
 */
public class App {
    public static void main(String[] args) throws FlooringMasteryPersistenceException, FlooringMasteryDataValidationException, FlooringMasteryNonExistOrderNumberException {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.hoon.flooringmastery");
        appContext.refresh();

        Controller controller = appContext.getBean("controller", Controller.class);
        controller.run();
        
        
//        UserIO myIO = new UserIOConsoleImpl();
//        View view = new View(myIO);
//        FlooringMasteryDao myDao = new FlooringMasteryDaoFileImpl();
//        FlooringMasteryServiceLayer myService = new FlooringMasteryServiceLayerImpl(myDao);
//        Controller controller = new Controller(myService, view);
//        
//        controller.run();
    }
}
