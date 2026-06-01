import { Calendar, CreditCard, DollarSign, QrCode, Download, Bell, AlertCircle, CheckCircle, Clock } from 'lucide-react';
import { Button } from '../components/Button';

const myTickets = [
  { id: '1247', title: 'Vazamento no banheiro', status: 'Em andamento', priority: 'Alta', date: '30/03/2026', color: 'orange' },
  { id: '1235', title: 'Interfone com defeito', status: 'Aberto', priority: 'Média', date: '25/03/2026', color: 'purple' },
  { id: '1220', title: 'Solicitação de limpeza', status: 'Resolvido', priority: 'Baixa', date: '20/03/2026', color: 'green' },
];

const buildingNotices = [
  {
    id: 1,
    title: 'Assembleia Geral - 15/06/2026',
    description: 'Convocamos todos os condôminos para a assembleia geral ordinária que acontecerá no salão de festas às 19h.',
    date: '01/06/2026',
    type: 'important',
    icon: Bell,
  },
  {
    id: 2,
    title: 'Manutenção Preventiva do Elevador',
    description: 'No dia 10/06 será realizada manutenção preventiva nos elevadores das 8h às 12h. Pedimos compreensão.',
    date: '28/05/2026',
    type: 'info',
    icon: AlertCircle,
  },
  {
    id: 3,
    title: 'Nova Política de Uso da Churrasqueira',
    description: 'A partir de junho, as reservas da churrasqueira devem ser feitas com 48h de antecedência através do aplicativo.',
    date: '25/05/2026',
    type: 'info',
    icon: Bell,
  },
];

export function PortalMorador() {
  return (
    <div className="p-8 space-y-8">
      {/* Welcome Header */}
      <div className="bg-gradient-to-br from-kondo-purple-600 to-kondo-purple-700 rounded-2xl p-8 text-white shadow-lg">
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-3xl font-bold mb-2">Bem-vindo de volta, João!</h1>
            <p className="text-kondo-purple-100">Residencial Sunset - Bloco A, Apt 301</p>
          </div>
          <div className="text-right">
            <p className="text-sm text-kondo-purple-200">Hoje</p>
            <p className="text-lg font-semibold">01 de Junho, 2026</p>
          </div>
        </div>
      </div>

      {/* Main Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column - Próxima Fatura */}
        <div className="lg:col-span-2 space-y-6">
          {/* Invoice Card */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 overflow-hidden">
            <div className="bg-gradient-to-r from-kondo-green-50 to-kondo-teal-50 px-6 py-4 border-b border-kondo-gray-200">
              <div className="flex items-center gap-2">
                <CreditCard className="w-5 h-5 text-kondo-green-600" />
                <h2 className="text-lg font-semibold text-kondo-gray-900">Próxima Fatura</h2>
              </div>
            </div>

            <div className="p-6">
              <div className="flex items-start justify-between mb-6">
                <div>
                  <p className="text-sm text-kondo-gray-600 mb-1">Valor Total</p>
                  <p className="text-4xl font-bold text-kondo-gray-900">R$ 1.850,00</p>
                  <p className="text-sm text-kondo-gray-600 mt-1">Referência: Junho/2026</p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-kondo-gray-600 mb-1">Vencimento</p>
                  <div className="flex items-center gap-2 mb-1">
                    <Calendar className="w-4 h-4 text-kondo-orange-600" />
                    <p className="text-lg font-semibold text-kondo-orange-600">10/06/2026</p>
                  </div>
                  <p className="text-xs text-kondo-gray-500">Em 9 dias</p>
                </div>
              </div>

              {/* Payment Details */}
              <div className="bg-kondo-gray-50 rounded-lg p-4 mb-6 space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-kondo-gray-600">Taxa de Condomínio</span>
                  <span className="font-medium text-kondo-gray-900">R$ 1.500,00</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-kondo-gray-600">Fundo de Reserva</span>
                  <span className="font-medium text-kondo-gray-900">R$ 200,00</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-kondo-gray-600">Água Individualizada</span>
                  <span className="font-medium text-kondo-gray-900">R$ 150,00</span>
                </div>
                <div className="border-t border-kondo-gray-200 pt-2 mt-2 flex justify-between">
                  <span className="text-sm font-semibold text-kondo-gray-900">Total</span>
                  <span className="font-bold text-kondo-gray-900">R$ 1.850,00</span>
                </div>
              </div>

              {/* Payment Actions */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                <Button variant="success" size="lg" className="w-full">
                  <QrCode className="w-5 h-5 mr-2" />
                  Pagar via PIX
                </Button>
                <Button variant="secondary" size="lg" className="w-full">
                  <Download className="w-5 h-5 mr-2" />
                  Baixar Boleto
                </Button>
              </div>

              <div className="mt-4 text-center">
                <button className="text-sm text-kondo-purple-600 hover:text-kondo-purple-700 font-medium">
                  Ver histórico de pagamentos
                </button>
              </div>
            </div>
          </div>

          {/* Meus Últimos Chamados */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h2 className="text-lg font-semibold text-kondo-gray-900 mb-1">Meus Últimos Chamados</h2>
                <p className="text-sm text-kondo-gray-600">Acompanhe o status das suas solicitações</p>
              </div>
              <Button variant="primary" size="sm">
                Novo Chamado
              </Button>
            </div>

            <div className="space-y-4">
              {myTickets.map((ticket) => {
                const statusConfig = {
                  orange: { icon: Clock, bg: 'bg-kondo-orange-100', text: 'text-kondo-orange-700', iconColor: 'text-kondo-orange-600' },
                  purple: { icon: AlertCircle, bg: 'bg-kondo-purple-100', text: 'text-kondo-purple-700', iconColor: 'text-kondo-purple-600' },
                  green: { icon: CheckCircle, bg: 'bg-kondo-green-100', text: 'text-kondo-green-700', iconColor: 'text-kondo-green-600' },
                };
                const config = statusConfig[ticket.color as keyof typeof statusConfig];
                const StatusIcon = config.icon;

                return (
                  <div key={ticket.id} className="flex items-start gap-4 p-4 border border-kondo-gray-200 rounded-lg hover:border-kondo-purple-300 hover:shadow-sm transition-all cursor-pointer">
                    <div className={`w-10 h-10 ${config.bg} rounded-lg flex items-center justify-center flex-shrink-0`}>
                      <StatusIcon className={`w-5 h-5 ${config.iconColor}`} />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 mb-1">
                        <h3 className="text-sm font-semibold text-kondo-gray-900">{ticket.title}</h3>
                        <span className="text-xs text-kondo-gray-500 flex-shrink-0">#{ticket.id}</span>
                      </div>
                      <div className="flex items-center gap-3 flex-wrap">
                        <span className={`px-2 py-0.5 ${config.bg} ${config.text} rounded-full text-xs font-medium`}>
                          {ticket.status}
                        </span>
                        <span className="text-xs text-kondo-gray-500">Prioridade: {ticket.priority}</span>
                        <span className="text-xs text-kondo-gray-500">{ticket.date}</span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>

            <div className="mt-4 text-center">
              <button className="text-sm text-kondo-purple-600 hover:text-kondo-purple-700 font-medium">
                Ver todos os chamados
              </button>
            </div>
          </div>
        </div>

        {/* Right Column - Avisos */}
        <div className="space-y-6">
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <div className="flex items-center gap-2 mb-6">
              <Bell className="w-5 h-5 text-kondo-purple-600" />
              <h2 className="text-lg font-semibold text-kondo-gray-900">Avisos do Prédio</h2>
            </div>

            <div className="space-y-4">
              {buildingNotices.map((notice) => {
                const Icon = notice.icon;
                return (
                  <div key={notice.id} className="pb-4 border-b border-kondo-gray-100 last:border-0 last:pb-0">
                    <div className="flex items-start gap-3 mb-2">
                      <div className={`w-8 h-8 ${notice.type === 'important' ? 'bg-kondo-orange-100' : 'bg-kondo-teal-100'} rounded-lg flex items-center justify-center flex-shrink-0`}>
                        <Icon className={`w-4 h-4 ${notice.type === 'important' ? 'text-kondo-orange-600' : 'text-kondo-teal-600'}`} />
                      </div>
                      <div className="flex-1 min-w-0">
                        <h3 className="text-sm font-semibold text-kondo-gray-900 mb-1">{notice.title}</h3>
                        <p className="text-xs text-kondo-gray-600 mb-2">{notice.description}</p>
                        <p className="text-xs text-kondo-gray-500">{notice.date}</p>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>

            <div className="mt-4 text-center pt-4 border-t border-kondo-gray-100">
              <button className="text-sm text-kondo-purple-600 hover:text-kondo-purple-700 font-medium">
                Ver todos os avisos
              </button>
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="text-sm font-semibold text-kondo-gray-900 mb-4">Ações Rápidas</h3>
            <div className="space-y-2">
              <button className="w-full px-4 py-3 text-left text-sm font-medium text-kondo-gray-700 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg transition-colors">
                Reservar Área Comum
              </button>
              <button className="w-full px-4 py-3 text-left text-sm font-medium text-kondo-gray-700 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg transition-colors">
                Solicitar Autorização
              </button>
              <button className="w-full px-4 py-3 text-left text-sm font-medium text-kondo-gray-700 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg transition-colors">
                Atualizar Cadastro
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
