# Analise de Gap do MVP - Kondo

## Objetivo

Este documento consolida o estado atual do backend em relacao ao MVP definido no [`README.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/README.md), para responder com clareza:

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
- webhook de pagamento com validacao de token, idempotencia e baixa automatica
- auditoria basica
- consulta de inadimplencia
- endpoints prontos para consumo do frontend (`pix`, `resumo`, `dashboard`)
- exclusao logica com preservacao de historico

O principal gap para considerar o MVP totalmente pronto neste repositorio nao esta mais no backend financeiro. O que ainda falta e, principalmente:

- frontend minimo
- eventual refinamento de documentacao e operacao
- ajustes de produto que o time ainda queira incorporar antes da demonstracao

Em resumo:

- Fases 1 a 6: implementadas no backend
- Fase 7: ainda pendente neste repositorio

## Fonte de evidencia

Esta analise foi baseada em:

- codigo-fonte em `src/main/java`
- migrations em `src/main/resources/db/migration`
- testes automatizados em `src/test/java`
- matriz de rastreabilidade em [`docs/matriz-rastreabilidade-kondo.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/docs/matriz-rastreabilidade-kondo.md)
- plano do MVP em [`README.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/README.md)

Tambem foi validado que a suite atual passa com sucesso:

```bash
./mvnw clean test
```

Resultado observado: `95 testes`, `0 falhas`, `0 erros`.

## Status por area do MVP

### 1. Autenticacao e Seguranca

### Status geral

`Implementado`

### O que ja esta feito

- Resource Server com JWT em [`SecurityConfig`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/config/SecurityConfig.java)
- validacao de issuer e audience em [`SecurityConfig`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/config/SecurityConfig.java)
- conversao de claims e grupos para roles em [`JwtRoleConverter`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/security/JwtRoleConverter.java)
- protecao por `@PreAuthorize` nos controllers
- resolucao do usuario autenticado com foco em `externalId` em [`CurrentUserResolver`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/security/CurrentUserResolver.java)
- vinculacao automatica de `externalId` quando aplicavel em [`UsuarioIdentityLinkService`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/security/UsuarioIdentityLinkService.java)

### O que valida essa parte

- [`SecurityAuthorizationIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/SecurityAuthorizationIntegrationTest.java)
- [`MeuContextoIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/MeuContextoIntegrationTest.java)
- [`StrictExternalIdentityIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/StrictExternalIdentityIntegrationTest.java)

### Conclusao

Esta area sustenta o MVP com seguranca suficiente para operacao real do fluxo financeiro.

## 2. Gestao de Condominio

### Status geral

`Implementado`

### O que ja esta feito

- CRUD de condominio em [`CondominioController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/CondominioController.java)
- CRUD de bloco em [`BlocoController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/BlocoController.java)
- CRUD de unidade em [`UnidadeController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/UnidadeController.java)
- CRUD de area comum em [`AreaComumController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/AreaComumController.java)
- validacoes de integridade e escopo em:
  - [`CondominioServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/CondominioServiceImpl.java)
  - [`BlocoServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/BlocoServiceImpl.java)
  - [`UnidadeServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/UnidadeServiceImpl.java)
  - [`AreaComumServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/AreaComumServiceImpl.java)
- exclusao logica para preservar historico em condominio, bloco e unidade

### O que ja esta feito em usuario/acesso

- CRUD de usuario em [`UsuarioController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/UsuarioController.java)
- CRUD de acesso em [`AcessoController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/AcessoController.java)
- vinculo entre usuario, condominio, perfil e unidade em [`AcessoServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/AcessoServiceImpl.java)
- sincronizacao do morador na unidade ao criar ou alterar acesso

### Conclusao

O bloco estrutural do MVP esta pronto no backend.

## 3. Financeiro - Cobranca

### Status geral

`Implementado`

### O que ja esta feito

- entidade de cobranca em [`Cobranca`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/domain/entity/Cobranca.java)
- CRUD e listagem em [`CobrancaController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/CobrancaController.java)
- regras de negocio em [`CobrancaServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/CobrancaServiceImpl.java)
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

- [`CobrancaBusinessIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/CobrancaBusinessIntegrationTest.java)
- [`CobrancaLoteIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/CobrancaLoteIntegrationTest.java)
- [`CobrancaResumoIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/CobrancaResumoIntegrationTest.java)
- [`FiltrosListagemIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/FiltrosListagemIntegrationTest.java)

### Conclusao

O modulo de cobranca ja entrega o escopo prometido para o MVP backend.

## 4. Financeiro - Pagamento

### Status geral

`Implementado`

### O que ja esta feito

- entidade de pagamento em [`Pagamento`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/domain/entity/Pagamento.java)
- CRUD e listagem em [`PagamentoController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/PagamentoController.java)
- registro manual de pagamento em [`PagamentoServiceImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/PagamentoServiceImpl.java)
- baixa automatica da cobranca para `PAGA`
- protecao contra duplicidade de `transactionId`
- `transactionId` globalmente unico, mesmo apos exclusao logica
- restricao para nao pagar cobranca ja paga
- exclusao logica com preservacao de historico

### O que valida essa parte

- [`PagamentoIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/PagamentoIntegrationTest.java)
- [`AsaasWebhookIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/AsaasWebhookIntegrationTest.java)

### Conclusao

O modulo de pagamento esta pronto para o MVP e ja cobre fluxo manual e automatico.

## 5. Webhook e Integracao com Gateway

### Status geral

`Implementado`

### O que ja esta feito

- integracao sandbox com Asaas em [`AsaasCobrancaGatewayImpl`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/service/impl/AsaasCobrancaGatewayImpl.java)
- criacao ou reutilizacao de customer no Asaas
- criacao de cobranca externa
- endpoint dedicado para webhook em [`AsaasWebhookController`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/controller/AsaasWebhookController.java)
- validacao de autenticidade por token configuravel
- idempotencia de evento com persistencia de evento processado
- tratamento transacional do fluxo webhook -> pagamento -> cobranca `PAGA`
- retorno consistente para reentrega de evento duplicado

### O que valida essa parte

- [`AsaasWebhookIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/AsaasWebhookIntegrationTest.java)

### Conclusao

O principal diferencial tecnico do MVP backend ja esta implementado.

## 6. Inadimplencia

### Status geral

`Implementado`

### O que ja existe

- status `VENCIDA` em [`StatusCobrancaEnum`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/main/java/com/codewithus/kondo/domain/enums/StatusCobrancaEnum.java)
- listagem de inadimplentes com base em cobrancas vencidas
- exposicao desse dado nos endpoints financeiros

### O que valida essa parte

- [`FiltrosListagemIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/FiltrosListagemIntegrationTest.java)
- [`CobrancaResumoIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/CobrancaResumoIntegrationTest.java)

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

- [`AuditoriaIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/AuditoriaIntegrationTest.java)
- [`AuditoriaControllerIntegrationTest`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/src/test/java/com/codewithus/kondo/AuditoriaControllerIntegrationTest.java)

### Conclusao

O nivel de auditoria basica prometido no MVP esta implementado.

## 8. Frontend

### Status geral

`Pendente neste repositorio`

### O que falta

- dashboard simples
- tela de cobrancas
- exibicao de status
- visualizacao de pagamentos processados

### O que o backend ja entrega para apoiar o frontend

- Swagger atualizado com contratos e exemplos
- endpoint `GET /cobrancas/{id}/pix`
- endpoint `GET /cobrancas/resumo`
- endpoint `GET /cobrancas/dashboard`
- regras de seguranca e escopo prontas para consumo

### Conclusao

O backend esta pronto para handoff ao frontend, mas o MVP completo do produto ainda depende da interface.

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

- frontend minimo

## Ordem recomendada daqui para frente

1. alinhar com a pessoa do frontend o consumo do Swagger e dos endpoints financeiros
2. revisar o README e a matriz de rastreabilidade para refletir o backend atual
3. decidir o que entra como primeiro passo pos-MVP: notificacoes, WhatsApp, relatorios ou robustez operacional

## Conclusao final

No estado atual, o Kondo ja tem o MVP backend essencialmente pronto. O principal trabalho restante para considerar o MVP completo como produto demonstravel e entregar a camada de frontend que consuma os fluxos ja implementados.
