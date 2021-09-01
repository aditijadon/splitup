package com.unhash.splitup.controller;

import com.unhash.splitup.exception.BadRequestException;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.exception.NoContentException;
import com.unhash.splitup.request.UserRequest;
import com.unhash.splitup.response.UserResponse;
import com.unhash.splitup.service.LoginSignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@Validated
@RestController
@RequestMapping("/splitup")
public class LoginSignupController {

    @Autowired
    private LoginSignupService loginSignupService;

    //METHOD FOR USER SIGNUP

    @PostMapping(path = "/signup")
    public @ResponseBody HashMap<String, String> signup(@RequestBody UserRequest userRequest) {
        loginSignupService.signup(userRequest);
        System.out.println(userRequest.getUserName() + userRequest.getPassword());
        HashMap<String,String> hm = new HashMap<>();
        hm.put("signup","true");
        log.info("signup controller is up!");
        return hm;
    }

    //METHOD FOR USER LOGIN

    @PostMapping(path = "/login")
    public @ResponseBody
    UserResponse login(@RequestBody UserRequest userRequest) {
        return loginSignupService.login(userRequest);
    }

    //EXCEPTION-HANDLER FOR BAD REQUEST

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestException(BadRequestException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    //EXCEPTION-HANDLER FOR INTERNAL EXCEPTION

    @ExceptionHandler(InternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInternalException(InternalException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    //EXCEPTION-HANDLER FOR NO CONTENT

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> handleNoContentException(NoContentException exception){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exception.getMessage());
    }
}
