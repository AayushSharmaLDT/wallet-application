package com.wallet_app.service.impl;

import com.wallet_app.entity.User;
import com.wallet_app.repository.UserRepository;
import com.wallet_app.request.CreateUserRequest;
import com.wallet_app.service.UserService;
import com.wallet_app.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;


    public User createUser(CreateUserRequest request) throws Exception {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        String encryptedPassword = EncryptionUtil.encrypt(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void addMoney(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setWalletBalance(user.getWalletBalance().add(amount));
        userRepository.save(user);
    }

    public void transferMoney(Long senderId, Long receiverId, BigDecimal amount) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getWalletBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setWalletBalance(sender.getWalletBalance().subtract(amount));
        receiver.setWalletBalance(receiver.getWalletBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public String login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String decryptedPassword = EncryptionUtil.decrypt(user.getPassword());
        if (!decryptedPassword.equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return "Login successful. Welcome, " + user.getUsername() + "!";
    }
}
