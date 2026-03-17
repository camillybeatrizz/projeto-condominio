package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;
import com.codewithus.kondo.service.CondominioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/condominios")
@RequiredArgsConstructor
public class CondominioController {

    private final CondominioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CondominioResponseDTO criar(@Valid @RequestBody CondominioRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<CondominioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public CondominioResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CondominioResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody CondominioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
