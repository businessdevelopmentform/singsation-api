package com.singsation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.singsation.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/yoco")
public class YocoWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(YocoWebhookController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/payment")
    public ResponseEntity<?> handlePaymentWebhook(@RequestBody String payload) {
        try {
            logger.info("Received Yoco webhook: {}", payload);
            
            // For now, just acknowledge receipt
            // You'll need to implement actual Yoco webhook parsing based on their documentation
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "received");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing Yoco webhook: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/check-access")
    public ResponseEntity<?> checkDownloadAccess(
            @RequestParam Long userId,
            @RequestParam Long songId) {
        
        boolean hasAccess = paymentService.hasDownloadAccess(userId, songId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasAccess", hasAccess);
        response.put("userId", userId);
        response.put("songId", songId);
        
        return ResponseEntity.ok(response);
    }
}