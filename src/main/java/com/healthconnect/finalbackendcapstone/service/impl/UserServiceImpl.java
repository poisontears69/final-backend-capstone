package com.healthconnect.finalbackendcapstone.service.impl;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.healthconnect.finalbackendcapstone.dto.RegisterRequest;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {
    private static final String COLLECTION_NAME = "users";

    @Override
    public User createUser(RegisterRequest request) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Check if user already exists
        Optional<User> existingUser = findByMobileNumber(request.getMobileNumber());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this mobile number already exists");
        }

        // Create new user
        User user = new User();
        user.setMobileNumber(request.getMobileNumber());
        user.setVerified(false);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // Generate Firestore document reference
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        user.setId(docRef.getId());

        // Save to Firestore
        docRef.set(user).get();
        return user;
    }

    @Override
    public void markUserAsVerified(String mobileNumber) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Find user by mobile number
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("mobileNumber", mobileNumber)
                .limit(1);

        QuerySnapshot querySnapshot = query.get().get();

        if (!querySnapshot.isEmpty()) {
            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
            DocumentReference docRef = document.getReference();

            // Update verification status
            docRef.update("verified", true, "updatedAt", new Date()).get();
        } else {
            throw new IllegalArgumentException("User not found with mobile number: " + mobileNumber);
        }
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("mobileNumber", mobileNumber)
                .limit(1);

        QuerySnapshot querySnapshot = query.get().get();

        if (!querySnapshot.isEmpty()) {
            return Optional.of(querySnapshot.getDocuments().get(0).toObject(User.class));
        }
        return Optional.empty();
    }
}