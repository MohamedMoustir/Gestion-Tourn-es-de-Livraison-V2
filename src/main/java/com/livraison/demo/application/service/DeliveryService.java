package com.livraison.demo.application.service;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.application.mapper.DeliveryMapper;
import com.livraison.demo.domain.dao.DeliveryDAO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.enums.DeliveryStatus;
import com.livraison.demo.domain.exception.DeliveryNotDeletedException;
import com.livraison.demo.domain.exception.VehicleFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {


        private final DeliveryDAO deliveryDAO;
        private final DeliveryMapper deliveryMapper;
        private final DeliveryHistoryService deliveryHistoryService ;

        private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryMapper.class);
    @Autowired
    public DeliveryService(DeliveryDAO deliveryDAO, DeliveryMapper deliveryMapper, DeliveryHistoryService deliveryHistoryService) {
            this.deliveryDAO = deliveryDAO;
            this.deliveryMapper = deliveryMapper;
           this.deliveryHistoryService = deliveryHistoryService;
    }

        public List<DeliveryDTO> rechercherdelivery() {
            return deliveryDAO.findAll().stream().map(deliveryMapper::toDTO)
                    .collect(Collectors.toList());
        }

    public List<DeliveryDTO> searchDeliveries(int pageNumber , int pageSize ,double weightKg,double volumeM3,DeliveryStatus status) {
        Pageable pageable = PageRequest.of(pageNumber , pageSize);
        Page<Delivery> deliveries =  deliveryDAO.findByWeightKgOrVolumeM3OrStatus( weightKg,volumeM3,status,pageable);
        return deliveries.stream().map(deliveryMapper::toDTO)
                .collect(Collectors.toList());
    }

        public DeliveryDTO getDeliveryById(Long id) {
            Delivery delivery = this.deliveryDAO.findById(id);
            return deliveryMapper.toDTO(delivery);
        }

        public void deleteDeliveryById(Long id) {
            try{
                this.deliveryDAO.deleteById(Math.toIntExact(id));
               LOGGER.info("suppression Delivery avec successful avec id : {}", id);

            }catch(Exception e){
                throw new DeliveryNotDeletedException("Error sur suppression Delivery ");
            }
        }

        public Delivery updateDelivery(Delivery delivery, Long id) {
            return this.deliveryDAO.findById(Math.toIntExact(id)).
                    map(existingDelivry ->{
                        existingDelivry.setLatitude(delivery.getLatitude());
                        existingDelivry.setLongitude(delivery.getLongitude());
                        existingDelivry.setStatus(delivery.getStatus());
                        existingDelivry.setTimeSlot(delivery.getTimeSlot());
                        existingDelivry.setVolumeM3(delivery.getVolumeM3());
                        existingDelivry.setWeightKg(delivery.getWeightKg());
                        return this.deliveryDAO.save(existingDelivry);
                    })
                    .orElseThrow(()->{
                        LOGGER.warn("Erreur lors de la mise à jour du delivery avec id: {}", delivery.getId());
                        new VehicleFoundException(
                                "Vehicle with id " + id + " does not exist"
                        );
                        return null;
                    });
        }

    public void updateStatus(Long id) {
        Delivery delivery = this.deliveryDAO.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setStatus(DeliveryStatus.COMPLETED);
        deliveryDAO.save(delivery);
        deliveryHistoryService.createDeliveryHistory(delivery);
    }

}
