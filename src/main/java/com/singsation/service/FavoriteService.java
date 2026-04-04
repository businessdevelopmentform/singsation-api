package com.singsation.service;

import com.singsation.model.Favorite;
import com.singsation.model.Song;
import com.singsation.model.User;
import com.singsation.repository.FavoriteRepository;
import com.singsation.repository.SongRepository;
import com.singsation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    public void addFavorite(Long userId, Long songId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));
        
        if (!favoriteRepository.existsByUserAndSong(user, song)) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setSong(song);
            favoriteRepository.save(favorite);
        }
    }
    
    public void removeFavorite(Long userId, Long songId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found"));
        
        favoriteRepository.deleteByUserAndSong(user, song);
    }
    
    public List<Song> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return favoriteRepository.findByUser(user)
            .stream()
            .map(Favorite::getSong)
            .collect(Collectors.toList());
    }
}