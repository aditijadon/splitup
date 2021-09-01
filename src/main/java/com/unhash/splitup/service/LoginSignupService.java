package com.unhash.splitup.service;

import com.unhash.splitup.entity.User;
import com.unhash.splitup.exception.InternalException;
import com.unhash.splitup.repository.UserRepository;
import com.unhash.splitup.request.UserRequest;
import com.unhash.splitup.response.UserResponse;
import com.unhash.splitup.util.ConvertorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LoginSignupService {
    @Autowired
    private UserRepository userRepository;

    //SERVICE METHOD FOR USER SIGNUP

    public void signup(UserRequest userRequest) {
        try {
            log.info("signup service starting!");
            Optional<User> userOptional = userRepository.getUserByEmail(userRequest.getEmail());

            if (userOptional.isPresent()) {
                throw new InternalException("User with email: " + userRequest.getEmail() + " already exists in the system. Try to login!");
            }

            User user = ConvertorUtil.userRequestToEntity(userRequest);
            user = userRepository.save(user);
            log.info("signup service is up!");

        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    //SERVICE METHOD FOR USER LOGIN

    public UserResponse login(UserRequest userRequest) {
        try{
            log.info("login service starting!");
            Optional<User> userOptional = userRepository.getUserByUsernamePassword(userRequest.getUserName(),userRequest.getPassword());
            System.out.println(userOptional.isPresent());
            if (!userOptional.isPresent()) {
                throw new InternalException("Wrong Credentials! Try again or signup!");
            }
            log.info("login service is up!");
            return ConvertorUtil.userEntityToResponse(userOptional.get());

        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

}
