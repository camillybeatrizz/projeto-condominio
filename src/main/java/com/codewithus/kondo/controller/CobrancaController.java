package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import com.codewithus.kondo.service.CobrancaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
public class CobrancaController {

    private final CobrancaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CobrancaResponseDTO criar(@Valid @RequestBody CobrancaRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<CobrancaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CobrancaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CobrancaResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody CobrancaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
