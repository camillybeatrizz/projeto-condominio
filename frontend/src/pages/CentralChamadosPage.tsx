import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { 
  MessageSquare, 
  Plus, 
  Clock, 
  CheckCircle2, 
  AlertCircle,
  MoreVertical,
  User,
  Home
} from 'lucide-react';
import { MainLayout } from '../components/MainLayout';
import { Card } from '../components/Card';
import { chamadoService } from '../services/operacional.service';
import { useAuth } from '../providers/AuthProvider';
import { Perfil, StatusChamado } from '../types/api';
import { useState } from 'react';

export function CentralChamadosPage() {
  const { activeAcesso } = useAuth();
  const queryClient = useQueryClient();
  const isMorador = activeAcesso?.perfil === Perfil.MORADOR;
  
  const [newChamadoDesc, setNewChamadoDesc] = useState('');
  const [showModal, setShowModal] = useState(false);

  const { data: chamadosPage, isLoading } = useQuery({
    queryKey: ['chamados', activeAcesso?.condominioId, activeAcesso?.unidadeId],
    queryFn: () => chamadoService.list({
      condominioId: isMorador ? undefined : activeAcesso?.condominioId,
      unidadeId: isMorador ? activeAcesso?.unidadeId : undefined,
    }),
  });

  const createMutation = useMutation({
    mutationFn: chamadoService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chamados'] });
      setShowModal(false);
      setNewChamadoDesc('');
    }
  });

  const updateStatusMutation = useMutation({
    mutationFn: ({ id, status }: { id: string; status: StatusChamado }) => 
      chamadoService.updateStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['chamados'] });
    }
  });

  const getStatusBadge = (status: StatusChamado) => {
    switch (status) {
      case StatusChamado.ABERTO:
        return <span className="px-2 py-0.5 bg-kondo-orange-100 text-kondo-orange-700 rounded text-[10px] font-bold uppercase flex items-center gap-1"><Clock className="w-3 h-3" /> Aberto</span>;
      case StatusChamado.ANDAMENTO:
        return <span className="px-2 py-0.5 bg-kondo-purple-100 text-kondo-purple-700 rounded text-[10px] font-bold uppercase flex items-center gap-1"><MessageSquare className="w-3 h-3" /> Em Andamento</span>;
      case StatusChamado.CONCLUIDO:
        return <span className="px-2 py-0.5 bg-kondo-green-100 text-kondo-green-700 rounded text-[10px] font-bold uppercase flex items-center gap-1"><CheckCircle2 className="w-3 h-3" /> Concluído</span>;
    }
  };

  return (
    <MainLayout breadcrumbs={[{ label: 'Chamados' }]}>
      <div className="space-y-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-kondo-gray-900">Central de Chamados</h1>
            <p className="text-kondo-gray-500">
              {isMorador ? 'Acompanhe e abra solicitações para sua unidade.' : 'Gerencie as solicitações dos moradores.'}
            </p>
          </div>
          {isMorador && (
            <button 
              onClick={() => setShowModal(true)}
              className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-white bg-kondo-purple-600 rounded-lg hover:bg-kondo-purple-700 transition-all shadow-md"
            >
              <Plus className="w-4 h-4" /> Novo Chamado
            </button>
          )}
        </div>

        <div className="grid grid-cols-1 gap-4">
          {isLoading ? (
            [1, 2, 3].map(i => <div key={i} className="h-24 bg-white rounded-xl animate-pulse border border-kondo-gray-100" />)
          ) : (
            chamadosPage?.content.map((chamado) => (
              <Card key={chamado.id} className="p-6 group hover:shadow-md transition-all border-l-4 border-l-kondo-purple-500">
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                  <div className="flex-1 space-y-2">
                    <div className="flex items-center gap-3">
                      {getStatusBadge(chamado.status)}
                      <span className="text-xs text-kondo-gray-400 font-medium">Aberto em {new Date(chamado.dataAbertura).toLocaleDateString('pt-BR')}</span>
                    </div>
                    <p className="text-kondo-gray-900 font-medium leading-relaxed">
                      {chamado.descricao}
                    </p>
                    <div className="flex items-center gap-4 text-[11px] text-kondo-gray-500 font-bold uppercase">
                      <span className="flex items-center gap-1"><Home className="w-3.5 h-3.5" /> Unidade {chamado.unidadeId.substring(0, 4)}</span>
                    </div>
                  </div>

                  {!isMorador && chamado.status !== StatusChamado.CONCLUIDO && (
                    <div className="flex items-center gap-2">
                      {chamado.status === StatusChamado.ABERTO && (
                        <button 
                          onClick={() => updateStatusMutation.mutate({ id: chamado.id, status: StatusChamado.ANDAMENTO })}
                          className="px-3 py-1.5 text-xs font-bold text-kondo-purple-600 bg-kondo-purple-50 rounded-lg hover:bg-kondo-purple-100 transition-colors"
                        >
                          Atender
                        </button>
                      )}
                      {chamado.status === StatusChamado.ANDAMENTO && (
                        <button 
                          onClick={() => updateStatusMutation.mutate({ id: chamado.id, status: StatusChamado.CONCLUIDO })}
                          className="px-3 py-1.5 text-xs font-bold text-kondo-green-600 bg-kondo-green-50 rounded-lg hover:bg-kondo-green-100 transition-colors"
                        >
                          Concluir
                        </button>
                      )}
                    </div>
                  )}
                </div>
              </Card>
            ))
          )}
          {chamadosPage?.content.length === 0 && (
            <div className="py-20 text-center bg-white rounded-2xl border-2 border-dashed border-kondo-gray-100">
              <MessageSquare className="w-12 h-12 text-kondo-gray-200 mx-auto mb-4" />
              <p className="text-kondo-gray-500 font-medium">Nenhum chamado encontrado.</p>
            </div>
          )}
        </div>
      </div>

      {/* Modal Novo Chamado */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-kondo-gray-900/60 backdrop-blur-sm animate-in fade-in duration-300">
          <Card className="w-full max-w-lg animate-in zoom-in-95 duration-300 overflow-hidden">
            <div className="p-6 border-b border-kondo-gray-100 flex items-center justify-between">
              <h3 className="font-bold text-kondo-gray-900">Novo Chamado</h3>
              <button onClick={() => setShowModal(false)} className="text-kondo-gray-400 hover:text-kondo-gray-600">
                <AlertCircle className="w-6 h-6 rotate-45" />
              </button>
            </div>
            <div className="p-6 space-y-4">
              <div>
                <label className="block text-sm font-bold text-kondo-gray-700 mb-2">Descrição do Problema</label>
                <textarea 
                  rows={4}
                  value={newChamadoDesc}
                  onChange={(e) => setNewChamadoDesc(e.target.value)}
                  placeholder="Descreva detalhadamente o problema ou solicitação..."
                  className="w-full bg-kondo-gray-50 border border-kondo-gray-200 rounded-xl p-4 text-sm outline-none focus:border-kondo-purple-500 transition-all resize-none"
                />
              </div>
              <div className="bg-kondo-purple-50 p-4 rounded-xl border border-kondo-purple-100 flex items-start gap-3">
                <AlertCircle className="w-5 h-5 text-kondo-purple-600 flex-shrink-0" />
                <p className="text-xs text-kondo-purple-700 leading-relaxed font-medium">
                  Sua solicitação será encaminhada diretamente para o síndico e zeladoria. Você receberá atualizações de status por aqui.
                </p>
              </div>
            </div>
            <div className="p-6 bg-kondo-gray-50 border-t border-kondo-gray-100 flex justify-end gap-3">
              <button 
                onClick={() => setShowModal(false)}
                className="px-4 py-2 text-sm font-bold text-kondo-gray-500 hover:text-kondo-gray-700"
              >
                Cancelar
              </button>
              <button 
                disabled={!newChamadoDesc || createMutation.isPending}
                onClick={() => createMutation.mutate({ descricao: newChamadoDesc, unidadeId: activeAcesso?.unidadeId! })}
                className="px-6 py-2 text-sm font-bold text-white bg-kondo-purple-600 rounded-lg hover:bg-kondo-purple-700 transition-all shadow-md disabled:opacity-50"
              >
                {createMutation.isPending ? 'Enviando...' : 'Abrir Chamado'}
              </button>
            </div>
          </Card>
        </div>
      )}
    </MainLayout>
  );
}
