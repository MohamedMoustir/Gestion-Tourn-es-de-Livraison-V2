package com.livraison.demo.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="deliveryHistory")
public class DeliveryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Delivery delivery;
    @ManyToOne
    private Tour tour ;

    @NotNull(message = "La planned time est obligatoire")
    private LocalTime planned_time ;

    @NotNull(message = "La actual time est obligatoire")
    private LocalTime actual_time;

    @NotNull(message = " delay est obligatoire")
    private String delay ;

    @NotNull(message = "La day est obligatoire")
    private String dayOfWeek ;

    @NotNull(message = "La address est obligatoire")
    private String address ;




}
