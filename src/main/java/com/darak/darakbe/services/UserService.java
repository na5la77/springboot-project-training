package com.darak.darakbe.services;

import com.darak.darakbe.entities.User;
import com.darak.darakbe.repos.UserRepository;
import com.darak.darakbe.utilities.customExceptions.UserCreationException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            String constraintName = extractConstraintName(e);
            if (constraintName != null) {
                handleConstraintViolation(constraintName, e);
            }

            logger.error("Failed to create user", e);
            throw new UserCreationException("Failed to create user", e);
        }
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            updateUserDetails(user, userDetails);
            try {
                return userRepository.save(user);
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                String constraintName = extractConstraintName(e);
                if (constraintName != null) {
                    handleConstraintViolation(constraintName, e);
                }

                logger.error("Failed to update user", e);
                throw new UserCreationException("Failed to update user", e);
            }
        });
    }



    private void updateUserDetails(User user, User userDetails) {
        Optional.ofNullable(userDetails.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDetails.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(userDetails.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        user.setEmailVerified(userDetails.isEmailVerified());
        user.setPhoneNumberVerified(userDetails.isPhoneNumberVerified());
        Optional.ofNullable(userDetails.getPassword()).ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(userDetails.getUserStatus()).ifPresent(user::setUserStatus);
        Optional.ofNullable(userDetails.getFcmTokens()).ifPresent(user::setFcmTokens);
        user.setAdmin(userDetails.isAdmin());
    }

    //TODO: this commented method has the same functionality as the one above it, we can choose later which one to follow
//    private void updateUserDetails(User user, User userDetails) {
//        String name = userDetails.getName();
//        if (name != null) {
//            user.setName(name);
//        }
//
//        String email = userDetails.getEmail();
//        if (email != null) {
//            user.setEmail(email);
//        }
//
//        String phoneNumber = userDetails.getPhoneNumber();
//        if (phoneNumber != null) {
//            user.setPhoneNumber(phoneNumber);
//        }
//
//        user.setEmailVerified(userDetails.isEmailVerified());
//        user.setPhoneNumberVerified(userDetails.isPhoneNumberVerified());
//
//        String password = userDetails.getPassword();
//        if (password != null) {
//            user.setPassword(passwordEncoder.encode(password));
//        }
//
//        String userStatus = userDetails.getUserStatus();
//        if (userStatus != null) {
//            user.setUserStatus(userStatus);
//        }
//
//        List<String> fcmTokens = userDetails.getFcmTokens();
//        if (fcmTokens != null) {
//            user.setFcmTokens(fcmTokens);
//        }
//
//        user.setAdmin(userDetails.isAdmin());
//    }



    private String extractConstraintName(Throwable e) {
        if (e.getCause() instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getSQLException().getMessage();
        }
        return null;
    }

    private void handleConstraintViolation(String constraintName, Exception e) {
        if (constraintName.contains("email")) {
            throw new IllegalArgumentException("Email is already in use.", e);
        } else if (constraintName.contains("phone_number")) {
            throw new IllegalArgumentException("Phone number is already in use.", e);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> getActiveUsersWithPhoneNumber(Pageable pageable, String phoneNumber) {
        logger.debug("Fetching active users with phone number: {}", phoneNumber);
        return userRepository.findActiveUsersWithPhoneNumber(pageable);
    }
}
