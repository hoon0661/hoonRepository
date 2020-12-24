/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.data;

import com.hoon.guessthenumber.TestApplicationConfiguration;
import com.hoon.guessthenumber.models.GuessNumber;
import com.hoon.guessthenumber.models.Round;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Hoon
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GuessTheNumberDaoImplTest {
    @Autowired
    GuessTheNumberDao dao;
    public GuessTheNumberDaoImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
       List<Round> rounds = dao.getAllRounds();
       for(Round round : rounds){
           dao.deleteRoundById(round.getId());
       }
       List<GuessNumber> numbers = dao.getAll();
       for(GuessNumber number : numbers){
           dao.deleteById(number.getId());
       }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of add method, of class GuessTheNumberDaoImpl.
     */
    @Test
    public void testAddAndGetNumber() {
        GuessNumber number = new GuessNumber();
        number.setNum(1234);
        
        number = dao.add(number);
        
        GuessNumber numberFromDao = dao.findGameById(1);
        
        assertEquals(number, numberFromDao);
    }



    
    
}
