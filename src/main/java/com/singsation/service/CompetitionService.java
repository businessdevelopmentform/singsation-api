package com.singsation.service;

import com.singsation.model.CompetitionEntry;
import com.singsation.model.User;
import com.singsation.repository.CompetitionRepository;
import com.singsation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {
    @Autowired
    private CompetitionRepository competitionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CompetitionEntry submitEntry(Long userId, CompetitionEntry entry) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        entry.setUser(user);
        return competitionRepository.save(entry);
    }
}