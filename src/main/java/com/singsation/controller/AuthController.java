package com.singsation.controller;

import com.singsation.dto.LoginRequest;
import com.singsation.dto.LoginResponse;
import com.singsation.dto.RegisterRequest;
import com.singsation.model.User;
import com.singsation.service.UserService;
import com.singsation.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> userOpt = userService.findByEmail(request.getUsername());
            if (userOpt.isEmpty()) {
                userOpt = userService.findByContact(request.getUsername());
            }
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (userService.checkPassword(request.getPassword(), user.getPassword())) {
                    String token = jwtUtil.generateToken(user.getEmail());
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("userId", String.valueOf(user.getId()));
                    
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("name", user.getName());
                    userMap.put("email", user.getEmail());
                    userMap.put("userid", user.getUserid() != null ? user.getUserid() : "SING_" + user.getId());
                    userMap.put("contact", user.getContact() != null ? user.getContact() : "");
                    userMap.put("winner", user.getWinner() != null ? user.getWinner() : "www.singsationsadec.com");
                    
                    response.put("user", userMap);
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Check if user exists
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
            }
            
            User user = new User();
            user.setName(request.getName());
            user.setSurname(request.getSurname());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setContact(request.getEmail());
            user.setUserid("SING_" + System.currentTimeMillis());
            user.setWinner("www.singsationsadec.com");
            
            User savedUser = userService.registerUser(user);
            String token = jwtUtil.generateToken(savedUser.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(savedUser.getId()));
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", savedUser.getName());
            userMap.put("email", savedUser.getEmail());
            userMap.put("userid", savedUser.getUserid());
            userMap.put("contact", savedUser.getContact() != null ? savedUser.getContact() : "");
            userMap.put("winner", savedUser.getWinner());
            
            response.put("user", userMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}