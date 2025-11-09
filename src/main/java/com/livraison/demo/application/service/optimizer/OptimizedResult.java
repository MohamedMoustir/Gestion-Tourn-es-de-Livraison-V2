package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptimizedResult {
    private List<Delivery> orderedDeliveries;
    private List<String> recommendations;
    private List<Map<String, Object>> predictedBestRoutes;


}
