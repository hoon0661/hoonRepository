/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Dao.HangManDao;
import Dao.HangManDaoImpl;
import Dto.Word;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hoon
 */
public class HangManServiceImpl implements HangManService{
    HangManDao dao;

    public HangManServiceImpl(HangManDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Character> playGame(Word word, List<Character> board, char letter) throws HangManExistingLetterException, HangManWrongLetterException {
        List<Character> arr = new ArrayList<>();
        for(int i = 0; i < word.getWord().length(); i++){
            arr.add(word.getWord().charAt(i));
        }
        if(board.contains(letter)){
                //////Throw error for already existing letter
            throw new HangManExistingLetterException("You already picked this letter");
        }
        else if(!arr.contains(letter)){
            throw new HangManWrongLetterException("You chose wrong letter");
        }
        else{
            for(int i = 0; i < arr.size(); i++){
                if(board.get(i) == '_' && arr.get(i) == letter){
                    board.set(i, letter);
                }
            }
        }
        return board;
    }    

    @Override
    public Word getWord() {
        return dao.getWord();
    }
    
    public List<Character> getListOfWord(Word word){
        List<Character> arr = new ArrayList<>();
        for(int i = 0; i < word.getWord().length(); i++){
            arr.add(word.getWord().charAt(i));
        }
        return arr;
    }
}
