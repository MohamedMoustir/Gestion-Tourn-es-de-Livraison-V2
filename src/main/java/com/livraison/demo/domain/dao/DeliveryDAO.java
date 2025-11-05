package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryDAO extends JpaRepository<Delivery,Integer> {

    Delivery findById(Long id);
//    Page<Delivery> findByWeightKgOrVolumeM3OrStatus(
//            Double  weightKg,
//            Double  volumeM3,
//            DeliveryStatus status,
//            Pageable pageable
//    );

    @Query("SELECT d FROM Delivery d WHERE " +
            "(:weightKg IS NULL OR d.weightKg = :weightKg) " +
            "OR(:volumeM3 IS NULL OR d.volumeM3 = :volumeM3)" +
            "OR (:status IS NULL OR d.status = :status)")

    Page<Delivery> findByWeightKgOrVolumeM3OrStatus(
            @Param("weightKg") double weightKg ,
            @Param("volumeM3") double volumeM3 ,
            @Param("status") DeliveryStatus status ,
            Pageable pageable);
}
