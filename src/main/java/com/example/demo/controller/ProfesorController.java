package com.example.demo.controller;

import com.example.demo.entity.Profesor;
import com.example.demo.repository.ProfesorRepository;
import com.example.demo.exception.RecursoNoEncontradoException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
@CrossOrigin
public class ProfesorController {
    private final ProfesorRepository repository;

    @GetMapping
    public List<Profesor> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Profesor obtener(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Profesor> insertar(@Valid @RequestBody Profesor p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(p));
    }

    @PutMapping("/{id}")
    public Profesor actualizar(@PathVariable Long id, @Valid @RequestBody Profesor d) {
        Profesor p = obtener(id);
        p.setNombre(d.getNombre());
        p.setEmail(d.getEmail());
        return repository.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        repository.delete(obtener(id));
        return ResponseEntity.noContent().build();
    }
}
