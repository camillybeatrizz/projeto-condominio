package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.config.AsaasIntegrationProperties;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.integration.AsaasCobrancaCommand;
import com.codewithus.kondo.dto.integration.AsaasCobrancaResult;
import com.codewithus.kondo.dto.integration.AsaasPixQrCodeResult;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.service.AsaasCobrancaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AsaasCobrancaGatewayImpl implements AsaasCobrancaGateway {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final AsaasIntegrationProperties properties;
    private final UsuarioRepository usuarioRepository;

    @Override
    public AsaasCobrancaResult criarCobranca(AsaasCobrancaCommand command) {
        if (!properties.isEnabled() || properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            String referenciaSimulada = "asaas-sim-" + command.cobrancaId();
            return new AsaasCobrancaResult(
                    referenciaSimulada,
                    properties.getBaseUrl() + "/simulacao/cobrancas/" + referenciaSimulada
            );
        }

        RestClient restClient = criarRestClient();
        String customerId = obterOuCriarCustomer(restClient, command);

        AsaasCreatePaymentResponse response = restClient.post()
                .uri("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "customer", customerId,
                        "billingType", "PIX",
                        "value", command.valor(),
                        "dueDate", DATE_FORMATTER.format(command.vencimento()),
                        "description", montarDescricao(command),
                        "externalReference", command.cobrancaId().toString()
                ))
                .retrieve()
                .body(AsaasCreatePaymentResponse.class);

        if (response == null || response.id() == null || response.id().isBlank()) {
            throw new BusinessException("Nao foi possivel criar cobranca no Asaas");
        }

        return new AsaasCobrancaResult(response.id(), response.invoiceUrl());
    }

    @Override
    public AsaasPixQrCodeResult consultarPixQrCode(String referenciaExterna) {
        if (referenciaExterna == null || referenciaExterna.isBlank()) {
            throw new BusinessException("Referencia externa da cobranca e obrigatoria para consultar QR Code Pix");
        }

        if (!properties.isEnabled() || properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            return new AsaasPixQrCodeResult(
                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8Xw8AApMBgU7n0S8AAAAASUVORK5CYII=",
                    "00020101021226890014br.gov.bcb.pix2567sandbox.kondo.local/pix/" + referenciaExterna + "520400005303986540410.005802BR5911KONDO6009FORTALEZA62070503***6304ABCD",
                    java.time.LocalDate.now().plusMonths(12).atTime(23, 59, 59)
            );
        }

        RestClient restClient = criarRestClient();
        AsaasPixQrCodeResponse response = restClient.get()
                .uri("/payments/{id}/pixQrCode", referenciaExterna)
                .retrieve()
                .body(AsaasPixQrCodeResponse.class);

        if (response == null || response.payload() == null || response.payload().isBlank()) {
            throw new BusinessException("Nao foi possivel consultar QR Code Pix no Asaas");
        }

        return new AsaasPixQrCodeResult(
                response.encodedImage(),
                response.payload(),
                response.expirationDate()
        );
    }

    private RestClient criarRestClient() {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("access_token", properties.getApiKey())
                .build();
    }

    private String obterOuCriarCustomer(RestClient restClient, AsaasCobrancaCommand command) {
        if (command.usuarioId() == null) {
            throw new BusinessException("A unidade precisa ter um morador vinculado para criar cobranca no Asaas");
        }

        Usuario usuario = usuarioRepository.findById(command.usuarioId())
                .orElseThrow(() -> new BusinessException("Morador vinculado nao encontrado para integracao com Asaas"));

        if (usuario.getAsaasCustomerId() != null && !usuario.getAsaasCustomerId().isBlank()) {
            return usuario.getAsaasCustomerId();
        }

        if (command.pagadorNome() == null || command.pagadorNome().isBlank()) {
            throw new BusinessException("Morador precisa ter nome preenchido para criar cobranca no Asaas");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", command.pagadorNome().trim());
        payload.put("externalReference", command.usuarioId().toString());

        if (command.pagadorEmail() != null && !command.pagadorEmail().isBlank()) {
            payload.put("email", command.pagadorEmail().trim());
        }

        if (command.pagadorTelefone() != null && !command.pagadorTelefone().isBlank()) {
            payload.put("mobilePhone", command.pagadorTelefone().trim());
        }

        AsaasCreateCustomerResponse response = restClient.post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(AsaasCreateCustomerResponse.class);

        if (response == null || response.id() == null || response.id().isBlank()) {
            throw new BusinessException("Nao foi possivel criar cliente no Asaas");
        }

        usuario.setAsaasCustomerId(response.id());
        usuarioRepository.save(usuario);
        return response.id();
    }

    private String montarDescricao(AsaasCobrancaCommand command) {
        StringBuilder descricao = new StringBuilder("Condominio - competencia ").append(command.competencia());
        if (command.unidadeId() != null) {
            descricao.append(" - unidade ").append(command.unidadeId());
        }
        return descricao.toString();
    }

    private record AsaasCreatePaymentResponse(String id, String invoiceUrl) {
    }

    private record AsaasCreateCustomerResponse(String id) {
    }

    private record AsaasPixQrCodeResponse(
            String encodedImage,
            String payload,
            java.time.LocalDateTime expirationDate
    ) {
    }
}
