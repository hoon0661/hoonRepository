/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.flooringmastery.view;

import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Hoon
 */

@Component
public class UserIOConsoleImpl implements UserIO{
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        String response = sc.nextLine();
        return response;
    }

    @Override
    public int readInt(String prompt) {
        int response;
        while(true){
//            System.out.println("*************************************************");
            System.out.println(prompt);
            try{
                response = sc.nextInt();
                sc.nextLine();
                break;
            }catch(Exception e){
                System.out.println("***You must type a number***");
                sc.next();
            }
        }
        
        return response;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int response;
        do{    
            while(true){
                System.out.println(prompt);
                try{
                    response = sc.nextInt();
                    sc.nextLine();
                    break;
                }catch(Exception e){
                    System.out.println("***You must type a number***");
                    sc.next();
                }
            }
        }while(response < min || response > max);       
        return response;     
    }

    @Override
    public double readDouble(String prompt) {
        double response;
        while(true){
            System.out.println(prompt);
            try{
                response = sc.nextDouble();
                break;
            }catch(Exception e){
                System.out.println("***You must type a number***");
                sc.next();
            }
        }
        return response;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double response;
        do{
            while(true){
                System.out.println(prompt);
                try{
                    response = sc.nextDouble();
                    sc.nextLine();
                    break;
                }catch(Exception e){
                    System.out.println("***You must type a number***");
                    sc.next();
                }
            }
            if(response < min){
                System.out.println("The minimum is " + min);
            }
            if(response > max){
                System.out.println("The maximum is " + max);
            }
        }while(response < min || response > max);       
        return response; 
    }

    @Override
    public float readFloat(String prompt) {
        float response;
        while(true){
            System.out.println(prompt);
            try{
                response = sc.nextFloat();
                sc.nextLine();
                break;
            }catch(Exception e){
                System.out.println("***You must type a number***");
                sc.next();
            }
        }
        return response;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float response;
        do{
            while(true){
                System.out.println(prompt);
                try{
                    response = sc.nextFloat();
                    sc.nextLine();
                    break;
                }catch(Exception e){
                    System.out.println("***You must type a number***");
                    sc.next();
                }
            }
        }while(response < min || response > max);       
        return response; 
    }

    @Override
    public long readLong(String prompt) {
        long response;
        while(true){
            System.out.println(prompt);
            try{
                response = sc.nextLong();
                sc.nextLine();
                break;
            }catch(Exception e){
                System.out.println("***You must type a number***");
                sc.next();
            }
        }
        return response;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long response;
        do{
            while(true){
                System.out.println(prompt);
                try{
                    response = sc.nextLong();
                    sc.nextLine();
                    break;
                }catch(Exception e){
                    System.out.println("***You must type a number***");
                    sc.next();
                }
            }
        }while(response < min || response > max);       
        return response; 
    }

}
