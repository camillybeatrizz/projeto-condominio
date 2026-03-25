package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Pagamento;
import com.codewithus.kondo.domain.entity.WebhookEventoProcessado;
import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.dto.webhook.AsaasPaymentWebhookRequestDTO;
import com.codewithus.kondo.dto.webhook.WebhookProcessamentoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.repository.WebhookEventoProcessadoRepository;
import com.codewithus.kondo.service.AuditoriaService;
import com.codewithus.kondo.service.AsaasWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AsaasWebhookServiceImpl implements AsaasWebhookService {

    private static final String EVENTO_PAGAMENTO_RECEBIDO = "PAYMENT_RECEIVED";
    private static final String TIPO_AUDITORIA_WEBHOOK = "PAGAMENTO_PROCESSADO_POR_WEBHOOK";
    private static final String TIPO_AUDITORIA_COBRANCA_PAGA = "COBRANCA_MARCADA_PAGA";

    private final CobrancaRepository cobrancaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final WebhookEventoProcessadoRepository webhookEventoProcessadoRepository;
    private final AuditoriaService auditoriaService;

    @Override
    public WebhookProcessamentoResponseDTO processar(AsaasPaymentWebhookRequestDTO payload) {
        validarPayload(payload);

        if (webhookEventoProcessadoRepository.findByEventoExternoId(payload.id()).isPresent()) {
            return new WebhookProcessamentoResponseDTO("IGNORED", "Evento ja processado anteriormente");
        }

        if (!EVENTO_PAGAMENTO_RECEBIDO.equals(payload.event())) {
            registrarEvento(payload, "Evento recebido sem acao financeira");
            return new WebhookProcessamentoResponseDTO("IGNORED", "Evento recebido sem acao configurada");
        }

        Cobranca cobranca = localizarCobranca(payload)
                .orElseThrow(() -> new ResourceNotFoundException("Cobranca com referencia externa nao encontrada"));

        if (cobranca.getStatus() == StatusCobrancaEnum.PAGA) {
            registrarEvento(payload, "Evento recebido para cobranca ja paga");
            return new WebhookProcessamentoResponseDTO("IGNORED", "Cobranca ja estava paga");
        }

        pagamentoRepository.findByTransactionId(payload.payment().id())
                .ifPresent(existing -> {
                    throw new BusinessException("Ja existe pagamento registrado para esta transacao externa");
                });

        Pagamento pagamento = new Pagamento();
        pagamento.setCobranca(cobranca);
        pagamento.setTransactionId(payload.payment().id());
        pagamento.setValor(payload.payment().value());
        pagamento.setDataPagamento(payload.payment().clientPaymentDate());
        pagamento.setForma(mapearFormaPagamento(payload.payment().billingType()));
        pagamentoRepository.save(pagamento);

        cobranca.setStatus(StatusCobrancaEnum.PAGA);
        cobrancaRepository.save(cobranca);

        registrarAuditoriaWebHook(payload, cobranca, pagamento);
        registrarEvento(payload, "Pagamento recebido e cobranca baixada");
        return new WebhookProcessamentoResponseDTO("PROCESSED", "Pagamento processado com sucesso");
    }

    private void validarPayload(AsaasPaymentWebhookRequestDTO payload) {
        if (payload == null || payload.id() == null || payload.id().isBlank()) {
            throw new BusinessException("Id do evento do webhook e obrigatorio");
        }

        if (payload.event() == null || payload.event().isBlank()) {
            throw new BusinessException("Tipo do evento do webhook e obrigatorio");
        }

        if (payload.payment() == null || payload.payment().id() == null || payload.payment().id().isBlank()) {
            throw new BusinessException("Pagamento do webhook e obrigatorio");
        }

        if (payload.payment().value() == null) {
            throw new BusinessException("Valor do pagamento do webhook e obrigatorio");
        }

        if (payload.payment().clientPaymentDate() == null) {
            throw new BusinessException("Data do pagamento do webhook e obrigatoria");
        }
    }

    private FormaPagamentoEnum mapearFormaPagamento(String billingType) {
        if (billingType == null || billingType.isBlank()) {
            throw new BusinessException("Forma de pagamento do webhook e obrigatoria");
        }

        String normalized = billingType.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "PIX" -> FormaPagamentoEnum.PIX;
            case "BOLETO" -> FormaPagamentoEnum.BOLETO;
            default -> throw new BusinessException("Forma de pagamento nao suportada no MVP: " + billingType);
        };
    }

    private Optional<Cobranca> localizarCobranca(AsaasPaymentWebhookRequestDTO payload) {
        Optional<Cobranca> porPagamento = cobrancaRepository.findByReferenciaExternaAndDeletedAtIsNull(payload.payment().id());
        if (porPagamento.isPresent()) {
            return porPagamento;
        }

        String paymentLink = payload.payment().paymentLink();
        if (paymentLink != null && !paymentLink.isBlank()) {
            return cobrancaRepository.findByReferenciaExternaAndDeletedAtIsNull(paymentLink.trim());
        }

        return Optional.empty();
    }

    private void registrarEvento(AsaasPaymentWebhookRequestDTO payload, String resumo) {
        WebhookEventoProcessado evento = new WebhookEventoProcessado();
        evento.setProvedor("ASAAS");
        evento.setEventoExternoId(payload.id());
        evento.setTipoEvento(payload.event());
        evento.setPayloadResumo(resumo);
        webhookEventoProcessadoRepository.save(evento);
    }

    private void registrarAuditoriaWebHook(AsaasPaymentWebhookRequestDTO payload, Cobranca cobranca, Pagamento pagamento) {
        auditoriaService.registrar(
                TIPO_AUDITORIA_WEBHOOK,
                "PAGAMENTO",
                pagamento.getId(),
                "ASAAS_WEBHOOK",
                "Pagamento recebido do gateway Asaas para a cobranca " + cobranca.getId() + " com transacao " + pagamento.getTransactionId()
        );

        auditoriaService.registrar(
                TIPO_AUDITORIA_COBRANCA_PAGA,
                "COBRANCA",
                cobranca.getId(),
                "ASAAS_WEBHOOK",
                "Cobranca baixada para PAGA a partir do evento " + payload.id()
        );
    }
}
