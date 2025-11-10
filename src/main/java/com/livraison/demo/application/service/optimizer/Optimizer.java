package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.DeliveryHistory;
import reactor.core.publisher.Mono;

import java.util.List;

public interface Optimizer {
    Mono<String> optimize(List<DeliveryHistory> deliveryHistories);
}
