/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Contoller;
import Dao.HangManDao;
import Dao.HangManDaoImpl;
import Dto.Word;
import Service.HangManExistingLetterException;
import Service.HangManService;
import Service.HangManServiceImpl;
import Service.HangManWrongLetterException;
import UI.HangManView;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author GKE
 */
public class HangManController {
    HangManView view;
    HangManService service;

    public HangManController(HangManView view, HangManService service) {
        this.view = view;
        this.service = service;
    }    
    public void run() throws HangManExistingLetterException, HangManWrongLetterException {    
//        char letter = askUser();
//        displayWord();
        Word word = getWord();
        List<Character> listOfWord = getListOfWord(word);
        List<Character> oldBoard = setBoard(word);
        List<Character> newBoard = new ArrayList<>();
        int numWrong = 0;
        
        printBoard(oldBoard);
        while(numWrong < 6){
            char letter = askUser();
            try{
                newBoard = play(word, oldBoard, letter);
                oldBoard = newBoard;
                printBoard(newBoard);
            }catch(HangManExistingLetterException e){
                printError(e);
                printBoard(newBoard);
            }catch(HangManWrongLetterException e){
                printError(e);
                printBoard(oldBoard);
                numWrong++;
                displayHangMan(numWrong);          
            }
            if(listOfWord.equals(newBoard)){
                displayWin();
                break;
            }
        }
        if(numWrong == 6){
            displayLose();
            displayWord(word);
            displayExit();
        }
        else{
            displayExit();
        }
        
    }
    
    private char askUser(){
        return view.printAskUser();
    }
    
    private void displayWord(Word word){
        view.displayWord(word);
    }
    
    private Word getWord(){
        return service.getWord();
    }
    
    private List<Character> setBoard(Word word){
        List<Character> arr = new ArrayList<>();
        arr = view.setBoard(word);
        return arr;
    }
    
    private List<Character> play(Word word, List<Character> board, char letter) throws HangManExistingLetterException, HangManWrongLetterException{
        return service.playGame(word, board, letter);
    }
    
    private void printBoard(List<Character> board){
        view.printBoard(board);
    }
    
    private void printError(Exception e){
        view.printError(e);
    }
    
    private List<Character> getListOfWord(Word word){
        return service.getListOfWord(word);
    }
    
    private void displayWin(){
        view.displayWin();
    }
    
    private void displayLose(){
        view.displayLose();
    }
    
    private void displayExit(){
        view.displayExit();
    }
    
    private void displayHangMan(int numWrong){
        view.displayHangMan(numWrong);
    }
    
    
}
