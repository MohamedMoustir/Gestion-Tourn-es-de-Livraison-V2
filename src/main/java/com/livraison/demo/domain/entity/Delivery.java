package com.livraison.demo.domain.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livraison.demo.domain.enums.DeliveryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La latitude est obligatoire")
    @DecimalMin(value="-90.0")
    @DecimalMax(value="90.0")
    private double latitude;

    @NotNull(message = "La longitude est obligatoire")
    @DecimalMin(value="-180.0")
    @DecimalMax(value="180.0")
    private double longitude;

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit être supérieur à 0")
    private double weightKg;

    @NotNull(message = "Le volume est obligatoire")
    @Positive(message = "Le volume doit être supérieur à 0")
    private double volumeM3;

    @NotBlank(message = "Le créneau horaire est obligatoire")
    private String timeSlot;

    @NotNull(message = "Le statut de la livraison est obligatoire")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ManyToOne
    @JoinColumn(name="tour_id")
    @NotNull(message = "La tournée associée est obligatoire")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "customer_id" ,nullable = false)
    private Customer  customer ;
}
