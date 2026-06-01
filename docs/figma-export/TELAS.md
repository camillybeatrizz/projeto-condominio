# KONDO - Telas da Aplicação

Documentação completa das 4 principais telas criadas para o sistema KONDO de gestão de condomínios.

---

## 🎯 1. Dashboard Principal (Gestão)

**Arquivo:** `/src/app/pages/DashboardGestao.tsx`  
**Perfil de Acesso:** Administrador, Síndico

### Descrição
Dashboard executivo com visão geral financeira e operacional do condomínio.

### Componentes Principais

#### KPIs Financeiros (4 Cards)
- **Receita Mensal**: R$ 55.600 (+8.2% ↑)
- **Despesas**: R$ 47.000 (-2.1% ↓)
- **Inadimplência**: R$ 7.400 (4 pendentes)
- **Saldo Atual**: R$ 23.450 (+12.5% ↑)

Usa o componente `FinancialCard` com ícones coloridos e indicadores de tendência.

#### Gráfico de Evolução Financeira
- Chart de área (Area Chart) usando Recharts
- Comparação Receitas vs Despesas
- Últimos 6 meses (Out - Mar)
- Gradientes verdes e laranjas
- Grid responsivo: 2 colunas no desktop

#### Resumo Rápido (Sidebar Card)
- Total de unidades: 64
- Ocupadas: 63
- Taxa de ocupação: 98.4%
- Chamados abertos por prioridade (Alta/Média/Baixa)

#### Atividades Recentes
- Lista de 6 atividades com timestamp
- Ícones e cores por tipo:
  - 💰 Pagamentos (verde)
  - 🔔 Chamados (laranja)
  - 👥 Usuários (roxo)
  - 📅 Manutenções (teal)
- Hover states elegantes

### Features
✅ Gráficos interativos com tooltips  
✅ Cards KPI com tendências (+/-)  
✅ Timeline de atividades em tempo real  
✅ Layout responsivo em grid  

---

## 🏠 2. Portal do Morador

**Arquivo:** `/src/app/pages/PortalMorador.tsx`  
**Perfil de Acesso:** Morador

### Descrição
Dashboard simplificado e acolhedor focado nas necessidades do morador individual.

### Componentes Principais

#### Welcome Header
- Banner roxo com gradiente
- Saudação personalizada: "Bem-vindo de volta, João!"
- Identificação da unidade: "Residencial Sunset - Bloco A, Apt 301"
- Data atual

#### Card de Próxima Fatura (Destaque)
- **Valor Total**: R$ 1.850,00 (display grande e bold)
- **Vencimento**: 10/06/2026 (com ícone de calendário)
- **Contador**: "Em 9 dias"
- **Detalhamento**:
  - Taxa de Condomínio: R$ 1.500,00
  - Fundo de Reserva: R$ 200,00
  - Água Individualizada: R$ 150,00
- **Ações Primárias**:
  - Botão verde: "Pagar via PIX" (com ícone QR Code)
  - Botão secundário: "Baixar Boleto" (com ícone Download)
- Background degradê verde/teal no header

#### Meus Últimos Chamados
- Lista de 3 chamados do morador
- Status coloridos:
  - 🟠 Em andamento (laranja)
  - 🟣 Aberto (roxo)
  - 🟢 Resolvido (verde)
- Prioridade e data de abertura
- Botão "Novo Chamado" no header
- Cards clicáveis com hover effect

#### Avisos do Prédio (Sidebar)
- 3 avisos importantes
- Tipos:
  - 🔴 Important (laranja) - Assembleia
  - ℹ️ Info (teal) - Manutenções e políticas
- Layout em lista vertical
- Ícones contextualizados

#### Ações Rápidas (Sidebar)
- Reservar Área Comum
- Solicitar Autorização
- Atualizar Cadastro

### Features
✅ Interface amigável e não-técnica  
✅ Foco em pagamentos e comunicação  
✅ Visual acolhedor com cores suaves  
✅ Ações rápidas acessíveis  

---

## 🏢 3. Gestão de Condomínio

**Arquivo:** `/src/app/pages/GestaoCondominio.tsx`  
**Perfil de Acesso:** Administrador, Síndico

### Descrição
Tela de listagem e gestão de todas as unidades e blocos do condomínio.

### Componentes Principais

#### Stats Cards (4 Cards)
- Total de Unidades: 12
- Ocupadas: 9 (teal)
- Vagas: 2 (cinza)
- Alugadas: 1 (roxo)

#### Filtros Rápidos (Painel de Filtros)
- **Busca**: Campo de busca por unidade ou morador
- **Bloco**: Dropdown (Todos, A, B)
- **Status**: Dropdown (Todos, Ocupadas, Vagas, Alugadas)
- Contador de resultados filtrados
- Botão "Limpar filtros"

#### Grid de Unidades
- Layout em grid responsivo (4 colunas no desktop)
- Usa o componente `UnitCard`:
  - Número da unidade (grande e bold)
  - Bloco
  - Tipo (Apartamento)
  - Status badge colorido
  - Proprietário
  - Telefone/Email
  - Botões: Visualizar | Editar
- Filtros aplicados em tempo real
- Estado vazio com ilustração

#### Header Actions
- Botão primário: "+ Adicionar Unidade"
- Alternador de visualização: Grade | Lista

### Features
✅ Filtros múltiplos simultâneos  
✅ Busca em tempo real  
✅ Grid responsivo (1-4 colunas)  
✅ Cards interativos com hover  
✅ Contador de resultados  

---

## 👥 4. Gestão de Usuários

**Arquivo:** `/src/app/pages/GestaoUsuarios.tsx`  
**Perfil de Acesso:** Administrador, Síndico

### Descrição
Tela de gerenciamento de moradores, funcionários e administradores.

### Componentes Principais

#### Stats Cards (4 Cards)
- Total de Usuários: 10
- Ativos: 9 (verde)
- Moradores: 6 (teal)
- Administradores: 1 (laranja)

#### Filtros
- **Busca**: Por nome ou email
- **Perfil**: Dropdown (Todos, Admin, Síndico, Morador, Funcionário)
- **Status**: Dropdown (Todos, Ativos, Inativos)

#### Tabela de Usuários (DataTable)
Colunas:
1. **Nome** (com avatar e unidade se morador)
   - Avatar circular com inicial
   - Gradiente roxo
   - Subtítulo: Bloco e Apt (para moradores)

2. **Email** (com ícone de envelope)

3. **Telefone** (com ícone de telefone)

4. **Perfil** (Badge com ícone)
   - 🛡️ Administrador (roxo)
   - 🏢 Síndico (laranja)
   - 👤 Morador (teal)
   - 👤 Funcionário (cinza)

5. **Status** (Badge)
   - Ativo (verde)
   - Inativo (cinza)

6. **Data de Cadastro** (ordenável)

#### Ações por Linha
- 👁️ Visualizar
- ✏️ Editar
- 🗑️ Deletar (com confirmação)

#### Paginação
- 8 usuários por página
- Navegação completa
- Contador: "Mostrando X de Y usuários"

### Features
✅ Tabela completa com ordenação  
✅ Filtros combinados  
✅ Badges visuais por perfil  
✅ Avatar com iniciais automáticas  
✅ Paginação funcional  
✅ Ações inline na tabela  

---

## 🎨 Design System Aplicado

Todas as telas utilizam consistentemente:

### Cores
- **Primary (Roxo)**: `#7C3AED` - Navegação, CTAs
- **Success (Verde)**: `#10B981` - Pagamentos, confirmações
- **Warning (Laranja)**: `#F97316` - Pendências, alertas
- **Danger (Vermelho)**: `#EF4444` - Erros, exclusões
- **Info (Teal)**: `#14B8A6` - Informações, moradores

### Componentes Reutilizados
- `FinancialCard` - KPIs financeiros
- `UnitCard` - Cards de unidades
- `DataTable` - Tabelas com paginação
- `Button` - Todos os CTAs
- `Input` - Campos de busca e formulários
- `Select` - Dropdowns de filtros

### Layout
- `MainLayout` - Shell com Sidebar + Header
- Breadcrumbs navegáveis
- Seletor de condomínio no header
- Badge de perfil do usuário

### Tipografia
- **Headings**: Inter Bold (2xl, 3xl)
- **Body**: Inter Regular (sm, base)
- **Captions**: Inter Medium (xs)

### Espaçamento
- Padding padrão: `p-8`
- Gap entre elementos: `gap-6`, `gap-8`
- Grid responsivo: `grid-cols-1 md:grid-cols-2 lg:grid-cols-4`

### Interatividade
- Hover states em cards e botões
- Focus rings roxos
- Transitions suaves (200ms)
- Active scale em botões

---

## 🔄 Navegação Entre Telas

### Context Selector
Ponto de entrada após login, permite escolher:
1. **Admin Global** → Dashboard de Gestão
2. **Edifício Aurora (Síndico)** → Dashboard de Gestão
3. **Residencial Sunset (Morador)** → Portal do Morador

### Menu Lateral (Gestão)
- Dashboard → Dashboard de Gestão
- Estrutura → Gestão de Condomínio
- Usuários → Gestão de Usuários
- Cobranças (placeholder)
- Chamados (placeholder com badges)

### Seletor de Condomínio (Header)
Permite trocar entre contextos:
- Edifício Aurora (Gestão)
- Residencial Sunset (Morador)
- Vista Verde (Morador)

### Breadcrumbs
Navegação hierárquica clicável:
- Dashboard
- Dashboard > Gestão de Unidades
- Dashboard > Gestão de Usuários

---

## 📱 Responsividade

Todas as telas são totalmente responsivas:

### Breakpoints
- **Mobile** (< 768px): 1 coluna
- **Tablet** (768px - 1024px): 2 colunas
- **Desktop** (> 1024px): 3-4 colunas

### Adaptações
- Cards empilham verticalmente no mobile
- Tabelas com scroll horizontal
- Sidebar fixa no desktop
- Filtros colapsam no mobile
- Grid adapta automaticamente

---

## 🚀 Como Navegar

1. Inicie na **Context Selector**
2. Escolha um perfil:
   - **Síndico/Admin** → Dashboard de Gestão
   - **Morador** → Portal do Morador
3. Use o **menu lateral** para acessar outras telas
4. Use o **seletor de condomínio** para trocar de contexto
5. Use **breadcrumbs** para voltar

---

## 📊 Dados Mock

Todas as telas utilizam dados mockados realistas:
- 12 unidades (Blocos A e B)
- 10 usuários (Admin, Síndicos, Moradores, Funcionários)
- 7 pagamentos
- 6 atividades recentes
- 3 chamados
- 3 avisos
- Gráfico com 6 meses de dados

---

## ✅ Status de Implementação

- ✅ Dashboard Principal (Gestão)
- ✅ Portal do Morador
- ✅ Gestão de Condomínio
- ✅ Gestão de Usuários
- ✅ Context Selector
- ✅ Navegação completa entre telas
- ✅ Design System aplicado
- ✅ Responsividade completa
