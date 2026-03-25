package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CadastrosTransversaisIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveRetornar409QuandoCriarFornecedorComCnpjDuplicado() throws Exception {
        String payload = """
                {
                  "nome": "Fornecedor A",
                  "cnpj": "98.765.432/0001-10",
                  "telefone": "(83) 98888-7777"
                }
                """;

        mockMvc.perform(post("/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe fornecedor com este CNPJ"));
    }

    @Test
    void deveRetornar409QuandoCriarContaBancariaDuplicadaNoMesmoCondominio() throws Exception {
        Condominio condominio = criarCondominio("201");

        String payload = """
                {
                  "banco": "Banco do Brasil",
                  "agencia": "1234-5",
                  "conta": "98765-4",
                  "tipo": "CORRENTE",
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(post("/contas-bancarias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/contas-bancarias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe conta bancária com esta agência e conta no condomínio informado"));
    }

    @Test
    void deveRetornar400AoExcluirFornecedorComContratoVinculado() throws Exception {
        Condominio condominio = criarCondominio("202");
        Fornecedor fornecedor = criarFornecedor("Fornecedor Vinculado", "11.222.333/0001-44");

        String payload = """
                {
                  "descricao": "Contrato Ativo",
                  "valor": 1500.00,
                  "dataInicio": "2026-01-01",
                  "dataFim": "2026-12-31",
                  "fornecedorId": "%s",
                  "condominioId": "%s"
                }
                """.formatted(fornecedor.getId(), condominio.getId());

        mockMvc.perform(post("/contratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/fornecedores/{id}", fornecedor.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir fornecedor que possui contratos vinculados"));
    }

    @Test
    void deveRealizarExclusaoLogicaDeFornecedorSemContrato() throws Exception {
        Fornecedor fornecedor = criarFornecedor("Fornecedor Arquivado", "77.888.999/0001-55");

        mockMvc.perform(delete("/fornecedores/{id}", fornecedor.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/fornecedores/{id}", fornecedor.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fornecedor não encontrado"));

        Fornecedor fornecedorPersistido = fornecedorRepository.findById(fornecedor.getId()).orElseThrow();
        assertThat(fornecedorPersistido.getDeletedAt()).isNotNull();
        assertThat(fornecedorPersistido.getDeletedBy()).isEqualTo("SYSTEM");
    }

    @Test
    void devePermitirReutilizarCnpjDeFornecedorAposExclusaoLogica() throws Exception {
        String payload = """
                {
                  "nome": "Fornecedor Reciclado",
                  "cnpj": "66.555.444/0001-33",
                  "telefone": "(83) 98888-7777"
                }
                """;

        String response = postAndReturnBody("/fornecedores", payload);
        String fornecedorId = jsonField(response, "id");

        mockMvc.perform(delete("/fornecedores/{id}", fornecedorId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cnpj").value("66.555.444/0001-33"));
    }

    private Condominio criarCondominio(String sufixo) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Cadastro " + sufixo);
        endereco.setNumero("20");
        endereco.setComplemento("Sala");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-300");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome("Condominio Cadastro " + sufixo);
        condominio.setCnpj("80.000.000/0001-" + sufixo);
        condominio.setTelefone("(83) 99999-3000");
        condominio.setEndereco(endereco);
        return condominioRepository.save(condominio);
    }

    private Fornecedor criarFornecedor(String nome, String cnpj) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone("(83) 98888-0000");
        return fornecedorRepository.save(fornecedor);
    }
}
