package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.integration.AsaasCobrancaCommand;
import com.codewithus.kondo.dto.integration.AsaasCobrancaResult;
import com.codewithus.kondo.dto.integration.AsaasPixQrCodeResult;

public interface AsaasCobrancaGateway {

    AsaasCobrancaResult criarCobranca(AsaasCobrancaCommand command);

    AsaasPixQrCodeResult consultarPixQrCode(String referenciaExterna);
}
