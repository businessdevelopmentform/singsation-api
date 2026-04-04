package com.singsation.repository;

import com.singsation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByContact(String contact);
    Optional<User> findByUserid(String userid);
    boolean existsByEmail(String email);
}