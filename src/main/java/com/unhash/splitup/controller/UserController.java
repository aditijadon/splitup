package com.unhash.splitup.controller;

import com.unhash.splitup.exception.BadRequestException;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.exception.NoContentException;
import com.unhash.splitup.request.UserRequest;
import com.unhash.splitup.response.UserContributionResponse;
import com.unhash.splitup.response.UserResponse;
import com.unhash.splitup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/splitup/user")
public class UserController {

    @Autowired
    private UserService userService;


    //METHOD TO GET ALL THE USERS IN SYSTEM
    @GetMapping(path = "/all")
    public @ResponseBody Set<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }


    //METHOD TO GET USER BY USER ID
    @GetMapping(path = "/id")
    public @ResponseBody UserResponse getById(@RequestParam int id) {
        return userService.getById(id);
    }


    //METHOD TO GET USERS BY GROUP ID
    @GetMapping(path = "/groupId")
    public @ResponseBody Set<UserResponse> getByGroupId(@RequestParam int groupId) {
        return userService.getByGroupId(groupId);
    }


    //METHOD TO EDIT USER DEATILS
    @PutMapping(path = "/update")
    public @ResponseBody UserResponse updateUser(@RequestBody UserRequest request) {
        UserResponse updatedUser = userService.updateUser(request);
        return updatedUser;
    }


    //METHOD TO DELETE USER
    @DeleteMapping(path = "/delete")
    public @ResponseBody Boolean updateUser(@RequestParam int id) {
        return userService.deleteById(id);
    }


    //METHOD TO GET USER CONTRIBUTION DETAILS IN A PARTICULAR GROUP
    @GetMapping(path = "/userContribution")
    public Set<UserContributionResponse> getGroupwiseContributionByUser(@RequestParam int userId){
        log.info("inside get-groupwise-contribution controller!");
        return userService.getGroupwiseContributionByUser(userId);
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
