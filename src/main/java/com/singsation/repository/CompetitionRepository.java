package com.singsation.repository;

import com.singsation.model.CompetitionEntry;
import com.singsation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompetitionRepository extends JpaRepository<CompetitionEntry, Long> {
    List<CompetitionEntry> findByUser(User user);
    List<CompetitionEntry> findByCategory(String category);
}