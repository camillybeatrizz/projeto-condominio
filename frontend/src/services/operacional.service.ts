import axiosClient from '../api/axios-client';
import { BlocoResponse, UnidadeResponse, AreaComumResponse, PageResponse } from '../types/api';

export const estruturaService = {
  // Blocos
  listBlocos: async (condominioId: string) => {
    const response = await axiosClient.get<BlocoResponse[]>(`/condominios/${condominioId}/blocos`);
    return response.data;
  },

  // Unidades
  listUnidades: async (blocoId: string) => {
    const response = await axiosClient.get<UnidadeResponse[]>(`/blocos/${blocoId}/unidades`);
    return response.data;
  },

  // Áreas Comuns
  listAreasComuns: async (condominioId: string) => {
    const response = await axiosClient.get<AreaComumResponse[]>(`/condominios/${condominioId}/areas-comuns`);
    return response.data;
  }
};

export const chamadoService = {
  list: async (params: { condominioId?: string; unidadeId?: string; status?: string; page?: number; size?: number }) => {
    const response = await axiosClient.get<PageResponse<any>>('/chamados', { params });
    return response.data;
  },

  create: async (data: { descricao: string; unidadeId: string }) => {
    const response = await axiosClient.post('/chamados', data);
    return response.data;
  },

  updateStatus: async (id: string, status: string) => {
    const response = await axiosClient.patch(`/chamados/${id}/status`, { status });
    return response.data;
  }
};
