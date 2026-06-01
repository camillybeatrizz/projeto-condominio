# KONDO Design System - Componentes

Biblioteca completa de componentes React para a plataforma KONDO de gestão de condomínios.

## 📦 Componentes Disponíveis

### 1. Cards

#### FinancialCard
Card para exibição de métricas financeiras com ícone, valor e tendência.

```tsx
<FinancialCard
  label="Receita Mensal"
  value="R$ 55.600"
  icon={TrendingUp}
  iconColor="bg-kondo-green-100 text-kondo-green-600"
  trend={{ value: '+8.2%', isPositive: true }}
  subtitle="Últimos 6 meses"
/>
```

**Props:**
- `label`: Rótulo do card
- `value`: Valor principal a ser exibido
- `icon`: Ícone Lucide React
- `iconColor`: Classes Tailwind para cor do ícone (opcional)
- `trend`: Objeto com tendência { value, isPositive } (opcional)
- `subtitle`: Texto adicional abaixo do valor (opcional)

#### StatusCard
Card para exibição de chamados/tickets com status colorido.

```tsx
<StatusCard
  id="1247"
  title="Vazamento no 5º andar"
  description="Descrição do problema..."
  status={{ label: 'Em andamento', color: 'orange' }}
  category={{ label: 'Alta', color: 'bg-kondo-red-100 text-kondo-red-700' }}
  metadata={[
    { label: 'Apto', value: '504' },
    { label: 'Aberto em', value: '30/03/2026' }
  ]}
  date="Atualizado em 31/03/2026"
/>
```

**Props:**
- `id`: ID do chamado
- `title`: Título do chamado
- `description`: Descrição detalhada
- `status`: { label, color } - Cores: green, orange, red, purple, teal, gray
- `category`: { label, color } (opcional)
- `metadata`: Array de { label, value } (opcional)
- `date`: Data de atualização (opcional)

#### UnitCard
Card para exibição de informações de unidades do condomínio.

```tsx
<UnitCard
  unit="101"
  block="A"
  owner="Pedro Almeida"
  type="Apartamento"
  status="occupied"
  phone="(83) 98999-1111"
  email="pedro@email.com"
  onView={() => {}}
  onEdit={() => {}}
/>
```

**Props:**
- `unit`: Número da unidade
- `block`: Bloco
- `owner`: Nome do proprietário
- `type`: Tipo da unidade
- `status`: 'occupied' | 'vacant' | 'rented'
- `phone`: Telefone (opcional)
- `email`: Email (opcional)
- `onView`: Callback para visualizar (opcional)
- `onEdit`: Callback para editar (opcional)

---

### 2. DataTable

Tabela de dados robusta com ordenação, paginação e ações.

```tsx
<DataTable
  columns={[
    { key: 'date', label: 'Data', sortable: true },
    { key: 'name', label: 'Nome', sortable: true },
    { 
      key: 'status', 
      label: 'Status',
      render: (row) => <Badge>{row.status}</Badge>
    }
  ]}
  data={myData}
  keyExtractor={(row) => row.id}
  onRowClick={(row) => console.log(row)}
  actions={{
    onView: (row) => {},
    onEdit: (row) => {},
    onDelete: (row) => {},
    onDownload: (row) => {}
  }}
  pagination={{
    currentPage: 1,
    totalPages: 10,
    pageSize: 20,
    totalItems: 200,
    onPageChange: (page) => {}
  }}
/>
```

**Props:**
- `columns`: Array de definições de colunas
  - `key`: Chave do dado
  - `label`: Rótulo da coluna
  - `sortable`: Habilitar ordenação (opcional)
  - `render`: Função customizada de renderização (opcional)
  - `width`: Largura da coluna (opcional)
- `data`: Array de dados
- `keyExtractor`: Função para extrair chave única
- `onRowClick`: Callback ao clicar na linha (opcional)
- `actions`: Ações disponíveis (opcional)
- `zebraStripe`: Habilitar zebra striping (padrão: true)
- `pagination`: Configuração de paginação (opcional)

**Features:**
- ✅ Ordenação por colunas (clique no header)
- ✅ Zebra striping alternado
- ✅ Coluna de ações com ícones
- ✅ Paginação completa
- ✅ Hover states

---

### 3. Main Layout (Shell)

Sistema completo de layout com Sidebar e Header.

#### MainLayout

```tsx
<MainLayout
  breadcrumbs={[
    { label: 'Dashboard', onClick: () => {} },
    { label: 'Pagamentos' }
  ]}
  userProfile={{
    name: 'João Silva',
    role: 'Síndico',
    avatar: 'J'
  }}
  activeCondominium={{
    name: 'Edifício Aurora',
    options: [
      { id: 'aurora', name: 'Edifício Aurora' },
      { id: 'sunset', name: 'Residencial Sunset' }
    ],
    onChange: (id) => {}
  }}
  onSearch={(query) => {}}
  onLogout={() => {}}
>
  {/* Conteúdo da página */}
</MainLayout>
```

**Props:**
- `children`: Conteúdo da página
- `breadcrumbs`: Array de breadcrumbs
- `userProfile`: Informações do usuário
- `activeCondominium`: Seletor de condomínio (opcional)
- `onSearch`: Callback de busca (opcional)
- `onLogout`: Callback de logout (opcional)

#### Sidebar

Barra lateral com navegação e logo.

**Features:**
- ✅ Logo do KONDO com gradiente roxo
- ✅ Menu de navegação com ícones
- ✅ Estados active/hover
- ✅ Badges de notificação
- ✅ Botão de logout

#### Header

Cabeçalho com breadcrumbs, busca e perfil.

**Features:**
- ✅ Breadcrumbs navegáveis
- ✅ Barra de busca centralizada
- ✅ Seletor de condomínio
- ✅ Badge de perfil (Síndico, Admin, Morador)
- ✅ Avatar do usuário
- ✅ Dropdown de perfil

---

### 4. Context Selector

Tela de seleção de contexto/condomínio após login.

```tsx
<ContextSelector
  userName="João Silva"
  contexts={[
    {
      id: 'admin',
      title: 'Administração Global',
      subtitle: 'Acesso total ao sistema',
      role: 'admin'
    },
    {
      id: 'aurora',
      title: 'Edifício Aurora',
      subtitle: 'Gestão do condomínio',
      role: 'sindico'
    },
    {
      id: 'sunset',
      title: 'Residencial Sunset',
      subtitle: 'Acesso às informações pessoais',
      role: 'morador',
      block: 'A',
      unit: '301'
    }
  ]}
  onLogout={() => {}}
/>
```

**Props:**
- `userName`: Nome do usuário
- `contexts`: Array de contextos disponíveis
  - `id`: ID único
  - `title`: Título do contexto
  - `subtitle`: Descrição
  - `role`: 'admin' | 'sindico' | 'morador'
  - `block`: Bloco (opcional, para moradores)
  - `unit`: Unidade (opcional, para moradores)
  - `onClick`: Callback ao selecionar (opcional)
- `onLogout`: Callback de logout (opcional)

**Features:**
- ✅ Cards grandes e clicáveis
- ✅ Ícones específicos por perfil
- ✅ Cores diferenciadas por role
- ✅ Badges de perfil, bloco e unidade
- ✅ Animações de hover
- ✅ Card informativo de quantidade de perfis

---

### 5. Form Components

#### Button

```tsx
<Button variant="primary" size="md">
  Salvar
</Button>
```

**Variants:** primary, secondary, ghost, danger, success  
**Sizes:** sm, md, lg  
**Estados:** default, hover, focus, disabled

#### Input

```tsx
<Input
  label="Email"
  type="email"
  placeholder="seu@email.com"
  leftIcon={<Mail />}
  error="Email inválido"
  helperText="Digite seu email"
/>
```

**Variants:** text, password (com toggle), search

#### Select

```tsx
<Select
  label="Condomínio"
  placeholder="Selecione..."
  options={[
    { value: '1', label: 'Aurora' },
    { value: '2', label: 'Sunset' }
  ]}
  error="Campo obrigatório"
/>
```

#### Checkbox

```tsx
<Checkbox
  label="Aceito os termos"
  description="Leia os termos antes de aceitar"
/>
```

#### Toggle

```tsx
<Toggle
  label="Notificações"
  description="Receber alertas por email"
/>
```

---

## 🎨 Design System

### Cores

- **Primary (Roxo):** `kondo-purple-[50-900]` - Confiança e identidade
- **Success (Verde):** `kondo-green-[50-800]` - Pagamentos confirmados
- **Warning (Laranja):** `kondo-orange-[50-700]` - Pendências
- **Danger (Vermelho):** `kondo-red-[50-700]` - Atrasos e erros
- **Info (Teal):** `kondo-teal-[50-700]` - Informações
- **Neutral (Cinza):** `kondo-gray-[50-900]` - Estrutura

### Espaçamento

- Padding interno: `p-6`, `p-8`
- Gap entre elementos: `gap-4`, `gap-6`
- Margens: `mb-2`, `mb-4`, `mb-6`

### Bordas

- Radius padrão: `rounded-lg` (8px)
- Cards: `rounded-xl` (12px)
- Pills/Badges: `rounded-full`

### Sombras

- `shadow-sm` - Cards e elementos leves
- `shadow-md` - Modais e dropdowns
- `shadow-lg` - Elementos flutuantes
- `shadow-xl` - Hero sections

### Tipografia

- **Font:** Inter (Sans-serif)
- **Heading 1:** text-3xl font-bold
- **Heading 2:** text-2xl font-bold
- **Heading 3:** text-xl font-semibold
- **Body:** text-base
- **Small:** text-sm
- **Caption:** text-xs

---

## 📱 Responsividade

Todos os componentes são responsivos e utilizam:
- Grid responsivo: `grid-cols-1 md:grid-cols-2 lg:grid-cols-3`
- Breakpoints Tailwind: sm, md, lg, xl
- Flex wrap para botões e badges

---

## ♿ Acessibilidade

- ✅ Focus states visíveis
- ✅ Labels semânticos
- ✅ ARIA attributes quando necessário
- ✅ Keyboard navigation
- ✅ Color contrast WCAG AA

---

## 🚀 Como Usar

```tsx
import { 
  MainLayout, 
  FinancialCard, 
  DataTable,
  Button 
} from '@/app/components';

function MyPage() {
  return (
    <MainLayout>
      <div className="p-8">
        <FinancialCard {...props} />
        <DataTable {...props} />
      </div>
    </MainLayout>
  );
}
```
