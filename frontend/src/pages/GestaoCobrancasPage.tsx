import { useQuery } from '@tanstack/react-query';
import { 
  Search, 
  Filter, 
  Download, 
  Plus, 
  CheckCircle2,
  Clock,
  AlertCircle,
  XCircle,
  QrCode,
  ArrowRight
} from 'lucide-react';
import { MainLayout } from '../components/MainLayout';
import { Card } from '../components/Card';
import { cobrancaService } from '../services/cobranca.service';
import { useAuth } from '../providers/auth-context';
import { useState } from 'react';
import { StatusCobranca, Perfil } from '../types/api';
import { formatCurrency, formatDateBr } from '../utils/formatters';

export function GestaoCobrancasPage() {
  const { activeAcesso } = useAuth();
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [competenciaFilter, setCompetenciaFilter] = useState<string>('');
  
  const isMorador = activeAcesso?.perfil === Perfil.MORADOR;
  const condominioId = activeAcesso?.condominioId || '';
  const unidadeId = isMorador ? activeAcesso?.unidadeId : undefined;

  const { data: cobrancasPage, isLoading } = useQuery({
    queryKey: ['cobrancas', condominioId, unidadeId, statusFilter, competenciaFilter],
    queryFn: () => cobrancaService.list({
      condominioId,
      unidadeId,
      status: statusFilter || undefined,
      competencia: competenciaFilter || undefined,
    }),
    enabled: !!condominioId,
  });

  const getStatusBadge = (status: StatusCobranca) => {
    switch (status) {
      case StatusCobranca.PAGA:
        return (
          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-kondo-green-50 text-kondo-green-700 text-xs font-bold border border-kondo-green-100">
            <CheckCircle2 className="w-3 h-3" /> PAGA
          </span>
        );
      case StatusCobranca.ABERTA:
        return (
          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-kondo-purple-50 text-kondo-purple-700 text-xs font-bold border border-kondo-purple-100">
            <Clock className="w-3 h-3" /> ABERTA
          </span>
        );
      case StatusCobranca.VENCIDA:
        return (
          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-kondo-red-50 text-kondo-red-700 text-xs font-bold border border-kondo-red-100">
            <AlertCircle className="w-3 h-3" /> VENCIDA
          </span>
        );
      default:
        return (
          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-kondo-gray-50 text-kondo-gray-700 text-xs font-bold border border-kondo-gray-100">
            <XCircle className="w-3 h-3" /> {status}
          </span>
        );
    }
  };

  return (
    <MainLayout breadcrumbs={[{ label: isMorador ? 'Minhas Cobranças' : 'Gestão de Cobranças' }]}>
      <div className="space-y-6">
        {/* Header Section */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-kondo-gray-900">
              {isMorador ? 'Minhas Cobranças' : 'Gestão de Cobranças'}
            </h1>
            <p className="text-kondo-gray-500 text-sm">
              {isMorador 
                ? `Acompanhe seus pagamentos da unidade ${activeAcesso?.unidadeNumero}`
                : `Gerenciamento financeiro do condomínio ${activeAcesso?.condominioNome}`}
            </p>
          </div>
          
          <div className="flex items-center gap-3">
            {!isMorador && (
              <button className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-white bg-kondo-purple-600 rounded-lg hover:bg-kondo-purple-700 transition-all shadow-md">
                <Plus className="w-4 h-4" /> Nova Cobrança
              </button>
            )}
            <button className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-kondo-gray-700 bg-white border border-kondo-gray-200 rounded-lg hover:bg-kondo-gray-50 transition-all">
              <Download className="w-4 h-4" /> Exportar
            </button>
          </div>
        </div>

        {/* Filters Section */}
        <Card className="p-4 flex flex-wrap items-center gap-4">
          <div className="relative flex-1 min-w-[200px]">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-kondo-gray-400" />
            <input 
              type="text" 
              placeholder="Buscar por unidade ou código..."
              className="w-full pl-10 pr-4 py-2 bg-kondo-gray-50 border border-kondo-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-kondo-purple-500/20 focus:border-kondo-purple-500 transition-all"
            />
          </div>
          
          <select 
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-4 py-2 bg-white border border-kondo-gray-200 rounded-lg text-sm font-medium text-kondo-gray-700 focus:outline-none focus:ring-2 focus:ring-kondo-purple-500/20 transition-all"
          >
            <option value="">Todos os Status</option>
            <option value="ABERTA">Aberta</option>
            <option value="PAGA">Paga</option>
            <option value="VENCIDA">Vencida</option>
          </select>

          <select 
            value={competenciaFilter}
            onChange={(e) => setCompetenciaFilter(e.target.value)}
            className="px-4 py-2 bg-white border border-kondo-gray-200 rounded-lg text-sm font-medium text-kondo-gray-700 focus:outline-none focus:ring-2 focus:ring-kondo-purple-500/20 transition-all"
          >
            <option value="">Todas as Competências</option>
            <option value="2026-04">Abril/2026</option>
            <option value="2026-03">Março/2026</option>
            <option value="2026-02">Fevereiro/2026</option>
          </select>

          <button className="p-2 text-kondo-gray-500 hover:bg-kondo-gray-100 rounded-lg transition-colors">
            <Filter className="w-5 h-5" />
          </button>
        </Card>

        {/* Table Section */}
        <Card className="overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead className="bg-kondo-gray-50 text-kondo-gray-500 text-[11px] uppercase tracking-wider">
                <tr>
                  {!isMorador && <th className="px-6 py-4 font-bold">Unidade</th>}
                  <th className="px-6 py-4 font-bold">Competência</th>
                  <th className="px-6 py-4 font-bold">Vencimento</th>
                  <th className="px-6 py-4 font-bold">Valor</th>
                  <th className="px-6 py-4 font-bold">Status</th>
                  <th className="px-6 py-4 font-bold text-right">Ações</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-kondo-gray-100">
                {isLoading ? (
                  [1, 2, 3, 4, 5].map((i) => (
                    <tr key={i} className="animate-pulse">
                      <td colSpan={isMorador ? 5 : 6} className="px-6 py-4">
                        <div className="h-5 bg-kondo-gray-100 rounded w-full" />
                      </td>
                    </tr>
                  ))
                ) : (
                  cobrancasPage?.content.map((cobranca) => (
                    <tr key={cobranca.id} className="hover:bg-kondo-gray-50/50 transition-colors group">
                      {!isMorador && (
                        <td className="px-6 py-4 text-sm font-bold text-kondo-gray-900">
                          {cobranca.unidadeId}
                        </td>
                      )}
                      <td className="px-6 py-4 text-sm text-kondo-gray-600 font-medium">
                        {cobranca.competencia}
                      </td>
                      <td className="px-6 py-4 text-sm text-kondo-gray-600">
                        {formatDateBr(cobranca.vencimento)}
                      </td>
                      <td className="px-6 py-4 text-sm font-bold text-kondo-gray-900">
                        {formatCurrency(cobranca.valor)}
                      </td>
                      <td className="px-6 py-4">
                        {getStatusBadge(cobranca.status)}
                      </td>
                      <td className="px-6 py-4 text-right">
                        <div className="flex items-center justify-end gap-2">
                          {cobranca.status !== StatusCobranca.PAGA && (
                            <button className="p-2 text-kondo-purple-600 hover:bg-kondo-purple-50 rounded-lg transition-colors shadow-sm border border-transparent hover:border-kondo-purple-100">
                              <QrCode className="w-4 h-4" />
                            </button>
                          )}
                          <button className="p-2 text-kondo-gray-400 hover:text-kondo-gray-600 hover:bg-kondo-gray-50 rounded-lg transition-colors">
                            <ArrowRight className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
                {!isLoading && cobrancasPage?.content.length === 0 && (
                  <tr>
                    <td colSpan={isMorador ? 5 : 6} className="px-6 py-12 text-center text-kondo-gray-400">
                      Nenhuma cobrança encontrada para os filtros selecionados.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          
          {/* Pagination */}
          <div className="px-6 py-4 bg-kondo-gray-50 border-t border-kondo-gray-100 flex items-center justify-between">
            <p className="text-xs text-kondo-gray-500 font-medium">
              Mostrando <span className="text-kondo-gray-900 font-bold">{cobrancasPage?.content.length || 0}</span> de <span className="text-kondo-gray-900 font-bold">{cobrancasPage?.totalElements || 0}</span> cobranças
            </p>
            <div className="flex items-center gap-2">
              <button className="px-3 py-1.5 text-xs font-bold text-kondo-gray-500 bg-white border border-kondo-gray-200 rounded hover:bg-kondo-gray-50 transition-all disabled:opacity-50" disabled>
                Anterior
              </button>
              <button className="px-3 py-1.5 text-xs font-bold text-kondo-purple-600 bg-white border border-kondo-purple-200 rounded hover:bg-kondo-purple-50 transition-all">
                Próxima
              </button>
            </div>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
}
