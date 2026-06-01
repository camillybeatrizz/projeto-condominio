export enum Perfil {
  ADMIN = 'ADMIN',
  SINDICO = 'SINDICO',
  MORADOR = 'MORADOR',
}

export enum StatusCobranca {
  PENDENTE = 'PENDENTE',
  PAGA = 'PAGA',
  VENCIDA = 'VENCIDA',
  CANCELADA = 'CANCELADA',
}

export enum FormaPagamento {
  PIX = 'PIX',
  BOLETO = 'BOLETO',
  CARTAO_CREDITO = 'CARTAO_CREDITO',
}

export interface MeuAcesso {
  acessoId: string;
  perfil: Perfil;
  condominioId: string;
  condominioNome: string;
  unidadeId?: string;
  unidadeNumero?: string;
}

export interface MeuContexto {
  usuarioId: string;
  nome: string;
  email: string;
  telefone?: string;
  ativo: boolean;
  acessos: MeuAcesso[];
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface CobrancaResponse {
  id: string;
  valor: number;
  vencimento: string;
  status: StatusCobranca;
  competencia: string;
  referenciaExterna?: string;
  urlPagamentoExterno?: string;
  unidadeId: string;
  createdAt: string;
  updatedAt: string;
}

export interface PagamentoResponse {
  id: string;
  valor: number;
  dataPagamento: string;
  forma: FormaPagamento;
  transactionId?: string;
  cobrancaId: string;
  createdAt: string;
  updatedAt: string;
}

export interface CobrancaResumoResponse {
  totalCobrancas: number;
  totalAbertas: number;
  totalPagas: number;
  totalInadimplentes: number;
  valorTotal: number;
  valorAberto: number;
  valorPago: number;
  valorInadimplente: number;
}

export interface CobrancaDashboardResponse {
  resumo: CobrancaResumoResponse;
  inadimplentesRecentes: CobrancaResponse[];
  pagamentosRecentes: PagamentoResponse[];
}

export interface CobrancaPixResponse {
  cobrancaId: string;
  referenciaExterna: string;
  urlPagamentoExterno: string;
  pixCopiaCola: string;
  pixQrCodeBase64: string;
  pixExpiracao: string;
}
