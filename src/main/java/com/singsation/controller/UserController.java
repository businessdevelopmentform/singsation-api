package com.singsation.controller;

import com.singsation.dto.SongDTO;
import com.singsation.model.Song;
import com.singsation.model.User;
import com.singsation.service.FavoriteService;
import com.singsation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FavoriteService favoriteService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(Map.of(
            "name", user.getName(),
            "email", user.getEmail(),
            "userid", user.getUserid(),
            "contact", user.getContact() != null ? user.getContact() : "",
            "winner", user.getWinner()
        ));
    }
    
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return ResponseEntity.ok(Map.of(
            "name", updatedUser.getName(),
            "email", updatedUser.getEmail(),
            "userid", updatedUser.getUserid(),
            "contact", updatedUser.getContact() != null ? updatedUser.getContact() : "",
            "winner", updatedUser.getWinner()
        ));
    }
    
    @PostMapping("/{userId}/favorites/{songId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long userId, @PathVariable Long songId) {
        favoriteService.addFavorite(userId, songId);
        return ResponseEntity.ok("Favorite added");
    }
    
    @DeleteMapping("/{userId}/favorites/{songId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long userId, @PathVariable Long songId) {
        favoriteService.removeFavorite(userId, songId);
        return ResponseEntity.ok("Favorite removed");
    }
    
    @GetMapping("/{userId}/favorites")
    public List<SongDTO> getFavorites(@PathVariable Long userId) {
        return favoriteService.getUserFavorites(userId)
            .stream()
            .map(SongDTO::new)
            .collect(Collectors.toList());
    }
    
    // ========== NEW: ACCOUNT DELETION ENDPOINT ==========
    @DeleteMapping("/{userId}/account")
    public ResponseEntity<?> deleteAccount(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {
        
        String password = request.get("password");
        
        try {
            userService.deleteUserAccount(userId, password);
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An unexpected error occurred"));
        }
    }
}