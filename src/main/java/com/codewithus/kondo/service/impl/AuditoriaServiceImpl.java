package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.AuditoriaEvento;
import com.codewithus.kondo.dto.auditoria.AuditoriaEventoResponseDTO;
import com.codewithus.kondo.repository.AuditoriaEventoRepository;
import com.codewithus.kondo.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaEventoRepository auditoriaEventoRepository;

    @Override
    public void registrar(String tipoEvento, String entidade, UUID entidadeId, String ator, String detalhe) {
        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setTipoEvento(tipoEvento);
        evento.setEntidade(entidade);
        evento.setEntidadeId(entidadeId != null ? entidadeId.toString() : null);
        evento.setAtor(ator);
        evento.setDetalhe(detalhe);
        auditoriaEventoRepository.save(evento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaEventoResponseDTO> listar(Pageable pageable) {
        Pageable pageableOrdenado = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return auditoriaEventoRepository.findAll(pageableOrdenado)
                .map(evento -> new AuditoriaEventoResponseDTO(
                        evento.getId(),
                        evento.getTipoEvento(),
                        evento.getEntidade(),
                        evento.getEntidadeId(),
                        evento.getAtor(),
                        evento.getDetalhe(),
                        evento.getCreatedAt(),
                        evento.getUpdatedAt()
                ));
    }
}
