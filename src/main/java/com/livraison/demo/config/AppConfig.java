package com.livraison.demo.config;

import com.livraison.demo.application.service.DeepseekClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DeepseekClient deepseekClient() {
        String apiKey = "sk-a4f186bf4719447c8bdbc0c6397a0617";
        return new DeepseekClient(apiKey);
    }
}
