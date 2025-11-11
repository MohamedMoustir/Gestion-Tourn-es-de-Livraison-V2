package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.TourDTO;
import com.livraison.demo.application.mapper.DeliveryHistoryMapper;
import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.DeepseekClient;
import com.livraison.demo.application.service.optimizer.AIOptimizer;
import com.livraison.demo.domain.entity.Tour;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deepseek")
public class DeepseekController {

    private final DeepseekClient deepseekClient;
    private final DeliveryHistoryMapper mapper;
    private final TourMapper tourMapper;
    public DeepseekController(DeepseekClient deepseekClient, DeliveryHistoryMapper mapper, TourMapper tourMapper) {
        this.deepseekClient = deepseekClient;
        this.mapper = mapper;
        this.tourMapper = tourMapper;
    }


    @PostMapping("/chat")
    public List<Map<String, Object>> calculateOptimalTour(@RequestBody TourDTO tourDTO) {
        Tour tour = tourMapper.toEntity(tourDTO);
        return deepseekClient.calculateOptimalTour(tour);
    }
}
