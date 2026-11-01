package com.example.demo.repository;

import com.example.demo.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByFechaReservaAndAulaIdAndFranjaId(LocalDate fecha, Long aulaId, Long franjaId);

    boolean existsByFechaReservaAndAulaIdAndFranjaIdAndIdNot(LocalDate fecha, Long aulaId, Long franjaId, Long id);

    @Query("""
            select case when count(r) > 0 then true else false end
            from Reserva r
            where r.fechaReserva = :fecha
              and r.aula.id = :aulaId
              and r.franja.horaInicio < :horaFin
              and r.franja.horaFin > :horaInicio
              and (:id is null or r.id <> :id)
            """)
    boolean existsOverlappingReserva(@Param("fecha") LocalDate fecha,
                                     @Param("aulaId") Long aulaId,
                                     @Param("horaInicio") LocalTime horaInicio,
                                     @Param("horaFin") LocalTime horaFin,
                                     @Param("id") Long id);
}
