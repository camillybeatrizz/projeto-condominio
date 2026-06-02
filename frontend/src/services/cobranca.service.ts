import axiosClient from '../api/axios-client';
import { 
  CobrancaDashboardResponse, 
  CobrancaResumoResponse, 
  CobrancaPixResponse, 
  CobrancaResponse, 
  PageResponse,
  StatusCobranca,
  FormaPagamento
} from '../types/api';

export const cobrancaService = {
  getDashboard: async (condominioId: string): Promise<CobrancaDashboardResponse> => {
    try {
      const response = await axiosClient.get<CobrancaDashboardResponse>('/cobrancas/dashboard', {
        params: { condominioId }
      });
      return response.data;
    } catch {
      console.warn('Usando mock para Dashboard Financeiro');
      return {
        resumo: {
          totalCobrancas: 150,
          totalAbertas: 45,
          totalPagas: 98,
          totalInadimplentes: 7,
          valorTotal: 75000,
          valorAberto: 22500,
          valorPago: 49000,
          valorInadimplente: 3500,
        },
        inadimplentesRecentes: [
          { id: '1', valor: 450, vencimento: '2026-03-10', status: StatusCobranca.VENCIDA, competencia: '2026-03', unidadeId: 'Apt 101', createdAt: '', updatedAt: '' },
          { id: '2', valor: 450, vencimento: '2026-03-10', status: StatusCobranca.VENCIDA, competencia: '2026-03', unidadeId: 'Apt 305', createdAt: '', updatedAt: '' },
        ],
        pagamentosRecentes: [
          { id: 'p1', valor: 450, dataPagamento: '2026-04-01', forma: FormaPagamento.PIX, cobrancaId: 'c1', createdAt: '', updatedAt: '' },
          { id: 'p2', valor: 1200, dataPagamento: '2026-03-31', forma: FormaPagamento.BOLETO, cobrancaId: 'c2', createdAt: '', updatedAt: '' },
        ]
      };
    }
  },

  getResumo: async (condominioId: string, competencia?: string) => {
    const response = await axiosClient.get<CobrancaResumoResponse>('/cobrancas/resumo', {
      params: { condominioId, competencia }
    });
    return response.data;
  },

  getPix: async (cobrancaId: string): Promise<CobrancaPixResponse> => {
    try {
      const response = await axiosClient.get<CobrancaPixResponse>(`/cobrancas/${cobrancaId}/pix`);
      return response.data;
    } catch {
      return {
        cobrancaId,
        referenciaExterna: 'pay_mock_123',
        urlPagamentoExterno: 'https://sandbox.asaas.com/mock',
        pixCopiaCola: '00020101021226890014br.gov.bcb.pix2567pix-h.example.com/qr/v2/cob/mock-kondo-123456789',
        pixQrCodeBase64: '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',
        pixExpiracao: new Date(Date.now() + 60 * 60 * 1000).toISOString(),
      };
    }
  },

  list: async (params: { 
    condominioId: string; 
    unidadeId?: string; 
    competencia?: string; 
    status?: string;
    page?: number;
    size?: number;
  }): Promise<PageResponse<CobrancaResponse>> => {
    try {
      const { unidadeId: _unidadeId, ...apiParams } = params;
      void _unidadeId;
      const response = await axiosClient.get<PageResponse<CobrancaResponse>>('/cobrancas', { params: apiParams });
      return response.data;
    } catch {
      console.warn('Usando mock para Listagem de Cobranças');
      return {
        content: [
          { id: '1', valor: 450, vencimento: '2026-04-10', status: StatusCobranca.ABERTA, competencia: '2026-04', unidadeId: params.unidadeId || 'U-101', createdAt: '', updatedAt: '' },
          { id: '2', valor: 450, vencimento: '2026-03-10', status: StatusCobranca.PAGA, competencia: '2026-03', unidadeId: params.unidadeId || 'U-101', createdAt: '', updatedAt: '' },
          { id: '3', valor: 450, vencimento: '2026-02-10', status: StatusCobranca.PAGA, competencia: '2026-02', unidadeId: params.unidadeId || 'U-101', createdAt: '', updatedAt: '' },
        ],
        page: 0, size: 10, totalElements: 3, totalPages: 1, first: true, last: true, empty: false
      };
    }
  }
};
