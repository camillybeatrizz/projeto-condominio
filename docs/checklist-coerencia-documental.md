# Checklist de Coerencia Documental do Kondo

## Objetivo

Este checklist consolida os ajustes necessarios para alinhar o documento de requisitos atualizado com os demais artefatos do projeto, principalmente:

- ERS (`docs/Especificações de Requisitos - KONDO.pdf`)
- regra de negocio (`docs/regra-de-negocio-kondo.md`)
- guia didatico (`docs/guia-didatico-spring-boot.md`)
- README (`README.md`)
- diagramas de casos de uso e sequencia (`docs/`)
- OpenAPI e configuracoes da API (`src/main/java` e `src/main/resources`)

## Prioridade Alta

### 1. Definir o escopo oficial do projeto

- [ ] Decidir se o ERS descreve o produto ja implementado ou uma visao futura por fases.
- [ ] Se houver funcionalidades futuras, marcar explicitamente no ERS o que e:
- [ ] `implementado`
- [ ] `em andamento`
- [ ] `planejado`
- [ ] Garantir que README, guia didatico e regra de negocio usem a mesma classificacao de escopo.

### 2. Alinhar os perfis de usuario

- [ ] Confirmar se o perfil `SUPORTE` faz parte do escopo real do sistema.
- [ ] Se `SUPORTE` for valido:
- [ ] adicionar o perfil na documentacao funcional e tecnica
- [ ] refletir o perfil em regras de autorizacao e OpenAPI
- [ ] revisar diagramas de casos de uso
- [ ] avaliar impacto no enum de perfis e nos testes
- [ ] Se `SUPORTE` nao for valido:
- [ ] remover o ator do ERS
- [ ] ajustar descricoes de usuarios para manter apenas `ADMIN`, `SINDICO` e `MORADOR`

### 3. Corrigir a narrativa de identidade/autenticacao

- [ ] Padronizar todos os documentos para afirmar que o identificador principal e `externalId` derivado do `sub` do OIDC.
- [ ] Remover qualquer texto que trate `email` como identidade canonica de autenticacao.
- [ ] Atualizar a explicacao de login para refletir OIDC com provedor externo, JWT e validacao por `issuer`/`JWKS`.
- [ ] Revisar exemplos, criterios de aceite e descricoes de seguranca para manter a mesma terminologia em todos os artefatos.

### 4. Reconciliar funcionalidades novas do ERS com a documentacao restante

- [ ] Verificar e classificar no ERS e na regra de negocio os modulos abaixo:
- [ ] WhatsApp como interface principal
- [ ] segunda via de boleto
- [ ] Pix e boleto automatico
- [ ] webhook de pagamento
- [ ] notificacoes por email
- [ ] previsao de fluxo de caixa
- [ ] assistente financeiro
- [ ] prestacao de contas automatica
- [ ] exportacao PDF
- [ ] areas comuns e reservas
- [ ] garantias, servicos recorrentes e comparacao de orcamentos
- [ ] alertas de vencimento
- [ ] Para cada item acima, registrar se esta:
- [ ] coberto por codigo
- [ ] coberto apenas por requisito
- [ ] ausente do backlog tecnico

## Prioridade Media

### 5. Atualizar o README

- [ ] Descrever o projeto alem do titulo.
- [ ] Incluir visao geral do sistema e objetivo do Kondo.
- [ ] Listar modulos implementados atualmente.
- [ ] Listar modulos previstos no ERS que ainda nao estao implementados.
- [ ] Documentar perfis de acesso existentes.
- [ ] Explicar a autenticacao OIDC de forma resumida.
- [ ] Informar stack principal:
- [ ] backend Spring Boot
- [ ] banco relacional
- [ ] integracao OIDC
- [ ] frontend React como componente previsto ou existente, conforme o caso
- [ ] Adicionar instrucoes minimas de execucao local.

### 6. Atualizar a regra de negocio

- [ ] Incluir explicitamente a decisao sobre o perfil `SUPORTE`.
- [ ] Decidir se a regra de negocio vai cobrir apenas o backend atual ou todo o produto descrito no ERS.
- [ ] Se cobrir todo o produto, adicionar secoes para:
- [ ] auditoria
- [ ] webhooks de pagamento
- [ ] idempotencia
- [ ] notificacoes
- [ ] reservas de areas comuns
- [ ] servicos recorrentes
- [ ] garantias e alertas
- [ ] prestacao de contas automatica
- [ ] Se nao cobrir todo o produto, incluir uma nota clara delimitando que o documento reflete apenas o backend implementado.
- [ ] Manter coerencia entre regras funcionais e criterios de aceite do ERS.

### 7. Atualizar o guia didatico

- [ ] Revisar a visao geral para explicar que o sistema usa autenticacao externa, nao login local.
- [ ] Incluir uma explicacao simples sobre:
- [ ] JWT
- [ ] OIDC
- [ ] `externalId`
- [ ] escopo por condominio
- [ ] Remover qualquer expectativa de fluxo com senha persistida localmente.
- [ ] Explicar os novos componentes de seguranca do projeto atual.
- [ ] Diferenciar claramente o que e implementacao atual e o que e requisito futuro.

### 8. Atualizar a OpenAPI e a documentacao tecnica embutida

- [ ] Revisar descricoes de perfis na [OpenApiConfig](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/config/OpenApiConfig.java).
- [ ] Documentar com clareza os comportamentos `401` e `403`.
- [ ] Documentar campos obrigatorios por perfil quando houver diferenca de regra.
- [ ] Documentar restricoes de escopo por condominio.
- [ ] Se `SUPORTE` existir, refletir isso na documentacao.
- [ ] Se webhook e integracoes externas forem parte do escopo imediato, planejar endpoints e contratos na documentacao.

## Prioridade Media/Baixa

### 9. Atualizar diagramas

- [ ] Revisar o diagrama de casos de uso para refletir os atores oficiais.
- [ ] Revisar os detalhamentos de UC01 a UC10 conforme os requisitos novos.
- [ ] Verificar se existem casos de uso faltantes para RFs novos, especialmente:
- [ ] RF41 garantias
- [ ] RF42 historico de fornecedor
- [ ] RF43 comparacao de orcamentos
- [ ] RF44 gestao completa de chamados
- [ ] Atualizar os diagramas de sequencia para cobrir os requisitos que faltam.
- [ ] Conferir nomenclatura consistente entre RF, UC e diagramas.

### 10. Refazer a matriz de rastreabilidade

- [ ] Substituir a matriz parcial do ERS por uma tabela completa.
- [ ] Relacionar cada RF relevante com:
- [ ] documento de apoio
- [ ] endpoint ou modulo tecnico correspondente
- [ ] teste existente
- [ ] status de implementacao
- [ ] Incluir tambem os RNFs mais importantes, como:
- [ ] OIDC
- [ ] validacao de JWT
- [ ] isolamento por condominio
- [ ] integridade transacional
- [ ] auditoria

### 11. Revisar metadados e historico do ERS

- [ ] Verificar se a data do documento esta correta em relacao a atualizacao recente.
- [ ] Atualizar a versao do documento, se necessario.
- [ ] Adicionar historico de revisoes para registrar o que mudou nesta versao.
- [ ] Garantir que os nomes dos responsaveis estejam padronizados.

## Checklist tecnico complementar

### 12. Validar coerencia entre documento e configuracao

- [ ] Revisar [application.properties](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/resources/application.properties) para alinhar o comentario sobre identidade principal com o uso de `externalId`.
- [ ] Revisar configuracoes `application-keycloak.properties` e `application-pinniped.properties` para manter a mesma narrativa documental.
- [ ] Conferir se os comentarios tecnicos condizem com o ERS e com a regra de negocio.

### 13. Validar coerencia entre documento e testes

- [ ] Mapear os testes de integracao existentes por modulo.
- [ ] Relacionar os testes de seguranca ao bloco de autenticacao/autorizacao do ERS.
- [ ] Relacionar os testes de negocio aos RFs realmente implementados.
- [ ] Registrar explicitamente quais RFs ainda nao possuem teste correspondente.

## Ordem sugerida de execucao

1. Definir escopo oficial e perfis oficiais.
2. Corrigir autenticacao/identidade em todos os documentos.
3. Atualizar README, regra de negocio e guia didatico.
4. Atualizar diagramas e matriz de rastreabilidade.
5. Revisar OpenAPI, comentarios tecnicos e configuracoes.

## Resultado esperado

Ao final da revisao, todos os artefatos devem responder de forma consistente:

- o que o Kondo e hoje;
- o que ja esta implementado;
- o que ainda e requisito futuro;
- quem sao os atores oficiais;
- como funciona autenticacao, autorizacao e escopo;
- quais requisitos possuem rastreabilidade para codigo e testes.
