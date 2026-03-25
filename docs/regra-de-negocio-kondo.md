# Regra de Negocio do Kondo

## 1. Objetivo

O Kondo e uma API REST de gestao condominial. A regra de negocio do sistema deve garantir:

- consistencia entre condominio, bloco, unidade, usuario e acessos;
- segregacao de dados por condominio;
- integridade do ciclo financeiro de cobrancas, pagamentos, despesas e contratos;
- rastreabilidade das operacoes e respostas padronizadas para erros de negocio.

Este documento foi consolidado a partir dos artefatos em `docs/` e da implementacao atual do projeto, principalmente os services, validacoes e testes de integracao.

### Delimitacao de escopo deste documento

- Este documento descreve prioritariamente a regra de negocio ja refletida no backend atual deste repositorio.
- O ERS do projeto apresenta uma visao mais ampla do produto, incluindo frontend web, WhatsApp, notificacoes, webhooks de pagamento, relatorios automaticos, areas comuns, garantias e outros modulos de evolucao.
- Quando um item existir no ERS, mas ainda nao estiver refletido em codigo, testes e regras detalhadas abaixo, ele deve ser tratado como requisito de evolucao e nao como comportamento ja implementado.

## 2. Principios de dominio

- Toda entidade filha deve pertencer de forma explicita a um agregado pai valido.
- Nenhuma operacao deve cruzar condominios sem autorizacao.
- Regras de negocio devem ficar na camada `service`, nunca no `controller`.
- Violacoes de dominio devem retornar erros semanticamente corretos:
  - `ResourceNotFoundException` para recurso inexistente;
  - `BusinessException` para regra funcional invalida;
  - `ConflictException` para duplicidade ou choque de estado.
- Operacoes que alteram mais de uma entidade relacionada devem ser transacionais.

## 3. Papeis do sistema

### ADMIN

- possui visao global do sistema;
- nao deve ser vinculado a condominio nem a unidade;
- pode operar em qualquer condominio.

### SINDICO

- deve estar vinculado a um condominio;
- nao deve ser vinculado a uma unidade;
- atua no escopo do proprio condominio.

### MORADOR

- deve estar vinculado a um condominio e a uma unidade;
- so pode atuar sobre dados do proprio escopo;
- visualiza apenas seus chamados, unidades, cobrancas e pagamentos quando o filtro de seguranca for aplicado.

### Observacao sobre atores do ERS

- O ERS menciona o ator `SUPORTE`, mas esse perfil ainda nao esta refletido no codigo, nos testes nem nas regras operacionais deste documento.
- Ate que esse papel seja implementado formalmente, os perfis oficiais do backend permanecem `ADMIN`, `SINDICO` e `MORADOR`.

## 4. Regras estruturais do dominio

- `Condominio` depende de um `Endereco` existente.
- `Bloco` pertence a um unico `Condominio`.
- `Unidade` pertence a um unico `Bloco`.
- `Cobranca` pertence a uma unica `Unidade`.
- `Pagamento` pertence a uma unica `Cobranca`.
- `Contrato`, `ContaBancaria` e `Despesa` pertencem a um unico `Condominio`.
- `Acesso` conecta `Usuario` ao seu papel e ao respectivo escopo de atuacao.

## 5. Regras globais

### 5.1 Escopo e autorizacao

- Usuarios autenticados e nao administradores so podem acessar condominios permitidos pelo seu escopo.
- Operacoes de leitura e escrita devem validar o condominio dono do recurso antes de retornar ou persistir dados.
- Filtros de listagem devem restringir resultados ao escopo permitido, mesmo quando o cliente nao informar filtros.

### 5.2 Integridade referencial de negocio

- Nenhuma entidade deve ser criada com referencia a um recurso inexistente.
- Mudancas de relacionamento devem validar o novo pai antes da persistencia.
- Quando uma operacao trocar o escopo de uma entidade, o sistema deve validar tanto o escopo atual quanto o novo escopo.

### 5.3 Consistencia transacional

- Regras que atualizam entidades correlatas devem ocorrer na mesma transacao.
- O exemplo principal e o pagamento, que grava o pagamento e altera o status da cobranca para `PAGA`.
- O vinculo de morador com unidade tambem deve permanecer consistente quando um acesso e criado, alterado ou removido.

### 5.4 Identidade e autenticacao

- O identificador principal do usuario autenticado deve ser `externalId`, correspondente ao `sub` emitido pelo provedor OIDC.
- O `email` e atributo de contato e apoio operacional, mas nao deve ser utilizado como chave primaria de autenticacao, autorizacao ou correlacao de identidade.
- O sistema deve suportar multiplos provedores OIDC, incluindo Keycloak e Pinniped, sem acoplamento da regra de negocio a um provedor especifico.
- A resolucao do usuario autenticado deve ser estavel entre provedores, priorizando sempre `externalId`.

## 6. Regras por modulo

### 6.1 Endereco

- Endereco e um cadastro de apoio.
- Pode ser reutilizado por outras entidades, especialmente condominio.
- O sistema deve impedir referencias a enderecos inexistentes.

### 6.2 Condominio

- Um condominio so pode ser criado com `enderecoId` valido.
- O `cnpj` deve ser unico no sistema.
- Atualizacao deve manter a unicidade do `cnpj`, desconsiderando o proprio registro em edicao.
- Nao e permitido excluir condominio que ainda possua blocos, contas bancarias, contratos ou despesas vinculadas.
- Usuarios fora do papel `ADMIN` so podem consultar, alterar ou remover condominios do proprio escopo.

### 6.3 Bloco

- Um bloco so pode existir dentro de um condominio valido.
- Usuarios fora do papel `ADMIN` so podem manipular blocos do proprio escopo condominial.

### 6.4 Unidade

- Uma unidade so pode ser criada dentro de um bloco valido.
- Quando houver `moradorId`, o usuario informado deve existir.
- O morador que consulta unidades deve enxergar apenas a propria unidade ou unidades ligadas ao seu usuario autenticado.
- A associacao direta de morador em unidade precisa permanecer coerente com o modulo de acessos.
- Uma unidade nao pode repetir o mesmo `numero` dentro do mesmo bloco.
- Nao e permitido excluir unidade que possua cobrancas ou chamados vinculados.

### 6.5 Usuario

- O `email` do usuario deve ser unico no sistema.
- O `externalId` deve ser o identificador principal do usuario no sistema e deve representar o `sub` recebido do provedor OIDC.
- O `externalId` deve ser unico quando informado.
- O usuario local nao armazena senha nem autentica diretamente na aplicacao.
- A autenticacao e externa, realizada por provedores OIDC compativeis, como Keycloak e Pinniped.
- O `email` nao deve ser utilizado como chave primaria de autenticacao.
- O `email` pode ser usado para comunicacao, exibicao, notificacao e apoio administrativo, desde que nao substitua `externalId` como referencia principal de identidade.
- Nao e permitido excluir usuario que possua acessos vinculados.

### 6.6 Acesso

- `ADMIN` nao pode possuir condominio nem unidade associados.
- `SINDICO` deve possuir condominio e nao pode possuir unidade.
- `MORADOR` deve possuir condominio e unidade.
- A unidade informada em um acesso de morador deve pertencer ao condominio informado.
- O mesmo usuario nao pode ter acesso duplicado com o mesmo perfil no mesmo escopo.
- Uma unidade nao pode ficar vinculada a mais de um acesso de morador.
- Uma unidade nao pode possuir morador divergente do usuario vinculado no acesso.
- Ao criar ou atualizar acesso de `MORADOR`, o sistema deve sincronizar `Unidade.morador`.
- Ao remover ou alterar um acesso de `MORADOR`, o sistema deve desfazer o vinculo anterior quando aplicavel.

### 6.7 Chamado

- Um chamado so pode ser criado para uma unidade existente.
- Um chamado deve nascer com status `ABERTO`.
- `dataAbertura` nao pode ser futura.
- Moradores podem operar chamados do proprio escopo.
- Perfis administrativos devem respeitar o escopo do condominio da unidade.
- Listagens devem permitir filtro por condominio e status, sem quebrar a restricao de escopo.
- As transicoes de status devem seguir fluxo controlado:
  - `ABERTO -> ANDAMENTO` ou `CONCLUIDO`
  - `ANDAMENTO -> CONCLUIDO`
  - nao e permitido reabrir chamado em `ANDAMENTO` ou `CONCLUIDO` para `ABERTO`
  - nao e permitido alterar o status de um chamado `CONCLUIDO`

### 6.8 Cobranca

- Uma cobranca so pode ser criada para uma unidade existente.
- `competencia` e obrigatoria.
- Nao e permitido criar nem atualizar cobranca diretamente com status `PAGA`.
- O status `PAGA` deve ser resultado do fluxo de pagamento, nao de cadastro manual.
- Moradores so podem visualizar cobrancas vinculadas ao proprio usuario autenticado.
- Sindicos e administradores podem listar cobrancas conforme o escopo autorizado.

### 6.9 Pagamento

- Um pagamento so pode ser registrado para uma cobranca existente.
- Nao e permitido registrar pagamento para cobranca ja marcada como `PAGA`.
- `transactionId` deve ser unico.
- Ao salvar um pagamento valido, o sistema deve atualizar a cobranca relacionada para `PAGA`.
- Filtros por intervalo de datas devem rejeitar consultas onde `dataInicio > dataFim`.
- Moradores so podem visualizar pagamentos ligados as suas cobrancas.

### 6.10 Despesa

- Toda despesa deve pertencer a um condominio valido.
- Listagens por periodo devem rejeitar intervalos invalidos.
- Usuarios fora do papel `ADMIN` so podem operar despesas do proprio condominio ou dos condominios permitidos.
- Nao e permitido excluir um condominio que possua despesas vinculadas.

### 6.11 Contrato

- Todo contrato deve estar vinculado a um fornecedor existente e a um condominio existente.
- `dataFim`, quando informada, nao pode ser anterior a `dataInicio`.
- Nao e permitido manter contratos com vigencia sobreposta para o mesmo fornecedor dentro do mesmo condominio.
- Listagens com periodo devem rejeitar consultas onde `dataInicio > dataFim`.
- A consulta deve permitir filtros por condominio, fornecedor e vigencia, sempre respeitando o escopo de acesso.

### 6.12 Conta bancaria

- Toda conta bancaria deve estar vinculada a um condominio existente.
- Nao pode existir mais de uma conta com a mesma combinacao `condominio + agencia + conta`.
- A consulta pode filtrar por tipo e banco.
- Usuarios fora do papel `ADMIN` so podem operar contas do proprio escopo.
- A existencia de conta bancaria vinculada impede a exclusao do condominio correspondente.

### 6.13 Fornecedor

- Fornecedor e um cadastro transversal, mas a visibilidade deve respeitar os condominios com os quais ele possui contratos.
- O `cnpj` deve ser unico no sistema.
- Nao e permitido excluir fornecedor que possua contratos vinculados.
- Usuarios nao administradores nao devem acessar fornecedor sem relacao com seu escopo condominial.

## 7. Estados e transicoes relevantes

### Cobranca

- `PENDENTE` ou equivalente pode ser criado manualmente.
- `PAGA` nao pode ser atribuida manualmente na criacao ou atualizacao.
- A transicao para `PAGA` ocorre pelo registro de pagamento.

### Acesso de morador

- criar acesso: unidade passa a apontar para o usuario morador;
- trocar acesso para outra unidade ou outro usuario: o vinculo anterior deve ser removido;
- remover acesso: a unidade deve ficar sem morador quando o vinculo removido for o vigente.

## 8. Boas praticas recomendadas para a continuidade do projeto

- Formalizar regras criticas com testes de integracao orientados a comportamento.
- Reforcar unicidade tambem no banco de dados para `usuario.email`, `usuario.externalId`, `condominio.cnpj`, `pagamento.transactionId`, `fornecedor.cnpj`, `bloco(condominio_id,nome)` e `unidade(bloco_id,numero)`.
- Evitar regras duplicadas entre mapper e service; invariantes devem ficar centralizadas no service.
- Padronizar mensagens de erro para manter previsibilidade da API.
- Documentar no OpenAPI quais campos sao obrigatorios por perfil e quais transicoes de estado sao aceitas.
- Evoluir regras financeiras com idempotencia para integracoes de pagamento.
- Padronizar o contrato de autenticacao em torno de claims OIDC estaveis, com prioridade para `sub` e independencia de fornecedor.
- Considerar auditoria para operacoes sensiveis, como mudanca de acesso, baixa de cobranca e alteracao de contrato.

## 9. Criterios de aceite para a regra de negocio

- Nenhum usuario fora do escopo pode ler ou alterar dados de outro condominio.
- O usuario autenticado deve ser resolvido prioritariamente por `externalId` (`OIDC sub`).
- O email nao deve ser tratado como chave primaria de autenticacao.
- A autenticacao deve funcionar com mais de um provedor OIDC, incluindo Keycloak e Pinniped.
- Nenhum cadastro pode ser salvo com referencia inexistente.
- Duplicidades de identificadores de negocio devem retornar conflito.
- Estados financeiros invalidos devem retornar erro de negocio.
- O vinculo entre morador, acesso e unidade deve permanecer consistente apos criar, editar e excluir.
- O documento de regra de negocio deve permanecer sincronizado com os testes de integracao do projeto.
