package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.acesso.AcessoRequestDTO;
import com.codewithus.kondo.dto.acesso.AcessoResponseDTO;
import com.codewithus.kondo.service.AcessoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/acessos")
@RequiredArgsConstructor
public class AcessoController {

    private final AcessoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcessoResponseDTO criar(@Valid @RequestBody AcessoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<AcessoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public AcessoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AcessoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody AcessoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
