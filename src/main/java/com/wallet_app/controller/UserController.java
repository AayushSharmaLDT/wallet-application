package com.wallet_app.controller;

import com.wallet_app.entity.User;
import com.wallet_app.request.AddMoneyRequest;
import com.wallet_app.request.CreateUserRequest;
import com.wallet_app.request.TransferMoneyRequest;
import com.wallet_app.response.Response;
import com.wallet_app.service.UserService;
import com.wallet_app.util.EncryptionUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@Validated
class UserController {
    @Autowired
    private  UserService userService;


    @PostMapping
    public ResponseEntity<Response<User>> createUser(@Valid @RequestBody CreateUserRequest request) throws Exception {
        String encryptedPassword = EncryptionUtil.encrypt(request.getPassword());
        request.setPassword(encryptedPassword);
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>("User created successfully", user));
    }

    @PostMapping("/add-money")
    public ResponseEntity<Response<String>> addMoney(@Valid @RequestBody AddMoneyRequest request) {
        userService.addMoney(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(new Response<>("Money added successfully", null));
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<Response<String>> transferMoney(@Valid @RequestBody TransferMoneyRequest request) {
        userService.transferMoney(request.getSenderId(), request.getReceiverId(), request.getAmount());
        return ResponseEntity.ok(new Response<>("Money transferred successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            String message = userService.login(email, password);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
