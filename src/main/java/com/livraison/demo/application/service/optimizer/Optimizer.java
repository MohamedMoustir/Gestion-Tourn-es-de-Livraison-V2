package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.DeliveryHistory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface Optimizer {
    Mono<List<Map<String, Object>>> optimize(List<DeliveryHistory> deliveryHistories);
}
