package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DeliveryHistoryDAO extends JpaRepository<DeliveryHistory ,Integer> {

}
