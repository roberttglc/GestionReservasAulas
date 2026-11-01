package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.exception.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
@CrossOrigin
public class ReservaController {
    private final ReservaRepository reservas;
    private final AulaRepository aulas;
    private final ProfesorRepository profesores;
    private final FranjaHorariaRepository franjas;

    @GetMapping
    public List<Reserva> listar() {
        return reservas.findAll();
    }

    @GetMapping("/{id}")
    public Reserva obtener(@PathVariable Long id) {
        return reservas.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada"));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Reserva> insertar(@Valid @RequestBody Reserva r) {
        r.setAula(aula(r.getAula()));
        r.setProfesor(profesor(r.getProfesor()));
        r.setFranja(franja(r.getFranja()));
        validar(r, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservas.save(r));
    }

    @PutMapping("/{id}")
    @Transactional
    public Reserva actualizar(@PathVariable Long id, @Valid @RequestBody Reserva d) {
        Reserva r = obtener(id);
        r.setFechaReserva(d.getFechaReserva());
        r.setAula(aula(d.getAula()));
        r.setProfesor(profesor(d.getProfesor()));
        r.setFranja(franja(d.getFranja()));
        validar(r, id);
        return reservas.save(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        reservas.delete(obtener(id));
        return ResponseEntity.noContent().build();
    }

    private void validar(Reserva r, Long id) {
        if (r.getFechaReserva() == null) throw new IllegalArgumentException("Debe indicar la fecha de reserva");
        if (r.getFechaReserva().isBefore(LocalDate.now()))
            throw new ReservaNoValidaException("La fecha de reserva no puede ser anterior a hoy");
        if (r.getAula() == null || r.getAula().getId() == null || r.getFranja() == null || r.getFranja().getId() == null)
            throw new IllegalArgumentException("Debe indicar aula y franja horaria");
        boolean ocupada = reservas.existsOverlappingReserva(
                r.getFechaReserva(),
                r.getAula().getId(),
                r.getFranja().getHoraInicio(),
                r.getFranja().getHoraFin(),
                id);
        if (ocupada) throw new ReservaNoValidaException("La franja horaria ya está ocupada para esa aula y fecha");
    }

    private Aula aula(Aula a) {
        if (a == null || a.getId() == null) throw new IllegalArgumentException("Debe indicar un aula");
        return aulas.findById(a.getId()).orElseThrow(() -> new RecursoNoEncontradoException("Aula no encontrada"));
    }

    private Profesor profesor(Profesor p) {
        if (p == null || p.getId() == null) throw new IllegalArgumentException("Debe indicar un profesor");
        return profesores.findById(p.getId()).orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado"));
    }

    private FranjaHoraria franja(FranjaHoraria f) {
        if (f == null || f.getId() == null) throw new IllegalArgumentException("Debe indicar una franja horaria");
        return franjas.findById(f.getId()).orElseThrow(() -> new RecursoNoEncontradoException("Franja horaria no encontrada"));
    }
}
