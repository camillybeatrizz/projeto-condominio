import axiosClient from '../api/axios-client';
import { 
  CobrancaDashboardResponse, 
  CobrancaResumoResponse, 
  CobrancaPixResponse, 
  CobrancaResponse, 
  PageResponse 
} from '../types/api';

export const cobrancaService = {
  getDashboard: async (condominioId: string) => {
    const response = await axiosClient.get<CobrancaDashboardResponse>('/cobrancas/dashboard', {
      params: { condominioId }
    });
    return response.data;
  },

  getResumo: async (condominioId: string, competencia?: string) => {
    const response = await axiosClient.get<CobrancaResumoResponse>('/cobrancas/resumo', {
      params: { condominioId, competencia }
    });
    return response.data;
  },

  getPix: async (cobrancaId: string) => {
    const response = await axiosClient.get<CobrancaPixResponse>(`/cobrancas/${cobrancaId}/pix`);
    return response.data;
  },

  list: async (params: { 
    condominioId: string; 
    unidadeId?: string; 
    competencia?: string; 
    status?: string;
    page?: number;
    size?: number;
  }) => {
    const response = await axiosClient.get<PageResponse<CobrancaResponse>>('/cobrancas', { params });
    return response.data;
  }
};
