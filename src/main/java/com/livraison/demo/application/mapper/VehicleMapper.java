package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.VehicleDTO;
import com.livraison.demo.domain.entity.Vehicle;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class VehicleMapper {

        public VehicleDTO toDTO(Vehicle vehicle){
            if(vehicle == null) return null;
            return VehicleDTO.builder()
                    .id(vehicle.getId())
                    .registrationNumber(vehicle.getRegistrationNumber())
                    .model(vehicle.getModel())
                    .type(vehicle.getType() != null ? vehicle.getType().name():null)
                    .maxWeightKg(vehicle.getMaxWeightKg())
                    .maxVolumeM3(vehicle.getMaxVolumeM3())
                    .tourIds(vehicle.getTours() != null
                            ? vehicle.getTours().stream().map(t ->Math.toIntExact(t.getId())).collect(Collectors.toList())
                            : null)
                    .build();


        }

    public Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) return null;

        return Vehicle.builder()
                .id(dto.getId())
                .registrationNumber(dto.getRegistrationNumber())
                .model(dto.getModel())
                .type(dto.getType() != null ? Enum.valueOf(com.livraison.demo.domain.enums.VehicleType.class, dto.getType()) : null)
                .maxWeightKg(dto.getMaxWeightKg())
                .maxVolumeM3(dto.getMaxVolumeM3())
                .build();
    }
}
