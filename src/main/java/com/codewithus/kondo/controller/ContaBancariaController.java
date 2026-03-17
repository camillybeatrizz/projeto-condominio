package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;
import com.codewithus.kondo.service.ContaBancariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas-bancarias")
@RequiredArgsConstructor
public class ContaBancariaController {

    private final ContaBancariaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaBancariaResponseDTO criar(@Valid @RequestBody ContaBancariaRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<ContaBancariaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ContaBancariaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ContaBancariaResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody ContaBancariaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
