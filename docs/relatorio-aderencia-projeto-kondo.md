# Relatorio de Aderencia do Projeto Kondo

## 1. Objetivo do relatorio

Este relatorio tem como finalidade analisar se a implementacao atual do projeto **Kondo** atende as diretrizes especificadas na documentacao da pasta `docs/`, principalmente:

- ERS em `docs/Especificações de Requisitos - KONDO.pdf`
- regra de negocio em `docs/regra-de-negocio-kondo.md`
- matriz de rastreabilidade em `docs/matriz-rastreabilidade-kondo.md`
- checklist de coerencia documental em `docs/checklist-coerencia-documental.md`
- README do projeto

A proposta deste documento e apresentar, de forma didatica:

- o que ja esta sendo atendido;
- o que esta apenas parcialmente atendido;
- o que ainda nao foi implementado;
- o que precisa ser melhorado para o projeto ficar mais fechado, coerente e defensavel como entrega academica e tecnica.

## 2. Escopo considerado na analise

Antes de avaliar a aderencia, e importante separar duas visoes que hoje convivem no projeto:

### 2.1 Visao ampla do produto

O ERS descreve uma visao mais completa da plataforma Kondo, incluindo:

- autenticacao federada;
- operacao financeira ampliada;
- integracoes com WhatsApp;
- emissao automatica de boleto e Pix;
- webhook de pagamento;
- notificacoes;
- relatorios e dashboards;
- areas comuns e reservas;
- manutencao ampliada com garantias e servicos recorrentes.

### 2.2 Visao implementada no repositorio atual

O repositorio atual implementa principalmente o **backend central** da plataforma, com foco em:

- estrutura condominial;
- usuarios e acessos;
- segregacao por condominio;
- chamados;
- cobrancas e pagamentos;
- despesas;
- contratos;
- fornecedores;
- contas bancarias;
- autenticacao externa baseada em OIDC.

### 2.3 Conclusao sobre o escopo real

O projeto esta coerente quando analisado como **backend principal da plataforma**.

Ele ainda **nao esta completo** quando comparado a toda a visao funcional apresentada no ERS.

Isso significa que, para o projeto ficar bem fechado e coerente com a proposta, e necessario oficializar uma destas duas leituras:

1. o ERS passa a representar o **produto por fases**, deixando claro o que ja foi entregue e o que e evolucao futura;
2. ou a equipe assume que o ERS representa o **escopo integral da entrega**, e entao o backlog tecnico precisa cobrir os modulos ainda ausentes.

Hoje, a documentacao mais recente ja caminha na direcao da primeira opcao.

## 3. Metodologia utilizada

Esta analise foi baseada em quatro frentes:

1. leitura dos documentos da pasta `docs`;
2. leitura da estrutura do backend em `src/main/java`;
3. verificacao dos testes automatizados em `src/test/java`;
4. execucao da suite de testes com `./mvnw test`.

### 3.1 Resultado da validacao tecnica

A suite automatizada executou com sucesso:

- **63 testes executados**
- **0 falhas**
- **0 erros**

Isso reforca que o nucleo implementado do backend esta estavel e possui boa base de validacao automatizada.

## 4. Panorama geral de aderencia

De forma resumida, o projeto hoje pode ser classificado assim:

### 4.1 O que esta bem atendido

- nucleo backend da gestao condominial;
- autenticacao e autorizacao com OIDC/JWT;
- segregacao de dados por condominio;
- modelagem principal do dominio;
- regras de negocio centrais em financeiro, acessos e chamados;
- protecao por perfil (`ADMIN`, `SINDICO`, `MORADOR`);
- conjunto consistente de testes de integracao.

### 4.2 O que esta parcialmente atendido

- aderencia integral ao ERS;
- rastreabilidade entre requisitos amplos e implementacao atual;
- documentacao completamente sincronizada entre todos os artefatos;
- requisitos financeiros avancados;
- historico e visao gerencial mais rica para o morador e para o sindico.

### 4.3 O que ainda nao esta atendido

- modulos de integracao externa;
- WhatsApp;
- areas comuns e reservas;
- notificacoes;
- webhooks de pagamento;
- dashboards e analiticos;
- exportacao PDF;
- modulo de auditoria;
- recursos de manutencao ampliada descritos no ERS.

## 5. O que esta sendo atendido corretamente

Esta secao detalha os pontos em que o projeto ja demonstra boa aderencia a proposta e a documentacao tecnica mais consistente.

### 5.1 Estrutura do dominio esta bem modelada

O backend implementa corretamente a cadeia estrutural principal do negocio:

- `Condominio` depende de `Endereco`;
- `Bloco` pertence a `Condominio`;
- `Unidade` pertence a `Bloco`;
- `Cobranca` pertence a `Unidade`;
- `Pagamento` pertence a `Cobranca`;
- `Despesa`, `Contrato` e `ContaBancaria` pertencem a `Condominio`;
- `Acesso` faz a ligacao entre usuario, perfil e escopo.

Essa modelagem esta coerente com a regra de negocio e com a proposta do sistema.

### 5.2 O nucleo de seguranca esta alinhado com uma arquitetura moderna

O projeto abandonou login local com senha persistida e passou a operar com:

- autenticacao externa via OIDC;
- validacao de JWT no backend;
- uso de `externalId` como identidade principal;
- suporte a mais de um provedor de identidade, como Keycloak e Pinniped.

Esse ponto e um dos maiores acertos do projeto, porque da consistencia tecnica e melhora bastante a narrativa arquitetural.

### 5.3 Os perfis principais estao coerentes

O backend trabalha hoje com tres perfis oficiais:

- `ADMIN`
- `SINDICO`
- `MORADOR`

Esses perfis aparecem de forma consistente:

- no enum do sistema;
- nas regras de negocio;
- nos controllers com `@PreAuthorize`;
- nos testes de seguranca.

Isso e importante porque evita ambiguidade sobre quem pode fazer o que.

### 5.4 Ha segregacao por condominio e controle de ownership

Um dos requisitos mais relevantes do projeto e evitar que usuarios de um condominio acessem dados de outro.

Esse comportamento esta bem atendido por meio de:

- filtros por escopo;
- servicos de validacao de condominio permitido;
- ownership por recurso;
- restricoes por perfil;
- testes de autorizacao.

Esse e um ponto forte do projeto porque da credibilidade a proposta multi-condominio.

### 5.5 Regras de negocio importantes ja estao implementadas

O backend nao esta apenas com CRUD simples. Ele possui regras reais de negocio, por exemplo:

- `ADMIN` nao pode ter condominio ou unidade associados;
- `SINDICO` precisa de condominio e nao pode ter unidade;
- `MORADOR` precisa de condominio e unidade;
- acesso de morador sincroniza o morador da unidade;
- cobranca nao pode ser criada diretamente com status `PAGA`;
- pagamento marca a cobranca como `PAGA`;
- `transactionId` de pagamento precisa ser unico;
- contrato nao pode ter vigencia sobreposta para o mesmo fornecedor no mesmo condominio;
- chamado tem fluxo controlado de status;
- unidade nao pode ser excluida se possuir cobrancas ou chamados;
- condominio nao pode ser excluido se ainda possuir blocos, contratos, despesas ou contas bancarias.

Isso mostra que o sistema ja possui comportamento de dominio consistente e nao apenas persistencia de dados.

### 5.6 Ha boa cobertura automatizada para o nucleo do backend

Os testes de integracao cobrem areas importantes:

- seguranca e autorizacao;
- identidade por `externalId`;
- usuarios e acessos;
- estrutura condominial;
- cobrancas e pagamentos;
- contratos;
- filtros de listagem;
- regras de chamados e despesas.

Para um projeto academico, isso e um diferencial importante, porque demonstra preocupacao com confiabilidade e nao apenas com implementacao visual.

## 6. O que precisa melhorar

Embora a base esteja boa, ainda ha pontos que enfraquecem a coerencia global do projeto.

### 6.1 O escopo oficial ainda precisa ser formalizado

Hoje existe uma tensao entre:

- o que o ERS promete;
- o que o backend realmente implementa;
- o que a documentacao mais recente tenta reinterpretar como escopo atual.

Na pratica, o projeto esta bom como backend central, mas essa decisao ainda precisa ficar totalmente oficializada para evitar a impressao de que parte da proposta ficou faltando sem justificativa.

#### Impacto

Sem essa formalizacao, a banca ou qualquer avaliador pode interpretar que:

- o projeto prometeu mais do que entregou;
- alguns modulos foram apenas citados, mas nao planejados tecnicamente;
- o backlog nao esta claramente priorizado.

#### Recomendacao

Atualizar o ERS para classificar explicitamente cada modulo como:

- implementado;
- parcial;
- planejado.

### 6.2 A documentacao ainda nao esta 100% sincronizada

Os documentos principais ja melhoraram bastante, mas ainda existem sinais de desalinhamento.

O caso mais visivel esta no `README.md`, que cita varios arquivos complementares que nao existem na pasta `docs` atual.

#### Impacto

Isso passa uma sensacao de:

- documentacao incompleta;
- material desatualizado;
- dificuldade de rastrear a versao oficial dos artefatos.

#### Recomendacao

Revisar o `README` para manter apenas referencias reais e atuais.

### 6.3 O checklist documental nao acompanha o que ja foi feito

O checklist registra muitas pendencias que, na pratica, ja foram parcialmente resolvidas pela documentacao atual.

#### Impacto

Isso gera um ruido de governanca:

- parece que nada foi concluido;
- dificulta saber o que ainda e realmente pendencia;
- enfraquece o acompanhamento do projeto.

#### Recomendacao

Atualizar o checklist marcando:

- o que ja foi resolvido;
- o que foi resolvido parcialmente;
- o que continua pendente.

### 6.4 Falta amadurecer a narrativa de produto completo

O backend esta bom, mas o produto completo descrito no ERS ainda nao existe no repositorio.

Isso nao e um problema em si, desde que fique claro que:

- o repositorio atual representa uma fase da solucao;
- os demais modulos sao evolucoes planejadas.

Sem isso, o projeto pode parecer tecnicamente bom, mas conceitualmente "aberto".

### 6.5 A protecao de acesso depende bastante da camada HTTP

A seguranca por ownership esta bem feita nos controllers, com `@PreAuthorize` e uso de `ResourceOwnershipService`.

Isso funciona corretamente para a API exposta, mas a regra de negocio afirma que as garantias principais devem viver no `service`.

#### Avaliacao

Nao e um erro grave.

Mas arquiteturalmente vale explicitar melhor que:

- a camada `controller` protege a entrada HTTP;
- a camada `service` protege as invariantes de negocio;
- ambas participam da seguranca final.

#### Recomendacao

Documentar melhor essa divisao no guia didatico e, quando fizer sentido, reforcar validacoes criticas tambem no `service`.

### 6.6 Existem pontos tecnicos de maturidade que ainda podem evoluir

Durante a execucao dos testes apareceram sinais que nao quebram o projeto, mas indicam margem para refinamento:

- aviso de `open-in-view`;
- endpoints do SpringDoc habilitados por padrao;
- aviso do Mockito sobre self-attach no JDK atual.

Esses pontos nao comprometem a entrega academica, mas indicam que a camada operacional e de polimento ainda pode evoluir.

## 7. O que ainda precisa ser implementado

Esta secao trata dos pontos que realmente faltam para aproximar o projeto da proposta ampla descrita no ERS.

### 7.1 Autenticacao e experiencia federada completas

Ainda faltam itens como:

- recuperacao de credenciais via Identity Provider;
- logout federado;
- fluxo mais completo de autenticacao fim a fim na experiencia do produto.

### 7.2 Integracoes financeiras externas

O backend possui cobranca e pagamento, mas ainda nao implementa:

- emissao automatica de boleto;
- Pix integrado;
- integracao com gateway de pagamento;
- webhook de confirmacao de pagamento;
- idempotencia de eventos de pagamento mais completa.

### 7.3 Comunicacao e notificacoes

Ainda nao existe modulo funcional para:

- notificacao de vencimento;
- notificacao de pagamento confirmado;
- envio por email;
- canais automatizados de comunicacao.

### 7.4 WhatsApp e autoatendimento

O ERS amplia bastante a experiencia via WhatsApp, mas no backend atual nao ha implementacao para:

- consultar debitos por WhatsApp;
- pedir segunda via por WhatsApp;
- abrir chamado por WhatsApp;
- acompanhar chamado por WhatsApp;
- reservar area comum por WhatsApp.

### 7.5 Areas comuns e reservas

Nao foram encontradas entidades, endpoints ou testes relacionados a:

- cadastro de areas comuns;
- regras de disponibilidade;
- reservas;
- historico de uso;
- conflitos de agenda.

### 7.6 Modulos analiticos e gerenciais

Tambem nao ha implementacao consolidada para:

- saldo atual;
- projecao de fluxo de caixa;
- analise de inadimplencia;
- simulacao de impacto financeiro;
- desvio orcamentario;
- dashboards;
- graficos financeiros.

### 7.7 Prestacao de contas automatica

Ainda faltam:

- consolidacao automatica de relatorios;
- anexos e documentos comprobatorios;
- exportacao em PDF;
- visao mensal consolidada para o sindico.

### 7.8 Manutencao ampliada e fornecedores

O projeto ja possui fornecedor e contrato, mas ainda faltam recursos como:

- garantias;
- servicos recorrentes;
- historico de fornecedor mais rico;
- comparacao de orcamentos;
- alertas de vencimento contratual.

### 7.9 Auditoria e governanca operacional

Um projeto com esse tipo de dominio se beneficia muito de um modulo claro de auditoria.

Hoje ainda nao existe algo consolidado para:

- registrar quem alterou o que;
- consultar historico de acoes sensiveis;
- rastrear mudancas em acessos, cobrancas, pagamentos e contratos.

Esse ponto faria muita diferenca para deixar o projeto mais robusto e profissional.

## 8. Analise por eixo funcional

Para facilitar o entendimento, abaixo esta uma avaliacao por blocos.

### 8.1 Eixo estrutural do condominio

#### Situacao

**Bem atendido**

#### O que ja existe

- enderecos;
- condominios;
- blocos;
- unidades;
- regras de integridade entre essas entidades;
- validacoes de exclusao e duplicidade;
- filtros e escopo.

#### Avaliacao

Este e um dos eixos mais maduros do sistema.

### 8.2 Eixo de usuarios, perfis e acessos

#### Situacao

**Bem atendido**

#### O que ja existe

- cadastro de usuarios;
- vinculo por acesso;
- perfis oficiais definidos;
- sincronizacao entre morador e unidade;
- regras de duplicidade;
- identidade baseada em `externalId`.

#### Avaliacao

Esse bloco esta forte tanto em regra de negocio quanto em seguranca.

### 8.3 Eixo operacional de chamados

#### Situacao

**Bem atendido no backend principal**

#### O que ja existe

- abertura de chamado;
- listagem;
- consulta;
- transicao de status;
- escopo por morador e por condominio.

#### O que falta

- canais alternativos como WhatsApp;
- trilha operacional mais rica;
- possivel auditoria de mudancas.

### 8.4 Eixo financeiro basico

#### Situacao

**Bem atendido no nucleo**

#### O que ja existe

- cobrancas;
- pagamentos;
- despesas;
- contas bancarias;
- contratos;
- fornecedores;
- regras de consistencia importantes.

#### O que falta

- automacao financeira;
- integracao bancaria;
- visoes analiticas;
- notificacoes;
- prestacao de contas automatica.

### 8.5 Eixo de experiencia do produto

#### Situacao

**Ainda incompleto**

#### O que falta

- frontend consistente no repositorio;
- WhatsApp;
- UX de autoatendimento;
- relatorios consumiveis;
- modulos de comunicacao.

#### Avaliacao

Este e o eixo mais distante da proposta ampla do ERS.

## 9. Riscos de incoerencia se nada for ajustado

Se o projeto seguir como esta, sem ajuste documental e sem priorizacao formal das lacunas, os principais riscos sao:

### 9.1 Risco de parecer que prometeu mais do que entregou

O backend esta bom, mas o ERS descreve uma plataforma mais abrangente.

Sem classificacao por fase, isso pode ser interpretado negativamente.

### 9.2 Risco de documentacao contraditoria

Se alguns documentos falam de produto futuro e outros falam apenas do backend atual, sem uma marcacao clara, a leitura do projeto fica confusa.

### 9.3 Risco de avaliacao superficial do valor real do backend

Como o backend esta tecnicamente consistente, seria injusto que ele fosse avaliado como "incompleto" apenas por falta de enquadramento documental.

Por isso, formalizar o escopo e tao importante quanto implementar novas features.

## 10. Recomendacoes prioritarias

Para deixar o projeto mais fechado e coerente com a proposta, a recomendacao e seguir uma ordem de prioridade.

### 10.1 Prioridade 1: fechar o escopo oficial

Definir formalmente:

- o que e entrega atual;
- o que e entrega parcial;
- o que e evolucao futura.

Esse ajuste deve aparecer no ERS, no README e na matriz.

### 10.2 Prioridade 2: atualizar o ERS para refletir fases

Cada RF e RNF deveria indicar claramente:

- implementado;
- parcial;
- planejado.

Isso resolve a maior parte do problema de coerencia do projeto.

### 10.3 Prioridade 3: limpar e sincronizar a documentacao

Fazer uma revisao fina em:

- `README.md`;
- checklist documental;
- OpenAPI;
- referencias a arquivos inexistentes;
- nomenclatura de perfis;
- narrativa de autenticacao e escopo.

### 10.4 Prioridade 4: priorizar o proximo bloco funcional

Depois de fechar a narrativa do escopo, a equipe deve escolher um proximo pacote de evolucao.

As melhores opcoes, pensando em valor e coerencia, seriam:

1. auditoria e historico de acoes;
2. notificacoes e webhook de pagamento;
3. areas comuns e reservas;
4. relatorios e prestacao de contas automatica.

### 10.5 Prioridade 5: fortalecer a camada de produto

Se a proposta final do trabalho exigir aderencia mais ampla ao ERS, o projeto precisa evoluir alem do backend principal e incluir ao menos um eixo visivel de experiencia, como:

- relatorio automatico;
- notificacao;
- integracao externa;
- ou frontend consumidor da API.

## 11. Conclusao final

O projeto **Kondo** esta **bem estruturado e coerente como backend central de gestao condominial**.

Ele atende de forma convincente:

- a modelagem principal do dominio;
- a seguranca com autenticacao externa;
- o controle de acesso por perfis e escopo;
- o nucleo operacional e financeiro;
- uma boa base de regras de negocio;
- uma cobertura automatizada relevante.

Por outro lado, ele **ainda nao atende integralmente a proposta mais ampla do ERS**.

As maiores lacunas nao estao no que ja foi implementado, mas no que ainda falta para transformar o backend atual em toda a plataforma descrita documentalmente.

### Sintese final

- **O que esta sendo atendido:** nucleo backend, seguranca, estrutura condominial, acessos, chamados e financeiro principal.
- **O que precisa melhorar:** alinhamento formal do escopo, sincronizacao da documentacao, rastreabilidade mais precisa e amadurecimento da narrativa do produto.
- **O que ainda falta implementar:** integracoes externas, WhatsApp, notificacoes, areas comuns, dashboards, auditoria, automacoes e modulos avancados previstos no ERS.

### Veredito

Se a proposta oficial for entendida como **backend principal da plataforma**, o projeto esta **forte, coerente e defensavel**.

Se a proposta oficial for entendida como **toda a plataforma descrita no ERS**, o projeto esta **parcial** e ainda precisa de implementacoes complementares para ser considerado fechado.

## 12. Proposta de fechamento do projeto

Para que o projeto fique "bem fechado e coerente com a proposta", a recomendacao final e:

1. oficializar o escopo por fases;
2. atualizar o ERS com status de implementacao;
3. corrigir as referencias documentais desatualizadas;
4. escolher um proximo modulo estrategico para consolidar a proposta ampla;
5. manter a matriz de rastreabilidade sincronizada sempre que houver nova entrega.

Com isso, o projeto passa a ter:

- coerencia entre proposta e codigo;
- narrativa tecnica mais forte;
- documentacao mais defensavel;
- e um plano claro de evolucao.
