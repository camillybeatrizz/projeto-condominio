# Interface Development Plan - KONDO

Este plano detalha a construção da interface do sistema Kondo, integrando a visão de UX definida para o produto com a estrutura técnica do back-end.

## Design Tokens (Foundations)

Os tokens visuais originalmente extraidos do Figma ja foram incorporados ao frontend real do projeto. A fonte atual de verdade para cores, raios e estilos globais e:

- `frontend/tailwind.config.js`
- `frontend/src/index.css`

A pasta `docs/figma-export` era apenas um artefato historico/exportado do Figma Make e nao e necessaria para build, execucao ou manutencao do frontend atual.

### Paleta de Cores (Kondo Palette)
- **Primary (Purple):** `#7C3AED` (600), `#4C1D95` (900) - Identidade e Confiança.
- **Success (Green):** `#10B981` (500), `#065F46` (800) - Financeiro e Pagamentos.
- **Danger (Red):** `#EF4444` (500), `#B91C1C` (700) - Atrasos e Erros.
- **Warning (Orange):** `#F97316` (500) - Pendências.
- **Info (Teal):** `#14B8A6` (500) - Destaques.
- **Neutral (Gray):** `#F9FAFB` (50/Bg), `#111827` (900/Text).

### Tipografia e Raio
- **Fonte:** Inter (Sans-serif).
- **Border Radius:** `0.75rem` (12px) para cards e botões.
- **Shadows:** `shadow-sm` (Padrão), `shadow-md` (Hover).

---

## Fase 1: Atoms (Componentes Base)
*Implementação direta baseada em `src/app/components/ui/`.*

### Task 1.1: Buttons & Inputs
- **Button:** Variants (`primary`, `secondary`, `ghost`, `danger`, `success`). Implementar estados hover/active com transições de 200ms.
- **Input:** Suporte a `leftIcon`, `rightIcon` (password toggle) e estados de erro.
- **Checkbox & Toggle:** Estilo minimalista usando as cores da marca.

---

## Fase 2: Molecules & Shared Organisms
*Foco: Componentes de composição e estrutura de layout.*

### Task 2.1: Especialized Cards
- **FinancialCard:** Exibe label, valor (2xl font), ícone com background suave e tendência (TrendingUp/Down).
- **StatusCard:** Para chamados. Badge de status (`green`, `orange`, `red`, `purple`, `teal`) e metadados na base.
- **UnitCard:** Focado em estrutura. Exibe número da unidade, bloco, proprietário e status de ocupação.

### Task 2.2: DataTable & Navigation
- **DataTable:** Tabela com `zebraStripe`, ordenação por coluna, paginação e coluna de ações.
- **Sidebar:** Gradiente `from-kondo-purple-700 to-kondo-purple-800`. Menu com ícones Lucide e badges de notificação.
- **Header:** Breadcrumbs dinâmicos, barra de busca central e seletor de condomínio ativo.

### Task 2.3: Context Selector (Gatekeeper)
- **Implementação:** Tela pós-login apresentando cards grandes para escolha de Perfil (Admin, Síndico, Morador). Uso de ícones específicos e badges de unidade/bloco para moradores.

---

## Fase 3: Core Modules (Páginas de Gestão)
*Foco: Integração com controllers do back-end.*

### Task 3.1: Dashboard (Síndico/Admin)
- **Layout:** Grid de `FinancialCards` no topo, seguido por lista de 'Atividades Recentes' (DataTable) e Status de Chamados.
- **API:** `GET /cobrancas/resumo` e `GET /cobrancas/dashboard`.

### Task 3.2: Gestão de Estrutura
- **Telas:** Listagem de Unidades e Blocos usando `UnitCard` e `DataTable`.
- **API:** `CondominioController` e `UnidadeController`.

### Task 3.3: Portal do Morador
- **Layout:** Dashboard simplificado com foco na 'Próxima Fatura' (FinancialCard com botão 'Pagar') e 'Meus Chamados'.

---

## Fase 4: Financeiro e Operacional
*Interfaces complexas e fluxos de dados.*

### Task 4.1: Cobranças e Pagamentos
- **Funcionalidade:** Listagem de boletos, geração de QR Code Pix e polling de status.
- **Referência Técnica:** `docs/frontend-financeiro-integracao.md`.

### Task 4.2: Central de Chamados
- **Layout:** Timeline de eventos do chamado e formulário de interação.

---

## Instruções para Execução

1. **Framework:** React + TypeScript + Tailwind CSS.
2. **Icons:** Lucide React.
3. **Atomic Design:** Seguir rigorosamente a hierarquia de componentes.
4. **API Strategy:** Usar `Axios` com interceptors para o Bearer Token (Task 1.2 do `frontend-plan.md`).
