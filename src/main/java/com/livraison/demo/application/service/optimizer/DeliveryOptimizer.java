package com.livraison.demo.application.service.optimizer;


import com.livraison.demo.domain.entity.DeliveryHistory;

import java.util.List;

public interface DeliveryOptimizer {
    OptimizedResult optimize(List<DeliveryHistory> histories);
}
