package com.healthconnect.finalbackendcapstone.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.healthconnect.finalbackendcapstone.model.User;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public FirebaseService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Save or update user
    public User saveUser(User user) throws ExecutionException, InterruptedException {
        if (user.getId() == null) {
            // New user - auto-generate ID
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
            user.setId(docRef.getId());
        }
        firestore.collection(COLLECTION_NAME).document(user.getId()).set(user).get();
        return user;
    }

    // Find by mobile number
    public Optional<User> findByMobileNumber(String mobileNumber) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("mobileNumber", mobileNumber)
                .limit(1);

        QuerySnapshot snapshot = query.get().get();
        if (!snapshot.isEmpty()) {
            return Optional.of(snapshot.getDocuments().get(0).toObject(User.class));
        }
        return Optional.empty();
    }

    // Other methods (findById, delete, etc.) can be added similarly
}