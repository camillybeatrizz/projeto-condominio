package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.usuario.UsuarioRequestDTO;
import com.codewithus.kondo.dto.usuario.UsuarioResponseDTO;
import com.codewithus.kondo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
