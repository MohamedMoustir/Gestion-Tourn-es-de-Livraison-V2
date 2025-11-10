package com.livraison.demo.application.service.optimizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.domain.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@ConditionalOnProperty(name="optimizer.type" , havingValue ="ai")
public class AIOptimizer implements TourOptimizer  {
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AIOptimizer(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/responses")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }



    @Override
    public List<Map<String, Object>> calculateOptimalTour(Tour tour) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String deliveryHistoryJson = mapper.writeValueAsString(tour);

            String prompt = String.format("""
Tu es un expert en optimisation de tournées de livraison.
Analyse attentivement le JSON suivant :
%s

Objectif :
Optimiser l'ordre des livraisons (optimizedDeliveries) selon la logique suivante :
- Minimiser la distance totale du trajet.
- Respecter les créneaux horaires (timeSlot) si disponibles.
- Prendre en compte le poids et le volume pour éviter une surcharge.
- Prioriser les livraisons proches les unes des autres.

Répond STRICTEMENT en JSON avec ce format exact :
{
    "optimizedDeliveries": [
        {
            "id": 1,
            "address": "string",
            "planned_time": [8,30],
            "actual_time": [10,30],
            "delay": "string",
            "dayOfWeek": "string",
            "position": 1
        }
    ]
}

Important :
- Ne change pas les clés JSON.
- Les heures doivent être sous forme [heure, minute].
- "position" doit indiquer la nouvelle position optimisée dans la tournée.
- Si certaines informations manquent, fais des estimations cohérentes.
""", deliveryHistoryJson);

            Map<String, Object> response = webClient.post()
                    .bodyValue(Map.of("model", "gpt-5-nano", "input", prompt, "store", true))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourId", tour.getId());
            tourMap.put("optimizationMethod", "AI");

            List<Map<String, Object>> orderedDeliveries = new ArrayList<>();
            int pos = 1;
            for (Delivery dh : tour.getDeliveries()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", dh.getId());
                map.put("position", pos++);
                map.put("latitude", dh.getLatitude());
                map.put("longitude", dh.getLongitude());
                map.put("weightKg", dh.getWeightKg());
                map.put("volumeM3", dh.getVolumeM3());
                map.put("timeSlot", dh.getTimeSlot());
                map.put("status", dh.getStatus());
                orderedDeliveries.add(map);
            }

            tourMap.put("orderedDeliveries", orderedDeliveries);

            Map<String, Object> aiOutput = new HashMap<>();
            try {
                if (response != null && response.get("output") instanceof List<?> list && !list.isEmpty()) {
                    Object lastItem = list.get(list.size() - 1);
                    if (lastItem instanceof Map<?, ?> lastMap) {
                        Object content = lastMap.get("content");
                        if (content instanceof List<?> contentList && !contentList.isEmpty()) {
                            Object textObj = ((Map<?, ?>) contentList.get(0)).get("text");
                            if (textObj instanceof String text) {
                                aiOutput = mapper.readValue(text, Map.class);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("⚠ Failed to parse AI output JSON: " + e.getMessage());
            }

            tourMap.put("recommendations", aiOutput.getOrDefault("recommendations", List.of()));
            tourMap.put("predictedBestRoutes", aiOutput.getOrDefault("predictedBestRoutes", List.of()));

            return List.of(tourMap);

        } catch (Exception e) {
            throw new RuntimeException("❌ AIOptimizer failed: " + e.getMessage(), e);
        }
    }

}


