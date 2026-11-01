package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"fecha_reserva", "aula_id", "franja_id"}))
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRealizacion;
    @ManyToOne(optional = false)
    @JoinColumn(name = "aula_id")
    private Aula aula;
    @ManyToOne(optional = false)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "franja_id")
    private FranjaHoraria franja;

    @PrePersist
    void establecerFechaRealizacion() {
        if (fechaRealizacion == null) fechaRealizacion = LocalDateTime.now();
    }
}
