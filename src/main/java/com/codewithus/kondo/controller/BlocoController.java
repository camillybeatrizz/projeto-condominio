package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import com.codewithus.kondo.service.BlocoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blocos")
@RequiredArgsConstructor
public class BlocoController {

    private final BlocoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlocoResponseDTO criar(@Valid @RequestBody BlocoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<BlocoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public BlocoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public BlocoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody BlocoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
