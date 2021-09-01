package com.unhash.splitup.controller;

import com.unhash.splitup.exception.BadRequestException;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.exception.NoContentException;
import com.unhash.splitup.request.GroupRequest;
import com.unhash.splitup.response.GroupResponse;
import com.unhash.splitup.service.GroupService;
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
@RequestMapping("/splitup/group")
public class GroupController {
    @Autowired
    private GroupService groupService;


    //METHOD TO CREATE A NEW GROUP

    @PostMapping(path= "/add")
    public @ResponseBody GroupResponse createGroup(@RequestBody GroupRequest groupRequest){
        log.info("create-group controller is up!");
        return groupService.createGroup(groupRequest);
    }

    //METHOD TO GET USER BY USER-ID

    @GetMapping(path= "/getByUserId")
    public @ResponseBody Set<GroupResponse> getGroupsByUserId(@RequestParam int id){
        Set<GroupResponse> groups = groupService.getGroupsByUserId(id);
        return groups;
    }

    //METHOD TO GET GROUP BY GROUP-ID

    @GetMapping(path= "/getByGroupId")
    public @ResponseBody GroupResponse getGroupByGroupId(@RequestParam int groupId){
        GroupResponse groupResponse = groupService.getGroupByGroupId(groupId);
        return groupResponse;
    }

//    @GetMapping(path= "/getByTransactionId")
//    public @ResponseBody GroupResponse getGroupByTransactionId(@RequestParam int transactionId){
//        GroupResponse groupResponse = groupService.getGroupByGroupId(transactionId);
//        return groupResponse;
//    }



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
