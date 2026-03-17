package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import com.codewithus.kondo.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChamadoResponseDTO criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<ChamadoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ChamadoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ChamadoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody ChamadoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
