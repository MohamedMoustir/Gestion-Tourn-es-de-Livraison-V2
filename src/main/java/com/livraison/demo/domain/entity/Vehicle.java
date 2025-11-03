package com.livraison.demo.domain.entity;


import com.livraison.demo.domain.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Le numéro d'immatriculation ne peut pas être vide")
    @Pattern(
            regexp = "^[A-Z0-9-]{5,15}$",
            message = "Le format du numéro d'immatriculation est invalide"
    )
    @Column(name="registration_number" ,unique=true)
    private String registrationNumber;

    @NotBlank(message = "Le modèle du véhicule est obligatoire")
    @Size(min = 2, max = 50, message = "Le modèle doit contenir entre 2 et 50 caractères")
    private String model;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type du véhicule est obligatoire")
    private VehicleType type;


    private Double maxWeightKg;
    private Double maxVolumeM3;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Tour> tours;


}
