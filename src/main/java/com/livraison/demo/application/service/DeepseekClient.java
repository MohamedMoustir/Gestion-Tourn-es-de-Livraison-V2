package com.livraison.demo.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livraison.demo.application.service.optimizer.TourOptimizer;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
@ConditionalOnProperty(name = "optimizer.type", havingValue = "ai")
public class DeepseekClient implements TourOptimizer {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String apiUrl = "https://api.deepseek.com/chat/completions";
    private final String apiKey;

    public DeepseekClient(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;

        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public List<Map<String, Object>> calculateOptimalTour(Tour tour) {
        try {
            String deliveryJson = convertToJson(tour.getDeliveries());

            String prompt = String.format(
                    "You are an expert in logistics optimization. " +
                            "Your task is to reorder the following list of deliveries in the best possible way to minimize total distance and delivery time. " +
                            "Starting point (warehouse): %s (lat: %s, lon: %s). " +
                            "Vehicle: %s (max weight: %s kg, max volume: %s m3). " +
                            "Deliveries to be optimized (JSON): %s. " +
                            "Please return strictly JSON with two fields: " +
                            "1. orderedDeliveryIds (array of Long), " +
                            "2. recommendations (string explaining reasoning)." +
                            "3. predictedBestRoutes(array of predicted routes with details,)",
                    tour.getWarehouse().getName(),
                    tour.getWarehouse().getLatitude(),
                    tour.getWarehouse().getLongitude(),
                    tour.getVehicle().getType(),
                    tour.getVehicle().getMaxWeightKg(),
                    tour.getVehicle().getMaxVolumeM3(),
                    deliveryJson
            );

            Map<String, Object> requestBody = Map.of(
                    "model", "deepseek-chat",
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a helpful assistant."),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "stream", false
            );

            var headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            var entity = new org.springframework.http.HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);
            System.out.println("=========================> " + response);

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> aiOutput = Map.of(
                    "orderedDeliveryIds", List.of(),
                    "recommendations", "AI response could not be parsed as JSON."
            );

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    String content = ((String) message.get("content")).trim();
                    content = content.replaceAll("```json|```", "").trim(); // تنظيف المحتوى
                    if (!content.isEmpty()) {
                        try {
                            aiOutput = mapper.readValue(content, Map.class);
                        } catch (Exception ex) {
                            System.err.println("Failed to parse AI response as JSON: " + ex.getMessage());
                        }
                    }
                }
            }

            List<Long> orderedIds = new ArrayList<>();
            Object rawIds = aiOutput.get("orderedDeliveryIds");
            if (rawIds instanceof List<?> list) {
                for (Object o : list) {
                    if (o instanceof Number n) {
                        orderedIds.add(n.longValue());
                    }
                }
            }

            Map<Long, Delivery> deliveryMap = tour.getDeliveries().stream()
                    .collect(Collectors.toMap(
                            d -> d.getId() != null ? d.getId() : UUID.randomUUID().getMostSignificantBits(),
                            d -> d,
                            (existing, replacement) -> existing
                    ));

            List<Map<String, Object>> orderedDeliveries = new ArrayList<>();
            int pos = 1;
            for (Long id : orderedIds) {
                Delivery dh = deliveryMap.get(id);
                if (dh != null) {
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
            }

            Map<String, Object> tourMap = new HashMap<>();
            tourMap.put("tourId", tour.getId());
            tourMap.put("optimizationMethod", "AI");
            tourMap.put("recommendations", aiOutput.getOrDefault("recommendations", ""));
            tourMap.put("orderedDeliveries", orderedDeliveries);
            tourMap.put("predictedBestRoutes", aiOutput.getOrDefault("predictedBestRoutes", List.of()));

            return List.of(tourMap);

        } catch (Exception e) {
            throw new RuntimeException("AIOptimizer failed: " + e.getMessage(), e);
        }
    }

    private String convertToJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (Exception e) {
            System.err.println("Failed to convert object to JSON: " + e.getMessage());
            return "[]";
        }
    }

}
