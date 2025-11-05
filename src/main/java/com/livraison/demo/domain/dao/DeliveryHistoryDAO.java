package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryHistoryDAO extends JpaRepository<DeliveryHistory ,Integer> {

}
