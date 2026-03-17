package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import com.codewithus.kondo.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FornecedorResponseDTO criar(@Valid @RequestBody FornecedorRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<FornecedorResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public FornecedorResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public FornecedorResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody FornecedorRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
