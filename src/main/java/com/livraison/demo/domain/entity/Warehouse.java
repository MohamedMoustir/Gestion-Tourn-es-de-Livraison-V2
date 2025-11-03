package com.livraison.demo.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'entrepôt est obligatoire.")
    private String name;

    @NotBlank(message = "L'adresse de l'entrepôt est obligatoire.")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères.")
    private String address;

    @NotNull(message = "La latitude est obligatoire.")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;


    @NotNull(message = "La longitude est obligatoire.")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @NotNull(message = "L'heure d'ouverture est obligatoire.")
    private LocalTime openingTime;

    @NotNull(message = "L'heure de fermeture est obligatoire.")
    private LocalTime closingTime;

    @OneToMany(mappedBy="warehouse", cascade = CascadeType.ALL)
    private List<Tour> tours;
}
