package com.singsation.controller;

import com.singsation.model.CompetitionEntry;
import com.singsation.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {
    
    @Autowired
    private CompetitionService competitionService;
    
    @PostMapping("/entry")
    public ResponseEntity<?> submitEntry(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        
        CompetitionEntry entry = new CompetitionEntry();
        entry.setCategory(request.get("category").toString());
        entry.setArtistName(request.get("artistName").toString());
        entry.setAge(Integer.parseInt(request.get("age").toString()));
        entry.setUserid(request.get("userid").toString());
        
        CompetitionEntry saved = competitionService.submitEntry(userId, entry);
        return ResponseEntity.ok(Map.of(
            "id", saved.getId(),
            "status", "SUCCESS"
        ));
    }
}