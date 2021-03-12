package com.logParser.logParser.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/connection")
public class ConnectionController {

    @GetMapping ("/check")
    public ResponseEntity<?>connectionCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}