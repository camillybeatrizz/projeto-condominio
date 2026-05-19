# Plano de Desenvolvimento Frontend - KONDO (MVP)

Este plano detalha as etapas para a implementação do frontend do KONDO, seguindo a `frontend-architecture.md` e a `frontend-spec.md`. O objetivo é entregar um MVP funcional integrado ao backend Spring Boot.

## Fase 1: Setup e Infraestrutura Core
**Objetivo:** Preparar o esqueleto do projeto e as ferramentas base.

- [ ] **Task 1.1: Inicialização do Projeto**
  - Executar `vite create` com template `react-ts`.
  - Instalar dependências base: `axios`, `lucide-react`, `react-router-dom`, `@tanstack/react-query`, `zod`, `react-hook-form`, `oidc-client-ts`.
  - Configurar Tailwind CSS e inicializar Shadcn/ui.
- [ ] **Task 1.2: Cliente de API e Interceptors**
  - Implementar o `axios-client.ts` com base na URL do `.env`.
  - Criar interceptor para injetar o `Bearer Token`.
  - Criar interceptor global para tratamento de erros (401, 403, 422).
- [ ] **Task 1.3: Estrutura de Pastas e Types**
  - Criar árvore de diretórios conforme a `frontend-architecture.md`.
  - Criar `types/api.ts` espelhando os DTOs e Enums do backend (Perfil, StatusCobranca).

---

## Fase 2: Autenticação e Gestão de Contexto (OIDC)
**Objetivo:** Garantir o acesso seguro e a identificação do perfil do usuário.

- [ ] **Task 2.1: Provedor de Autenticação OIDC**
  - Configurar o `AuthProvider` genérico para suportar Keycloak/Pinniped.
  - Implementar hook `useAuth` para gerenciar estado de login.
- [ ] **Task 2.2: Integração com /meu-contexto**
  - Criar service para chamar `GET /meu-contexto`.
  - Implementar tela de **Seleção de Contexto**: se o usuário tiver > 1 acesso, deve escolher qual condomínio/perfil quer operar.
- [ ] **Task 2.3: Guarda de Rotas (RBAC)**
  - Implementar componente `ProtectedRoute` que valida permissões por Role (`ADMIN`, `SINDICO`, `MORADOR`).

---

## Fase 3: Layout Base e Navegação Dinâmica
**Objetivo:** Criar a moldura do sistema que se adapta ao perfil.

- [ ] **Task 3.1: Layout Principal (AppShell)**
  - Criar Sidebar responsiva e Navbar.
  - O menu da Sidebar deve ser filtrado dinamicamente com base no perfil ativo.
- [ ] **Task 3.2: Feedback Visual Global**
  - Implementar provedor de Toasts (Sonner ou Shadcn).
  - Criar componentes de Loading e Skeletons para transição de rotas.

---

## Fase 4: Módulo Financeiro (Core do MVP)
**Objetivo:** Implementar o fluxo de valor: Dashboard -> Cobrança -> Pagamento Pix.

- [ ] **Task 4.1: Dashboard Financeiro (Síndico/Admin)**
  - Consumir `GET /cobrancas/dashboard` e `GET /cobrancas/resumo`.
  - Implementar cards de indicadores e lista de inadimplentes recentes.
- [ ] **Task 4.2: Gestão de Cobranças**
  - Tela de listagem de cobranças com filtros de Competência e Status.
  - Implementar listagem específica para o Morador (apenas as dele).
- [ ] **Task 4.3: Tela de Pagamento Pix e Polling**
  - Criar componente de exibição de QR Code e Copia-e-Cola (`GET /cobrancas/{id}/pix`).
  - **Lógica de Polling:** Implementar verificação automática de status a cada 10s até detectar `PAGA`.

---

## Fase 5: Estrutura, Cadastro e Operacional
**Objetivo:** Permitir a gestão da hierarquia do condomínio e chamados.

- [ ] **Task 5.1: Gestão de Estrutura (Hierárquica)**
  - Listagem de Blocos e Unidades vinculadas ao condomínio selecionado.
  - Visualização de Áreas Comuns (Apenas listagem para o MVP).
- [ ] **Task 5.2: Central de Chamados**
  - Formulário de abertura de chamados para Moradores.
  - Dashboard de gestão de chamados para Síndicos (Mudança de status).
- [ ] **Task 5.3: Logs de Auditoria**
  - Tela administrativa para consulta de eventos do sistema (`GET /auditoria-eventos`).

---

## Fase 6: Validação, QA e Refinamento
**Objetivo:** Garantir a qualidade e o alinhamento com os critérios de sucesso.

- [ ] **Task 6.1: Tratamento de Erros de Negócio**
  - Validar se todas as `BusinessException` do backend são exibidas corretamente para o usuário.
- [ ] **Task 6.2: Testes de Fluxo de Ponta a Ponta**
  - Simular pagamento via Webhook (Backend) e verificar atualização no Frontend.
  - Validar isolamento de dados entre Moradores de unidades diferentes.
- [ ] **Task 6.3: Refinamento de UX/UI**
  - Ajustar responsividade para uso mobile (foco no Morador).
  - Implementar Empty States em todas as tabelas e dashboards.

---

## Critérios de Aceite para Finalização do MVP
1. Usuário consegue logar e escolher seu contexto de acesso.
2. O dashboard financeiro reflete os dados reais da API.
3. O morador consegue visualizar sua cobrança e gerar o Pix.
4. O sistema bloqueia acesso a rotas administrativas para o perfil `MORADOR`.
5. Todas as ações de `Soft Delete` funcionam sem deixar registros fantasmas na UI.
