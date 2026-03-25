# Checklist de Handoff para Frontend

## Objetivo

Este documento serve como checklist rapido para a pessoa responsavel pelo frontend iniciar a integracao com o backend do Kondo sem depender de descoberta manual de contratos e regras de negocio.

A fonte oficial continua sendo o Swagger da aplicacao:

- `GET /swagger-ui.html`
- `GET /v3/api-docs`

Este checklist existe para orientar a ordem de consumo e destacar os pontos que mais costumam gerar duvida.

## 1. Preparacao inicial

- confirmar a URL base da API no ambiente local ou de teste
- abrir o Swagger e validar os endpoints disponiveis
- confirmar o fluxo de autenticacao com JWT
- alinhar qual perfil sera usado na integracao inicial: `ADMIN`, `SINDICO` ou `MORADOR`

## 2. Endpoints minimos para o MVP

### Contexto do usuario

- `GET /meu-contexto`

Uso:

- descobrir perfil atual
- descobrir escopo do usuario autenticado
- iniciar a montagem da navegacao da interface

### Dashboard financeiro

- `GET /cobrancas/dashboard`

Uso:

- cards principais
- pagamentos recentes
- inadimplentes recentes

### Resumo financeiro

- `GET /cobrancas/resumo`

Uso:

- totais de cobrancas
- totais pagos
- totais em aberto
- totais inadimplentes

### Lista de cobrancas

- `GET /cobrancas`

Filtros uteis:

- `condominioId`
- `status`
- `competencia`

### Inadimplencia

- `GET /cobrancas/inadimplentes`

Uso:

- tela ou secao de inadimplencia

### Detalhe da cobranca

- `GET /cobrancas/{id}`

### Pagamento Pix

- `GET /cobrancas/{id}/pix`

Campos uteis:

- `urlPagamentoExterno`
- `pixCopiaCola`
- `pixQrCodeBase64`
- `pixExpiracao`

### Historico de pagamentos

- `GET /pagamentos`

### Estrutura condominial

- `GET /condominios`
- `GET /blocos`
- `GET /unidades`
- `GET /areas-comuns`

### Cadastros de apoio

- `GET /fornecedores`
- `GET /despesas`
- `GET /chamados`

## 3. Regras de escopo que o frontend precisa respeitar

- `ADMIN` pode operar em todo o escopo
- `SINDICO` opera apenas nos condominios permitidos
- `MORADOR` enxerga apenas o proprio escopo financeiro e operacional
- em `Chamados`, o morador lista apenas os proprios chamados
- em `Cobrancas`, o morador lista apenas as proprias cobrancas
- em `Pagamentos`, o morador lista apenas os proprios pagamentos
- em `Unidades`, o morador lista apenas a propria unidade
- em `Despesas` e `Fornecedores`, o morador pode visualizar os dados do proprio condominio

## 4. Regras importantes de comportamento

- os endpoints `DELETE` principais do MVP fazem exclusao logica, nao remocao fisica
- registros arquivados deixam de aparecer nas listagens comuns
- `transactionId` de pagamento continua unico globalmente
- `referenciaExterna` de cobranca continua unica globalmente
- `unidade + competencia` so pode se repetir quando a cobranca anterior estiver arquivada

## 5. Ordem recomendada de integracao

1. integrar autenticacao e envio do token Bearer
2. consumir `GET /meu-contexto`
3. montar dashboard com `GET /cobrancas/dashboard`
4. montar listagem com `GET /cobrancas`
5. montar detalhe com `GET /cobrancas/{id}`
6. montar pagamento com `GET /cobrancas/{id}/pix`
7. montar historico com `GET /pagamentos`

## 6. Itens para validar cedo com o backend

- formato exato do token JWT usado no ambiente
- comportamento esperado quando o usuario nao possui acesso vinculado
- mensagens de erro que a interface deve tratar
- filtros realmente necessarios nas primeiras telas
- quais telas do MVP entram na primeira entrega e quais ficam para depois

## 7. Checklist de pronto para a integracao

- o frontend consegue autenticar e chamar a API com Bearer token
- o frontend consegue obter contexto do usuario
- o dashboard carrega sem regra duplicada no cliente
- a listagem de cobrancas respeita o escopo por perfil
- a tela de pagamento consegue mostrar QR Code e copia-e-cola Pix
- o historico de pagamentos funciona para o perfil alvo
- o frontend trata respostas de erro e acesso negado

## 8. Referencias complementares

- [`docs/frontend-financeiro-integracao.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/docs/frontend-financeiro-integracao.md)
- [`docs/mvp-gap-analysis.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/docs/mvp-gap-analysis.md)
- [`docs/mvp-backlog.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/docs/mvp-backlog.md)
- [`README.md`](/Users/alinesilva/Desktop/UNIESP/APS/kondo/Kondo/README.md)

## Observacao final

Se houver qualquer divergencia entre este checklist e o Swagger, considere o Swagger como fonte de verdade.
