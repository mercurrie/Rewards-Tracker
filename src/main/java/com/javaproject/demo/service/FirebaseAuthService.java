package com.javaproject.demo.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.javaproject.demo.model.User;
import com.javaproject.demo.model.UserRole;
import com.javaproject.demo.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;

    public FirebaseAuthService(FirebaseAuth firebaseAuth, UserRepository userRepository) {
        this.firebaseAuth = firebaseAuth;
        this.userRepository = userRepository;
    }

    public User verify(String idToken) throws FirebaseAuthException {
        FirebaseToken decoded = firebaseAuth.verifyIdToken(idToken);
        String uid = decoded.getUid();
        String email = decoded.getEmail();
        String name = decoded.getName();
        return findOrCreate(uid, email, name);
    }

    private User findOrCreate(String uid, String email, String name) {
        Optional<User> existing = userRepository.findByFirebaseUid(uid);
        if (existing.isPresent()) {
            User u = existing.get();
            if (email != null && !email.equals(u.getEmail())) u.setEmail(email);
            if (name != null && !name.equals(u.getDisplayName())) u.setDisplayName(name);
            return userRepository.save(u);
        }
        User u = new User(uid, email, name, UserRole.USER);
        return userRepository.save(u);
    }
}
