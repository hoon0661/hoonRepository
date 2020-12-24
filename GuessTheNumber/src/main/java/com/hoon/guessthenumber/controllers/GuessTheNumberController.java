/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.guessthenumber.controllers;

import com.hoon.guessthenumber.data.GuessTheNumberDao;
import com.hoon.guessthenumber.models.GuessNumber;
import com.hoon.guessthenumber.models.Round;
import com.hoon.guessthenumber.service.GuessTheNumberService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hoon
 */
@RestController
@RequestMapping("/api/guessthenumber")
public class GuessTheNumberController {
    private final GuessTheNumberService service;
    
    public GuessTheNumberController(GuessTheNumberService service){
        this.service = service;
    }
    
    @GetMapping("/game")
//    public List<GuessNumber> all(){
    public List<String> all(){
        return service.getAll();
    }
    
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public GuessNumber create(GuessNumber number) {
        return service.add(number);
    }
    
    @PostMapping("/guess")
    public Round guess(int id, int guessNum){
        return service.guessNum(id, guessNum);
    }
    
    @GetMapping("/game/{id}")
    public ResponseEntity<GuessNumber> findGameById(@PathVariable int id){
        GuessNumber result = service.findGameById(id);
        if(result == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/rounds/{id}")
    public List<Round> findRoundById(@PathVariable int id){
        return service.findRoundById(id);
    }
//    public ResponseEntity<Round> findRoundById(@PathVariable int id){
//        Round result = service.findRoundById(id);
//        if(result == null){
//            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
//        }
//        return ResponseEntity.ok(result);
//    }
    
//    @PutMapping("/{id}")
//    public ResponseEntity update(@PathVariable int id, @RequestBody GuessNumber number) {
//        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
//        if (id != number.getId()) {
//            response = new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
//        } else if (service.update(number)) {
//            response = new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//        return response;
//    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        if(service.deleteById(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
