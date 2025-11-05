package com.livraison.demo.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotNull(message = "Le name est obligatoire")
    private String name ;
    @NotNull(message = "Le address est obligatoire")
    private String address;

    @NotNull(message = "La latitude est obligatoire.")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private double latitude;

    @NotNull(message = "La longitude est obligatoire.")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private double longitude;

    @NotNull(message = "Le créneau préféré est obligatoire.")
    private String preferredTimeSlot;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> delivers;
}
