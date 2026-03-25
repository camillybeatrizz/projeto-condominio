package com.codewithus.kondo.dto.cobranca;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record CobrancaPixResponseDTO(
        @Schema(description = "Identificador interno da cobranca no Kondo.", example = "aa0e8400-e29b-41d4-a716-446655440005")
        UUID cobrancaId,
        @Schema(description = "Referencia externa da cobranca no gateway Asaas. Normalmente corresponde ao ID do pagamento no provedor.", example = "pay_000123456")
        String referenciaExterna,
        @Schema(description = "URL publica para pagamento hospedada pelo gateway. O frontend pode abrir este link como alternativa ao fluxo Pix embutido.", example = "https://sandbox.asaas.com/i/pay_000123456")
        String urlPagamentoExterno,
        @Schema(description = "Payload Pix no formato copia-e-cola. Deve ser oferecido em botao de copiar no frontend.", example = "00020101021226890014br.gov.bcb.pix2567pix-h.example.com/qr/v2/cob/abc123520400005303986540410.005802BR5913KONDO TESTE6008BRASILIA62070503***6304ABCD")
        String pixCopiaCola,
        @Schema(description = "Imagem do QR Code Pix em Base64, sem prefixo data URI. O frontend pode montar a imagem com data:image/png;base64,<valor>.", example = "iVBORw0KGgoAAAANSUhEUgAA...")
        String pixQrCodeBase64,
        @Schema(description = "Data e hora limite de validade do QR Code Pix, quando informada pelo gateway.", example = "2026-04-10T23:59:59")
        LocalDateTime pixExpiracao
) {
}
