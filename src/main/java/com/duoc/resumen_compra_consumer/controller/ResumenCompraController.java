package com.duoc.resumen_compra_consumer.controller;

import com.duoc.resumen_compra_consumer.model.ResumenCompra;
import com.duoc.resumen_compra_consumer.repository.ResumenCompraRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resumenes")
public class ResumenCompraController {

    private final ResumenCompraRepository resumenCompraRepository;

    public ResumenCompraController(ResumenCompraRepository resumenCompraRepository) {
        this.resumenCompraRepository = resumenCompraRepository;
    }

    // GET /resumenes -> lista todos los resumenes procesados
    @GetMapping
    public ResponseEntity<List<ResumenCompra>> listarTodos() {
        return ResponseEntity.ok(resumenCompraRepository.findAll());
    }

    // GET /resumenes/{inscripcionId} -> busca el resumen de una inscripción específica
    @GetMapping("/{inscripcionId}")
    public ResponseEntity<ResumenCompra> buscarPorInscripcion(@PathVariable Long inscripcionId) {
        return resumenCompraRepository.findAll().stream()
                .filter(r -> r.getInscripcionId().equals(inscripcionId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}