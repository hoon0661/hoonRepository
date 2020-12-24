/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Dto.Word;
import java.util.List;

/**
 *
 * @author Hoon
 */
public interface HangManService {
    Word getWord();
    List<Character> playGame(Word word, List<Character> board, char letter) throws HangManExistingLetterException, HangManWrongLetterException;
    public List<Character> getListOfWord(Word word);
}
