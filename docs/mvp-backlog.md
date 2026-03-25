# Backlog Tecnico do MVP - Kondo

## Objetivo

Este backlog transforma a analise de gaps do MVP em um plano de execucao objetivo, organizado por prioridade, epicos, tarefas tecnicas e criterios de aceite.

Documento de referencia:

- [`README.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/README.md)
- [`docs/mvp-gap-analysis.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/docs/mvp-gap-analysis.md)

## Meta do MVP

Entregar um fluxo funcional e demonstravel de:

- autenticacao segura
- gestao basica do condominio
- geracao de cobrancas
- recebimento automatico de pagamento via webhook
- baixa automatica da cobranca
- consulta de status financeiro
- rastreabilidade minima das operacoes criticas

## Status geral do backlog

### Ja concluido no backend

- autenticacao e autorizacao
- estrutura condominial
- usuario e acesso
- area comum sem reserva
- cobranca manual
- cobranca em lote simples
- integracao sandbox com Asaas
- webhook com validacao de token
- idempotencia de evento
- baixa automatica da cobranca
- inadimplencia
- auditoria basica
- Pix, resumo e dashboard para o frontend
- soft delete nas entidades principais do MVP

### Principal pendencia do MVP do produto

- frontend minimo

## Priorizacao geral

### Prioridade P0

Itens sem os quais o MVP do produto ainda nao fica demonstravel de ponta a ponta.

- frontend MVP
- validacao final de integracao com o responsavel pelo frontend
- ajuste documental final do estado do projeto

### Prioridade P1

Itens importantes para melhorar robustez operacional logo apos a entrega do MVP.

- refinamento de observabilidade
- melhoria de documentacao operacional
- revisao da matriz de rastreabilidade
- scripts e massa de dados para demonstracao

### Prioridade P2

Itens de evolucao natural pos-MVP.

- notificacoes
- WhatsApp
- relatorios
- automacoes adicionais

## Epico 1 - Handoff para Frontend

### Status

`Pendente`

### Objetivo

Garantir que a pessoa responsavel pelo frontend consiga consumir o backend do MVP sem depender de redescoberta de contrato ou regra de negocio.

### Tarefa 1.1 - Validar contratos com o frontend

### Descricao

Revisar junto com a pessoa do frontend:

- autenticacao esperada
- headers necessarios
- fluxo de listagem por perfil
- uso de `pix`, `resumo` e `dashboard`
- comportamento de exclusao logica

### Criterios de aceite

- endpoints necessarios para o frontend estao confirmados
- nao existe ambiguidade sobre regras por perfil
- o frontend consegue navegar no Swagger como fonte oficial

### Tarefa 1.2 - Refinar exemplos de payload se necessario

### Descricao

Caso a integracao do frontend levante duvidas, complementar o Swagger com exemplos adicionais de request e response.

### Criterios de aceite

- exemplos cobrem os fluxos principais de cobranca e pagamento
- erros comuns estao descritos de forma clara

### Tarefa 1.3 - Preparar massa de dados para demonstracao

### Descricao

Disponibilizar uma base simples para demo com:

- um condominio
- blocos e unidades
- moradores
- cobrancas abertas, pagas e vencidas
- pagamentos recentes

### Criterios de aceite

- existe um caminho simples para subir o ambiente de demonstracao
- os dados permitem exercitar dashboard, resumo, Pix e inadimplencia

## Epico 2 - Fechamento Documental do MVP

### Status

`Em andamento`

### Objetivo

Fazer a documentacao refletir com fidelidade o estado atual do backend e reduzir ruido entre README, backlog, matriz e swagger.

### Tarefa 2.1 - Atualizar gap analysis e backlog

### Status da tarefa

`Concluida`

### Tarefa 2.2 - Revisar README e matriz de rastreabilidade

### Descricao

Ajustar os documentos para refletir:

- webhook implementado
- auditoria implementada
- area comum implementada
- inadimplencia implementada
- soft delete e preservacao de historico

### Criterios de aceite

- a documentacao principal nao contradiz o codigo
- o status de cada RF esta coerente com testes e implementacao

## Epico 3 - Robustez Operacional

### Status

`Pos-MVP`

### Objetivo

Melhorar a operacao do backend sem alterar o escopo funcional do MVP.

### Tarefa 3.1 - Observabilidade do fluxo financeiro

### Descricao

Melhorar logs e rastreabilidade de:

- criacao de cobranca
- integracao com Asaas
- recebimento de webhook
- baixa automatica
- falhas de autenticacao e duplicidade

### Criterios de aceite

- logs permitem diagnosticar falhas de ponta a ponta
- eventos criticos possuem contexto minimo para suporte

### Tarefa 3.2 - Guia operacional local

### Descricao

Documentar de forma pratica:

- configuracao do Keycloak
- configuracao do Asaas sandbox
- execucao local
- acesso ao Swagger

### Criterios de aceite

- uma pessoa nova consegue subir e testar o backend com o guia

## Epico 4 - Evolucao Pos-MVP

### Status

`Pos-MVP`

### Objetivo

Organizar os proximos passos naturais do produto apos o MVP funcional.

### Itens sugeridos

- notificacoes automaticas
- WhatsApp
- relatorios
- Pix e boleto com maior refinamento operacional
- filas e arquitetura orientada a eventos

## Itens que saem do backlog do MVP

Os itens abaixo deixaram de ser backlog do MVP backend porque ja estao implementados:

- integracao sandbox com gateway
- webhook de pagamento
- validacao de assinatura por token configuravel
- idempotencia de evento
- baixa automatica da cobranca
- testes ponta a ponta do fluxo automatico
- cobranca em lote simples
- endpoint e visao de inadimplencia
- auditoria basica

## Definicao de pronto do MVP

O MVP sera considerado pronto como produto quando:

- o backend permanecer estavel com a suite verde
- o frontend minimo consumir os fluxos principais
- o fluxo `cobranca -> Pix/webhook -> pagamento -> cobranca PAGA` puder ser demonstrado
- o dashboard e a inadimplencia puderem ser visualizados na interface

## Conclusao

O backlog do MVP mudou de natureza. O principal volume de implementacao backend ja foi entregue, e o foco agora deve migrar para handoff, integracao com frontend e fechamento documental do produto.
