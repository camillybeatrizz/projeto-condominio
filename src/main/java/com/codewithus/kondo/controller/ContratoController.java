package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;
import com.codewithus.kondo.service.ContratoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratoResponseDTO criar(@Valid @RequestBody ContratoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<ContratoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ContratoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ContratoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody ContratoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
