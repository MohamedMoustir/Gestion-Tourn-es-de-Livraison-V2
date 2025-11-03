package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import com.livraison.demo.domain.entity.Warehouse;

import java.util.List;
import java.util.Map;

public interface TourOptimizer {
    List<Map<String, Object>>  calculateOptimalTour(Tour tour);
}
