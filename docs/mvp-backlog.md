# Backlog Tecnico do MVP - Kondo

## Objetivo

Este backlog transforma a analise de gaps do MVP em um plano de execucao objetivo, organizado por prioridade, epicos, tarefas tecnicas e criterios de aceite.

Documento de referencia:

- [`README.md`](../README.md)
- [`mvp-gap-analysis.md`](mvp-gap-analysis.md)

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
- webhook com validacao HMAC e suporte legado por token configuravel
- idempotencia de evento
- baixa automatica da cobranca
- inadimplencia
- auditoria basica
- Pix, resumo e dashboard para o frontend
- soft delete nas entidades principais do MVP

### Ja concluido no frontend MVP

- dashboard financeiro integrado ao backend
- tela de cobrancas integrada ao backend
- visualizacao de Pix para o morador
- estrutura de blocos e unidades integrada ao backend
- central de chamados com listagem, abertura e transicao de status
- selecao de contexto e modo local de demonstracao
- massa de dados local para demonstracao

## Priorizacao geral

### Prioridade P0

Itens necessarios para manter o MVP demonstravel e coerente.

- validacao final de integracao backend + frontend
- ajuste documental final do estado do projeto
- revisao da matriz de rastreabilidade para refletir o frontend MVP

### Prioridade P1

Itens importantes para melhorar robustez operacional logo apos a entrega do MVP.

- login OIDC real no frontend
- formularios completos para acoes administrativas que hoje estao apenas visuais
- refinamento de observabilidade
- melhoria de documentacao operacional
- ampliacao da massa de dados para demonstracao

### Prioridade P2

Itens de evolucao natural pos-MVP.

- notificacoes
- WhatsApp
- relatorios
- automacoes adicionais

## Epico 1 - Handoff para Frontend

### Status

`Concluido como MVP demonstravel`

### Objetivo

Garantir que a pessoa responsavel pelo frontend consiga consumir o backend do MVP sem depender de redescoberta de contrato ou regra de negocio.
No estado atual, o frontend ja consome os contratos principais via API local e proxy do Vite.

### Tarefa 1.1 - Validar contratos com o frontend

### Descricao

Revisar junto com a pessoa do frontend:

- autenticacao esperada
- headers necessarios
- fluxo de listagem por perfil
- uso de `pix`, `resumo` e `dashboard`
- comportamento de exclusao logica

### Criterios de aceite

- endpoints necessarios para o frontend MVP estao confirmados
- o frontend consome dashboard, cobrancas, Pix, estrutura e chamados
- nao existe ambiguidade sobre regras por perfil no recorte demonstravel
- o Swagger permanece como fonte oficial dos contratos

### Tarefa 1.2 - Refinar exemplos de payload se necessario

### Descricao

Caso a integracao do frontend levante duvidas, complementar o Swagger com exemplos adicionais de request e response.

### Criterios de aceite

- exemplos cobrem os fluxos principais de cobranca e pagamento
- erros comuns estao descritos de forma clara

### Tarefa 1.3 - Preparar massa de dados para demonstracao

### Status da tarefa

`Concluida`

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
- a massa local esta disponivel em `src/main/resources/data-local.sql`

## Epico 1.5 - Frontend MVP

### Status

`Concluido como MVP demonstravel`

### Objetivo

Permitir que o MVP seja demonstrado pela interface web, consumindo os endpoints principais do backend.

### Entregas

- dashboard financeiro do sindico
- listagem e status de cobrancas
- visualizacao Pix no portal do morador
- estrutura de blocos e unidades
- central de chamados com abertura e atualizacao de status
- proxy local do Vite para `/api`
- fallback local de contexto para estudo quando `/meu-contexto` nao estiver disponivel

### Pendencias pos-MVP

- autenticar o frontend com OIDC real
- implementar formularios completos para nova cobranca, novo bloco, adicionar unidade e demais cadastros
- completar telas de despesas, contratos, moradores e comunicados

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
- validacao de assinatura HMAC e suporte legado por token configuravel
- idempotencia de evento
- baixa automatica da cobranca
- testes ponta a ponta do fluxo automatico
- cobranca em lote simples
- endpoint e visao de inadimplencia
- auditoria basica

## Definicao de pronto do MVP

O MVP sera considerado pronto como produto quando:

- o backend permanecer estavel com a suite verde: concluido
- o frontend minimo consumir os fluxos principais: concluido para demonstracao local
- o fluxo `cobranca -> Pix/webhook -> pagamento -> cobranca PAGA` puder ser demonstrado: concluido no backend e consultavel pela interface
- o dashboard e a inadimplencia puderem ser visualizados na interface: concluido
- a documentacao principal refletir o estado atual: em fechamento

## Conclusao

O backlog do MVP mudou de natureza. O principal volume de implementacao backend e o frontend minimo demonstravel ja foram entregues. O foco agora deve migrar para fechamento documental, validacao final, login OIDC real no frontend e evolucoes pos-MVP.
