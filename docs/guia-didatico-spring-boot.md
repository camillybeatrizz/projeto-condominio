# Guia Didatico do Backend Kondo

Este documento explica como o backend do Kondo funciona hoje no codigo. A ideia e ajudar quem esta aprendendo Spring Boot a entender nao apenas o que foi implementado, mas por que a aplicacao foi organizada dessa forma.

## 1. Visao geral

O Kondo e uma API REST para gestao condominial. No MVP atual, o backend cobre principalmente:

- autenticacao federada via OIDC e validacao de JWT
- gestao estrutural de condominios, blocos, unidades e areas comuns
- cadastro de usuarios e acessos
- ciclo financeiro de cobrancas e pagamentos
- integracao com o gateway Asaas
- processamento seguro de webhook
- auditoria basica e trilha de eventos

Esse backend nao implementa login local com senha. A autenticacao vem de um provedor externo, como Keycloak ou Pinniped, e o backend atua como Resource Server.

## 2. Arquitetura em camadas

O projeto segue uma arquitetura em camadas porque isso deixa o codigo mais simples de manter, testar e evoluir.

### 2.1 Organizacao dos pacotes

- `controller`: recebe requisicoes HTTP, valida entrada e devolve JSON
- `service`: define contratos de negocio
- `service/impl`: implementa as regras de negocio e a orquestracao
- `repository`: acesso a dados com Spring Data JPA
- `repository/specification`: filtros dinamicos para listagens e escopo
- `domain/entity`: entidades JPA e relacionamentos do banco
- `domain/enums`: enumeracoes do dominio, como perfis e status
- `dto`: objetos de entrada e saida da API
- `mapper`: conversao entre DTOs e entidades
- `security`: autenticacao, autorizacao, ownership e resolucao do usuario atual
- `config`: configuracoes de beans, seguranca, OpenAPI e Flyway
- `exception`: excecoes de negocio e tratamento global de erro

### 2.2 Responsabilidade de cada camada

O fluxo padrao do projeto e:

`HTTP -> Controller -> Service -> Repository -> Banco -> Service -> Controller -> JSON`

Exemplo real com cobranca:

1. `CobrancaController` recebe `POST /cobrancas`
2. o DTO entra validado com `@Valid`
3. `CobrancaServiceImpl` aplica regras de negocio
4. `UnidadeRepository` e `CobrancaRepository` consultam e persistem dados
5. `AsaasCobrancaGateway` integra com o Asaas quando necessario
6. o service devolve um `CobrancaResponseDTO`
7. o controller retorna `201 Created`

Essa separacao existe por um motivo importante:

- controller nao deve carregar regra de negocio
- service nao deve conhecer detalhes de HTTP
- repository nao deve decidir regra funcional

## 3. Como o Spring Boot estrutura a aplicacao

## 3.1 Ponto de entrada

A classe `KondoApplication` usa `@SpringBootApplication`. Essa anotacao junta tres ideias importantes:

- `@Configuration`: a classe pode registrar configuracoes
- `@EnableAutoConfiguration`: o Spring Boot monta muita coisa automaticamente
- `@ComponentScan`: o Spring procura componentes no pacote `com.codewithus.kondo` e nos subpacotes

Na pratica, quando a aplicacao sobe, o Spring:

1. le o `pom.xml` e as dependencias
2. cria o contexto de aplicacao
3. encontra classes anotadas com `@Component`, `@Service`, `@Repository`, `@Controller`, `@Configuration`
4. cria os beans necessarios
5. injeta as dependencias automaticamente
6. sobe o servidor web e publica os endpoints REST

## 3.2 IoC e injecao de dependencia

### O que e IoC

Inversao de Controle significa que os objetos nao sao criados manualmente com `new` pela maior parte da aplicacao. Quem controla isso e o Spring.

Em vez de um controller criar seu proprio service, ele declara a dependencia:

```java
private final CondominioService service;
```

Como a classe usa `@RequiredArgsConstructor`, o Spring injeta esse bean automaticamente. Isso reduz acoplamento e facilita testes.

### Por que isso foi usado no Kondo

Esse projeto usa bastante injecao de dependencia porque ela ajuda a:

- trocar implementacoes sem alterar controllers
- centralizar configuracoes
- isolar responsabilidades
- testar fluxos com mais previsibilidade

Exemplo: `CobrancaServiceImpl` depende de repository, mapper, seguranca e gateway externo. O service nao precisa saber como construir cada colaborador; o Spring entrega tudo pronto.

## 3.3 Configuracao de beans

O Spring cria beans de duas formas no projeto:

### Beans criados por anotacao estereotipo

- `@Service`: como `CondominioServiceImpl`, `CobrancaServiceImpl`, `AsaasWebhookServiceImpl`
- `@Component`: como `JwtRoleConverter`, `CurrentUserResolver`, `AsaasWebhookSignatureVerifier`
- `@Repository`: os repositories do Spring Data sao registrados automaticamente a partir das interfaces

### Beans criados manualmente em `@Configuration`

O projeto faz isso quando precisa controlar melhor a instancia criada.

Exemplos reais:

- `SecurityConfig.securedFilterChain(...)`: cria o `SecurityFilterChain`
- `SecurityConfig.jwtDecoder(...)`: cria o `JwtDecoder` com validacao de issuer e audience
- `OpenApiConfig.kondoOpenAPI()`: cria o bean da documentacao Swagger/OpenAPI
- `FlywayConfig.flyway(...)`: cria o bean do Flyway e executa as migrations

Isso foi feito dessa forma porque esses objetos nao sao so classes de dominio. Eles representam infraestrutura da aplicacao.

## 3.4 `@ConfigurationProperties`

O projeto usa classes como:

- `KondoSecurityProperties`
- `AsaasIntegrationProperties`
- `AsaasWebhookProperties`

Essas classes leem valores do `application.properties` e dos profiles. Isso foi escolhido para evitar strings soltas pelo codigo e deixar a configuracao centralizada.

Exemplo de configuracoes reais:

- `kondo.security.enabled`
- `kondo.security.audiences`
- `kondo.integrations.asaas.enabled`
- `kondo.integrations.asaas.webhook.hmac-secret`

## 3.5 Ciclo de requisicao no Spring Boot

No Kondo, o fluxo mais comum e:

1. o cliente envia uma requisicao HTTP
2. o Spring Security filtra a chamada antes de chegar no controller
3. se o acesso for permitido, o controller recebe a requisicao
4. o controller converte JSON em DTO
5. o service aplica regras de negocio
6. o repository usa JPA para ler ou gravar no banco
7. o service transforma a resposta em DTO
8. o controller devolve JSON
9. se algo falhar, `ApiExceptionHandler` padroniza o erro

## 3.6 Anotacoes principais usadas no projeto

### Web e API

- `@RestController`: expõe endpoints REST
- `@RequestMapping`: define rota base
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: ligam metodos HTTP
- `@RequestBody`: le o JSON do corpo
- `@PathVariable`: le parametros da URL
- `@RequestParam`: le filtros da query string
- `@ResponseStatus`: define o status HTTP
- `@Valid`: dispara validacao do DTO

### Regra de negocio e infraestrutura

- `@Service`: marca a camada de negocio
- `@Component`: registra componentes auxiliares
- `@Configuration`: registra classes de configuracao
- `@Bean`: cria beans manualmente
- `@ConfigurationProperties`: faz bind de propriedades

### Persistencia

- `@Entity`: marca entidades JPA
- `@Table`: define a tabela
- `@Id`, `@GeneratedValue`: identificador
- `@ManyToOne`, `@JoinColumn`: relacionamentos
- `@Enumerated(EnumType.STRING)`: persiste enums como texto

### Seguranca

- `@EnableMethodSecurity`: ativa autorizacao em nivel de metodo
- `@PreAuthorize`: aplica RBAC e regras de ownership

### Transacao

- `@Transactional`: garante consistencia nas operacoes de negocio

Isso e importante no financeiro. Em `PagamentoServiceImpl` e `AsaasWebhookServiceImpl`, pagamento e atualizacao da cobranca acontecem dentro de transacao para evitar estado incoerente.

## 4. Persistencia e modelagem do dominio

As entidades ficam em `domain/entity`. Elas representam a estrutura do banco e os relacionamentos principais do negocio.

Exemplos centrais:

- `Condominio` referencia `Endereco`
- `Bloco` pertence a `Condominio`
- `Unidade` pertence a `Bloco` e pode ter `morador`
- `Cobranca` pertence a `Unidade`
- `Pagamento` pertence a `Cobranca`
- `Acesso` vincula `Usuario`, `Perfil`, `Condominio` e, quando faz sentido, `Unidade`

### BaseEntity

As entidades principais herdam de `BaseEntity`, que concentra:

- `createdAt`
- `updatedAt`

Isso foi feito para padronizar auditoria temporal sem repetir codigo.

### Soft delete

Entidades como `Condominio`, `Cobranca` e `Pagamento` usam:

- `deletedAt`
- `deletedBy`

Em vez de apagar fisicamente, o sistema faz exclusao logica. Isso preserva historico, o que e especialmente importante em dados estruturais e financeiros.

### Repositories e Specifications

Os repositories estendem `JpaRepository`, entao o Spring gera muita implementacao automaticamente.

Exemplos:

- `findByIdAndDeletedAtIsNull(...)`
- `findAllByDeletedAtIsNull(...)`
- `findByExternalId(...)`

Quando a busca precisa ser dinamica, o projeto usa `Specification`, como em:

- `CobrancaSpecifications`
- `PagamentoSpecifications`
- `ChamadoSpecifications`

Isso foi escolhido porque os filtros variam conforme perfil, condominio, status e periodo.

## 5. DTOs, mappers e tratamento de erro

### DTOs

O projeto usa DTOs para separar:

- o formato da API
- o modelo de persistencia

Isso evita expor entidades diretamente e deixa a API mais estavel.

### Mappers

Os mappers, como `CondominioMapper` e `CobrancaMapper`, convertem DTO para entidade e entidade para DTO.

Essa escolha foi feita para:

- evitar controller inchado
- evitar logica de montagem espalhada
- manter a camada de service focada em regra de negocio

### Tratamento global de erro

`ApiExceptionHandler` usa `@RestControllerAdvice` para transformar excecoes em respostas padronizadas.

Exemplos:

- `ResourceNotFoundException` -> `404`
- `BusinessException` -> `400`
- `ConflictException` -> `409`
- `AccessDeniedException` -> `403`

Isso melhora a experiencia do frontend e deixa o comportamento previsivel.

## 6. Seguranca da aplicacao

## 6.1 Modelo adotado

O backend usa Spring Security com `spring-boot-starter-oauth2-resource-server`.

Isso significa que:

- o login acontece fora da API
- o cliente envia um token JWT Bearer
- o backend valida assinatura e claims do token
- o backend decide autorizacao com base em roles e escopo

Hoje, o projeto esta preparado para OIDC com profiles como:

- `application-keycloak.properties`
- `application-pinniped.properties`

Entao o sistema e compativel com Keycloak, mas nao depende de uma implementacao exclusiva dele.

## 6.2 `SecurityConfig`

`SecurityConfig` define duas cadeias de seguranca.

### Quando a seguranca esta habilitada

Com `kondo.security.enabled=true`, o bean `securedFilterChain`:

- desabilita CSRF, o que faz sentido em API stateless com JWT
- libera apenas endpoints publicos, como `/actuator/health`, Swagger e `/webhooks/asaas`
- exige autenticacao para o resto
- configura o Resource Server para usar JWT
- usa `JwtRoleConverter` para transformar claims em authorities do Spring

### Quando a seguranca esta desabilitada

Com `kondo.security.enabled=false`, o bean `openFilterChain` entra em acao.

Existem dois comportamentos:

- se `allowInsecureOpenAccess=false`, a aplicacao falha fechada e bloqueia tudo, exceto infraestrutura minima
- se `allowInsecureOpenAccess=true`, a API fica aberta para estudo e desenvolvimento local

Isso foi uma decisao importante do projeto: fora do profile local, o padrao e seguro por default.

## 6.3 Validacao de JWT

O `JwtDecoder` criado em `SecurityConfig` valida:

- `issuer`: quem emitiu o token
- `audience`: para qual aplicacao o token foi emitido

Se o token nao tiver a audience esperada, ele e rejeitado.

Isso alinha o backend com os requisitos RF01, RF02, RNF02 e RNF03.

## 6.4 Conversao de roles

`JwtRoleConverter` extrai roles de varias partes do token:

- `realm_access.roles`
- `resource_access.{clientId}.roles`
- `roles`
- `groups`

Depois converte para authorities do Spring, como:

- `ROLE_ADMIN`
- `ROLE_SINDICO`
- `ROLE_MORADOR`

Esse desenho foi adotado para suportar diferentes formatos de token entre provedores OIDC e ambientes.

## 6.5 Identidade do usuario local

O backend trabalha com duas ideias diferentes:

- identidade autenticada no token
- registro local do usuario na base

O elo principal entre as duas e `usuario.externalId`, normalmente ligado ao `sub` do token.

O fluxo e implementado por `CurrentUserResolver`:

1. pega o `subject` e o `email` do token
2. tenta localizar `Usuario` por `externalId`
3. se nao achar e `strictExternalIdentity=false`, pode fazer fallback por email
4. se o usuario local ainda nao tiver `externalId`, `UsuarioIdentityLinkService` vincula esse `subject`

Esse comportamento existe para permitir migracao segura de usuarios ja cadastrados no banco antes da identidade federada estar totalmente consolidada.

As migrations `V3__drop_usuario_senha.sql` e `V4__add_usuario_external_id.sql` mostram bem essa mudanca de estrategia:

- senha local foi removida
- `external_id` passou a existir como identificador federado unico

## 6.6 RBAC e escopo por condominio

O controle de acesso nao depende so de role. O projeto combina duas camadas:

### RBAC por perfil

Nos controllers, `@PreAuthorize` define quem pode chamar cada endpoint.

Exemplos:

- `POST /condominios`: `ADMIN` e `SINDICO`
- `GET /meu-contexto`: `ADMIN`, `SINDICO`, `MORADOR`
- `GET /pagamentos/{id}`: `ADMIN`, `SINDICO` ou dono do recurso

### Escopo por condominio e ownership

Mesmo quando a role permite o endpoint, o service ainda pode restringir o dado acessado.

Componentes centrais:

- `CondominioScopeService`
- `ResourceOwnershipService`

Na pratica:

- `ADMIN` tem acesso global
- `SINDICO` acessa apenas os condominios aos quais esta vinculado
- `MORADOR` ve apenas o que pertence a sua unidade ou ao seu contexto

Isso atende RF04, RF05, RNF08 e RNF17.

## 6.7 Seguranca do webhook

O endpoint `/webhooks/asaas` fica liberado no filtro HTTP porque o Asaas nao envia JWT do usuario. Mesmo assim, ele nao esta "sem seguranca".

`AsaasWebhookController` protege o endpoint de duas formas:

- modo legado por `asaas-access-token`
- modo recomendado por assinatura HMAC e timestamp

`AsaasWebhookSignatureVerifier` valida:

- existencia do segredo configurado
- timestamp dentro da janela permitida
- assinatura HMAC SHA-256 do conteudo

Isso existe para impedir payloads forjados e ataques de replay.

## 7. Fluxos principais do sistema

## 7.1 Fluxo de autenticacao do usuario

Importante: o backend nao possui endpoint `POST /login`.

Fluxo real:

1. o usuario autentica no provedor OIDC
2. o cliente recebe um JWT
3. o cliente chama a API com `Authorization: Bearer <token>`
4. o Spring Security valida issuer, audience e assinatura
5. `JwtRoleConverter` transforma claims em roles do Spring
6. `CurrentUserResolver` tenta associar o token a um `Usuario` local
7. o endpoint segue apenas se autenticacao e autorizacao estiverem corretas

### Exemplo ponta a ponta: `GET /meu-contexto`

1. o request chega com Bearer token
2. `SecurityFilterChain` autentica a requisicao
3. `MeuContextoController.buscar()` chama `MeuContextoService`
4. `MeuContextoServiceImpl` usa `CurrentUserResolver.getRequiredUsuario()`
5. `AcessoRepository` busca os acessos do usuario
6. a API responde com dados do usuario e seus vinculos de acesso

Por que esse endpoint existe? Porque o frontend precisa descobrir rapidamente "quem sou eu no sistema" e "quais condominios/unidades posso acessar".

## 7.2 Cadastro e gestao de condominio

O fluxo de condominio e um bom exemplo do padrao controller -> service -> repository.

### Exemplo ponta a ponta: `POST /condominios`

1. o cliente envia um `CondominioRequestDTO`
2. `CondominioController.criar(...)` recebe o body validado
3. `@PreAuthorize` exige `ADMIN` ou `SINDICO`
4. `CondominioServiceImpl.salvar(...)` valida CNPJ duplicado
5. o service busca o `Endereco` existente
6. `CondominioMapper` monta a entidade
7. `CondominioRepository.saveAndFlush(...)` persiste o registro
8. o mapper transforma a entidade em `CondominioResponseDTO`
9. o controller devolve `201 Created`

### Regras importantes nesse modulo

- nao pode existir condominio ativo com o mesmo CNPJ
- o endereco relacionado precisa existir
- a listagem respeita o escopo do usuario autenticado
- a exclusao e logica, nao fisica
- um condominio nao pode ser removido se ainda tiver blocos, areas comuns, contas bancarias, contratos ou despesas vinculadas

### Por que fazer assim

Essa abordagem protege integridade referencial e evita apagar uma raiz estrutural que ainda sustenta dados importantes.

Relacionamento com requisitos:

- RF08: cadastro de condominio
- RNF17: isolamento multi-condominio

## 7.3 Geracao e controle de cobrancas

O modulo de cobrancas e o centro do MVP financeiro.

### Criacao manual de cobranca

Fluxo de `POST /cobrancas`:

1. o controller recebe `CobrancaRequestDTO`
2. `CobrancaServiceImpl.salvar(...)` valida:
   - competencia obrigatoria
   - nao permitir criar diretamente com status `PAGA`
3. busca a unidade
4. valida se o usuario pode operar no condominio daquela unidade
5. impede duplicidade de cobranca ativa para a mesma unidade e competencia
6. impede duplicidade de referencia externa
7. salva a cobranca
8. se necessario, chama `AsaasCobrancaGateway` para gerar referencia externa e URL de pagamento
9. consulta dados Pix e preenche QR Code e copia e cola
10. devolve a resposta ao cliente

### Geracao em lote

`POST /cobrancas/gerar-lote`:

1. recebe `condominioId`, `competencia`, `valor` e `vencimento`
2. valida escopo do condominio
3. lista unidades do condominio
4. cria cobranca para cada unidade que ainda nao possui cobranca ativa naquela competencia
5. ignora duplicadas em vez de quebrar o lote inteiro

### Consulta, resumo e inadimplencia

O projeto tambem implementa:

- `GET /cobrancas`
- `GET /cobrancas/resumo`
- `GET /cobrancas/dashboard`
- `GET /cobrancas/inadimplentes`
- `GET /cobrancas/{id}/pix`

Essas consultas usam `Specification` para montar filtros por:

- condominio
- status
- competencia
- morador atual
- escopo do sindico

### Por que o resumo e o dashboard ficam no backend

Porque o backend ja conhece as regras de escopo e o estado correto da cobranca. Se o frontend calculasse tudo sozinho, haveria mais chance de inconsistencias.

Relacionamento com requisitos:

- RF13: geracao de taxas por competencia
- RF14: visualizacao de cobrancas
- RF15: emissao de Pix/boleto no contexto do gateway, ainda parcial no MVP
- RF18: controle de inadimplencia
- RF19: historico financeiro do morador

## 7.4 Registro e controle de pagamentos

O sistema tem dois caminhos para pagamento:

- registro manual por operador
- registro automatico por webhook

### Pagamento manual

Fluxo de `POST /pagamentos`:

1. o controller exige `ADMIN` ou `SINDICO`
2. `PagamentoServiceImpl.salvar(...)` busca a cobranca
3. valida escopo do condominio
4. impede pagamento em cobranca ja paga
5. impede `transactionId` duplicado
6. salva o pagamento
7. atualiza a cobranca para `PAGA`
8. registra auditoria
9. devolve `PagamentoResponseDTO`

Por que atualizar a cobranca no mesmo service? Porque pagamento e status da cobranca precisam ficar consistentes.

## 7.5 Integracao com pagamentos e webhook

### Criacao da cobranca externa

`AsaasCobrancaGatewayImpl` abstrai a integracao com o Asaas.

Quando a integracao esta habilitada e ha API key:

1. procura ou cria o cliente no Asaas
2. cria um pagamento externo do tipo Pix
3. recebe `id` externo e `invoiceUrl`
4. consulta o QR Code Pix
5. devolve essas informacoes para a cobranca local

Quando a integracao nao esta habilitada:

- o gateway gera referencias simuladas
- isso permite desenvolvimento e testes do backend sem depender do ambiente externo

Essa escolha foi importante para manter o MVP produtivo em ambiente local e CI.

### Processamento do webhook

Fluxo ponta a ponta de `POST /webhooks/asaas`:

1. o Asaas envia o payload bruto
2. `AsaasWebhookController` valida assinatura HMAC ou token legado
3. o payload JSON e convertido para `AsaasPaymentWebhookRequestDTO`
4. `AsaasWebhookServiceImpl.processar(...)` valida os campos obrigatorios
5. verifica se o evento externo ja foi processado em `WebhookEventoProcessado`
6. ignora eventos repetidos
7. processa apenas `PAYMENT_RECEIVED`
8. localiza a cobranca pela referencia externa
9. impede duplicidade de `transactionId`
10. cria `Pagamento`
11. marca a `Cobranca` como `PAGA`
12. registra auditoria
13. registra o evento processado
14. responde `PROCESSED` ou `IGNORED`

### Por que a idempotencia foi implementada

Webhooks podem ser reenviados pelo provedor. Sem idempotencia, o sistema poderia registrar pagamento duplicado. Por isso o projeto protege o fluxo em duas frentes:

- tabela de eventos processados por `eventoExternoId`
- unicidade de `transactionId` em `Pagamento`

Relacionamento com requisitos:

- RF16: registro automatico de pagamento por webhook
- RF17: integracao com gateway
- RNF06: validacao de webhook
- RNF07 e RNF20: protecao contra duplicidade e idempotencia
- RNF21: registro de eventos financeiros

## 8. Como os componentes se conectam na pratica

## 8.1 Exemplo resumido: autenticacao e contexto

`JWT -> SecurityFilterChain -> JwtDecoder -> JwtRoleConverter -> MeuContextoController -> MeuContextoServiceImpl -> CurrentUserResolver + AcessoRepository -> JSON`

## 8.2 Exemplo resumido: cadastro de condominio

`POST /condominios -> CondominioController -> CondominioServiceImpl -> EnderecoRepository + CondominioMapper + CondominioRepository -> CondominioResponseDTO`

## 8.3 Exemplo resumido: criacao de cobranca com Pix

`POST /cobrancas -> CobrancaController -> CobrancaServiceImpl -> UnidadeRepository + CobrancaRepository -> AsaasCobrancaGateway -> CobrancaResponseDTO`

## 8.4 Exemplo resumido: webhook de pagamento

`POST /webhooks/asaas -> AsaasWebhookController -> AsaasWebhookSignatureVerifier -> AsaasWebhookServiceImpl -> CobrancaRepository + PagamentoRepository + AuditoriaService + WebhookEventoProcessadoRepository -> WebhookProcessamentoResponseDTO`

## 9. Relacao com os requisitos do projeto

Os pontos mais claramente implementados no backend atual sao:

- RF01 e RF02: autenticacao OIDC e validacao de JWT
- RF04 e RF05: autorizacao por claims e identidade externa vinculada ao usuario local
- RF07: auditoria de eventos importantes
- RF08 a RF12: estrutura do condominio
- RF13 e RF14: cobrancas por competencia e visualizacao
- RF16 e RF17: webhook e integracao com gateway
- RF18 e RF19: inadimplencia, historico e visao financeira por perfil
- RNF16, RNF17, RNF19, RNF20 e RNF27: arquitetura stateless, isolamento por condominio, consistencia transacional, idempotencia e versionamento de banco

Tambem e importante registrar o que ainda nao esta completo no backend atual:

- RF03 e RF06: recuperacao de credenciais e logout federado nao possuem endpoint proprio
- RF15: o fluxo de emissao existe via Asaas, mas ainda esta classificado como parcial no projeto
- recursos de notificacao, WhatsApp, relatorios e analise avancada continuam fora do MVP consolidado

## 10. Conclusao

O backend do Kondo foi estruturado do jeito mais comum e recomendado para um projeto Spring Boot com regra de negocio real:

- controllers finos
- services com orquestracao e validacao
- repositories para persistencia
- DTOs e mappers para separar API de banco
- Spring Security para JWT, RBAC e escopo
- transacoes para manter consistencia
- integracao externa isolada atras de gateway

Isso foi feito assim porque o sistema nao e apenas um CRUD simples. Ele precisa lidar com identidade federada, multi-condominio, dados financeiros e eventos externos. A arquitetura atual organiza essas preocupacoes de forma clara, o que torna o MVP mais seguro, mais ensinavel e mais facil de evoluir.
