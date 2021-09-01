package com.unhash.splitup.util;

import com.unhash.splitup.entity.*;
import com.unhash.splitup.request.GroupRequest;
import com.unhash.splitup.request.TransactionRequest;
import com.unhash.splitup.request.UserRequest;
import com.unhash.splitup.response.GroupResponse;
import com.unhash.splitup.response.TransactionResponse;
import com.unhash.splitup.response.UserResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.unhash.splitup.util.HelperUtil.getLatestNotNull;

@Slf4j
public class ConvertorUtil {

    //METHOD TO CONVERT USER-ENTITY TO USER-RESPONSE

    public static UserResponse userEntityToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUserName(user.getUserName());
        userResponse.setFullName(user.getFullName());
        userResponse.setPhone(user.getPhone());
        userResponse.setEmail(user.getEmail());
        //userResponse.setPassword(user.getPassword());
        return userResponse;
    }

    //METHOD TO CONVERT USER-REQUEST TO USER-ENTITY

    public static User userRequestToEntity(UserRequest userRequest) {
        log.info("inside user-request-to-entity");
        User user = new User();
        user.setId(userRequest.getId());
        user.setUserName(userRequest.getUserName());
        user.setFullName(userRequest.getFullName());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        //user.setRelatedGroup(userRequest.getRelatedGroup());
        log.info("user-request-to-entity done!");
        return user;
    }

    //METHOD TO GET UPDATE USER
    public static User getUpdatedUserEntity(User current, User updatedUser) {
        User user = current;
        user.setUserName(getLatestNotNull(user.getUserName(), updatedUser.getUserName()));
        user.setFullName(getLatestNotNull(user.getFullName(), updatedUser.getFullName()));
        user.setEmail(getLatestNotNull(user.getEmail(), updatedUser.getEmail()));
        user.setPhone(getLatestNotNull(user.getPhone(), updatedUser.getPhone()));
        user.setPassword(getLatestNotNull(user.getPassword(), updatedUser.getPassword()));
        return user;

    }

    //METHOD TO CONVERT GROUP-ENTITY TO GROUP-RESPONSE

    public static GroupResponse groupEntityToResponse(Group group) {
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(group.getId());
        groupResponse.setGroupName(group.getGroupName());
        groupResponse.setDescription(group.getDescription());
        groupResponse.setMember(
                group.getMember().stream().map(entity -> userEntityToResponse(entity)).collect(Collectors.toSet())
        );
        groupResponse.setTransactions(
                group.getTransaction().stream().map(entity -> transactionEntityToResponse(entity)).collect(Collectors.toSet())
        );
        return groupResponse;
    }

    //METHOD TO CONVERT GROUP-REQUEST TO GROUP-ENTITY

    public static Group groupRequestToEntity(GroupRequest groupRequest) {
        log.info("inside group-request-to-entity");
        Group group = new Group();
        group.setId(groupRequest.getId());
        group.setGroupName(groupRequest.getGroupName());
        group.setDescription(groupRequest.getDescription());
        group.setMember(
                groupRequest.getMember().stream().map(entity -> userRequestToEntity(entity)).collect(Collectors.toSet())
        );
        log.info("group-request-to-entity done!");
        return group;
    }

    //METHOD TO CONVERT TRANSACTION-ENTITY TO TRANSACTION-RESPONSE

    public static TransactionResponse transactionEntityToResponse(Transaction transaction) {
        log.info("inside transaction-entity-to-response");
        Set<UserResponse> sharedBy = new HashSet<>();
        Map<String, Double> paidBy = new HashMap<>();

        transaction.getSplitTransaction().forEach(entry -> {
            SplitTransaction splitTransaction = new SplitTransaction();
            SplitTransactionPK splitTransactionPK = new SplitTransactionPK();
            if(splitTransactionPK.getTransactionType() == TransactionType.CREDIT){
                sharedBy.add(ConvertorUtil.userEntityToResponse(entry.getUser()));
            } else {
                paidBy.put(entry.getUser().getId().toString(),entry.getMoney());
            }

        });


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setBillFor(transaction.getBillFor());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setPaidOn(transaction.getPaidOn());
        transactionResponse.setGroupId(transaction.getGroup().getId());
        transactionResponse.setPaidBy(paidBy);
        transactionResponse.setSharedBy(sharedBy);

        log.info("transaction-entity-to-response done!");
        return transactionResponse;
    }

    //METHOD TO CONVERT TRANSACTION-REQUEST TO TRANSACTION-ENTITY

    public static Transaction transactionRequestToEntity(TransactionRequest transactionRequest) {
        log.info("inside transaction-request-to-entity");
        Group group = new Group();
        group.setId(transactionRequest.getGroupId());
        Set<SplitTransaction> splitTransactions = new HashSet<>();

        transactionRequest.getPaidBy().entrySet().forEach(entry -> {
            SplitTransaction splitTransaction = new SplitTransaction();
            SplitTransactionPK splitTransactionPK = new SplitTransactionPK();
            splitTransactionPK.setUserId(Integer.valueOf(entry.getKey()));
            splitTransactionPK.setTransactionType(TransactionType.CREDIT);
            splitTransaction.setId(splitTransactionPK);
            splitTransaction.setMoney(entry.getValue());
            splitTransactions.add(splitTransaction);
        });


        double amountPerHead = (transactionRequest.getAmount() / transactionRequest.getSharedBy().size());
        transactionRequest.getSharedBy().forEach(entry -> {
            SplitTransaction splitTransaction = new SplitTransaction();
            SplitTransactionPK splitTransactionPK = new SplitTransactionPK();
            splitTransactionPK.setTransactionType(TransactionType.DEBIT);
            splitTransaction.setId(splitTransactionPK);
            splitTransaction.setMoney(amountPerHead);
            splitTransactions.add(splitTransaction);
        });

        Transaction transaction = new Transaction();
        transaction.setId(transactionRequest.getId());
        transaction.setBillFor(transactionRequest.getBillFor());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setPaidOn(transactionRequest.getPaidOn());
        transaction.setGroup(group);
        transaction.setSplitTransaction(splitTransactions);

        log.info("transaction-request-to-entity done!");
        return transaction;
    }
}
