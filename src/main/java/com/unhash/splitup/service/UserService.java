package com.unhash.splitup.service;

import com.unhash.splitup.entity.Group;
import com.unhash.splitup.entity.SplitTransaction;
import com.unhash.splitup.entity.TransactionType;
import com.unhash.splitup.entity.User;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.repository.SplitTransactionRepository;
import com.unhash.splitup.repository.TransactionRepository;
import com.unhash.splitup.repository.UserRepository;
import com.unhash.splitup.request.UserRequest;
import com.unhash.splitup.response.UserContributionResponse;
import com.unhash.splitup.response.UserResponse;
import com.unhash.splitup.util.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.unhash.splitup.util.ConvertorUtil.getUpdatedUserEntity;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SplitTransactionRepository splitTransactionRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    //METHOD TO GET ALL USERS
    public Set<UserResponse> getAllUsers() {
        try {
            log.info("all-users service starting!");
            Set<UserResponse> users = new HashSet<>();
            for (User user : userRepository.findAll()) {
                users.add(ConvertorUtil.userEntityToResponse(user));
            }
            log.info("all-users service is up!");
            return users;
        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }


    //METHOD TO GET USER BY USER-ID
    public UserResponse getById(int id) {
        try {
            log.info("get-user-by-id service starting!");
            Optional<User> userOptional = userRepository.findById(id);
            UserResponse userResponse = ConvertorUtil.userEntityToResponse(userOptional.get());
            log.info("get-user-by-id service service is up!");
            return userResponse;
        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }


    //EDIT USER DETAILS
    public UserResponse updateUser(UserRequest userRequest) {
        try {
            User updatedUser = ConvertorUtil.userRequestToEntity(userRequest);
            Optional<User> userOptional = userRepository.findById(updatedUser.getId());

            if (userOptional.isPresent()) {
                User user = getUpdatedUserEntity(userOptional.get(), updatedUser);
                return ConvertorUtil.userEntityToResponse(userRepository.save(user));
            } else {
                throw new InternalException("User with id " + updatedUser.getId() + " is not present in system.");
            }
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }


    //DELETE USER ACCOUNT BY ID
    public Boolean deleteById(int id) {
        try {
            log.info("del-user-by-id service starting!");
            userRepository.deleteById(id);
            boolean idStillExists = userRepository.existsById(id);
            log.info("del-user-by-id service service is up!");
            return idStillExists;
        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }


    //SERVICE METHOD TO GET USER BY GROUP-ID
    public Set<UserResponse> getByGroupId(int groupId) {
        try {
            log.info("get-user-by-group-id service starting!");
            Set<UserResponse> usersResponse = new HashSet<>();
            for (User user : userRepository.findUsersByGroupId(groupId)) {
                usersResponse.add(ConvertorUtil.userEntityToResponse(user));
            }
            log.info("get-user-by-group-id service is up!");
            return usersResponse;
        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }


    //SERVICE METHOD TO GET GROUPWISE SUMMARY OF USER
    public Set<UserContributionResponse> getGroupwiseContributionByUser(int userId){
        try {
            log.info("inside get-groupwise-contribution service!");
            Set<SplitTransaction> st = splitTransactionRepository.getGroupwiseContributionByUser(userId);
            Set<UserContributionResponse> userContributionResponse = new HashSet<>();
            Map<Integer, Set<SplitTransaction>> map = new HashMap<>();
            st.forEach(
                entry -> {
                    Group group = transactionRepository.getGroupsByTransactionId(entry.getId().getTransactionId());
                    Set<SplitTransaction> updatedSt = new HashSet<>();
                    if (map.containsKey(group.getId())) {
                        updatedSt = map.get(group.getId());
                    }
                    map.put(group.getId(), updatedSt);
                });

            map.entrySet().forEach(entry -> {
                UserContributionResponse userContributionDB = new UserContributionResponse();
                userContributionDB.setGroupId(entry.getKey());
                userContributionDB.setUserId(userId);
                userContributionDB.setTransactionType(TransactionType.DEBIT);

                double dbAmount = 0.0;
                double cdAmount = 0.0;

                for (SplitTransaction s : entry.getValue()) {
                    if (s.getId().getTransactionType() == TransactionType.CREDIT) {
                        cdAmount = cdAmount + s.getMoney();
                    } else {
                        dbAmount = dbAmount + s.getMoney();
                    }
                }

                if (dbAmount > cdAmount) {
                    userContributionDB.setTransactionType(TransactionType.DEBIT);
                    userContributionDB.setAmount(dbAmount - cdAmount);
                } else {
                    userContributionDB.setTransactionType(TransactionType.CREDIT);
                    userContributionDB.setAmount(cdAmount - dbAmount);
                }

                userContributionResponse.add(userContributionDB);
            });
            return userContributionResponse;

        } catch (Exception e){
            throw new InternalException(e.getMessage());
        }
    }
}
