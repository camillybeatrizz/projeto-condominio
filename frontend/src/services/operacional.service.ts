import axiosClient from '../api/axios-client';
import { BlocoResponse, UnidadeResponse, AreaComumResponse, PageResponse, StatusChamado, ChamadoResponse } from '../types/api';

export const estruturaService = {
  listBlocos: async (condominioId: string): Promise<BlocoResponse[]> => {
    try {
      const response = await axiosClient.get<PageResponse<BlocoResponse>>('/blocos', {
        params: { condominioId, size: 100 },
      });
      return response.data.content;
    } catch {
      console.warn('Usando mock para Blocos');
      return [
        { id: 'b1', nome: 'Bloco A', condominioId },
        { id: 'b2', nome: 'Bloco B', condominioId },
        { id: 'b3', nome: 'Bloco C', condominioId },
      ];
    }
  },

  listUnidades: async (blocoId: string): Promise<UnidadeResponse[]> => {
    try {
      const response = await axiosClient.get<PageResponse<UnidadeResponse>>('/unidades', {
        params: { blocoId, size: 100 },
      });
      return response.data.content;
    } catch {
      console.warn('Usando mock para Unidades');
      return [
        { id: 'u1', numero: '101', andar: '1', tipo: 'APARTAMENTO', blocoId, moradorId: 'm1' },
        { id: 'u2', numero: '102', andar: '1', tipo: 'APARTAMENTO', blocoId, moradorId: 'm2' },
        { id: 'u3', numero: '201', andar: '2', tipo: 'APARTAMENTO', blocoId },
        { id: 'u4', numero: '202', andar: '2', tipo: 'APARTAMENTO', blocoId, moradorId: 'm4' },
      ];
    }
  },

  listAreasComuns: async (condominioId: string): Promise<AreaComumResponse[]> => {
    const response = await axiosClient.get<PageResponse<AreaComumResponse>>('/areas-comuns', {
      params: { condominioId, size: 100 },
    });
    return response.data.content;
  }
};

export const chamadoService = {
  list: async (params: { condominioId?: string; unidadeId?: string; status?: string; page?: number; size?: number }): Promise<PageResponse<ChamadoResponse>> => {
    try {
      const { unidadeId: _unidadeId, ...apiParams } = params;
      void _unidadeId;
      const response = await axiosClient.get<PageResponse<ChamadoResponse>>('/chamados', { params: apiParams });
      return response.data;
    } catch {
      console.warn('Usando mock para Chamados');
      return {
        content: [
          { id: 'ch1', descricao: 'Vazamento na pia da cozinha', status: StatusChamado.ABERTO, dataAbertura: '2026-04-01', unidadeId: '101A' },
          { id: 'ch2', descricao: 'Lâmpada do corredor queimada', status: StatusChamado.ANDAMENTO, dataAbertura: '2026-03-30', unidadeId: '302B' },
          { id: 'ch3', descricao: 'Barulho excessivo no andar de cima', status: StatusChamado.CONCLUIDO, dataAbertura: '2026-03-25', unidadeId: '201A' },
        ],
        page: 0, size: 10, totalElements: 3, totalPages: 1, first: true, last: true, empty: false
      };
    }
  },

  create: async (data: { descricao: string; unidadeId: string }): Promise<ChamadoResponse> => {
    const response = await axiosClient.post<ChamadoResponse>('/chamados', {
      ...data,
      status: StatusChamado.ABERTO,
      dataAbertura: new Date().toISOString().slice(0, 10),
    });
    return response.data;
  },

  updateStatus: async (chamado: ChamadoResponse, status: StatusChamado): Promise<ChamadoResponse> => {
    const response = await axiosClient.put<ChamadoResponse>(`/chamados/${chamado.id}`, {
      descricao: chamado.descricao,
      unidadeId: chamado.unidadeId,
      dataAbertura: chamado.dataAbertura,
      status,
    });
    return response.data;
  }
};
