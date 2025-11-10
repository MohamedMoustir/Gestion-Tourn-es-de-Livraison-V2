package com.livraison.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        boolean exists = context.containsBean("aIOptimizer");
        logger.info("AiService bean exists? {}", exists);

        if (exists) {
            logger.info(" Envoi de la requête à GPT...");
        } else {
            logger.error("AiService introuvable !");
        }
    }
}
