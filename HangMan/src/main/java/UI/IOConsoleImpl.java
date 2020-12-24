/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.util.Scanner;

/**
 *
 * @author GKE
 */

public class IOConsoleImpl implements IOUser{
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    
    @Override
    public char readChar(String prompt) {
        System.out.println(prompt);
        return sc.nextLine().charAt(0);
       
    }
    
}
    
  
 

