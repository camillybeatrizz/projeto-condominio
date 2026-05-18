# Frontend Specification - KONDO

## 1. Visão Geral
O KONDO é um ecossistema de gestão condominial focado em transparência financeira e eficiência operacional. Esta especificação define os requisitos para o desenvolvimento da interface do usuário (Web e Mobile), garantindo integração perfeita com a API REST existente.

### 1.1 Arquitetura de Integração
- **Backend:** Spring Boot (Java 21)
- **Autenticação:** OIDC (OpenID Connect) via JWT (Bearer Token).
- **Protocolo:** RESTful com JSON.
- **Segurança:** Segregação de dados rigorosa baseada no perfil e escopo (`condominioId`, `unidadeId`).

---

## 2. Fundação Técnica Recomendada
Para garantir manutenibilidade e performance, recomenda-se:
- **Framework:** React ou Angular (TypeScript).
- **Gerenciamento de Estado:** Redux Toolkit, Pinia ou React Context API.
- **Estilização:** CSS Moderno (Sass/Modules) ou Component Library (Tailwind, Material UI, Shadcn/ui).
- **Validação:** Zod ou Yup para schemas de formulários.
- **Comunicação:** Axios ou TanStack Query (para cache e sincronização).

---

## 3. Personas e Controle de Acesso (RBAC)

O sistema deve adaptar sua interface com base nos perfis retornados pelo endpoint `/meu-contexto`:

| Perfil | Escopo de Visão | Funcionalidades Principais |
| :--- | :--- | :--- |
| **ADMIN** | Global (Todos os condomínios) | Gestão de Condomínios, Usuários, Configurações Globais. |
| **SINDICO** | Condomínios Vinculados | Dashboard Financeiro, Gestão de Blocos/Unidades, Despesas, Contratos, Chamados do Condomínio. |
| **MORADOR** | Própria Unidade | Minhas Cobranças, Pagamento Pix, Meus Chamados, Visualização de Áreas Comuns. |

---

## 4. Módulos Funcionais e Casos de Uso

### 4.1 Autenticação e Contexto
- **UC01: Login Único:** O usuário autentica-se via provedor OIDC.
- **UC02: Resolução de Contexto:** Após o login, o sistema consome `/meu-contexto` para carregar o perfil e os condomínios acessíveis.
- **Criterio de Sucesso:** Se o usuário tiver mais de um acesso, ele deve selecionar o condomínio/perfil antes de entrar no dashboard.

### 4.2 Dashboard Financeiro (Síndico/Admin)
- **UC03: Visão de Saúde Financeira:** Exibir cards com Total de Cobranças, Total Pago, Total Aberto e Total Inadimplente (`/cobrancas/resumo`).
- **UC04: Monitoramento de Movimentações:** Listar pagamentos recentes e inadimplentes recentes no dashboard (`/cobrancas/dashboard`).
- **Caso de Borda:** Se não houver dados no período, exibir *empty states* instrutivos em vez de telas em branco.

### 4.3 Gestão Financeira e Pagamentos
- **UC05: Listagem de Cobranças:** Filtros por competência (MM/YYYY) e status (PENDENTE, PAGA, VENCIDA).
- **UC06: Pagamento via Pix (Morador):** Gerar QR Code e Copia-e-Cola para cobranças pendentes (`/cobrancas/{id}/pix`).
- **UC07: Histórico de Pagamentos:** Consulta de transações confirmadas com filtro de data.
- **Criterio de Sucesso:** O status da cobrança deve mudar para "PAGA" em tempo real (ou via refresh) após a confirmação do pagamento pelo gateway.

### 4.4 Operacional e Estrutura
- **UC08: Gestão de Chamados:** Moradores abrem chamados; Síndicos alteram status (ABERTO -> ANDAMENTO -> CONCLUIDO).
- **UC09: Estrutura Condominial:** Visualização hierárquica (Condomínio -> Bloco -> Unidade).
- **UC10: Fornecedores e Contratos:** Cadastro e consulta de vigência de contratos.
- **UC11: Áreas Comuns:** Visualização das áreas disponíveis no condomínio (Nota: O MVP atual foca na listagem/cadastro, o sistema de reservas é uma evolução pós-MVP).

---

## 5. Casos de Borda e Resiliência

### 5.1 Tratamento de Erros
- **401 Unauthorized:** Redirecionar imediatamente para o Login.
- **403 Forbidden:** Exibir tela de "Acesso Negado" amigável, explicando que o perfil não tem permissão para aquele recurso.
- **Business Exceptions (400/422):** Exibir a mensagem de erro retornada pelo backend (ex: "Não é possível excluir unidade com cobranças ativas").

### 5.2 Concorrência e Estados
- **Duplicidade:** Impedir cliques duplos em botões de "Pagar" ou "Salvar".
- **Soft Delete (Exclusão Lógica):** O backend utiliza exclusão lógica. O frontend deve confiar que as listagens da API já filtram registros ativos. Caso um registro seja "excluído", a interface deve atualizar o estado local para removê-lo da visão do usuário, sem esperar uma remoção física no banco.
- **Validação de Datas:** Bloquear no frontend `dataInicio > dataFim` para evitar erros 400.
- **Sessão Expirada:** Tratar expiração de token OIDC com refresh token ou novo redirecionamento para login.

---

## 6. Critérios de Sucesso do Front-end

1. **Segurança:** O token JWT nunca deve ser exposto em logs e deve ser enviado em todas as requisições via interceptor.
2. **Performance:** Carregamento inicial (LCP) abaixo de 2.5s.
3. **Usabilidade:** Design responsivo que funcione bem em tablets e desktops (foco administrativo) e smartphones (foco morador).
4. **Fidelidade de Dados:** O dashboard financeiro deve bater exatamente com os totais das listagens filtradas.
5. **Acessibilidade:** Seguir padrões WCAG básicos para leitura e navegação.

---

## 7. Próximos Passos (Roadmap Front-end)
1. **Fase 1 (MVP):** Autenticação, Dashboard de Cobranças, Pagamento Pix, Listagem de Unidades.
2. **Fase 2:** Gestão de Chamados completa, Áreas Comuns (Reserva), Gestão de Despesas.
3. **Fase 3:** Dashboards Avançados, Relatórios PDF, Integração com WhatsApp.
