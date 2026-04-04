package com.singsation.repository;

import com.singsation.model.Favorite;
import com.singsation.model.User;
import com.singsation.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndSong(User user, Song song);
    boolean existsByUserAndSong(User user, Song song);
    void deleteByUserAndSong(User user, Song song);
}