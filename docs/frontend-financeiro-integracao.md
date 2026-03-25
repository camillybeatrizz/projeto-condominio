# Integração Frontend Financeiro

## Fonte oficial
A fonte oficial de contratos da API do Kondo é o Swagger/OpenAPI exposto pela própria aplicação:

- `GET /v3/api-docs`
- `GET /swagger-ui.html`

Este guia existe apenas para acelerar o consumo do frontend nas telas financeiras do MVP.
Os exemplos e descrições detalhadas devem ser consultados primeiro no Swagger.

## Fluxo sugerido por tela

### 1. Dashboard financeiro
Endpoint principal:

- `GET /cobrancas/dashboard`

Uso esperado:

- preencher cards de totais
- listar inadimplentes recentes
- listar pagamentos recentes

Campos principais:

- `resumo`
- `inadimplentesRecentes`
- `pagamentosRecentes`

Quando usar filtro:

- enviar `condominioId` para contexto de síndico ou administração
- omitir `condominioId` quando a tela representar todo o escopo do usuário autenticado

### 2. Cards de resumo
Endpoint principal:

- `GET /cobrancas/resumo`

Uso esperado:

- cards de totais no topo da tela
- resumo rápido em telas secundárias

Campos principais:

- `totalCobrancas`
- `totalAbertas`
- `totalPagas`
- `totalInadimplentes`
- `valorTotal`
- `valorAberto`
- `valorPago`
- `valorInadimplente`

### 3. Lista de cobranças
Endpoint principal:

- `GET /cobrancas`

Filtros úteis:

- `condominioId`
- `status`
- `competencia`

Uso esperado:

- listagem principal de cobranças
- filtros por competência e status

### 4. Lista de inadimplentes
Endpoint principal:

- `GET /cobrancas/inadimplentes`

Uso esperado:

- tabela dedicada de inadimplência
- alertas para síndico

### 5. Detalhe da cobrança
Endpoint principal:

- `GET /cobrancas/{id}`

Uso esperado:

- cabeçalho da tela de detalhe
- dados básicos da cobrança

### 6. Pagamento Pix da cobrança
Endpoint principal:

- `GET /cobrancas/{id}/pix`

Uso esperado:

- exibir QR Code
- exibir botão copiar Pix
- exibir botão para abrir link externo do gateway
- exibir informação de expiração

Campos principais:

- `urlPagamentoExterno`
- `pixCopiaCola`
- `pixQrCodeBase64`
- `pixExpiracao`

Implementação sugerida:

- montar imagem com `data:image/png;base64,<pixQrCodeBase64>`
- usar `pixCopiaCola` em ação de copiar para área de transferência

### 7. Lista de pagamentos
Endpoint principal:

- `GET /pagamentos`

Filtros úteis:

- `condominioId`
- `dataInicio`
- `dataFim`

Uso esperado:

- histórico de pagamentos
- auditoria operacional do financeiro

## Segurança e perfis
O Swagger já documenta a segurança Bearer JWT e as permissões por endpoint.

Resumo prático:

- `ADMIN` vê todo o escopo
- `SINDICO` vê apenas condomínios permitidos
- `MORADOR` vê apenas cobranças e pagamentos próprios

## Ordem recomendada de consumo no frontend

1. chamar `GET /cobrancas/dashboard` na tela inicial
2. usar `GET /cobrancas` para a listagem detalhada
3. usar `GET /cobrancas/{id}` ao abrir um detalhe
4. usar `GET /cobrancas/{id}/pix` na tela de pagamento
5. usar `GET /pagamentos` para histórico ou conciliação

## Observação final
Se houver qualquer divergência entre este guia e o Swagger, considere o Swagger como fonte de verdade.
