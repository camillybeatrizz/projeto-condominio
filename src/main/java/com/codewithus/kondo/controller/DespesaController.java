package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import com.codewithus.kondo.service.DespesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaResponseDTO criar(@Valid @RequestBody DespesaRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<DespesaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public DespesaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public DespesaResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody DespesaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
