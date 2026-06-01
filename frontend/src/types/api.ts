export enum Perfil {
  ADMIN = 'ADMIN',
  SINDICO = 'SINDICO',
  MORADOR = 'MORADOR',
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
