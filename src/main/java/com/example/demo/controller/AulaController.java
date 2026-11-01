package com.example.demo.controller;

import com.example.demo.entity.Aula;
import com.example.demo.repository.AulaRepository;
import com.example.demo.exception.RecursoNoEncontradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aulas")
@RequiredArgsConstructor
@CrossOrigin
public class AulaController {
    private final AulaRepository repository;

    @GetMapping
    public List<Aula> listar(@RequestParam(required = false) Boolean ordenadores,
                             @RequestParam(required = false) Integer capacidad) {
        if (ordenadores != null && capacidad != null)
            return repository.findByCapacidadGreaterThan(capacidad).stream().filter(a -> a.isOrdenadores() == ordenadores).toList();
        if (ordenadores != null) return repository.findByOrdenadores(ordenadores);
        if (capacidad != null) return repository.findByCapacidadGreaterThan(capacidad);
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Aula obtener(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Aula no encontrada"));
    }

    @PostMapping
    public ResponseEntity<Aula> insertar(@Valid @RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(aula));
    }

    @PutMapping("/{id}")
    public Aula actualizar(@PathVariable Long id, @Valid @RequestBody Aula datos) {
        Aula a = obtener(id);
        a.setNombre(datos.getNombre());
        a.setCapacidad(datos.getCapacidad());
        a.setOrdenadores(datos.isOrdenadores());
        return repository.save(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        repository.delete(obtener(id));
        return ResponseEntity.noContent().build();
    }
}
