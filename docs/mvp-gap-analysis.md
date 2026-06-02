# Analise de Gap do MVP - Kondo

## Objetivo

Este documento consolida o estado atual do projeto em relacao ao MVP definido no [`README.md`](../README.md), para responder com clareza:

- o que ja esta implementado
- o que esta parcial
- o que ainda falta fazer
- qual deve ser a ordem mais eficiente para deixar o MVP pronto

## Resumo executivo

O backend do Kondo evoluiu bastante e hoje cobre praticamente todo o core tecnico do MVP:

- autenticacao e autorizacao com OIDC/JWT
- controle de acesso por roles
- isolamento por condominio
- CRUD estrutural de condominio, bloco e unidade
- cadastro de usuarios e acessos
- area comum sem reserva
- cobranca manual e em lote
- integracao sandbox com Asaas
- webhook de pagamento com validacao HMAC, suporte legado por token, idempotencia e baixa automatica
- auditoria basica
- consulta de inadimplencia
- endpoints prontos para consumo do frontend (`pix`, `resumo`, `dashboard`)
- exclusao logica com preservacao de historico
- frontend React/Vite integrado aos endpoints principais do backend
- massa de dados local para demonstracao do dashboard, cobrancas, Pix, estrutura e chamados

O principal gap para considerar o MVP demonstravel foi resolvido com a integracao do frontend minimo. O que permanece como complemento ou evolucao e:

- substituir o login simulado do frontend por fluxo OIDC real quando o ambiente de identidade estiver disponivel
- implementar formularios completos para botoes que hoje ainda sao atalhos visuais, como nova cobranca, novo bloco e adicionar unidade
- manter documentacao e matriz de rastreabilidade sincronizadas com a evolucao do frontend
- decidir ajustes de produto que o time queira incorporar antes de uma entrega produtiva

Em resumo:

- Fases 1 a 6: implementadas no backend
- Fase 7: implementada como frontend MVP demonstravel localmente

## Fonte de evidencia

Esta analise foi baseada em:

- codigo-fonte em `src/main/java`
- migrations em `src/main/resources/db/migration`
- testes automatizados em `src/test/java`
- matriz de rastreabilidade em [`matriz-rastreabilidade-kondo.md`](matriz-rastreabilidade-kondo.md)
- plano do MVP em [`README.md`](../README.md)

Tambem foi validado que a suite atual passa com sucesso:

```bash
./mvnw clean test
```

Resultado observado: `99 testes`, `0 falhas`, `0 erros`.

## Status por area do MVP

### 1. Autenticacao e Seguranca

### Status geral

`Implementado`

### O que ja esta feito

- Resource Server com JWT em [`SecurityConfig`](../src/main/java/com/codewithus/kondo/config/SecurityConfig.java)
- validacao de issuer e audience em [`SecurityConfig`](../src/main/java/com/codewithus/kondo/config/SecurityConfig.java)
- conversao de claims e grupos para roles em [`JwtRoleConverter`](../src/main/java/com/codewithus/kondo/security/JwtRoleConverter.java)
- protecao por `@PreAuthorize` nos controllers
- resolucao do usuario autenticado com foco em `externalId` em [`CurrentUserResolver`](../src/main/java/com/codewithus/kondo/security/CurrentUserResolver.java)
- vinculacao automatica de `externalId` quando aplicavel em [`UsuarioIdentityLinkService`](../src/main/java/com/codewithus/kondo/security/UsuarioIdentityLinkService.java)

### O que valida essa parte

- [`SecurityAuthorizationIntegrationTest`](../src/test/java/com/codewithus/kondo/SecurityAuthorizationIntegrationTest.java)
- [`MeuContextoIntegrationTest`](../src/test/java/com/codewithus/kondo/MeuContextoIntegrationTest.java)
- [`StrictExternalIdentityIntegrationTest`](../src/test/java/com/codewithus/kondo/StrictExternalIdentityIntegrationTest.java)

### Conclusao

Esta area sustenta o MVP com seguranca suficiente para operacao real do fluxo financeiro.

## 2. Gestao de Condominio

### Status geral

`Implementado`

### O que ja esta feito

- CRUD de condominio em [`CondominioController`](../src/main/java/com/codewithus/kondo/controller/CondominioController.java)
- CRUD de bloco em [`BlocoController`](../src/main/java/com/codewithus/kondo/controller/BlocoController.java)
- CRUD de unidade em [`UnidadeController`](../src/main/java/com/codewithus/kondo/controller/UnidadeController.java)
- CRUD de area comum em [`AreaComumController`](../src/main/java/com/codewithus/kondo/controller/AreaComumController.java)
- validacoes de integridade e escopo em:
  - [`CondominioServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/CondominioServiceImpl.java)
  - [`BlocoServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/BlocoServiceImpl.java)
  - [`UnidadeServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/UnidadeServiceImpl.java)
  - [`AreaComumServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/AreaComumServiceImpl.java)
- exclusao logica para preservar historico em condominio, bloco e unidade

### O que ja esta feito em usuario/acesso

- CRUD de usuario em [`UsuarioController`](../src/main/java/com/codewithus/kondo/controller/UsuarioController.java)
- CRUD de acesso em [`AcessoController`](../src/main/java/com/codewithus/kondo/controller/AcessoController.java)
- vinculo entre usuario, condominio, perfil e unidade em [`AcessoServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/AcessoServiceImpl.java)
- sincronizacao do morador na unidade ao criar ou alterar acesso

### Conclusao

O bloco estrutural do MVP esta pronto no backend.

## 3. Financeiro - Cobranca

### Status geral

`Implementado`

### O que ja esta feito

- entidade de cobranca em [`Cobranca`](../src/main/java/com/codewithus/kondo/domain/entity/Cobranca.java)
- CRUD e listagem em [`CobrancaController`](../src/main/java/com/codewithus/kondo/controller/CobrancaController.java)
- regras de negocio em [`CobrancaServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/CobrancaServiceImpl.java)
- filtros por condominio, status e competencia
- visualizacao por morador e por sindico com isolamento por escopo
- geracao de cobranca manual
- geracao de cobranca em lote simples
- integracao com Asaas na criacao da cobranca
- persistencia de `referenciaExterna`, URL externa e dados Pix
- endpoint de detalhes Pix por cobranca
- endpoint de resumo financeiro
- endpoint de dashboard financeiro
- unicidade de `unidade + competencia` apenas entre cobrancas ativas
- unicidade global de `referenciaExterna`, mesmo apos exclusao logica
- exclusao logica com preservacao de historico

### O que valida essa parte

- [`CobrancaBusinessIntegrationTest`](../src/test/java/com/codewithus/kondo/CobrancaBusinessIntegrationTest.java)
- [`CobrancaLoteIntegrationTest`](../src/test/java/com/codewithus/kondo/CobrancaLoteIntegrationTest.java)
- [`CobrancaResumoIntegrationTest`](../src/test/java/com/codewithus/kondo/CobrancaResumoIntegrationTest.java)
- [`FiltrosListagemIntegrationTest`](../src/test/java/com/codewithus/kondo/FiltrosListagemIntegrationTest.java)

### Conclusao

O modulo de cobranca ja entrega o escopo prometido para o MVP backend.

## 4. Financeiro - Pagamento

### Status geral

`Implementado`

### O que ja esta feito

- entidade de pagamento em [`Pagamento`](../src/main/java/com/codewithus/kondo/domain/entity/Pagamento.java)
- CRUD e listagem em [`PagamentoController`](../src/main/java/com/codewithus/kondo/controller/PagamentoController.java)
- registro manual de pagamento em [`PagamentoServiceImpl`](../src/main/java/com/codewithus/kondo/service/impl/PagamentoServiceImpl.java)
- baixa automatica da cobranca para `PAGA`
- protecao contra duplicidade de `transactionId`
- `transactionId` globalmente unico, mesmo apos exclusao logica
- restricao para nao pagar cobranca ja paga
- exclusao logica com preservacao de historico

### O que valida essa parte

- [`PagamentoIntegrationTest`](../src/test/java/com/codewithus/kondo/PagamentoIntegrationTest.java)
- [`AsaasWebhookIntegrationTest`](../src/test/java/com/codewithus/kondo/AsaasWebhookIntegrationTest.java)

### Conclusao

O modulo de pagamento esta pronto para o MVP e ja cobre fluxo manual e automatico.

## 5. Webhook e Integracao com Gateway

### Status geral

`Implementado`

### O que ja esta feito

- integracao sandbox com Asaas em [`AsaasCobrancaGatewayImpl`](../src/main/java/com/codewithus/kondo/service/impl/AsaasCobrancaGatewayImpl.java)
- criacao ou reutilizacao de customer no Asaas
- criacao de cobranca externa
- endpoint dedicado para webhook em [`AsaasWebhookController`](../src/main/java/com/codewithus/kondo/controller/AsaasWebhookController.java)
- validacao de autenticidade por HMAC, com suporte legado por token configuravel
- idempotencia de evento com persistencia de evento processado
- tratamento transacional do fluxo webhook -> pagamento -> cobranca `PAGA`
- retorno consistente para reentrega de evento duplicado

### O que valida essa parte

- [`AsaasWebhookIntegrationTest`](../src/test/java/com/codewithus/kondo/AsaasWebhookIntegrationTest.java)

### Conclusao

O principal diferencial tecnico do MVP backend ja esta implementado.

## 6. Inadimplencia

### Status geral

`Implementado`

### O que ja existe

- status `VENCIDA` em [`StatusCobrancaEnum`](../src/main/java/com/codewithus/kondo/domain/enums/StatusCobrancaEnum.java)
- listagem de inadimplentes com base em cobrancas vencidas
- exposicao desse dado nos endpoints financeiros

### O que valida essa parte

- [`FiltrosListagemIntegrationTest`](../src/test/java/com/codewithus/kondo/FiltrosListagemIntegrationTest.java)
- [`CobrancaResumoIntegrationTest`](../src/test/java/com/codewithus/kondo/CobrancaResumoIntegrationTest.java)

### Conclusao

O recurso de inadimplencia minimo do MVP esta pronto no backend.

## 7. Auditoria

### Status geral

`Implementado`

### O que ja esta feito

- tabela de auditoria via migration
- entidade e service de auditoria
- registro de eventos criticos financeiros e operacionais
- endpoint administrativo de consulta

### O que valida essa parte

- [`AuditoriaIntegrationTest`](../src/test/java/com/codewithus/kondo/AuditoriaIntegrationTest.java)
- [`AuditoriaControllerIntegrationTest`](../src/test/java/com/codewithus/kondo/AuditoriaControllerIntegrationTest.java)

### Conclusao

O nivel de auditoria basica prometido no MVP esta implementado.

## 8. Frontend

### Status geral

`Implementado como MVP demonstravel`

### O que ja esta feito

- app React + TypeScript + Vite em `frontend/`
- proxy local `/api` para consumir o backend em `localhost:8080`
- dashboard financeiro consumindo `GET /cobrancas/dashboard`
- tela de cobrancas consumindo `GET /cobrancas`
- modal/visualizacao Pix consumindo `GET /cobrancas/{id}/pix`
- portal do morador com historico de cobrancas e Pix
- tela de estrutura consumindo `GET /blocos` e `GET /unidades`
- central de chamados consumindo `GET /chamados`, `POST /chamados` e `PUT /chamados/{id}`
- selecao de contexto por perfil usando `/meu-contexto` quando disponivel e fallback local para demonstracao visual
- massa local em `src/main/resources/data-local.sql` para demonstracao

### O que ainda e evolucao

- login OIDC real no frontend
- formularios completos para criacao/edicao de cobrancas, blocos, unidades e demais cadastros
- telas completas de despesas, contratos, moradores, comunicados e relatorios

### Conclusao

O frontend minimo ja permite demonstrar o MVP de ponta a ponta em ambiente local. Para uso produtivo, o principal complemento e trocar o login simulado pelo provedor OIDC real e completar os formularios de cadastro que ficaram fora do recorte minimo.

## Checklist consolidado

### Ja concluido no backend

- autenticacao via OIDC e validacao de JWT
- controle de acesso por roles
- isolamento por condominio
- CRUD de condominio, bloco, unidade e area comum
- cadastro de usuarios e acessos
- cobranca manual e em lote
- integracao sandbox com gateway
- webhook com validacao e idempotencia
- registro automatico do pagamento
- atualizacao automatica da cobranca para `PAGA`
- inadimplencia
- auditoria basica
- Swagger refinado para integracao
- soft delete nas entidades criticas do MVP

### Ainda falta para o MVP completo do produto

- login OIDC real no frontend
- formularios completos para acoes administrativas que ainda estao apenas visuais
- documentacao operacional final para demonstracao e entrega

## Ordem recomendada daqui para frente

1. validar o fluxo local completo com backend e frontend rodando juntos
2. revisar a matriz de rastreabilidade para refletir o frontend MVP
3. integrar o login OIDC real no frontend, se isso for exigido para a apresentacao ou entrega
4. decidir o que entra como primeiro passo pos-MVP: notificacoes, WhatsApp, relatorios ou robustez operacional

## Conclusao final

No estado atual, o Kondo possui um MVP demonstravel localmente: backend validado por testes, frontend minimo integrado, massa de dados local e fluxo financeiro consultavel pela interface. As pendencias restantes sao evolucoes de produto e acabamento operacional, principalmente login OIDC real no frontend e formularios completos para cadastros administrativos.
