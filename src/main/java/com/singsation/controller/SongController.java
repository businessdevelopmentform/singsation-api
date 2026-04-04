package com.singsation.controller;

import com.singsation.dto.SongDTO;
import com.singsation.model.Song;
import com.singsation.service.PaymentService;
import com.singsation.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    
    @Autowired
    private SongService songService;
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping
    public List<SongDTO> getAllSongs() {
        return songService.getAllSongs()
            .stream()
            .map(SongDTO::new)
            .collect(Collectors.toList());
    }
    
    @GetMapping("/search")
    public List<SongDTO> searchSongs(@RequestParam String query) {
        return songService.searchSongs(query)
            .stream()
            .map(SongDTO::new)
            .collect(Collectors.toList());
    }
    
    @GetMapping("/{id}")
    public SongDTO getSong(@PathVariable Long id) {
        return new SongDTO(songService.getSongById(id));
    }
    
    @GetMapping("/{songId}/download")
    public ResponseEntity<?> downloadVideo(
            @PathVariable Long songId,
            @RequestParam Long userId) {
        
        try {
            boolean hasAccess = paymentService.hasDownloadAccess(userId, songId);
            
            if (!hasAccess) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Payment required to download this video");
                errorResponse.put("paymentRequired", true);
                errorResponse.put("amount", 50.00);
                errorResponse.put("currency", "ZAR");
                errorResponse.put("paymentLink", "https://pay.yoco.com/r/4WXyWk");
                return ResponseEntity.status(403).body(errorResponse);
            }
            
            Song song = songService.getSongById(songId);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("downloadUrl", song.getVideo());
            successResponse.put("title", song.getTitle());
            successResponse.put("artist", song.getArtist());
            successResponse.put("message", "Download access granted");
            
            return ResponseEntity.ok(successResponse);
            
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred");
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}