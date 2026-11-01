package com.example.demo.controller;

import com.example.demo.entity.FranjaHoraria;
import com.example.demo.repository.FranjaHorariaRepository;
import com.example.demo.exception.RecursoNoEncontradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franjas")
@RequiredArgsConstructor
@CrossOrigin
public class FranjaHorariaController {
    private final FranjaHorariaRepository repository;

    @GetMapping
    public List<FranjaHoraria> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public FranjaHoraria obtener(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Franja horaria no encontrada"));
    }

    @PostMapping
    public ResponseEntity<FranjaHoraria> insertar(@Valid @RequestBody FranjaHoraria f) {
        if (!f.getHoraInicio().isBefore(f.getHoraFin()))
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(f));
    }

    @PutMapping("/{id}")
    public FranjaHoraria actualizar(@PathVariable Long id, @Valid @RequestBody FranjaHoraria d) {
        if (!d.getHoraInicio().isBefore(d.getHoraFin()))
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        FranjaHoraria f = obtener(id);
        f.setHoraInicio(d.getHoraInicio());
        f.setHoraFin(d.getHoraFin());
        return repository.save(f);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        repository.delete(obtener(id));
        return ResponseEntity.noContent().build();
    }
}
