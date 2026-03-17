# Guia Didatico do Projeto Kondo

Este documento explica o projeto de forma simples, pensando em quem esta vendo Spring Boot pela primeira vez. Todos os exemplos citados foram tirados do codigo real do projeto.

## 1. Visao geral do projeto

### Objetivo do sistema

O projeto **Kondo** e uma API REST para **gestao de condominio**. Pelas classes e endpoints existentes, o sistema permite cadastrar e consultar informacoes como:

- enderecos
- condominios
- blocos
- unidades
- usuarios
- acessos
- chamados
- cobrancas
- pagamentos
- despesas
- fornecedores
- contratos
- contas bancarias

Exemplos reais do dominio:

- A classe `Condominio` possui `nome`, `cnpj`, `telefone` e um `Endereco` relacionado.
- A classe `Bloco` pertence a um `Condominio`.
- A classe `Unidade` pertence a um `Bloco`.
- A classe `Cobranca` pertence a uma `Unidade`.
- A classe `Pagamento` pertence a uma `Cobranca`.

Ou seja, o sistema modela a estrutura de um condominio e tambem partes da gestao financeira e operacional.

### Arquitetura utilizada

O projeto usa uma arquitetura em camadas, muito comum em Spring Boot:

- **controller**: recebe as requisicoes HTTP.
- **service**: concentra a regra de negocio e a orquestracao.
- **repository**: conversa com o banco via Spring Data JPA.
- **domain/entity**: representa as tabelas e relacionamentos do banco.
- **dto**: define os dados de entrada e de saida da API.
- **mapper**: converte DTO em entidade e entidade em DTO.
- **exception**: trata erros de forma padronizada.

Na pratica, o fluxo segue este padrao:

`Requisicao HTTP -> Controller -> Service -> Repository -> Banco -> Service -> Controller -> Resposta JSON`

Esse padrao deixa o projeto organizado, porque cada camada tem uma responsabilidade bem definida.

## 2. Explicacao por package

### controller

#### Responsabilidade

O package `controller` expõe os endpoints da API. Ele e a "porta de entrada" do sistema.

Exemplo real em `src/main/java/com/codewithus/kondo/controller/CondominioController.java`:

```java
@RestController
@RequestMapping("/condominios")
@RequiredArgsConstructor
public class CondominioController {

    private final CondominioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CondominioResponseDTO criar(@Valid @RequestBody CondominioRequestDTO dto) {
        return service.salvar(dto);
    }
}
```

Aqui o controller:

- recebe a chamada HTTP
- pega os dados enviados no corpo da requisicao
- valida esses dados com `@Valid`
- chama o service
- devolve a resposta para o cliente

#### Principais anotacoes

- `@RestController`: diz ao Spring que a classe e um controller REST e que os retornos serao enviados como JSON.
- `@RequestMapping("/condominios")`: define a rota base do controller.
- `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`: definem qual metodo HTTP sera atendido.
- `@RequestBody`: diz que o parametro vem do corpo da requisicao.
- `@PathVariable`: pega valores da URL, como o `id`.
- `@ResponseStatus`: define o status HTTP retornado.
- `@Valid`: dispara as validacoes do DTO.

#### Fluxo das requisicoes

Usando `CondominioController` como exemplo:

1. O cliente faz `POST /condominios`.
2. O metodo `criar(...)` recebe um `CondominioRequestDTO`.
3. O Spring valida o DTO por causa do `@Valid`.
4. O controller chama `service.salvar(dto)`.
5. O service processa os dados e salva no banco.
6. O controller devolve um `CondominioResponseDTO`.

O mesmo padrao aparece em outros controllers, como:

- `PagamentoController`
- `CobrancaController`
- `UsuarioController`
- `BlocoController`

Isso mostra que o projeto segue um padrao consistente de CRUD.

### service

#### Papel na regra de negocio

O package `service` e onde fica a regra de negocio. Ele nao recebe HTTP diretamente e nao acessa o banco "na mao". Em vez disso, ele:

- valida a existencia de dados relacionados
- decide o que deve ser salvo
- usa mapper para montar entidades
- usa repository para persistir
- lanca excecoes quando algo nao existe

Exemplo real em `src/main/java/com/codewithus/kondo/service/impl/CondominioServiceImpl.java`:

```java
public CondominioResponseDTO salvar(CondominioRequestDTO dto) {
    Endereco endereco = buscarEndereco(dto.enderecoId());
    Condominio entity = mapper.toEntity(dto, endereco);
    return mapper.toResponseDTO(condominioRepository.save(entity));
}
```

Esse metodo mostra bem a regra:

1. primeiro busca o `Endereco` pelo `enderecoId`
2. depois monta a entidade `Condominio`
3. salva no repository
4. converte a entidade salva em DTO de resposta

#### Como se conecta ao controller

O controller injeta a interface do service:

```java
private final CondominioService service;
```

Depois chama metodos como:

- `service.salvar(dto)`
- `service.listar()`
- `service.buscarPorId(id)`
- `service.atualizar(id, dto)`
- `service.deletar(id)`

Ou seja: o controller delega o trabalho para o service.

#### Explicacao dos metodos principais

Quase todos os services possuem estes metodos:

- `salvar(...)`: cria um novo registro.
- `buscarPorId(...)`: busca um registro especifico.
- `listar()`: retorna todos os registros.
- `atualizar(...)`: altera um registro existente.
- `deletar(...)`: remove um registro.

Exemplo real em `CobrancaServiceImpl`:

```java
private Unidade buscarUnidade(UUID id) {
    return unidadeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
}
```

Esse trecho mostra uma logica muito importante no projeto: antes de salvar uma cobranca, o sistema confirma se a `Unidade` realmente existe. Se nao existir, ele lanca uma excecao.

Outro exemplo em `PagamentoServiceImpl`:

```java
private Cobranca buscarCobranca(UUID id) {
    return cobrancaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
}
```

Aqui o pagamento depende de uma cobranca existente. Isso mostra a ligacao entre as entidades do sistema.

### domain/entity

#### Estrutura das classes

O package `domain/entity` representa as tabelas do banco. Cada classe e uma entidade JPA.

Exemplo real em `src/main/java/com/codewithus/kondo/domain/entity/Condominio.java`:

```java
@Entity
@Table(name = "condominio")
@Getter @Setter
public class Condominio {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;
    private String cnpj;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
```

Essa classe mostra:

- o identificador `id`
- campos simples como `nome` e `telefone`
- um relacionamento com `Endereco`

Outros exemplos importantes:

- `Bloco` possui relacao com `Condominio`
- `Unidade` possui relacao com `Bloco`
- `Cobranca` possui relacao com `Unidade`
- `Pagamento` possui relacao com `Cobranca`
- `Despesa` possui relacao com `Condominio`
- `Acesso` possui relacao com `Usuario`

Isso forma uma cadeia bem clara do negocio.

#### Uso de anotacoes JPA

As anotacoes JPA dizem ao Spring e ao Hibernate como mapear as classes para o banco.

Exemplos reais:

- `@Entity`: marca a classe como entidade persistente.
- `@Table(name = "condominio")`: define o nome da tabela.
- `@Id`: marca o campo identificador.
- `@GeneratedValue`: gera o ID automaticamente.
- `@ManyToOne`: define relacionamento muitos-para-um.
- `@JoinColumn(name = "endereco_id")`: define a coluna de chave estrangeira.
- `@Enumerated(EnumType.STRING)`: grava enums como texto.
- `@Column(unique = true)`: define restricao de unicidade.

Exemplo em `Pagamento.java`:

```java
@Column(unique = true)
private String transactionId;
```

Isso indica que o `transactionId` nao pode se repetir.

Exemplo em `Cobranca.java`:

```java
@Enumerated(EnumType.STRING)
private StatusCobrancaEnum status;
```

Aqui o status da cobranca e salvo como texto no banco, por exemplo algo como `PENDENTE` ou `PAGO`, dependendo dos valores do enum.

### repository

#### Funcao

O package `repository` faz o acesso ao banco de dados. Em vez de escrever SQL manual para operacoes basicas, o projeto usa `JpaRepository`.

Exemplo real em `src/main/java/com/codewithus/kondo/repository/CondominioRepository.java`:

```java
public interface CondominioRepository extends JpaRepository<Condominio, UUID> {
}
```

Mesmo sem escrever nenhum metodo, esse repository ja ganha varios recursos prontos, como:

- `save(...)`
- `findById(...)`
- `findAll()`
- `delete(...)`

#### Relacao com JPA

O Spring Data JPA usa as entidades do package `domain/entity` para saber:

- qual tabela consultar
- quais colunas usar
- quais relacionamentos existem

Quando o service chama:

```java
condominioRepository.findById(id)
```

o Spring monta a consulta usando o mapeamento da entidade `Condominio`.

## 3. Anotacoes utilizadas no projeto

Nesta secao, cada anotacao e explicada com funcao, local de uso e exemplo real.

### `@SpringBootApplication`

- Para que serve: marca a classe principal da aplicacao e ativa a configuracao automatica do Spring Boot.
- Onde e usada: `KondoApplication`.
- Exemplo do projeto: `src/main/java/com/codewithus/kondo/KondoApplication.java`.

### `@RestController`

- Para que serve: transforma a classe em controller REST e retorna JSON automaticamente.
- Onde e usada: todos os controllers.
- Exemplo do projeto: `CondominioController`, `PagamentoController`, `UsuarioController`.

### `@RequestMapping`

- Para que serve: define a rota base de um controller.
- Onde e usada: classes controller.
- Exemplo do projeto: `@RequestMapping("/condominios")` em `CondominioController`.

### `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`

- Para que serve: mapeiam os metodos HTTP.
- Onde e usada: metodos dos controllers.
- Exemplo do projeto: `@PostMapping` no metodo `criar(...)` de `CondominioController`.

### `@RequestBody`

- Para que serve: informa que o dado vem no corpo da requisicao.
- Onde e usada: metodos `criar(...)` e `atualizar(...)`.
- Exemplo do projeto: `public CondominioResponseDTO criar(@Valid @RequestBody CondominioRequestDTO dto)`.

### `@PathVariable`

- Para que serve: captura valores da URL.
- Onde e usada: metodos de busca, atualizacao e exclusao.
- Exemplo do projeto: `buscar(@PathVariable UUID id)` em `CobrancaController`.

### `@ResponseStatus`

- Para que serve: define o status HTTP de resposta.
- Onde e usada: principalmente em `POST` e `DELETE`.
- Exemplo do projeto: `HttpStatus.CREATED` em `criar(...)` e `HttpStatus.NO_CONTENT` em `deletar(...)`.

### `@Valid`

- Para que serve: ativa as validacoes declaradas no DTO.
- Onde e usada: parametros dos controllers.
- Exemplo do projeto: `@Valid @RequestBody CondominioRequestDTO dto`.

### `@NotBlank`

- Para que serve: exige texto nao vazio.
- Onde e usada: DTOs de entrada.
- Exemplo do projeto: `String nome` em `CondominioRequestDTO`.

### `@NotNull`

- Para que serve: exige valor obrigatorio.
- Onde e usada: DTOs de entrada.
- Exemplo do projeto: `UUID enderecoId` em `CondominioRequestDTO`.

### `@Size`

- Para que serve: limita o tamanho de um texto.
- Onde e usada: DTOs de entrada.
- Exemplo do projeto: `@Size(max = 120)` em `CondominioRequestDTO`.

### `@Positive`

- Para que serve: exige numero positivo.
- Onde e usada: DTOs financeiros.
- Exemplo do projeto: `BigDecimal valor` em `CobrancaRequestDTO`.

### `@Email`

- Para que serve: valida formato de email.
- Onde e usada: `UsuarioRequestDTO`.
- Exemplo do projeto: campo de email do usuario.

### `@Service`

- Para que serve: marca a classe como componente de servico.
- Onde e usada: classes `*ServiceImpl`.
- Exemplo do projeto: `CondominioServiceImpl`.

### `@Component`

- Para que serve: registra um componente no Spring.
- Onde e usada: classes mapper.
- Exemplo do projeto: `CondominioMapper`, `CobrancaMapper`, `PagamentoMapper`.

### `@RequiredArgsConstructor`

- Para que serve: o Lombok gera um construtor com os atributos `final`.
- Onde e usada: controllers e services.
- Exemplo do projeto: `private final CondominioService service;` em `CondominioController`.

### `@Entity`

- Para que serve: marca uma classe como tabela do banco.
- Onde e usada: classes do package `domain/entity`.
- Exemplo do projeto: `Condominio`, `Cobranca`, `Pagamento`.

### `@Table`

- Para que serve: define o nome da tabela.
- Onde e usada: entidades.
- Exemplo do projeto: `@Table(name = "pagamento")` em `Pagamento`.

### `@Id`

- Para que serve: identifica a chave primaria.
- Onde e usada: entidades.
- Exemplo do projeto: `private UUID id;` em praticamente todas as entidades.

### `@GeneratedValue`

- Para que serve: pede geracao automatica do identificador.
- Onde e usada: entidades.
- Exemplo do projeto: `Condominio`, `Usuario`, `Pagamento`.

### `@ManyToOne`

- Para que serve: cria relacionamento muitos-para-um.
- Onde e usada: entidades relacionadas.
- Exemplo do projeto: muitos `Bloco` podem apontar para um `Condominio`.

### `@JoinColumn`

- Para que serve: define a coluna da chave estrangeira.
- Onde e usada: relacionamentos.
- Exemplo do projeto: `@JoinColumn(name = "condominio_id")` em `Bloco`.

### `@Enumerated(EnumType.STRING)`

- Para que serve: persiste enums como texto.
- Onde e usada: entidades com enums.
- Exemplo do projeto: `status` em `Cobranca`, `forma` em `Pagamento`, `perfil` em `Acesso`.

### `@Column(unique = true)`

- Para que serve: cria uma restricao de unicidade.
- Onde e usada: alguns campos especificos.
- Exemplo do projeto: `email` em `Usuario` e `transactionId` em `Pagamento`.

### `@CreationTimestamp`

- Para que serve: preenche automaticamente a data de criacao.
- Onde e usada: `Endereco` e `Condominio`.
- Exemplo do projeto: `private LocalDateTime createdAt;`.

### `@UpdateTimestamp`

- Para que serve: atualiza automaticamente a data da ultima alteracao.
- Onde e usada: `Condominio`.
- Exemplo do projeto: `private LocalDateTime updateAt;`.

### `@RestControllerAdvice`

- Para que serve: tratamento global de excecoes da API.
- Onde e usada: `ApiExceptionHandler`.
- Exemplo do projeto: captura `ResourceNotFoundException` e devolve `404`.

### `@ExceptionHandler`

- Para que serve: diz qual metodo trata determinada excecao.
- Onde e usada: `ApiExceptionHandler`.
- Exemplo do projeto: `handleNotFound(...)` trata `ResourceNotFoundException`.

## 4. Fluxo completo do sistema

Vamos usar uma requisicao real para explicar o fluxo: **criar um condominio**.

### Passo 1. O cliente envia a requisicao

Exemplo:

```http
POST /condominios
Content-Type: application/json

{
  "nome": "Condominio Jardim Azul",
  "cnpj": "12.345.678/0001-99",
  "telefone": "(11) 99999-9999",
  "enderecoId": "uuid-do-endereco"
}
```

Esse JSON segue o formato de `CondominioRequestDTO`.

### Passo 2. O controller recebe e valida

No `CondominioController`, o metodo abaixo recebe a requisicao:

```java
public CondominioResponseDTO criar(@Valid @RequestBody CondominioRequestDTO dto)
```

O que acontece aqui:

- `@RequestBody` converte o JSON em objeto Java
- `@Valid` executa as validacoes do DTO
- se faltar `nome` ou `enderecoId`, por exemplo, o Spring interrompe o fluxo e gera erro

### Passo 3. O controller chama o service

O controller executa:

```java
return service.salvar(dto);
```

Ele nao sabe como salvar. Ele apenas repassa para a camada de negocio.

### Passo 4. O service busca dependencias necessarias

Em `CondominioServiceImpl`, o primeiro passo e:

```java
Endereco endereco = buscarEndereco(dto.enderecoId());
```

Isso significa: antes de criar o condominio, o sistema confere se o endereco informado existe.

### Passo 5. O repository consulta o banco

Dentro de `buscarEndereco(...)`, o service chama:

```java
enderecoRepository.findById(id)
```

Se o endereco nao existir:

```java
orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado"));
```

Entao o sistema para e devolve erro.

### Passo 6. O mapper monta a entidade

Se o endereco existir, o service chama:

```java
Condominio entity = mapper.toEntity(dto, endereco);
```

O mapper copia os dados do DTO para a entidade:

- `nome`
- `cnpj`
- `telefone`
- `endereco`

### Passo 7. O repository salva

Depois disso:

```java
condominioRepository.save(entity)
```

Nesse momento o JPA/Hibernate grava os dados no banco.

### Passo 8. O service converte para resposta

Depois de salvar, o retorno e convertido para DTO:

```java
mapper.toResponseDTO(condominioRepository.save(entity))
```

Assim a API nao devolve a entidade inteira do banco. Ela devolve um objeto pensado para resposta.

### Passo 9. O controller devolve JSON

O controller devolve o `CondominioResponseDTO`, com status `201 CREATED`.

### Passo 10. Se der erro, o tratamento global responde

Se algo falhar, o `ApiExceptionHandler` cuida da resposta.

Exemplos reais:

- se o registro nao existir: retorna `404 NOT FOUND`
- se a validacao falhar: retorna `400 BAD REQUEST`
- se houver conflito de integridade: retorna `409 CONFLICT`

## 5. Explicacao da logica empregada

### Logica principal do sistema

A logica do projeto e simples e muito comum em APIs de cadastro: o sistema organiza entidades relacionadas e garante que essas relacoes existam antes de salvar.

Exemplos reais da logica:

- para criar um `Condominio`, o `Endereco` informado precisa existir
- para criar um `Bloco`, o `Condominio` informado precisa existir
- para criar uma `Unidade`, o `Bloco` informado precisa existir
- para criar uma `Cobranca`, a `Unidade` informada precisa existir
- para criar um `Pagamento`, a `Cobranca` informada precisa existir
- para criar um `Contrato`, o `Fornecedor` e o `Condominio` precisam existir

Em outras palavras: o sistema evita salvar registros "soltos" ou incoerentes.

### O papel dos DTOs

O projeto nao recebe diretamente as entidades do banco no controller. Ele usa DTOs.

Exemplo:

- entrada: `CondominioRequestDTO`
- saida: `CondominioResponseDTO`

Isso e bom porque:

- protege a entidade
- deixa a API mais organizada
- facilita validacao
- separa entrada e saida

### O papel dos mappers

Os mappers fazem a traducao entre camadas.

Exemplo real em `CondominioMapper`:

- `toEntity(...)`: transforma o DTO em entidade
- `updateEntity(...)`: atualiza uma entidade existente
- `toResponseDTO(...)`: transforma a entidade em resposta

Isso evita colocar muita logica de conversao dentro do controller ou do service.

### O papel das excecoes

O projeto centraliza erros com `ApiExceptionHandler`.

Isso deixa a API mais profissional porque, em vez de quebrar com erro generico, ela responde com padrao.

Exemplo:

- se o usuario informar um `enderecoId` inexistente, o service lanca `ResourceNotFoundException`
- essa excecao e tratada globalmente
- a API devolve uma resposta estruturada com data, status, mensagem e caminho

### Resumindo a ideia do projeto

Se eu explicasse o projeto em linguagem bem simples, seria assim:

> O sistema recebe uma requisicao, valida os dados, verifica se os registros relacionados existem, monta a entidade correta, salva no banco e devolve uma resposta em JSON.

Esse e o coracao do projeto.

## Observacoes finais importantes

- O projeto usa `Spring Web`, `Spring Data JPA`, `Validation`, `Security`, `Flyway` e `PostgreSQL` no `pom.xml`.
- Porem, pelo codigo atual, ainda nao aparecem classes implementadas nos packages `config`, `security`, `integration` e `shared`.
- Isso indica que a estrutura do projeto ja foi preparada para crescer, mas parte dessas areas ainda parece estar vazia ou em construcao.
- O arquivo `application.properties` hoje tem apenas `spring.application.name=Kondo`, entao as configuracoes de banco ainda nao aparecem nesse repositorio.

## Sugestao de leitura para iniciantes

Se voce quiser entender o projeto em ordem facil, vale ler nesta sequencia:

1. `KondoApplication`
2. um controller, por exemplo `CondominioController`
3. a interface `CondominioService`
4. a implementacao `CondominioServiceImpl`
5. a entidade `Condominio`
6. o `CondominioRepository`
7. o `CondominioMapper`
8. os DTOs `CondominioRequestDTO` e `CondominioResponseDTO`
9. o `ApiExceptionHandler`

Essa sequencia ajuda muito porque mostra o fluxo completo de ponta a ponta.
