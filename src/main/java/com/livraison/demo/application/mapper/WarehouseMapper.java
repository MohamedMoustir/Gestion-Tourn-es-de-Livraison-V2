package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.WarehouseDTO;
import com.livraison.demo.domain.entity.Warehouse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class WarehouseMapper {

    public WarehouseDTO toDTO(Warehouse warehouse){
        if(warehouse == null) return null;
        return WarehouseDTO.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .address(warehouse.getAddress())
                .latitude(warehouse.getLatitude())
                .longitude(warehouse.getLongitude())
                .openingTime(warehouse.getOpeningTime())
                .closingTime(warehouse.getClosingTime())
                .tourIds(warehouse.getTours() != null
                        ? warehouse.getTours().stream().map(t -> Math.toIntExact(t.getId())).collect(Collectors.toList())
                        : null)
                .build();
    }

    public Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) return null;

        return Warehouse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .build();
    }
}
