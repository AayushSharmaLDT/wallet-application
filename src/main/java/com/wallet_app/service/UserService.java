package com.wallet_app.service;

import com.wallet_app.entity.User;
import com.wallet_app.request.CreateUserRequest;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    public User createUser(CreateUserRequest request) throws Exception;
    public Optional<User> findUserById(Long userId);
    public void addMoney(Long userId, BigDecimal amount);
    public void transferMoney(Long senderId, Long receiverId, BigDecimal amount);
    public String login(String email, String password) throws Exception;
}
