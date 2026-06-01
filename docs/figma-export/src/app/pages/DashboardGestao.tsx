import { TrendingUp, CreditCard, Wallet, AlertCircle, Calendar, Users, DollarSign, Activity } from 'lucide-react';
import { FinancialCard } from '../components/Card';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts';

const monthlyData = [
  { month: 'Out', receita: 52000, despesas: 48000 },
  { month: 'Nov', receita: 54000, despesas: 47000 },
  { month: 'Dez', receita: 53500, despesas: 49000 },
  { month: 'Jan', receita: 55000, despesas: 46500 },
  { month: 'Fev', receita: 54800, despesas: 47200 },
  { month: 'Mar', receita: 55600, despesas: 47000 },
];

const recentActivities = [
  { id: 1, type: 'payment', title: 'Pagamento Recebido - Apt 301', description: 'João Silva pagou R$ 1.850,00 via PIX', time: 'Há 5 minutos', icon: DollarSign, color: 'bg-kondo-green-100 text-kondo-green-600' },
  { id: 2, type: 'ticket', title: 'Novo Chamado #1248', description: 'Problema no elevador - Bloco B', time: 'Há 15 minutos', icon: AlertCircle, color: 'bg-kondo-orange-100 text-kondo-orange-600' },
  { id: 3, type: 'payment', title: 'Pagamento Recebido - Apt 504', description: 'Maria Santos pagou R$ 1.850,00 via Boleto', time: 'Há 1 hora', icon: DollarSign, color: 'bg-kondo-green-100 text-kondo-green-600' },
  { id: 4, type: 'user', title: 'Novo Morador Cadastrado', description: 'Carlos Oliveira - Apt 602', time: 'Há 2 horas', icon: Users, color: 'bg-kondo-purple-100 text-kondo-purple-600' },
  { id: 5, type: 'maintenance', title: 'Manutenção Agendada', description: 'Limpeza da caixa d\'água - 05/06/2026', time: 'Há 3 horas', icon: Calendar, color: 'bg-kondo-teal-100 text-kondo-teal-600' },
  { id: 6, type: 'ticket', title: 'Chamado Resolvido #1241', description: 'Limpeza extra da piscina concluída', time: 'Há 4 horas', icon: Activity, color: 'bg-kondo-green-100 text-kondo-green-600' },
];

export function DashboardGestao() {
  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div>
        <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Dashboard de Gestão</h1>
        <p className="text-kondo-gray-600">Visão geral do Edifício Aurora - Abril 2026</p>
      </div>

      {/* KPIs Financeiros */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <FinancialCard
          label="Receita Mensal"
          value="R$ 55.600"
          icon={TrendingUp}
          iconColor="bg-kondo-green-100 text-kondo-green-600"
          trend={{ value: '+8.2%', isPositive: true }}
          subtitle="Últimos 6 meses"
        />
        <FinancialCard
          label="Despesas"
          value="R$ 47.000"
          icon={CreditCard}
          iconColor="bg-kondo-orange-100 text-kondo-orange-600"
          trend={{ value: '-2.1%', isPositive: false }}
          subtitle="Distribuição anual"
        />
        <FinancialCard
          label="Inadimplência"
          value="R$ 7.400"
          icon={Wallet}
          iconColor="bg-kondo-red-100 text-kondo-red-600"
          subtitle="4 pendentes"
        />
        <FinancialCard
          label="Saldo Atual"
          value="R$ 23.450"
          icon={DollarSign}
          iconColor="bg-kondo-teal-100 text-kondo-teal-600"
          trend={{ value: '+12.5%', isPositive: true }}
          subtitle="Caixa do condomínio"
        />
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Chart */}
        <div className="lg:col-span-2 bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-kondo-gray-900 mb-1">Evolução Financeira</h2>
            <p className="text-sm text-kondo-gray-600">Últimos 6 meses - Receitas vs Despesas</p>
          </div>
          <ResponsiveContainer width="100%" height={300}>
            <AreaChart data={monthlyData}>
              <defs>
                <linearGradient id="colorReceita" x1="0" y1="0" x2="0" y2="1">
                  <stop key="stop-receita-1" offset="5%" stopColor="#10B981" stopOpacity={0.3}/>
                  <stop key="stop-receita-2" offset="95%" stopColor="#10B981" stopOpacity={0}/>
                </linearGradient>
                <linearGradient id="colorDespesas" x1="0" y1="0" x2="0" y2="1">
                  <stop key="stop-despesas-1" offset="5%" stopColor="#F97316" stopOpacity={0.3}/>
                  <stop key="stop-despesas-2" offset="95%" stopColor="#F97316" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
              <XAxis dataKey="month" stroke="#6B7280" style={{ fontSize: '12px' }} />
              <YAxis stroke="#6B7280" style={{ fontSize: '12px' }} />
              <Tooltip
                contentStyle={{
                  backgroundColor: '#fff',
                  border: '1px solid #E5E7EB',
                  borderRadius: '8px',
                  boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
                }}
              />
              <Area
                key="area-receita"
                type="monotone"
                dataKey="receita"
                stroke="#10B981"
                strokeWidth={2}
                fillOpacity={1}
                fill="url(#colorReceita)"
                name="Receita"
              />
              <Area
                key="area-despesas"
                type="monotone"
                dataKey="despesas"
                stroke="#F97316"
                strokeWidth={2}
                fillOpacity={1}
                fill="url(#colorDespesas)"
                name="Despesas"
              />
            </AreaChart>
          </ResponsiveContainer>
          <div className="flex items-center justify-center gap-6 mt-4">
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-kondo-green-500"></div>
              <span className="text-sm text-kondo-gray-600">Receita</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-kondo-orange-500"></div>
              <span className="text-sm text-kondo-gray-600">Despesas</span>
            </div>
          </div>
        </div>

        {/* Quick Stats */}
        <div className="space-y-4">
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="text-sm font-semibold text-kondo-gray-700 mb-4">Resumo Rápido</h3>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-kondo-gray-600">Total de Unidades</span>
                <span className="text-lg font-bold text-kondo-gray-900">64</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-kondo-gray-600">Ocupadas</span>
                <span className="text-lg font-bold text-kondo-green-600">63</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-kondo-gray-600">Taxa de Ocupação</span>
                <span className="text-lg font-bold text-kondo-purple-600">98.4%</span>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="text-sm font-semibold text-kondo-gray-700 mb-4">Chamados Abertos</h3>
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 rounded-full bg-kondo-red-500"></div>
                  <span className="text-sm text-kondo-gray-600">Alta</span>
                </div>
                <span className="text-sm font-semibold text-kondo-gray-900">2</span>
              </div>
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 rounded-full bg-kondo-orange-500"></div>
                  <span className="text-sm text-kondo-gray-600">Média</span>
                </div>
                <span className="text-sm font-semibold text-kondo-gray-900">3</span>
              </div>
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <div className="w-2 h-2 rounded-full bg-kondo-teal-500"></div>
                  <span className="text-sm text-kondo-gray-600">Baixa</span>
                </div>
                <span className="text-sm font-semibold text-kondo-gray-900">1</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Recent Activities */}
      <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-lg font-semibold text-kondo-gray-900 mb-1">Atividades Recentes</h2>
            <p className="text-sm text-kondo-gray-600">Últimas atualizações do sistema</p>
          </div>
          <button className="text-sm font-medium text-kondo-purple-600 hover:text-kondo-purple-700">
            Ver todas
          </button>
        </div>

        <div className="space-y-4">
          {recentActivities.map((activity) => {
            const Icon = activity.icon;
            return (
              <div key={activity.id} className="flex items-start gap-4 p-4 rounded-lg hover:bg-kondo-gray-50 transition-colors">
                <div className={`w-10 h-10 ${activity.color} rounded-lg flex items-center justify-center flex-shrink-0`}>
                  <Icon className="w-5 h-5" />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-kondo-gray-900">{activity.title}</p>
                  <p className="text-sm text-kondo-gray-600 mt-0.5">{activity.description}</p>
                </div>
                <span className="text-xs text-kondo-gray-500 flex-shrink-0">{activity.time}</span>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
