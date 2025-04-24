package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.dto.RegisterRequest;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface UserService {
    User createUser(RegisterRequest request) throws ExecutionException, InterruptedException;
    void markUserAsVerified(String mobileNumber) throws ExecutionException, InterruptedException;
    Optional<User> findByMobileNumber(String mobileNumber) throws ExecutionException, InterruptedException;
}