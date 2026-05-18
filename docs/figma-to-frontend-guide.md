# Guia: Figma para Frontend com Gemini AI

Este guia descreve como integrar o seu fluxo de design no Figma com a inteligência do Gemini para acelerar o desenvolvimento da interface visual do KONDO, respeitando a `frontend-architecture.md`.

## 1. Preparação do Ambiente

### 1.1 No Figma
1.  **Habilite o Dev Mode:** Se tiver uma conta paga ou educacional, clique no ícone `< >` no topo direito. Ele facilita a extração de cores, espaçamentos e tokens de design.
2.  **Organize em Frames:** Certifique-se de que cada "tela" ou "componente" (ex: Card de Cobrança, Sidebar) esteja dentro de um Frame nomeado. Isso ajuda o Gemini a entender a hierarquia.
3.  **Exportação de Assets:** Exporte ícones e imagens em SVG ou PNG para a pasta `src/assets/` do projeto antes de pedir o código.

### 1.2 No Gemini (Workflow Visual)
O Gemini possui capacidades multimodais. Você não precisa de um "plugin MVC" específico; você usará o Gemini como o **tradutor visual**.

---

## 2. Passo a Passo da Integração

### Passo 1: Captura de Contexto
Tire um screenshot nítido do componente ou tela no Figma. Se estiver usando o Dev Mode, copie também o CSS ou as propriedades de layout (Flexbox/Grid) que o Figma exibe no painel lateral.

### Passo 2: O Prompt "MVC" (Model-View-Controller)
Para que o Gemini gere código que se encaixe perfeitamente no KONDO, use a seguinte estrutura de prompt ao enviar a imagem:

> **Prompt Estruturado:**
> "Gemini, utilize a imagem em anexo como referência visual para criar o componente `[Nome do Componente]`.
> 
> **Contexto de Arquitetura:**
> - Siga a estrutura definida em `docs/frontend-architecture.md`.
> - Utilize **Tailwind CSS** e **Shadcn/ui**.
> - O componente deve ser um 'Functional Component' em TypeScript.
> 
> **Mapeamento MVC:**
> 1. **Model:** Utilize os tipos definidos em `src/types/api.ts` (ex: `CobrancaDTO`).
> 2. **View:** Reproduza fielmente o layout, cores e tipografia da imagem.
> 3. **Controller:** Implemente a lógica necessária no componente (ou em um custom hook se for complexo), como o estado de hover ou cliques.
> 
> **Código Desejado:** Gere o arquivo `.tsx` e explique onde ele deve ser salvo na pasta `src/features/[feature-name]/components/`."

---

## 3. Como pedir ao Gemini para realizar o desenvolvimento

Para obter os melhores resultados, divida o pedido por **nível de granularidade**:

### A. Peça Componentes Atômicos Primeiro
Não peça a tela inteira de uma vez. Peça:
1. "Gere o Card de Cobrança Inadimplente baseado nesta imagem."
2. "Gere a Sidebar lateral com suporte a perfis."

### B. Peça a Montagem da Tela (Page)
Após ter os componentes:
"Agora, utilize os componentes que criamos e esta imagem da tela de Dashboard para montar a `DashboardPage.tsx` na pasta `features/dashboard/pages/`."

---

## 4. Dicas de Ouro para Fidelidade Visual

1.  **Tokens de Cor:** Se o seu Figma tiver cores hexadecimais específicas, forneça uma lista ao Gemini no início da sessão (ex: "Primary: #2563eb").
2.  **Responsividade:** Pergunte ao Gemini: "Como este layout deve se comportar em telas de 375px (mobile)?" com base na referência visual.
3.  **Interatividade:** Se a imagem mostra um modal aberto, diga: "A imagem mostra o estado 'Aberto'. Gere o código usando o componente `Dialog` do Shadcn/ui para este comportamento."

## 5. Integração Direta (Opcional - Ferramentas Auxiliares)
Se preferir usar ferramentas de automação que se integram ao Gemini:
*   **Figma plugin "Html.to.design" ou "Builder.io":** Podem exportar o código base que você depois submete ao Gemini para "Refatorar conforme a arquitetura KONDO".
*   **Screenshot para Código:** Você pode colar a URL do Figma (se pública) ou o arquivo no chat do Gemini e pedir: "Analise este link do Figma e implemente a estrutura de arquivos proposta no nosso `frontend-plan.md`."
