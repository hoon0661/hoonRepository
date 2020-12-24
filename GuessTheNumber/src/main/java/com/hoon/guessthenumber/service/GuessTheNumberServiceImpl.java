/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.service;

import com.hoon.guessthenumber.data.GuessTheNumberDao;
import com.hoon.guessthenumber.models.GuessNumber;
import com.hoon.guessthenumber.models.Round;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hoon
 */
@Repository
public class GuessTheNumberServiceImpl implements GuessTheNumberService{
    
    GuessTheNumberDao dao;
    
    public GuessTheNumberServiceImpl(GuessTheNumberDao dao){
        this.dao = dao;
    }

    @Override
    public Round guessNum(int id, int num) {
        int answer = dao.findGameById(id).getNum();
        
        GuessNumber number = dao.findGameById(id);
        
        Date date = new Date();
       
        Round round = new Round();
        round.setId(id);
        round.setGuessNumber(num);
        round.setDate(new Timestamp(date.getTime()));
        
        if(answer == num){
            number.setFinished(true);
            update(number);
            round.setExactMatches(4);
            dao.addRound(round);
            return round;
        }
        
        String strAnswer = Integer.toString(answer);
        char[] arrayAnswer = strAnswer.toCharArray();
        
        String strNum = Integer.toString(num);
        char[] arrayNum = strNum.toCharArray();
        
        List<Character> listAnswer = new ArrayList<>();
        for(int i = 0; i < arrayAnswer.length; i++){
            listAnswer.add(arrayAnswer[i]);
        }
        
        List<Character> listNum = new ArrayList<>();
        for(int i = 0; i < arrayNum.length; i++){
            listNum.add(arrayNum[i]);
        }

        
        int exact = 0;
        int partial = 0;
        for(int i = 0; i < listAnswer.size(); i++){
            if(listAnswer.get(i) == listNum.get(i)){
                exact++;
            } else {
                if(listAnswer.contains(listNum.get(i))){
                    partial++;
                }
            }
        }
        round.setExactMatches(exact);
        round.setPartialMatches(partial);
        dao.addRound(round);
        return round;
    }
    
    private Round addRound(Round round){
        return dao.addRound(round);
    }

    @Override
    public GuessNumber add(GuessNumber number) {
        Random rd = new Random();
        Set<Character> set = new HashSet<>();
        int rand = 0;
        while(true){
            set.clear();
            rand = rd.nextInt(9000) + 1000;
            String randStr = Integer.toString(rand);
            var chArray = randStr.toCharArray();
            for(var ch : chArray){
                set.add(ch);
            }
            if(set.size() == 4){
                break;
            }
        }
        
        number.setNum(rand);
        return dao.add(number);
    }

    @Override
      public List<String> getAll() {
        List<GuessNumber> list = dao.getAll();
        List<String> strList = new ArrayList<>();
        for(var item : list){
            String str = "";
            str += "id : " + item.getId() + "***";
            str += "finished : " + item.isFinished();
            if(item.isFinished() == true){
                str += "***answer : " + item.getNum();
            }
            strList.add(str);
            
        }
        return strList;
    }

    @Override
    public GuessNumber findGameById(int id) {
        return dao.findGameById(id);
    }
    
    @Override
    public List<Round> findRoundById(int id) {
        return dao.findRoundById(id);
    }

    @Override
    public void update(GuessNumber number) {
        dao.update(number);
    }

    @Override
    public boolean deleteById(int id) {
        return dao.deleteById(id);
    }
    
}
