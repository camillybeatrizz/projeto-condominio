import { useState } from 'react';
import { MessageSquare, User, CheckCircle, Clock, AlertCircle, Send, Paperclip, X } from 'lucide-react';
import { Button } from '../components/Button';
import { StatusCard } from '../components/Card';

interface TimelineEvent {
  id: string;
  type: 'created' | 'comment' | 'status_change' | 'assigned' | 'resolved';
  user: string;
  timestamp: string;
  content?: string;
  oldStatus?: string;
  newStatus?: string;
  assignedTo?: string;
}

const timelineEvents: TimelineEvent[] = [
  {
    id: '1',
    type: 'created',
    user: 'Maria Santos',
    timestamp: '30/03/2026 às 14:30',
    content: 'Vazamento de água no banheiro social, causando infiltração no apartamento de baixo. A situação está crítica e precisa de atenção urgente.',
  },
  {
    id: '2',
    type: 'assigned',
    user: 'João Silva (Síndico)',
    timestamp: '30/03/2026 às 14:45',
    assignedTo: 'Carlos Oliveira (Encanador)',
  },
  {
    id: '3',
    type: 'status_change',
    user: 'João Silva (Síndico)',
    timestamp: '30/03/2026 às 14:45',
    oldStatus: 'Aberto',
    newStatus: 'Em andamento',
  },
  {
    id: '4',
    type: 'comment',
    user: 'Carlos Oliveira',
    timestamp: '30/03/2026 às 16:20',
    content: 'Visitei o local e identifiquei o problema. É necessário substituir o sifão e fazer o reparo na tubulação. Materiais já foram solicitados.',
  },
  {
    id: '5',
    type: 'comment',
    user: 'Maria Santos',
    timestamp: '31/03/2026 às 09:15',
    content: 'A infiltração continua. Quando poderemos ter uma previsão de conclusão do reparo?',
  },
  {
    id: '6',
    type: 'comment',
    user: 'Carlos Oliveira',
    timestamp: '31/03/2026 às 10:00',
    content: 'Os materiais chegaram hoje pela manhã. Vou iniciar o reparo agora e estimo conclusão até às 15h.',
  },
  {
    id: '7',
    type: 'status_change',
    user: 'Carlos Oliveira',
    timestamp: '31/03/2026 às 15:30',
    oldStatus: 'Em andamento',
    newStatus: 'Aguardando Validação',
  },
  {
    id: '8',
    type: 'comment',
    user: 'Carlos Oliveira',
    timestamp: '31/03/2026 às 15:30',
    content: 'Reparo concluído. Sifão substituído e tubulação reparada. Testei por 30 minutos e não há mais vazamentos. Aguardando validação do morador.',
  },
];

const relatedTickets = [
  { id: '1245', title: 'Infiltração no teto - Apt 403', status: 'resolved', priority: 'Alta' },
  { id: '1240', title: 'Vazamento na cozinha - Apt 502', status: 'open', priority: 'Média' },
];

export function OperacionalChamados() {
  const [newComment, setNewComment] = useState('');

  const getTimelineIcon = (type: TimelineEvent['type']) => {
    switch (type) {
      case 'created':
        return { icon: AlertCircle, color: 'bg-kondo-purple-100 text-kondo-purple-600 border-kondo-purple-200' };
      case 'comment':
        return { icon: MessageSquare, color: 'bg-kondo-teal-100 text-kondo-teal-600 border-kondo-teal-200' };
      case 'status_change':
        return { icon: Clock, color: 'bg-kondo-orange-100 text-kondo-orange-600 border-kondo-orange-200' };
      case 'assigned':
        return { icon: User, color: 'bg-kondo-purple-100 text-kondo-purple-600 border-kondo-purple-200' };
      case 'resolved':
        return { icon: CheckCircle, color: 'bg-kondo-green-100 text-kondo-green-600 border-kondo-green-200' };
    }
  };

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <button className="text-kondo-gray-600 hover:text-kondo-gray-900">
            ← Voltar para lista
          </button>
        </div>
        <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Chamado #1247</h1>
        <p className="text-kondo-gray-600">Acompanhe o histórico completo e interações</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left - Timeline */}
        <div className="lg:col-span-2 space-y-6">
          {/* Ticket Info Card */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <div className="flex items-start justify-between mb-4">
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-kondo-gray-900 mb-2">
                  Vazamento no 5º andar
                </h2>
                <p className="text-kondo-gray-600 mb-4">
                  Vazamento de água no banheiro social, causando infiltração no apartamento de baixo.
                </p>
                <div className="flex items-center gap-3 flex-wrap">
                  <span className="px-3 py-1 bg-kondo-orange-100 text-kondo-orange-700 rounded-full text-sm font-medium">
                    Em andamento
                  </span>
                  <span className="px-3 py-1 bg-kondo-red-100 text-kondo-red-700 rounded-full text-sm font-medium">
                    Alta Prioridade
                  </span>
                  <span className="px-2 py-1 bg-kondo-gray-100 text-kondo-gray-700 rounded text-xs">
                    Categoria: Hidráulica
                  </span>
                </div>
              </div>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 pt-4 border-t border-kondo-gray-200">
              <div>
                <p className="text-xs text-kondo-gray-500 mb-1">Solicitante</p>
                <p className="text-sm font-semibold text-kondo-gray-900">Maria Santos</p>
                <p className="text-xs text-kondo-gray-600">Apt 504 - Bloco B</p>
              </div>
              <div>
                <p className="text-xs text-kondo-gray-500 mb-1">Responsável</p>
                <p className="text-sm font-semibold text-kondo-gray-900">Carlos Oliveira</p>
                <p className="text-xs text-kondo-gray-600">Encanador</p>
              </div>
              <div>
                <p className="text-xs text-kondo-gray-500 mb-1">Aberto em</p>
                <p className="text-sm font-semibold text-kondo-gray-900">30/03/2026</p>
                <p className="text-xs text-kondo-gray-600">14:30</p>
              </div>
              <div>
                <p className="text-xs text-kondo-gray-500 mb-1">Última atualização</p>
                <p className="text-sm font-semibold text-kondo-gray-900">31/03/2026</p>
                <p className="text-xs text-kondo-gray-600">15:30</p>
              </div>
            </div>
          </div>

          {/* Timeline */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="text-lg font-semibold text-kondo-gray-900 mb-6">Histórico do Chamado</h3>

            <div className="relative">
              {/* Vertical Line */}
              <div className="absolute left-6 top-8 bottom-8 w-0.5 bg-kondo-gray-200"></div>

              {/* Timeline Events */}
              <div className="space-y-6">
                {timelineEvents.map((event, index) => {
                  const config = getTimelineIcon(event.type);
                  const Icon = config.icon;

                  return (
                    <div key={event.id} className="relative flex gap-4">
                      {/* Icon */}
                      <div className={`relative z-10 w-12 h-12 ${config.color} rounded-full flex items-center justify-center flex-shrink-0 border-2`}>
                        <Icon className="w-5 h-5" />
                      </div>

                      {/* Content */}
                      <div className="flex-1 pt-1">
                        <div className="bg-kondo-gray-50 rounded-lg p-4">
                          <div className="flex items-start justify-between mb-2">
                            <div>
                              <p className="text-sm font-semibold text-kondo-gray-900">{event.user}</p>
                              <p className="text-xs text-kondo-gray-500">{event.timestamp}</p>
                            </div>
                          </div>

                          {event.type === 'created' && (
                            <div>
                              <p className="text-sm font-medium text-kondo-purple-700 mb-1">Criou o chamado</p>
                              {event.content && (
                                <p className="text-sm text-kondo-gray-700">{event.content}</p>
                              )}
                            </div>
                          )}

                          {event.type === 'comment' && (
                            <div>
                              <p className="text-sm font-medium text-kondo-teal-700 mb-1">Comentou</p>
                              <p className="text-sm text-kondo-gray-700">{event.content}</p>
                            </div>
                          )}

                          {event.type === 'status_change' && (
                            <div>
                              <p className="text-sm font-medium text-kondo-orange-700 mb-1">Alterou o status</p>
                              <div className="flex items-center gap-2 text-sm">
                                <span className="px-2 py-0.5 bg-kondo-gray-200 text-kondo-gray-700 rounded text-xs">
                                  {event.oldStatus}
                                </span>
                                <span className="text-kondo-gray-400">→</span>
                                <span className="px-2 py-0.5 bg-kondo-orange-200 text-kondo-orange-800 rounded text-xs font-medium">
                                  {event.newStatus}
                                </span>
                              </div>
                            </div>
                          )}

                          {event.type === 'assigned' && (
                            <div>
                              <p className="text-sm font-medium text-kondo-purple-700 mb-1">Atribuiu o chamado</p>
                              <p className="text-sm text-kondo-gray-700">
                                Responsável: <span className="font-semibold">{event.assignedTo}</span>
                              </p>
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          {/* New Comment */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="text-lg font-semibold text-kondo-gray-900 mb-4">Adicionar Comentário</h3>
            <div className="space-y-4">
              <textarea
                className="w-full px-4 py-3 bg-kondo-gray-50 border border-kondo-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-kondo-purple-600 focus:border-transparent resize-none"
                rows={4}
                placeholder="Digite seu comentário..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
              />
              <div className="flex items-center justify-between">
                <button className="flex items-center gap-2 px-3 py-2 text-sm text-kondo-gray-700 hover:bg-kondo-gray-100 rounded-lg transition-colors">
                  <Paperclip className="w-4 h-4" />
                  Anexar arquivo
                </button>
                <Button variant="primary" disabled={!newComment.trim()}>
                  <Send className="w-4 h-4 mr-2" />
                  Enviar Comentário
                </Button>
              </div>
            </div>
          </div>
        </div>

        {/* Right - Sidebar */}
        <div className="space-y-6">
          {/* Quick Actions */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="font-semibold text-kondo-gray-900 mb-4">Ações</h3>
            <div className="space-y-2">
              <Button variant="success" className="w-full justify-start">
                <CheckCircle className="w-4 h-4 mr-2" />
                Marcar como Resolvido
              </Button>
              <Button variant="secondary" className="w-full justify-start">
                <User className="w-4 h-4 mr-2" />
                Reatribuir Chamado
              </Button>
              <Button variant="ghost" className="w-full justify-start text-kondo-gray-700">
                <Clock className="w-4 h-4 mr-2" />
                Alterar Prioridade
              </Button>
            </div>
          </div>

          {/* Status Flow */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="font-semibold text-kondo-gray-900 mb-4">Fluxo de Status</h3>
            <div className="space-y-3">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 bg-kondo-purple-100 text-kondo-purple-600 rounded-full flex items-center justify-center text-xs font-bold">
                  ✓
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-kondo-gray-900">Aberto</p>
                  <p className="text-xs text-kondo-gray-500">30/03 14:30</p>
                </div>
              </div>
              <div className="h-6 w-0.5 bg-kondo-gray-200 ml-4"></div>
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 bg-kondo-orange-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
                  2
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-kondo-gray-900">Em andamento</p>
                  <p className="text-xs text-kondo-gray-500">Atual</p>
                </div>
              </div>
              <div className="h-6 w-0.5 bg-kondo-gray-200 ml-4"></div>
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 bg-kondo-gray-200 text-kondo-gray-500 rounded-full flex items-center justify-center text-xs font-bold">
                  3
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-kondo-gray-500">Aguardando Validação</p>
                </div>
              </div>
              <div className="h-6 w-0.5 bg-kondo-gray-200 ml-4"></div>
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 bg-kondo-gray-200 text-kondo-gray-500 rounded-full flex items-center justify-center text-xs font-bold">
                  4
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-kondo-gray-500">Resolvido</p>
                </div>
              </div>
            </div>
          </div>

          {/* Related Tickets */}
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <h3 className="font-semibold text-kondo-gray-900 mb-4">Chamados Relacionados</h3>
            <div className="space-y-3">
              {relatedTickets.map((ticket) => (
                <button
                  key={ticket.id}
                  className="w-full p-3 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg text-left transition-colors"
                >
                  <div className="flex items-center justify-between mb-1">
                    <span className="text-xs font-medium text-kondo-gray-600">#{ticket.id}</span>
                    <span className={`px-2 py-0.5 text-xs rounded-full ${
                      ticket.status === 'resolved'
                        ? 'bg-kondo-green-100 text-kondo-green-700'
                        : 'bg-kondo-purple-100 text-kondo-purple-700'
                    }`}>
                      {ticket.status === 'resolved' ? 'Resolvido' : 'Aberto'}
                    </span>
                  </div>
                  <p className="text-sm font-medium text-kondo-gray-900">{ticket.title}</p>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
