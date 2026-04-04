package com.singsation.service;

import com.singsation.model.Payment;
import com.singsation.model.User;
import com.singsation.model.Song;
import com.singsation.repository.PaymentRepository;
import com.singsation.repository.UserRepository;
import com.singsation.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SongRepository songRepository;

    @Transactional
    public Payment grantDownloadAccess(Long userId, Long songId, String transactionId, BigDecimal amount) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Song song = songRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));
        
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setSong(song);
        payment.setTransactionId(transactionId);
        payment.setAmount(amount);
        payment.setCurrency("ZAR");
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setDownloadAccessExpiry(LocalDateTime.now().plusDays(30));
        payment.setDescription("Video download: " + song.getTitle());
        
        return paymentRepository.save(payment);
    }
    
    public boolean hasDownloadAccess(Long userId, Long songId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Song> songOpt = songRepository.findById(songId);
        
        if (userOpt.isEmpty() || songOpt.isEmpty()) {
            return false;
        }
        
        return paymentRepository.existsByUserAndSongAndStatusAndDownloadAccessExpiryAfter(
            userOpt.get(), songOpt.get(), "COMPLETED", LocalDateTime.now());
    }
    
    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }
}