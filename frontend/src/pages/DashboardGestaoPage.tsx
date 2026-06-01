import { useQuery } from '@tanstack/react-query';
import { 
  Wallet, 
  TrendingUp, 
  AlertCircle, 
  ArrowUpRight, 
  Download,
  Filter,
  CreditCard,
  Clock,
  CheckCircle2
} from 'lucide-react';
import { MainLayout } from '../components/MainLayout';
import { FinancialCard, Card } from '../components/Card';
import { cobrancaService } from '../services/cobranca.service';
import { useAuth } from '../providers/AuthProvider';

export function DashboardGestaoPage() {
  const { activeAcesso } = useAuth();
  const condominioId = activeAcesso?.condominioId || '';

  const { data: dashboard, isLoading } = useQuery({
    queryKey: ['dashboard', condominioId],
    queryFn: () => cobrancaService.getDashboard(condominioId),
    enabled: !!condominioId,
  });

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value || 0);
  };

  const formatDate = (dateStr: string) => {
    return new Date(dateStr).toLocaleDateString('pt-BR');
  };

  return (
    <MainLayout breadcrumbs={[{ label: 'Dashboard' }]}>
      <div className="space-y-8">
        {/* Top Header */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-kondo-gray-900">Saúde Financeira</h1>
            <p className="text-kondo-gray-500">Resumo consolidado do condomínio {activeAcesso?.condominioNome}</p>
          </div>
          <div className="flex items-center gap-3">
            <button className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-kondo-gray-700 bg-white border border-kondo-gray-200 rounded-lg hover:bg-kondo-gray-50 transition-all shadow-sm">
              <Download className="w-4 h-4" /> Exportar
            </button>
            <button className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-white bg-kondo-purple-600 rounded-lg hover:bg-kondo-purple-700 transition-all shadow-md">
              <Filter className="w-4 h-4" /> Filtros
            </button>
          </div>
        </div>

        {/* Financial KPIs */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <FinancialCard
            label="Receita Total"
            value={formatCurrency(dashboard?.resumo.valorTotal || 0)}
            icon={Wallet}
            iconColor="bg-kondo-purple-100 text-kondo-purple-600"
            isLoading={isLoading}
            subtitle="Acumulado total"
          />
          <FinancialCard
            label="Total Recebido"
            value={formatCurrency(dashboard?.resumo.valorPago || 0)}
            icon={TrendingUp}
            iconColor="bg-kondo-green-100 text-kondo-green-600"
            trend={{ value: '82%', isPositive: true }}
            isLoading={isLoading}
            subtitle={`${dashboard?.resumo.totalPagas || 0} cobranças pagas`}
          />
          <FinancialCard
            label="Inadimplência"
            value={formatCurrency(dashboard?.resumo.valorInadimplente || 0)}
            icon={AlertCircle}
            iconColor="bg-kondo-red-100 text-kondo-red-600"
            trend={{ value: '12%', isPositive: false }}
            isLoading={isLoading}
            subtitle={`${dashboard?.resumo.totalInadimplentes || 0} em atraso`}
          />
          <FinancialCard
            label="Aguardando"
            value={formatCurrency(dashboard?.resumo.valorAberto || 0)}
            icon={Clock}
            iconColor="bg-kondo-orange-100 text-kondo-orange-600"
            isLoading={isLoading}
            subtitle={`${dashboard?.resumo.totalAbertas || 0} pendentes`}
          />
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Latest Payments */}
          <Card className="lg:col-span-2 overflow-hidden">
            <div className="p-6 border-b border-kondo-gray-100 flex items-center justify-between">
              <h3 className="font-bold text-kondo-gray-900 flex items-center gap-2">
                <CheckCircle2 className="w-5 h-5 text-kondo-green-500" />
                Pagamentos Recentes
              </h3>
              <button className="text-sm font-bold text-kondo-purple-600 hover:text-kondo-purple-700 transition-colors">
                Ver todos
              </button>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead className="bg-kondo-gray-50 text-kondo-gray-500 text-[11px] uppercase tracking-wider">
                  <tr>
                    <th className="px-6 py-3 font-bold">Data</th>
                    <th className="px-6 py-3 font-bold">Valor</th>
                    <th className="px-6 py-3 font-bold">Forma</th>
                    <th className="px-6 py-3 font-bold">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-kondo-gray-100">
                  {dashboard?.pagamentosRecentes.map((pagamento) => (
                    <tr key={pagamento.id} className="hover:bg-kondo-gray-50/50 transition-colors group">
                      <td className="px-6 py-4 text-sm text-kondo-gray-600 font-medium">
                        {formatDate(pagamento.dataPagamento)}
                      </td>
                      <td className="px-6 py-4 text-sm font-bold text-kondo-gray-900">
                        {formatCurrency(pagamento.valor)}
                      </td>
                      <td className="px-6 py-4">
                        <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-md bg-kondo-gray-100 text-kondo-gray-700 text-xs font-bold uppercase">
                          <CreditCard className="w-3 h-3" />
                          {pagamento.forma}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <span className="text-kondo-green-600 font-bold text-xs uppercase flex items-center gap-1">
                          <CheckCircle2 className="w-4 h-4" /> Confirmado
                        </span>
                      </td>
                    </tr>
                  ))}
                  {(!dashboard?.pagamentosRecentes || dashboard.pagamentosRecentes.length === 0) && !isLoading && (
                    <tr>
                      <td colSpan={4} className="px-6 py-12 text-center text-kondo-gray-400 italic">
                        Nenhum pagamento registrado recentemente.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </Card>

          {/* Inadimplentes Recentes */}
          <Card className="flex flex-col h-full">
            <div className="p-6 border-b border-kondo-gray-100 flex items-center justify-between">
              <h3 className="font-bold text-kondo-gray-900 flex items-center gap-2">
                <AlertCircle className="w-5 h-5 text-kondo-red-500" />
                Alertas de Atraso
              </h3>
            </div>
            <div className="flex-1 p-6 space-y-4">
              {dashboard?.inadimplentesRecentes.map((cobranca) => (
                <div key={cobranca.id} className="flex items-center justify-between p-4 bg-kondo-red-50/50 rounded-xl border border-kondo-red-100 group hover:border-kondo-red-300 transition-all">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-white rounded-lg flex items-center justify-center border border-kondo-red-100 shadow-sm">
                      <Clock className="w-5 h-5 text-kondo-red-500" />
                    </div>
                    <div>
                      <p className="text-sm font-bold text-kondo-gray-900">Apto {cobranca.unidadeId.substring(0, 4)}</p>
                      <p className="text-xs text-kondo-red-600 font-medium">Vencido em {formatDate(cobranca.vencimento)}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-bold text-kondo-gray-900">{formatCurrency(cobranca.valor)}</p>
                    <button className="text-[10px] uppercase font-bold text-kondo-purple-600 hover:text-kondo-purple-700 flex items-center gap-1 ml-auto mt-1">
                      Detalhes <ArrowUpRight className="w-3 h-3" />
                    </button>
                  </div>
                </div>
              ))}
              {(!dashboard?.inadimplentesRecentes || dashboard.inadimplentesRecentes.length === 0) && !isLoading && (
                <div className="flex flex-col items-center justify-center h-full text-center space-y-3 py-12">
                  <div className="w-16 h-16 bg-kondo-green-50 rounded-full flex items-center justify-center">
                    <CheckCircle2 className="w-8 h-8 text-kondo-green-500" />
                  </div>
                  <p className="text-kondo-gray-500 text-sm font-medium">Parabéns! Nenhuma inadimplência crítica detectada hoje.</p>
                </div>
              )}
            </div>
          </Card>
        </div>
      </div>
    </MainLayout>
  );
}
