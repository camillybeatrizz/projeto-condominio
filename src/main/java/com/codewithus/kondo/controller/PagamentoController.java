package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import com.codewithus.kondo.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoResponseDTO criar(@Valid @RequestBody PagamentoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<PagamentoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PagamentoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public PagamentoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody PagamentoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
