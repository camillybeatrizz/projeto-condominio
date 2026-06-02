# 🎨 Prompt Maestro para Figma AI - Projeto KONDO

Este documento contém uma sequência de prompts estruturados para serem utilizados no **Figma AI / Figma Make**. Siga a ordem sugerida para garantir a consistência do **Atomic Design**.

---

## 🟢 Etapa 1: Fundação e Identidade Visual (Design Tokens)
**Objetivo:** Gerar o `docs/assets/01-design-system.png` e a base do sistema.

> **Prompt:** 
> "Crie um Design System moderno e profissional para uma plataforma SaaS de gestão de condomínios chamada 'KONDO'. 
> A identidade deve transmitir confiança, transparência e eficiência. 
> Gere uma folha de estilos contendo:
> 1. Paleta de Cores: Azul Profundo (Trust/Primary), Verde Esmeralda (Success/Financial), Coral (Urgency/Danger), tons de cinza neutro para superfícies.
> 2. Tipografia: Escala tipográfica completa usando uma fonte Sans-serif moderna (ex: Inter ou Roboto), clara e legível.
> 3. Elevações (Shadows): Soft shadows para cards.
> 4. Border Radius: Bordas levemente arredondadas (8px a 12px) para um ar moderno.
> 5. Ícones: Set de ícones lineares e consistentes para navegação (Dashboard, Prédio, Dinheiro, Chave, Usuários, Suporte).
> Apresente tudo de forma organizada em um layout de 'Style Guide'."

---

## 🟢 Etapa 2: Átomos de Interface (UI Components)
**Objetivo:** Gerar o `docs/assets/02-atoms-ui.png`.

> **Prompt:**
> "Com base no Design System da KONDO, gere uma folha de componentes básicos (Atoms) contendo:
> 1. Botões: Versões Primary, Secondary, Ghost e Danger. Inclua estados: Default, Hover, Focus e Disabled.
> 2. Inputs: Campo de texto, senha e busca, com placeholders e ícones. Inclua estados de erro e foco.
> 3. Select e Dropdown: Estilo moderno e limpo.
> 4. Checkbox e Toggle: Estilo minimalista.
> Mantenha a consistência visual com as cores e o arredondamento definidos anteriormente."

---

## 🟡 Etapa 3: Moléculas e Estrutura (Composição)
**Objetivo:** Gerar `03-cards-variants.png`, `04-table-design.png` e `05-main-layout.png`.

> **Prompt (Peça em sequência ou um por um):**
> "1. Crie variantes de Cards para o sistema KONDO: um card de resumo financeiro (valor e label), um card de status de chamado (badge colorida e descrição) e um card de informação de unidade (número, bloco e proprietário).
> 2. Desenhe uma Data Table robusta: Colunas bem espaçadas, header com opção de ordenação, linhas com zebra-striping suave, e uma coluna de ações com ícones. Inclua uma paginação no rodapé.
> 3. Projete o Main Layout (Shell): Uma barra lateral (Sidebar) escura e elegante com o logo da KONDO e menu de navegação, e um Header branco com breadcrumbs, barra de busca e perfil do usuário (avatar e nome). O Header deve conter uma badge clara indicando o perfil ativo (ex: 'Síndico') e um seletor de condomínio.
> 4. Gere a Tela de Seleção de Contexto (05b-context-selector.png): Uma tela de 'Boas-vindas' limpa após o login, apresentando cards grandes para o usuário escolher qual de seus condomínios/perfis deseja acessar no momento."

---

## 🔵 Etapa 4: Dashboard e Módulos Core (Páginas)
**Objetivo:** Gerar `06-dashboard.png`, `06b-portal-morador.png`, `07-condominio-unidades.png` e `08-usuarios-acessos.png`.

> **Prompt:**
> "Utilizando os componentes criados, gere as seguintes telas:
> 1. Dashboard Principal (Gestão): Destaque KPIs financeiros (Inadimplência, Receita, Despesas), um gráfico simples de evolução e uma lista de 'Atividades Recentes'.
> 2. Portal do Morador (06b-portal-morador.png): Um dashboard simplificado e acolhedor. Em destaque: Card de 'Próxima Fatura' com valor, data de vencimento e botão 'Pagar via Pix'. Abaixo, uma lista de 'Meus Últimos Chamados' e um card de 'Avisos do Prédio'.
> 3. Gestão de Condomínio: Uma tela de listagem de unidades e blocos em grid, com filtros rápidos no topo e um botão 'Adicionar Novo'.
> 4. Gestão de Usuários: Uma lista de moradores e funcionários com badges indicando o perfil (Admin, Síndico, Morador) e status (Ativo/Inativo)."

---

## 🔴 Etapa 5: Financeiro e Operacional (Complexo)
**Objetivo:** Gerar `09-financeiro-cobrancas.png`, `10-financeiro-despesas.png`, `11-chamados.png` e `12-areas-comuns.png`.

> **Prompt:**
> "Projete as interfaces para os módulos complexos da KONDO:
> 1. Financeiro - Cobranças: Lista de boletos com status coloridos. Destaque uma área para 'Gerar QR Code Pix' com um exemplo visual de QR Code.
> 2. Financeiro - Despesas: Um formulário limpo para lançamento de despesas, incluindo um campo de upload de arquivo (comprovante).
> 3. Operacional - Chamados: Uma timeline vertical mostrando o histórico de um chamado (abertura, comentário, mudança de status).
> 4. Áreas Comuns: Um calendário ou grid visual mostrando a disponibilidade de espaços como 'Salão de Festas' e 'Academia'."

---

## 🟣 Etapa 6: Feedback e Responsividade
**Objetivo:** Gerar `13-feedback-states.png` e `14-mobile-view.png`.

> **Prompt:**
> "Para finalizar o projeto KONDO:
> 1. Crie uma folha de 'Feedback States': Telas de erro 404 personalizadas, 'Empty States' com ilustrações amigáveis para quando não há dados, e 'Toast Notifications' para mensagens de sucesso e erro.
> 2. Mobile View: Mostre como o Dashboard e a lista de cobranças se adaptam para uma tela de smartphone (iPhone 15), transformando a sidebar em um menu hambúrguer e a tabela em cards empilhados."
