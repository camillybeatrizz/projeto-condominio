package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import com.codewithus.kondo.service.UnidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadeController {

    private final UnidadeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnidadeResponseDTO criar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<UnidadeResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UnidadeResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UnidadeResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody UnidadeRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
