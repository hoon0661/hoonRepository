/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.data;

import com.hoon.guessthenumber.models.GuessNumber;
import com.hoon.guessthenumber.models.Round;
import java.util.List;

/**
 *
 * @author Hoon
 */
public interface GuessTheNumberDao {
    GuessNumber add(GuessNumber number);
    Round addRound(Round round);
    List<GuessNumber> getAll();
    List<Round> getAllRounds();
    GuessNumber findGameById(int id);
    List<Round> findRoundById(int id);
//    Round findRoundById(int id);
    void update(GuessNumber number);
    boolean deleteById(int id);
    boolean deleteRoundById(int id);
}
