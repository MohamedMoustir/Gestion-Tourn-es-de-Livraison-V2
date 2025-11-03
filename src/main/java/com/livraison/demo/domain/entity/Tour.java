package com.livraison.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NotNull(message = "La date de la tournée ne doit pas être nulle.")
    private LocalDateTime tour_day  ;

    @Min(value = 0, message = "La distance totale ne peut pas être négative.")
    private double total_distance_km ;

    @NotNull(message = "L'entrepôt associé est obligatoire.")
    @ManyToOne
    @JoinColumn(name="warehouse_id")
    private Warehouse warehouse ;

    @NotNull(message = "Le véhicule associé est obligatoire.")
    @ManyToOne
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle ;

    @OneToMany(mappedBy="tour",cascade=CascadeType.ALL)
    private List<Delivery> deliveries ;
}
