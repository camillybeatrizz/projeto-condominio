# Arquitetura de Frontend - KONDO

Este documento descreve a estrutura técnica, organização de pastas e padrões de implementação para o frontend do sistema KONDO, baseando-se nas definições da `frontend-spec.md` e na realidade do backend (Spring Boot + OIDC).

## 1. Stack Tecnológica Recomendada
*   **Core:** React (TypeScript) + Vite.
*   **Autenticação:** `oidc-client-ts` ou `react-oidc-context` (para suporte agnóstico a Keycloak e Pinniped).
*   **Estado e Cache:** TanStack Query (React Query) para sincronização de dados e gerenciamento de cache.
*   **Roteamento:** React Router Dom (v6+).
*   **Estilização:** Tailwind CSS + Shadcn/ui.
*   **Formulários:** React Hook Form + Zod (validação de schemas).
*   **Cliente HTTP:** Axios (com interceptors).

---

## 2. Estrutura de Pastas (Feature-Based)

A organização segue o princípio de separação por domínios de negócio, facilitando o isolamento de regras e componentes.

```text
src/
├── api/                # Cliente Axios, interceptors e queries/mutations base
├── assets/             # Estilos globais, imagens e SVGs
├── components/         # Componentes transversais (Design System)
│   ├── ui/             # Componentes primitivos (Button, Input, Modal)
│   └── shared/         # Componentes de negócio reutilizáveis (CobrancaStatusBadge)
├── config/             # Configuração OIDC, Variáveis de Ambiente
├── features/           # Módulos de negócio (Mapeados conforme controladores do Backend)
│   ├── auth/           # Fluxo de login OIDC e seleção de contexto
│   ├── dashboard/      # Cards financeiros e visão geral
│   ├── financeiro/     # Cobranças, Pagamento Pix, Histórico, Inadimplência
│   ├── estrutura/      # Condomínios, Blocos e Unidades
│   ├── chamados/       # Gestão de chamados e suporte
│   └── auditoria/      # Visualização de logs de eventos (Admin/Síndico)
├── hooks/              # Custom hooks globais (useAuth, useTenant)
├── providers/          # Context Providers (QueryClient, AuthProvider, Theme)
├── routes/             # Definição de rotas e Guardas de Acesso (RBAC)
├── types/              # Espelhamento de DTOs e Enums do Backend
└── utils/              # Formatadores (Currency, Date-fns, CPF/CNPJ)
```

---

## 3. Padrões de Integração e Segurança

### 3.1 Autenticação Multi-Provedor (OIDC)
O frontend não deve conter lógica específica para Keycloak. Deve-se configurar o `OidcClient` para ler do `discovery document` (`.well-known/openid-configuration`) do provedor configurado no `.env`, garantindo compatibilidade com Keycloak, Pinniped ou outros provedores.

### 3.2 Interceptadores e Erros
*   **401 (Unauthorized):** Disparar o fluxo de `signoutRedirect` ou refresh de token.
*   **403 (Forbidden):** Notificar o usuário via Toast sobre falta de permissão e redirecionar para o dashboard.
*   **400/422 (Business Rules):** Mapear o campo `message` do DTO de erro do backend diretamente para o componente de Toast ou erro de formulário.

### 3.3 Sincronização de Tipos (Contracts)
Para garantir integridade, os tipos TypeScript devem seguir rigorosamente os enums do backend:
*   `PerfilEnum`: `ADMIN`, `SINDICO`, `MORADOR`.
*   `StatusCobrancaEnum`: `PENDENTE`, `PAGA`, `VENCIDA`, `CANCELADA`.

---

## 4. Estratégias de UX para o Financeiro

### 4.1 Polling de Pagamento
Como o backend é notificado via Webhook pelo Asaas, o frontend deve implementar uma estratégia de **polling otimista** na tela de detalhe do Pix:
1.  Usuário abre a tela de Pix.
2.  Frontend inicia um polling (ex: a cada 5 ou 10 segundos) no endpoint `GET /cobrancas/{id}`.
3.  Quando o status mudar para `PAGA`, interromper o polling e exibir tela de sucesso.

### 4.2 Seleção de Contexto (Tenant/Scope)
Após o login, se o endpoint `/meu-contexto` retornar mais de um item na lista de `acessos`, o sistema deve obrigatoriamente forçar o usuário a escolher um contexto (Condomínio/Perfil) antes de carregar o `dashboard`.

---

## 5. Casos de Borda Técnicos
*   **Token Expirado em Background:** Se o usuário deixar a aba aberta, o polling de pagamento ou outras queries devem falhar graciosamente, forçando a reautenticação se o refresh token também falhar.
*   **Formatos de Identificação:** Utilizar UUIDs (string em TS) para todas as referências de ID, conforme o padrão de banco de dados (PostgreSQL) do backend.
*   **Soft Delete:** O frontend nunca deve enviar flags de "exclusão física". Ao chamar `DELETE`, a interface deve assumir que o registro sumirá da próxima listagem via cache invalidation do React Query.

---

## 6. Ferramentas de Desenvolvimento e QA
*   **Testes:** Vitest + React Testing Library (foco em fluxos de RBAC e formulários).
*   **Linting:** ESLint com regras de acessibilidade (A11y).
*   **Documentação:** Storybook para componentes do Design System.
