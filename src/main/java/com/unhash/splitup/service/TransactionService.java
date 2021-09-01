package com.unhash.splitup.service;

import com.unhash.splitup.entity.Group;
import com.unhash.splitup.entity.SplitTransaction;
import com.unhash.splitup.entity.Transaction;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.repository.GroupRepository;
import com.unhash.splitup.repository.SplitTransactionRepository;
import com.unhash.splitup.repository.TransactionRepository;
import com.unhash.splitup.repository.UserRepository;
import com.unhash.splitup.request.TransactionRequest;
import com.unhash.splitup.util.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private SplitTransactionRepository splitTransactionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    //CREATE A NEW TRANSACTION (EXPENSE)

    public void addTransaction(TransactionRequest transactionRequest) {
        try {
            log.info("create transaction is starting!");
            Transaction transaction = ConvertorUtil.transactionRequestToEntity(transactionRequest);

            Optional<Group> groupOptional = groupRepository.findById(transactionRequest.getGroupId());
            if (!groupOptional.isPresent()) {
                throw new RuntimeException("Group with id " + transactionRequest.getGroupId() + " is not present in system.");
            }

            transaction.setGroup(groupOptional.get());
            log.info("below transaction.setGroup");

            List<Integer> userIdsInGroup = groupOptional.get().getMember().stream().map(member -> member.getId()).collect(
                    Collectors.toList());
            Set<SplitTransaction> splitTransactions = transaction.getSplitTransaction();

            log.info("before for each");
            splitTransactions.forEach(
                    splitTransaction -> {
                        if(!userIdsInGroup.contains(splitTransaction.getId().getUserId())) {
                            throw new RuntimeException("User with id " + splitTransaction.getUser().getId() + " is not present in this group.");
                        }
                        splitTransaction.setUser(userRepository.findById(splitTransaction.getId().getUserId()).get());
                    }
            );

            log.info("below for each");
            transaction.setSplitTransaction(new HashSet<>());
            transaction = transactionRepository.save(transaction);

            log.info("above for loop");
            for (SplitTransaction splitTransaction: splitTransactions) {
                splitTransaction.setTransaction(transaction);
                splitTransaction.getId().setTransactionId(transaction.getId());
            }

            log.info("above savee all");
            splitTransactionRepository.saveAll(splitTransactions);
            log.info("done savee all");

        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

}
