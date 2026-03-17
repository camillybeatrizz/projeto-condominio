package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.endereco.EnderecoRequestDTO;
import com.codewithus.kondo.dto.endereco.EnderecoResponseDTO;
import com.codewithus.kondo.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnderecoResponseDTO criar(@Valid @RequestBody EnderecoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<EnderecoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public EnderecoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public EnderecoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody EnderecoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
