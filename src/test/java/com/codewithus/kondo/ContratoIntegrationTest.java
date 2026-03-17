package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContratoIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveCriarContratoComFornecedorECondominioExistentes() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Av. Litoranea");
        endereco.setNumero("500");
        endereco.setComplemento("Sala 1");
        endereco.setBairro("Praia");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        String condominioPayload = """
                {
                  "nome": "Condominio Atlântico",
                  "cnpj": "98.765.432/0001-10",
                  "telefone": "(83) 3222-1000",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());

        String condominioResponse = postAndReturnBody("/condominios", condominioPayload);
        String condominioId = jsonField(condominioResponse, "id");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Servicos Gerais LTDA");
        fornecedor.setCnpj("11.222.333/0001-44");
        fornecedor.setTelefone("(83) 3000-2000");
        fornecedor = fornecedorRepository.save(fornecedor);

        String payload = """
                {
                  "descricao": "Manutenção de elevadores",
                  "valor": 1200.00,
                  "dataInicio": "2026-03-01",
                  "dataFim": "2026-12-31",
                  "fornecedorId": "%s",
                  "condominioId": "%s"
                }
                """.formatted(fornecedor.getId(), condominioId);

        mockMvc.perform(post("/contratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.descricao").value("Manutenção de elevadores"))
                .andExpect(jsonPath("$.valor").value(1200.00))
                .andExpect(jsonPath("$.dataInicio").value("2026-03-01"))
                .andExpect(jsonPath("$.dataFim").value("2026-12-31"))
                .andExpect(jsonPath("$.fornecedorId").value(fornecedor.getId().toString()))
                .andExpect(jsonPath("$.condominioId").value(condominioId));
    }

    @Test
    void deveRetornar400QuandoDescricaoDoContratoForVazia() throws Exception {
        String payload = """
                {
                  "descricao": "",
                  "valor": 1200.00,
                  "dataInicio": "2026-03-01",
                  "dataFim": "2026-12-31",
                  "fornecedorId": "11111111-1111-1111-1111-111111111111",
                  "condominioId": "22222222-2222-2222-2222-222222222222"
                }
                """;

        mockMvc.perform(post("/contratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("descricao: Descricao é obrigatória"))
                .andExpect(jsonPath("$.path").value("/contratos"));
    }

    @Test
    void deveAtualizarContratoExistente() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Nova");
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Bessa");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58035-000");
        endereco = enderecoRepository.save(endereco);

        String condominioPayload = """
                {
                  "nome": "Condominio Sol",
                  "cnpj": "55.444.333/0001-22",
                  "telefone": "(83) 3333-4444",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());

        String condominioResponse = postAndReturnBody("/condominios", condominioPayload);
        String condominioId = jsonField(condominioResponse, "id");

        Fornecedor fornecedorOriginal = new Fornecedor();
        fornecedorOriginal.setNome("Fornecedor A");
        fornecedorOriginal.setCnpj("10.000.000/0001-10");
        fornecedorOriginal.setTelefone("(83) 3000-0001");
        fornecedorOriginal = fornecedorRepository.save(fornecedorOriginal);

        String createPayload = """
                {
                  "descricao": "Portaria remota",
                  "valor": 900.00,
                  "dataInicio": "2026-02-01",
                  "dataFim": "2026-12-31",
                  "fornecedorId": "%s",
                  "condominioId": "%s"
                }
                """.formatted(fornecedorOriginal.getId(), condominioId);

        String contratoResponse = postAndReturnBody("/contratos", createPayload);
        String contratoId = jsonField(contratoResponse, "id");

        Fornecedor fornecedorNovo = new Fornecedor();
        fornecedorNovo.setNome("Fornecedor B");
        fornecedorNovo.setCnpj("20.000.000/0001-20");
        fornecedorNovo.setTelefone("(83) 3000-0002");
        fornecedorNovo = fornecedorRepository.save(fornecedorNovo);

        String updatePayload = """
                {
                  "descricao": "Segurança eletrônica",
                  "valor": 1500.00,
                  "dataInicio": "2026-03-01",
                  "dataFim": "2027-02-28",
                  "fornecedorId": "%s",
                  "condominioId": "%s"
                }
                """.formatted(fornecedorNovo.getId(), condominioId);

        mockMvc.perform(put("/contratos/{id}", contratoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contratoId))
                .andExpect(jsonPath("$.descricao").value("Segurança eletrônica"))
                .andExpect(jsonPath("$.valor").value(1500.00))
                .andExpect(jsonPath("$.dataInicio").value("2026-03-01"))
                .andExpect(jsonPath("$.dataFim").value("2027-02-28"))
                .andExpect(jsonPath("$.fornecedorId").value(fornecedorNovo.getId().toString()))
                .andExpect(jsonPath("$.condominioId").value(condominioId));
    }

    @Test
    void deveDeletarContratoExistente() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua das Palmeiras");
        endereco.setNumero("77");
        endereco.setComplemento("Torre B");
        endereco.setBairro("Manaira");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58038-000");
        endereco = enderecoRepository.save(endereco);

        String condominioPayload = """
                {
                  "nome": "Condominio Verde",
                  "cnpj": "44.333.222/0001-11",
                  "telefone": "(83) 3555-6666",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());

        String condominioResponse = postAndReturnBody("/condominios", condominioPayload);
        String condominioId = jsonField(condominioResponse, "id");

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Delete");
        fornecedor.setCnpj("33.222.111/0001-00");
        fornecedor.setTelefone("(83) 3777-8888");
        fornecedor = fornecedorRepository.save(fornecedor);

        String createPayload = """
                {
                  "descricao": "Limpeza técnica",
                  "valor": 700.00,
                  "dataInicio": "2026-01-01",
                  "dataFim": "2026-06-30",
                  "fornecedorId": "%s",
                  "condominioId": "%s"
                }
                """.formatted(fornecedor.getId(), condominioId);

        String contratoResponse = postAndReturnBody("/contratos", createPayload);
        String contratoId = jsonField(contratoResponse, "id");

        mockMvc.perform(delete("/contratos/{id}", contratoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/contratos/{id}", contratoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contrato não encontrado"));
    }
}
