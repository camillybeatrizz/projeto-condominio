# Kondo

Plataforma para gestao condominial com autenticacao delegada via OIDC e foco em operacao financeira segura, auditavel e escalavel.

## Visao do Produto

O Kondo tem como proposta central unir a gestao estrutural do condominio ao ciclo financeiro completo, reduzindo atrito operacional para sindicos, administradores e moradores.

No MVP, o objetivo e validar o fluxo principal do produto:

- gestao do condominio
- geracao de cobrancas
- recebimento de pagamentos automatizados via gateway
- atualizacao financeira confiavel com auditoria basica

## MVP

### Objetivo

Validar o fluxo principal do produto: gestao do condominio + ciclo financeiro completo, da cobranca ao pagamento automatico.

### Status atual

No estado atual do repositorio, o MVP esta demonstravel localmente com backend e frontend integrados.

- backend financeiro com cobranca, Pix, webhook, pagamento automatico e auditoria
- frontend React integrado aos endpoints principais do backend
- dashboard financeiro, tela de cobrancas, estrutura, portal do morador e chamados consumindo a API local
- massa de dados local para demonstracao em `src/main/resources/data-local.sql`
- exclusao logica nas entidades centrais para preservar historico
- principal ressalva para uso produtivo: substituir o login simulado do frontend por OIDC real

### Escopo do MVP

#### Autenticacao e seguranca

Requisitos: `RF01`, `RF02`, `RF04`, `RF05`

- login via OIDC com Keycloak
- validacao de JWT no backend
- controle de acesso baseado em roles

#### Gestao de condominio

Requisitos: `RF08` a `RF12`

- CRUD de condominio
- CRUD de bloco
- CRUD de unidade
- cadastro de moradores via modulo de acesso
- cadastro de areas comuns sem reserva

#### Financeiro

O modulo financeiro e o core do MVP.

**Cobranca**  
Requisitos: `RF13`, `RF14`

- geracao de cobrancas automáticas
- geracao de cobrancas em lote simples
- visualizacao por morador e por sindico

**Pagamento automatizado**  
Requisitos: `RF16`, `RF17`

- integracao com gateway em modo simples ou sandbox
- endpoint de webhook
- validacao de assinatura
- idempotencia com `transactionId` unico
- registro automatico do pagamento
- atualizacao automatica da cobranca para status `PAGA`

**Inadimplencia**  
Requisito: `RF18`

- listagem automatica de inadimplentes com base em cobrancas vencidas

#### Auditoria basica

Requisito: `RF07`

- log de acoes criticas, como pagamentos e alteracoes relevantes

### Fora do escopo do MVP

Os itens abaixo ficam planejados para evolucao posterior:

- WhatsApp
- notificacoes
- relatorios
- assistente financeiro
- contratos e manutencao

## Fluxos Core

### 1. Login

Fluxo: `OIDC -> JWT -> backend valida token -> autorizacao por role`.

### 2. Estrutura do condominio

O administrador cria:

- condominio
- blocos
- unidades

### 3. Usuario e acesso

O usuario e sincronizado via `externalId` e vinculado a uma unidade.

### 4. Geracao de cobranca

A cobranca e vinculada a unidade e criada com status inicial `ABERTA`.

### 5. Pagamento automatico

Fluxo principal:

1. o gateway envia um webhook
2. o backend valida a assinatura
3. o backend verifica o `transactionId` para garantir idempotencia
4. o sistema registra o pagamento
5. a cobranca é atualizada para `PAGA`
6. o evento é registrado em auditoria

### 6. Inadimplencia

A inadimplencia é identificada a partir de cobrancas vencidas e ainda nao pagas.

## Plano de Implementacao

### Fase 1 - Base

- Spring Boot
- Keycloak
- JWT

### Fase 2 - Dominio

- `Condominio`
- `Bloco`
- `Unidade`

### Fase 3 - Usuario e acesso

- `externalId`
- roles
- vinculo entre usuario e unidade

### Fase 4 - Financeiro

Fase mais importante do MVP.

- `Cobranca`
- `Pagamento`
- status e regras de transicao
- geracao e consulta de cobrancas

### Fase 5 - Webhook

Fase critica para o diferencial do produto.

- endpoint seguro
- validacao de assinatura
- idempotencia
- consistencia transacional no registro do pagamento

### Fase 6 - Auditoria

- logs essenciais
- rastreabilidade de eventos financeiros e operacionais

### Fase 7 - Frontend

- dashboard simples
- tela de cobrancas
- exibicao de status
- visualizacao de pagamentos processados
- portal do morador com cobrancas e Pix
- estrutura e chamados integrados ao backend

## Status do Plano

- Fase 1 - Base: concluida no backend
- Fase 2 - Dominio: concluida no backend
- Fase 3 - Usuario e acesso: concluida no backend
- Fase 4 - Financeiro: concluida no backend
- Fase 5 - Webhook: concluida no backend
- Fase 6 - Auditoria: concluida no backend
- Fase 7 - Frontend: concluida como MVP demonstravel localmente

## Evolucao Pos-MVP

### Proximos passos naturais

- Pix e boleto oficial
- notificacoes automaticas
- WhatsApp como diferencial competitivo
- relatorios automaticos

### Evolucao tecnica

- filas com RabbitMQ ou Kafka
- separacao futura em microservicos
- arquitetura orientada a eventos

### Pontos criticos

- validacao de webhook para seguranca
- idempotencia para evitar pagamento duplicado
- multi-tenant com isolamento por condominio
- consistencia transacional entre cobranca e pagamento

## Escopo atual do repositorio

O repositorio implementa principalmente o backend da plataforma Kondo, com foco em:

- estrutura condominial: enderecos, condominios, blocos e unidades
- areas comuns sem reserva
- administracao e seguranca: usuarios, acessos, perfis e escopo por condominio
- operacao: chamados
- financeiro: cobrancas, pagamentos, despesas, contratos, fornecedores e contas bancarias

Tambem ja estao implementados neste backend:

- integracao sandbox com Asaas para cobrancas
- webhook de pagamento com idempotencia
- auditoria basica
- endpoints financeiros de `pix`, `resumo` e `dashboard`
- soft delete para preservar historico

No frontend, o repositorio possui uma camada React/Vite integrada a API local para demonstrar:

- selecao de contexto por perfil
- dashboard financeiro do sindico
- gestao/listagem de cobrancas
- visualizacao de Pix pelo morador
- gestao estrutural de blocos e unidades
- abertura e acompanhamento de chamados

Os perfis atualmente refletidos no codigo e nas regras de negocio sao:

- `ADMIN`
- `SINDICO`
- `MORADOR`

## Relacao com o ERS

O documento de requisitos em `docs/Especificações de Requisitos - KONDO.pdf` descreve uma visao mais ampla do produto, incluindo frontend web, integracoes externas e automacoes operacionais e financeiras.

Neste repositorio, a implementacao consolidada hoje corresponde ao nucleo backend da plataforma e a um frontend MVP demonstravel. Itens como login OIDC real no frontend, WhatsApp, envio de notificacoes, webhooks bancarios, Pix nativo, relatorios automaticos e cache devem ser interpretados como requisitos de evolucao do produto, salvo quando houver codigo e testes especificos cobrindo esses modulos.

## Autenticacao e autorizacao

- a autenticacao e externa, via OIDC
- o backend atua como Resource Server e valida tokens JWT
- o identificador principal do usuario autenticado e `externalId`, correspondente ao `sub` do provedor OIDC
- o `email` e atributo de contato e apoio operacional, nao a chave primaria de identidade
- o acesso aos dados respeita escopo por condominio e perfil

## Stack principal

- Java 21
- Spring Boot
- Spring Security OAuth2 Resource Server
- Spring Data JPA
- Flyway
- OpenAPI / Swagger
- banco relacional - PostgreSQL

## Execucao local

Exemplo para subir a API com configuracao local:

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

O perfil `local` usa H2 em memoria, seguranca aberta para estudo e carrega a massa de demonstracao de `src/main/resources/data-local.sql`.

Para subir o frontend:

```bash
cd frontend
npm install
npm run dev
```

O frontend usa `VITE_API_URL=/api` e o proxy do Vite redireciona as chamadas para `http://localhost:8080`. Acesse:

- API/Swagger: `http://localhost:8080/swagger-ui.html`
- Frontend: `http://127.0.0.1:5173/`

Com a seguranca habilitada fora do perfil local, use um token JWT emitido pelo provedor configurado no ambiente.

## Documentos importantes

- `docs/regra-de-negocio-kondo.md`: regras de negocio alinhadas ao backend implementado
- `docs/guia-didatico-spring-boot.md`: explicacao didatica da arquitetura e do codigo
- `docs/checklist-coerencia-documental.md`: checklist para manter ERS, codigo e documentos coerentes
- `docs/matriz-rastreabilidade-kondo.md`: rastreabilidade entre ERS, backend e testes
- `docs/frontend-handoff-checklist.md`: checklist rapido para handoff e integracao do frontend com o backend
- `docs/frontend-financeiro-integracao.md`: guia de consumo dos endpoints financeiros do MVP
- `docs/pendencias-diagramas-ers.md`: lacunas atuais dos diagramas frente ao ERS
- `docs/roteiro-novos-diagramas-ers.md`: conteudo textual base para desenhar os novos diagramas
- `docs/diagramas-plantuml-casos-de-uso-rf01-rf44.md`: fonte PlantUML dos casos de uso revisados
- `docs/diagramas-plantuml-sequencia-rf01-rf44.md`: fonte PlantUML dos diagramas de sequencia revisados
- `docs/diagramas-plantuml-casos-de-uso-clean.md`: versao visualmente mais enxuta dos casos de uso
- `docs/diagramas-plantuml-sequencia-clean.md`: versao visualmente mais enxuta dos fluxos de sequencia
- `docs/plantuml/README.md`: indice dos arquivos `.puml` gerados por modulo e por RF
- `docs/mermaid/visoes-gerais-rf01-rf44.md`: visoes gerais em Mermaid para navegacao rapida
