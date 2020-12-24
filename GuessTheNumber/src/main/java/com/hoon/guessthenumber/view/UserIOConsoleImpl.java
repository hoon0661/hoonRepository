/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.view;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author jonat
 */
@Component
public class UserIOConsoleImpl implements UserIO {
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void print(String message){
        System.out.println(message);
    }
    
    @Override
    public String readString(String prompt){
        System.out.println(prompt);
        String userInput = sc.nextLine();
        return userInput;
    }
    
    @Override
    public int readInt(String prompt){
        System.out.println(prompt);
        int userInput = 0;
        boolean isValid = false;
        do {            
            try {
                userInput = sc.nextInt();
                isValid = true;
            } catch (InputMismatchException ime) {
                System.out.println("Input must be an integer.");
                sc.nextLine();
            }
        } while (!isValid);
        return userInput;
    }
    
    @Override
    public int readInt(String prompt, int min, int max){
        int userInput = 0;
        boolean isValid = false;
        
        do {
            try {
                System.out.println(prompt);
                userInput = sc.nextInt();
                if(userInput < min || userInput > max){
                    System.out.println("Integer is not within specified range.");
                }else{
                    isValid = true;
                }
            } catch (InputMismatchException ime) {
                System.out.println("Input must be an integer.");
                sc.nextLine();
            }        
        } while(!isValid);
        
        return userInput;
    }
    
    @Override
    public double readDouble(String prompt){
        System.out.println(prompt);
        double userInput = 0;
        boolean isValid = false;
        do {            
            try {
                userInput = sc.nextDouble();
                isValid = true;
            } catch (InputMismatchException ime) {
                System.out.println("Input must be a valid number.");
                sc.nextLine();
            }
        } while (!isValid);
        return userInput;
    }
    
    @Override
    public double readDouble(String prompt, double min, double max){
        double userInput = 0;
        boolean isValid = false;
        
        do {            
            try {
                System.out.println(prompt);
                userInput = sc.nextDouble();
                if(userInput < min || userInput > max){
                    System.out.println("Double is not within specified range.");
                }else{
                    isValid = true;
                }
            } catch (InputMismatchException ime) {
                System.out.println("Input must be a double.");
                sc.nextLine();
            }
        } while (!isValid);
        
        return userInput;
    }
    
    @Override
    public float readFloat(String prompt){
        System.out.println(prompt);
        float userInput = sc.nextFloat();
        return userInput;
    }
    
    @Override
    public float readFloat(String prompt, float min, float max){
        float userInput = 0;
        boolean isValid = false;
        
        do {            
            try {
                System.out.println(prompt);
                userInput = sc.nextFloat();
                if(userInput < min || userInput > max){
                    System.out.println("Float is not within specified range.");
                }else{
                    isValid = true;
                }
            } catch (InputMismatchException ime) {
                System.out.println("Input must be a float.");
                sc.nextLine();
            }
        } while (!isValid);
        
        return userInput;
    }
    
    @Override
    public long readLong(String prompt){
        System.out.println(prompt);
        long userInput = sc.nextLong();
        return userInput;
    }
    
    @Override
    public long readLong(String prompt, long min, long max){
        long userInput = 0;
        boolean isValid = false;
        
        do {            
            try {
                System.out.println(prompt);
                userInput = sc.nextLong();
                if(userInput < min || userInput > max){
                    System.out.println("Long is not within specified range.");
                }else{
                    isValid = true;
                }
            } catch (InputMismatchException ime) {
                System.out.println("Input must be a long.");
                sc.nextLine();
            }
        } while (!isValid);
        
        return userInput;
    }    
}
