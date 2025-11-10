package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.DeliveryHistoryDTO;
import com.livraison.demo.application.dto.TourDTO;
import com.livraison.demo.application.mapper.DeliveryHistoryMapper;
import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.optimizer.AIOptimizer;
import com.livraison.demo.domain.entity.DeliveryHistory;
import com.livraison.demo.domain.entity.Tour;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AiController {

    private AIOptimizer aiOptimizer;
    private final DeliveryHistoryMapper mapper;
    private final TourMapper tourMapper;

    public AiController(AIOptimizer aiOptimizer, DeliveryHistoryMapper mapper, TourMapper tourMapper) {
        this.aiOptimizer = aiOptimizer;
        this.mapper = mapper;
        this.tourMapper = tourMapper;
    }

    @PostMapping("/Ask")
    public List<Map<String, Object>> calculateOptimalTour(@RequestBody TourDTO tourDTO) {
        Tour tour = tourMapper.toEntity(tourDTO);
        return aiOptimizer.calculateOptimalTour(tour);
    }

}

