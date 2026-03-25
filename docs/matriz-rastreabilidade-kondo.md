# Matriz de Rastreabilidade do Kondo

## Objetivo

Este documento complementa o ERS e registra a rastreabilidade entre requisitos, backend atual, testes automatizados e status de implementacao no repositorio.

## Legenda de status

- `Implementado`: ha reflexo claro em codigo do backend e artefatos de teste.
- `Parcial`: existe base tecnica relacionada, mas o requisito do ERS ainda nao esta completo no repositorio.
- `Planejado`: o requisito aparece no ERS, mas ainda nao ha evidencia suficiente de implementacao no backend atual.

## Requisitos Funcionais

| ID | Requisito | Modulo/artefato relacionado no backend | Testes/artefatos relacionados | Status |
| --- | --- | --- | --- | --- |
| RF01 | Autenticacao e autorizacao via provedor OIDC | `SecurityConfig`, `CurrentUserResolver`, `JwtRoleConverter`, profiles `application-keycloak.properties` e `application-pinniped.properties` | `SecurityAuthorizationIntegrationTest`, `MeuContextoIntegrationTest` | Implementado |
| RF02 | Validacao de token JWT | `SecurityConfig`, `KondoSecurityProperties` | `SecurityAuthorizationIntegrationTest` | Implementado |
| RF03 | Recuperacao de credenciais via Identity Provider | documentado no ERS, sem endpoint proprio no backend | sem teste especifico no repositorio | Planejado |
| RF04 | Autorizacao baseada em claims | `SecurityConfig`, `JwtRoleConverter`, regras de escopo e controllers protegidos | `SecurityAuthorizationIntegrationTest` | Implementado |
| RF05 | Sincronizacao de usuario local via `externalId` | `CurrentUserResolver`, `UsuarioIdentityLinkService`, `UsuarioServiceImpl` | `StrictExternalIdentityIntegrationTest`, `UsuarioIntegrationTest`, `UsuarioBusinessIntegrationTest` | Implementado |
| RF06 | Logout federado | nao ha endpoint/controlador dedicado no backend atual | sem teste especifico no repositorio | Planejado |
| RF07 | Auditoria de acoes | `AuditoriaEvento`, `AuditoriaController`, `AuditoriaServiceImpl`, migrations e registro de eventos criticos | `AuditoriaIntegrationTest`, `AuditoriaControllerIntegrationTest` | Implementado |
| RF08 | Cadastro de condominio | `CondominioController`, services, repository, migrations | `CondominioIntegrationTest`, `CondominioBusinessIntegrationTest` | Implementado |
| RF09 | Cadastro de blocos | `BlocoController`, service, repository | `EstruturaIntegridadeIntegrationTest` | Implementado |
| RF10 | Cadastro de unidades | `UnidadeController`, service, repository | `EstruturaIntegridadeIntegrationTest`, `AcessoIntegrationTest` | Implementado |
| RF11 | Cadastro de moradores vinculados a unidades | `UsuarioController`, `AcessoController`, `UnidadeService` | `AcessoIntegrationTest`, `UsuarioIntegrationTest`, `UsuarioBusinessIntegrationTest` | Implementado |
| RF12 | Cadastro de areas comuns | `AreaComumController`, `AreaComumServiceImpl`, entidade, repository e migration propria | `AreaComumIntegrationTest` | Implementado |
| RF13 | Geracao de taxas condominiais por competencia | modulo de cobranca com emissao manual e em lote simples por competencia | `CobrancaBusinessIntegrationTest`, `CobrancaLoteIntegrationTest` | Implementado |
| RF14 | Visualizacao de cobrancas | `CobrancaController`, filtros e escopo por usuario | `CobrancaBusinessIntegrationTest`, `FiltrosListagemIntegrationTest` | Implementado |
| RF15 | Emissao automatica de boleto/Pix | integracao com Asaas gera cobranca externa e expoe dados Pix por cobranca | `CobrancaBusinessIntegrationTest`, `AsaasWebhookIntegrationTest` | Parcial |
| RF16 | Registro automatico de pagamento por webhook | `AsaasWebhookController`, `AsaasWebhookServiceImpl`, persistencia de evento processado e baixa automatica | `AsaasWebhookIntegrationTest` | Implementado |
| RF17 | Integracao com gateway de pagamento | integracao sandbox com Asaas para customer, cobranca externa, Pix e webhook | `AsaasWebhookIntegrationTest`, `CobrancaBusinessIntegrationTest` | Implementado |
| RF18 | Controle de inadimplencia | filtros, resumo e dashboard com cobrancas vencidas e nao pagas | `CobrancaResumoIntegrationTest`, `FiltrosListagemIntegrationTest` | Implementado |
| RF19 | Historico financeiro do morador | cobrancas e pagamentos com escopo por morador, incluindo resumo e dashboard | `PagamentoIntegrationTest`, `FiltrosListagemIntegrationTest`, `CobrancaResumoIntegrationTest` | Implementado |
| RF20 | Notificacao de vencimento | sem modulo de notificacao | sem teste especifico no repositorio | Planejado |
| RF21 | Notificacao de pagamento confirmado | sem modulo de notificacao | sem teste especifico no repositorio | Planejado |
| RF22 | Previsao de fluxo de caixa | nao ha modulo analitico especifico | sem teste especifico no repositorio | Planejado |
| RF23 | Solicitar segunda via via WhatsApp | nao ha integracao WhatsApp no backend atual | sem teste especifico no repositorio | Planejado |
| RF24 | Consultar debitos via WhatsApp | nao ha integracao WhatsApp no backend atual | sem teste especifico no repositorio | Planejado |
| RF25 | Reserva de areas comuns via WhatsApp | nao ha modulo de areas comuns/reserva | sem teste especifico no repositorio | Planejado |
| RF26 | Abertura de chamado via WhatsApp | existe modulo de chamado, mas nao interface WhatsApp | `ChamadoDespesaBusinessIntegrationTest` cobre chamado no backend | Parcial |
| RF27 | Consulta de status do chamado via WhatsApp | existe consulta de chamado, mas nao interface WhatsApp | `ChamadoDespesaBusinessIntegrationTest` | Parcial |
| RF28 | Consulta de saldo atual | endpoints `GET /cobrancas/resumo` e `GET /cobrancas/dashboard` agregam totais, pagos, abertos e inadimplentes | `CobrancaResumoIntegrationTest`, `FiltrosListagemIntegrationTest` | Parcial |
| RF29 | Projecao de impacto financeiro | nao ha simulador financeiro especifico | sem teste especifico no repositorio | Planejado |
| RF30 | Analise de inadimplencia | base financeira existe, mas nao ha modulo analitico dedicado | sem teste especifico no repositorio | Planejado |
| RF31 | Avaliacao de desvio orcamentario | nao ha modulo de orcamento previsto versus realizado | sem teste especifico no repositorio | Planejado |
| RF32 | Simulacao de decisao financeira | nao ha motor de simulacao no backend atual | sem teste especifico no repositorio | Planejado |
| RF33 | Geracao automatica de relatorio mensal | nao ha gerador de relatorio mensal consolidado | sem teste especifico no repositorio | Planejado |
| RF34 | Categorizacao automatica de despesas | existe categoria em despesa, mas nao classificacao automatica evidente | `ChamadoDespesaBusinessIntegrationTest` cobre regras de despesa | Parcial |
| RF35 | Inclusao de documentos comprobatórios | nao ha modulo de anexos no backend atual | sem teste especifico no repositorio | Planejado |
| RF36 | Graficos financeiros | nao ha recurso de grafico no backend atual | sem teste especifico no repositorio | Planejado |
| RF37 | Exportacao da prestacao de contas em PDF | nao ha exportacao PDF no backend atual | sem teste especifico no repositorio | Planejado |
| RF38 | Cadastro de contratos | `ContratoController`, services, repository | `ContratoIntegrationTest` | Implementado |
| RF39 | Controle de garantias | nao ha entidade/modulo de garantia no backend atual | sem teste especifico no repositorio | Planejado |
| RF40 | Registro de servicos recorrentes | nao ha modulo de servico recorrente no backend atual | sem teste especifico no repositorio | Planejado |
| RF41 | Historico de fornecedor | ha fornecedor e contratos, mas nao historico consolidado como funcionalidade propria | `CadastrosTransversaisIntegrationTest`, `ContratoIntegrationTest` | Parcial |
| RF42 | Comparacao de orcamentos | nao ha modulo de comparacao de orcamentos | sem teste especifico no repositorio | Planejado |
| RF43 | Alertas de vencimento | nao ha modulo de alerta automatizado | sem teste especifico no repositorio | Planejado |
| RF44 | Gestao completa de chamados | `ChamadoController`, services, enum de status e regras de transicao | `ChamadoDespesaBusinessIntegrationTest`, `FiltrosListagemIntegrationTest` | Implementado |

## Requisitos Nao Funcionais

| ID | Requisito | Evidencia no repositorio | Testes/artefatos relacionados | Status |
| --- | --- | --- | --- | --- |
| RNF01 | Comunicacao segura via HTTPS/TLS | depende de infraestrutura e ambiente; nao e verificavel so pelo repositorio | sem teste especifico no repositorio | Planejado |
| RNF02 | Autenticacao via OIDC | configuracoes de Resource Server e profiles de IdP | `SecurityAuthorizationIntegrationTest`, `MeuContextoIntegrationTest` | Implementado |
| RNF03 | Validacao de assinatura/JWKS | `SecurityConfig` usa `issuer-uri` e `jwk-set-uri` | `SecurityAuthorizationIntegrationTest` | Implementado |
| RNF04 | Expiracao de token | suportado pelo Resource Server/JWT validator | coberto indiretamente pela configuracao de seguranca | Parcial |
| RNF05 | Zero armazenamento de credenciais | migration remove senha local e documento de negocio reforca regra | `V3__drop_usuario_senha.sql`, `StrictExternalIdentityIntegrationTest` | Implementado |
| RNF06 | Validacao de webhook | webhook protegido por token configuravel e processamento controlado | `AsaasWebhookIntegrationTest` | Implementado |
| RNF07 | Protecao contra processamento duplicado | idempotencia por evento processado e unicidade de `transactionId` | `AsaasWebhookIntegrationTest`, `PagamentoIntegrationTest` | Implementado |
| RNF08 | Controle de acesso baseado em claims e escopo | seguranca por roles e escopo de condominio | `SecurityAuthorizationIntegrationTest`, `MeuContextoIntegrationTest`, `FiltrosListagemIntegrationTest` | Implementado |
| RNF09 | Registro de auditoria | modulo consolidado de auditoria com persistencia e endpoint de consulta | `AuditoriaIntegrationTest`, `AuditoriaControllerIntegrationTest` | Implementado |
| RNF10 | Tempo de resposta da API | depende de observabilidade e medicao de ambiente | sem teste de desempenho no repositorio | Planejado |
| RNF11 | Processamento de webhook em ate 2s | webhook implementado, mas sem teste formal de desempenho no repositorio | sem teste especifico de performance | Parcial |
| RNF12 | Atualizacao de dashboard em ate 5s | dashboard backend implementado, mas sem teste formal de desempenho no repositorio | `CobrancaResumoIntegrationTest`, `FiltrosListagemIntegrationTest` | Parcial |
| RNF13 | Disponibilidade minima mensal | depende de operacao/infraestrutura | sem teste especifico no repositorio | Planejado |
| RNF14 | Backup automatico | depende de infraestrutura | sem teste especifico no repositorio | Planejado |
| RNF15 | Recuperacao automatica de falhas | depende de infraestrutura | sem teste especifico no repositorio | Planejado |
| RNF16 | Arquitetura stateless | backend usa JWT e nao depende de sessao no servidor | configuracao de seguranca e arquitetura atual | Implementado |
| RNF17 | Isolamento multi-condominio | ha filtros de escopo e ownership por condominio | `SecurityAuthorizationIntegrationTest`, `FiltrosListagemIntegrationTest`, `MeuContextoIntegrationTest` | Implementado |
| RNF18 | Uso de cache | nao ha evidencia de Redis/cache no backend atual | sem teste especifico no repositorio | Planejado |
| RNF19 | Integridade transacional | services financeiros e de acesso aplicam consistencia transacional | `PagamentoIntegrationTest`, `AcessoIntegrationTest` | Implementado |
| RNF20 | Idempotencia de eventos | pipeline de eventos consolidado com evento processado e restricao de transacao | `AsaasWebhookIntegrationTest`, `PagamentoIntegrationTest` | Implementado |
| RNF21 | Registro de eventos financeiros | trilha de eventos em auditoria para cobrancas, pagamentos e webhook | `AuditoriaIntegrationTest`, `AsaasWebhookIntegrationTest` | Implementado |
| RNF22 | Interface responsiva | requisito de frontend; nao verificavel no backend atual | sem artefato correspondente no repositorio | Planejado |
| RNF23 | Interface simplificada via WhatsApp | nao ha implementacao no backend atual | sem artefato correspondente no repositorio | Planejado |
| RNF24 | Clareza das informacoes financeiras | requisito de UX/frontend; backend fornece base parcial de dados | sem artefato correspondente no repositorio | Planejado |
| RNF25 | Conformidade com LGPD | ha base de segregacao e identidade, mas exclusao/anonimizacao ainda precisa de formalizacao | documentos e regras atuais | Parcial |
| RNF26 | Retencao de dados financeiros por 5 anos | nao ha politica tecnica evidenciada no repositorio | sem teste especifico no repositorio | Planejado |
| RNF27 | Versionamento de banco | Flyway em `src/main/resources/db/migration` | migrations V1 a V19 | Implementado |
| RNF28 | Padrao arquitetural do backend | estrutura em camadas clara no codigo | `docs/guia-didatico-spring-boot.md` e packages do projeto | Implementado |
| RNF29 | Padronizacao de codigo, logs e monitoramento | arquitetura e excecoes estao padronizadas, mas logs estruturados/monitoramento ainda nao estao formalizados | `ApiExceptionHandler`, OpenAPI, convencoes de package | Parcial |

## Testes de integracao relevantes por tema

- Seguranca e identidade: `SecurityAuthorizationIntegrationTest`, `MeuContextoIntegrationTest`, `StrictExternalIdentityIntegrationTest`
- Usuarios e acessos: `UsuarioIntegrationTest`, `UsuarioBusinessIntegrationTest`, `AcessoIntegrationTest`
- Estrutura condominial: `CondominioIntegrationTest`, `CondominioBusinessIntegrationTest`, `EstruturaIntegridadeIntegrationTest`, `AreaComumIntegrationTest`
- Financeiro: `CobrancaBusinessIntegrationTest`, `CobrancaLoteIntegrationTest`, `PagamentoIntegrationTest`, `CobrancaResumoIntegrationTest`, `ContratoIntegrationTest`, `CadastrosTransversaisIntegrationTest`, `AsaasWebhookIntegrationTest`, `AuditoriaIntegrationTest`, `AuditoriaControllerIntegrationTest`
- Regras transversais e filtros: `FiltrosListagemIntegrationTest`, `ChamadoDespesaBusinessIntegrationTest`

## Principais lacunas atuais frente ao ERS

- fluxo de recuperacao de credenciais e logout federado
- reservas de areas comuns
- integracoes externas adicionais: email, WhatsApp e evolucao de meios de pagamento
- recursos analiticos avancados: projecoes, simulacoes e prestacao de contas ampliada
- prestacao de contas automatica, anexos, graficos e exportacao PDF
- manutencao ampliada: garantias, servicos recorrentes, orcamentos e alertas

## Uso recomendado deste documento

- manter esta matriz sincronizada sempre que um RF ou RNF sair de `Planejado` para `Parcial` ou `Implementado`
- usar esta tabela como base para atualizar o ERS, o backlog e os diagramas
- relacionar novos testes aos RFs antes de considerar um requisito como implementado
