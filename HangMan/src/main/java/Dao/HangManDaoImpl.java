/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;
import Dto.Word;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author GKE
 */
public class HangManDaoImpl implements HangManDao{
    public final String WORDS_FILE;
    List<Word> wordList = new ArrayList<>();
    
    
    
    public HangManDaoImpl(){
        WORDS_FILE= "wordlist.txt";
    }
    public HangManDaoImpl(String wordWordsFile){
       WORDS_FILE= wordWordsFile;
       
    }
    @Override
    public Word getWord() {
        loadFile();
        Random rd = new Random();
        int num = rd.nextInt(wordList.size());
        Word word = wordList.get(num);
        return word;
    }
    
    private void loadFile(){
        Scanner scanner = null;
        try{
            scanner = new Scanner(new BufferedReader(new FileReader(WORDS_FILE)));
        } catch(FileNotFoundException e){
            System.out.println("Errorrrrrrrrrrrrrrrrrrrrrrrrr");
        }
        String currentLine;
        
        while(scanner.hasNextLine()){
            Word words = new Word();
            currentLine = scanner.nextLine();
            words.setWord(currentLine);
            wordList.add(words);
        }
        scanner.close();
    }
    
}
