package com.singsation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YocoConfig {
    
    @Value("${yoco.secret.key:}")
    private String secretKey;
    
    @Value("${yoco.public.key:}")
    private String publicKey;
    
    @Value("${yoco.api.url:https://online.yoco.com/v1}")
    private String apiUrl;
    
    @Value("${yoco.webhook.secret:}")
    private String webhookSecret;
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getWebhookSecret() {
        return webhookSecret;
    }
}