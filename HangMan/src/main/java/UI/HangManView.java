/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Dto.Word;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GKE
 */
public class HangManView {
    private IOUser io;

    public HangManView(IOUser io) {
        this.io = io;
    }
     
    public char printAskUser(){
        return io.readChar("\nPick a letter\n");
    }
        
    public void displayWord(Word word){
        io.print("The word is : " + word.getWord());
    }
    
    public List<Character> setBoard(Word word){
        List<Character> board = new ArrayList<>();
        for(int i = 0; i < word.getWord().length(); i++){
            board.add('_');
        }
        return board;
    }
    
    public void printBoard(List<Character> board){
        for(char i : board){
            System.out.print(i);
        }
    }
    
    public void printError(Exception e){
        io.print(e.getMessage());
    }
    
    public void displayWin(){
        io.print("You win");
    }
    
    public void displayLose(){
        io.print("\nYou lose");
    }
    
    public void displayExit(){
        io.print("Good bye!");
    }
    
    public void displayHangMan(int numWrong){
       switch(numWrong){
           case 1:
               io.print("\n    --------\n    |      |\n    |      O\n    |\n    |\n    |\n____|____");
               break;
           case 2:
               io.print("\n    --------\n    |      |\n    |      O\n    |      |\n    |      |\n    |\n____|____");
               break;
           case 3:
               io.print("\n    --------\n    |      |\n    |      O\n    |     /|\n    |      |\n    |\n____|____");
               break;
           case 4:
               io.print("\n    --------\n    |      |\n    |      O\n    |     /|\\\n    |      |\n    |\n____|____");
               break;
           case 5:
               io.print("\n    --------\n    |      |\n    |      O\n    |     /|\\\n    |      |\n    |     /\n____|____");
               break;
           case 6:
               io.print("\n    --------\n    |      |\n    |      O\n    |     /|\\\n    |      |\n    |     / \\\n____|____DEAD");
       }   
    }
       
    
    
    
}
