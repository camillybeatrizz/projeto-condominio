# KONDO - Módulos Complexos

Documentação das 4 interfaces complexas criadas para módulos específicos do sistema KONDO.

---

## 💰 1. Financeiro - Cobranças

**Arquivo:** `/src/app/pages/FinanceiroCobrancas.tsx`  
**Navegação:** Dashboard > Cobranças

### Descrição
Gestão completa de cobranças e boletos do condomínio com geração de QR Code PIX.

### Componentes Principais

#### Stats Cards (4 Cards)
- **Total a Receber**: Soma de todas as cobranças
- **Pagos**: Quantidade de cobranças pagas (verde)
- **Pendentes**: Cobranças aguardando pagamento (laranja)
- **Atrasados**: Cobranças vencidas (vermelho)

#### Filtros
- Busca por unidade ou morador
- Dropdown de status (Todos, Pagos, Pendentes, Atrasados)

#### Tabela de Cobranças (DataTable)
**Colunas:**
- Unidade
- Morador
- Referência (mês/ano)
- Vencimento (com ícone de calendário)
- Valor (bold)
- Status (badge colorido com ícone):
  - ✅ Pago (verde)
  - ⏱️ Pendente (laranja)
  - ❌ Atrasado (vermelho)
- Pagamento (data e método se pago)

**Ações:**
- Visualizar
- Download boleto

#### QR Code PIX (Sidebar - Destaque)
**Card Verde Gradiente:**
- Cabeçalho com ícone QR Code
- QR Code visual (SVG pattern simulado)
- Display do valor grande
- Informações da cobrança:
  - Unidade e morador
  - Referência
  - Vencimento
- Botão "Copiar Código PIX"
- Estados:
  - Selecionado: Mostra QR Code
  - Não selecionado: Mensagem de instrução
  - Pago: Mensagem "já foi paga"

#### Ações Rápidas (Sidebar)
- Enviar Boletos por Email
- Download em Lote

### Features
✅ Tabela com paginação e ordenação  
✅ QR Code PIX visual com SVG  
✅ Filtros em tempo real  
✅ Status coloridos com ícones  
✅ Seleção de cobrança para PIX  
✅ Stats financeiros calculados  

---

## 📝 2. Financeiro - Despesas

**Arquivo:** `/src/app/pages/FinanceiroDespesas.tsx`  
**Navegação:** Dashboard > Despesas

### Descrição
Formulário completo para lançamento de despesas com upload de comprovantes.

### Componentes Principais

#### Formulário de Nova Despesa
**Campos:**
1. **Descrição** (text)
   - Placeholder: "Ex: Manutenção preventiva do elevador"

2. **Categoria** (select)
   - Manutenção
   - Utilidades
   - Material
   - Limpeza
   - Segurança
   - Jardinagem
   - Administrativo
   - Outros

3. **Valor** (text com ícone $)
   - Placeholder: "R$ 0,00"

4. **Data da Despesa** (date)
   - Com ícone de calendário

5. **Fornecedor** (text)
   - Nome do fornecedor

6. **Método de Pagamento** (select)
   - Dinheiro
   - Transferência Bancária
   - PIX
   - Boleto
   - Cartão
   - Cheque

7. **Upload de Comprovante** (file upload)
   - **Área de Drop Zone**:
     - Border dashed com hover effect
     - Ícone de upload
     - Texto: "Clique para fazer upload"
     - Suporte: PDF, JPG, PNG até 5MB
   - **Arquivo Anexado**:
     - Ícone de documento
     - Nome do arquivo
     - Tamanho em KB
     - Botão remover (X vermelho)

8. **Observações** (textarea)
   - 4 linhas
   - Opcional

**Ações:**
- Botão Primary: "Lançar Despesa"
- Botão Secondary: "Limpar"

#### Card de Dicas (Sidebar)
**Background Teal Gradiente:**
- Ícone de alerta
- 4 dicas importantes:
  - Sempre anexe o comprovante
  - Categorize corretamente
  - Descreva de forma clara
  - Verifique os dados

#### Despesas Recentes (Sidebar)
- Lista de 3 despesas recentes
- Cada item mostra:
  - Descrição
  - Badge de categoria (roxo)
  - Badge de status (verde/laranja)
  - Data e valor
- Hover effect nos cards
- Link "Ver todas as despesas"

### Features
✅ Formulário completo e validado  
✅ Upload de arquivo com preview  
✅ Remover arquivo anexado  
✅ Categorização de despesas  
✅ Reset form após submit  
✅ Dicas contextualizadas  
✅ Histórico visual de despesas recentes  

---

## 🎫 3. Operacional - Chamados

**Arquivo:** `/src/app/pages/OperacionalChamados.tsx`  
**Navegação:** Dashboard > Chamados > #1247

### Descrição
Visualização detalhada de um chamado com timeline vertical de histórico completo.

### Componentes Principais

#### Card de Informações do Chamado
- **Título**: "Vazamento no 5º andar" (2xl bold)
- **Descrição**: Texto completo do problema
- **Badges**:
  - Status (laranja): "Em andamento"
  - Prioridade (vermelho): "Alta Prioridade"
  - Categoria (cinza): "Hidráulica"
- **Grid de Informações** (4 colunas):
  - Solicitante (nome + unidade)
  - Responsável (nome + função)
  - Aberto em (data + hora)
  - Última atualização (data + hora)

#### Timeline Vertical (Componente Principal)
**Estrutura:**
- Linha vertical cinza conectando os eventos
- Cada evento possui:
  - **Ícone circular colorido** (12px, borda 2px):
    - 🟣 Criado (roxo)
    - 💬 Comentário (teal)
    - ⏱️ Mudança de status (laranja)
    - 👤 Atribuído (roxo)
    - ✅ Resolvido (verde)
  - **Card de conteúdo** (bg-gray-50):
    - Nome do usuário (bold)
    - Timestamp
    - Tipo de ação (colorido)
    - Conteúdo/descrição

**Tipos de Eventos:**
1. **Criado**: Mensagem inicial + descrição
2. **Comentário**: Texto do comentário
3. **Mudança de Status**: 
   - Badge antigo → Badge novo
   - Visual com seta
4. **Atribuído**: Nome do responsável

**8 Eventos no Mock:**
- Criação do chamado
- Atribuição de responsável
- 2 mudanças de status
- 4 comentários de acompanhamento

#### Adicionar Comentário
- Textarea (4 linhas)
- Botão "Anexar arquivo" (com ícone)
- Botão "Enviar Comentário" (desabilitado se vazio)

#### Sidebar - Ações
**Card de Ações:**
- ✅ Marcar como Resolvido (verde)
- 👤 Reatribuir Chamado (secondary)
- ⏱️ Alterar Prioridade (ghost)

#### Sidebar - Fluxo de Status
**Progress Visual:**
- 4 etapas com círculos numerados
- ✓ Aberto (completo - roxo)
- 2️⃣ Em andamento (atual - laranja)
- 3️⃣ Aguardando Validação (pendente - cinza)
- 4️⃣ Resolvido (pendente - cinza)
- Linhas conectoras verticais

#### Sidebar - Chamados Relacionados
- 2 chamados similares
- ID + título
- Badge de status
- Clicáveis

### Features
✅ Timeline vertical elegante  
✅ 8 tipos de eventos diferentes  
✅ Ícones e cores por tipo  
✅ Linha conectora visual  
✅ Comentários em tempo real  
✅ Upload de anexos  
✅ Fluxo de status visual  
✅ Ações rápidas contextualizadas  

---

## 📅 4. Áreas Comuns - Reservas

**Arquivo:** `/src/app/pages/AreasComuns.tsx`  
**Navegação:** Dashboard > Áreas Comuns

### Descrição
Calendário visual para gestão de reservas de espaços do condomínio.

### Componentes Principais

#### Cards de Áreas (5 Cards Clicáveis)
**Grid Responsivo:**
- Salão de Festas (roxo)
- Churrasqueira (laranja)
- Piscina (teal)
- Academia (verde)
- Quadra de Esportes (vermelho)

**Cada Card:**
- Ícone de mapa
- Nome da área
- Quantidade de reservas
- Clicável para filtrar
- Estado ativo: cor cheia + texto branco
- Estado inativo: branco + borda

#### Calendário Visual Mensal
**Header (Roxo Gradiente):**
- Navegação: ← Mês/Ano →
- Filtro ativo (se selecionado) com botão X

**Grid do Calendário:**
- 7 colunas (Dom - Sáb)
- Células de dias:
  - Número do dia (14px bold)
  - Lista de reservas (máx 2 visíveis):
    - Badge colorido por área
    - Horário de início
  - "+X" se houver mais reservas
  - Destaque hoje: background roxo claro
  - Hover effect

**Funcionalidades:**
- Navegação mês a mês
- Filtro por área (clique nos cards)
- Click no dia para criar reserva
- Cores consistentes por área

#### Sidebar - Próximas Reservas
**Lista de Reservas:**
- Card por reserva com:
  - Ícone colorido da área
  - Nome da área
  - Data (formato: "05 jun")
  - Horário de início
  - Nome do usuário
  - Badge de status:
    - Confirmada (verde)
    - Pendente (laranja)
- Máximo 5 visíveis
- Link "Ver todas"

#### Sidebar - Regras de Reserva
**Card Teal Gradiente:**
- 4 regras principais:
  - Antecedência de 30 dias
  - Máximo 2 reservas/mês
  - Taxa de limpeza do salão
  - Cancelamento gratuito 48h antes

#### Modal de Nova Reserva
**Formulário:**
- Select: Área Comum
- Date: Data da reserva
- Time: Horário de início
- Time: Horário de término
- Number: Número de convidados
- Botões: Confirmar | Cancelar

**Comportamento:**
- Overlay escuro (50% opacity)
- Centralizado
- Scroll interno se necessário
- Fecha com X ou Cancelar

### Features
✅ Calendário mensal completo  
✅ Grid visual de dias  
✅ Reservas inline nos dias  
✅ Navegação mês a mês  
✅ Filtro por área (toggle)  
✅ Cards de áreas clicáveis  
✅ Modal de nova reserva  
✅ Lista de próximas reservas  
✅ Cores consistentes por área  
✅ Regras de reserva visíveis  

---

## 🎨 Design System Aplicado

### Cores por Contexto

**Financeiro - Cobranças:**
- Verde: QR Code PIX, pagamentos confirmados
- Laranja: Pendentes
- Vermelho: Atrasados

**Financeiro - Despesas:**
- Teal: Card de dicas
- Roxo: Categorias
- Verde/Laranja: Status de aprovação

**Operacional - Chamados:**
- Roxo: Criação, atribuição
- Teal: Comentários
- Laranja: Mudanças de status
- Verde: Resolução

**Áreas Comuns:**
- Roxo: Salão de Festas
- Laranja: Churrasqueira
- Teal: Piscina, Regras
- Verde: Academia
- Vermelho: Quadra

### Componentes Reutilizados
- `Button` - CTAs e ações
- `Input` - Formulários
- `Select` - Dropdowns
- `DataTable` - Tabela de cobranças
- `StatusCard` - Cards de chamados
- Layout base com Sidebar + Header

### Padrões de Interação

**Upload de Arquivo:**
- Drop zone com border dashed
- Hover effect (borda roxa)
- Preview do arquivo anexado
- Remover com ícone X

**Timeline Vertical:**
- Linha conectora cinza
- Ícones circulares coloridos
- Cards de conteúdo alternados
- Scroll vertical

**Calendário:**
- Grid responsivo 7x6
- Células clicáveis
- Badges coloridos inline
- Navegação com chevrons

**Modais:**
- Overlay escuro
- Card centralizado
- Header sticky
- Scroll interno

---

## 🔄 Navegação

### Menu Lateral
Todas as páginas são acessíveis pelo menu:
- Dashboard → Dashboard de Gestão
- Cobranças → Financeiro - Cobranças
- Despesas → Financeiro - Despesas
- Chamados → Lista → Detalhes (#1247)
- Áreas Comuns → Calendário de Reservas

### Breadcrumbs
- Cobranças: Dashboard > Financeiro > Cobranças
- Despesas: Dashboard > Financeiro > Despesas
- Chamados: Dashboard > Chamados > #1247
- Áreas Comuns: Dashboard > Áreas Comuns

---

## 📊 Dados Mock

**Cobranças:** 8 registros com mix de status  
**Despesas:** 3 despesas recentes  
**Chamados:** 8 eventos de timeline  
**Áreas Comuns:** 6 reservas em junho/2026  

---

## ✅ Status de Implementação

- ✅ Financeiro - Cobranças (com QR Code PIX)
- ✅ Financeiro - Despesas (com upload)
- ✅ Operacional - Chamados (timeline vertical)
- ✅ Áreas Comuns (calendário + modal)
- ✅ Navegação integrada no menu
- ✅ Breadcrumbs contextualizados
- ✅ Design System aplicado
- ✅ Responsividade completa
