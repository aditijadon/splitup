package com.unhash.splitup.controller;

import com.unhash.splitup.exception.BadRequestException;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.exception.NoContentException;
import com.unhash.splitup.request.TransactionRequest;
import com.unhash.splitup.service.TransactionService;
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
@RequestMapping(path = "/splitup/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    //METHOD TO CREATE A TRANSACTION IN AN EXISTING GROUP

    @PostMapping(path = "/add", consumes = {"application/json"})
    public @ResponseBody HashMap<String, String> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        log.info("inside add transaction controller");
        transactionService.addTransaction(transactionRequest);
        System.out.println("Bill is added for: " + transactionRequest.getBillFor());
        HashMap<String,String> hm = new HashMap<>();
        hm.put("add-transaction","true");
        log.info("add transaction controller is working!");
        return hm;
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
