/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

/**
 *
 * @author Hoon
 */
public class HangManWrongLetterException extends Exception {
    public HangManWrongLetterException(String message){
        super(message);
    }
    
    public HangManWrongLetterException(String message, Throwable cause) {
        super(message, cause);
    }
}
